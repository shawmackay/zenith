package org.blarty.zenith.messaging.system.mapping;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.jini.config.ConfigurationProvider;

import org.blarty.zenith.messaging.channels.MessageChannel;
import org.blarty.zenith.messaging.channels.PointToPointChannel;
import org.blarty.zenith.messaging.channels.PublishingChannel;
import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.components.routers.ChannelFilterLink;
import org.blarty.zenith.messaging.components.routers.ContentBasedRouterAction;
import org.blarty.zenith.messaging.components.routers.Filter;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.messages.MessageHeader;
import org.blarty.zenith.messaging.messages.ObjectMessage;
import org.blarty.zenith.messaging.system.ChannelException;
import org.blarty.zenith.messaging.system.MessagingListener;
import org.blarty.zenith.messaging.system.MessagingManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ChannelMapLoader {
        private MessagingManager mgr = null;

        private Document channelMap = null;

        public ChannelMapLoader(Document channelMap, MessagingManager mgr) {
                this.mgr = mgr;
                this.channelMap = channelMap;
        }

        public void init() throws Exception {
                setupChannels(channelMap.getElementsByTagName("channel"));
                setupEndpoints(channelMap.getElementsByTagName("endpoint"));
                System.out.println("Running Initialisation messages");
                runInitMessages(channelMap.getElementsByTagName("init"));
        }

        public void setupChannels(NodeList list) throws Exception {
                for (int i = 0; i < list.getLength(); i++) {                 
                        Node node = list.item(i);
                        NamedNodeMap attributes = node.getAttributes();
                        Node name = attributes.getNamedItem("name");
                        Node listenerClass = attributes.getNamedItem("listenerclass");
                        Node type = attributes.getNamedItem("type");
                        MessageChannel channel = null;
                        if (name == null)
                                throw new UnsupportedOperationException("A Channel cannot be created withuot a name");
                        if (type != null) {
                                Class cl = Class.forName(type.getNodeValue());
                                Constructor c = cl.getConstructor(new Class[] { String.class });
                                channel = (MessageChannel) c.newInstance(new Object[] { name.getNodeValue() });
                        } else
                                channel = new PointToPointChannel(name.getNodeValue());
                        mgr.addChannel(channel);
                        if (listenerClass != null) {
                                Class cl = Class.forName(listenerClass.getNodeValue());
                                MessagingListener listener = (MessagingListener) cl.newInstance();                        
                                mgr.getReceivingChannel(name.getNodeValue()).setReceivingListener(listener);
                        }

                }
        }

        public void setupEndpoints(NodeList list) throws Exception {
                for (int i = 0; i < list.getLength(); i++) {

                        Node node = list.item(i);
                        NamedNodeMap attributes = node.getAttributes();
                        Node id = attributes.getNamedItem("id");
                        System.out.println("Creating endpoint " + id.getNodeValue());
                        for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                                Node subNode = node.getChildNodes().item(j);
                                if (subNode.getNodeName().equals("contentBasedRouter")) {
                                        setupContentBasedRouter(subNode);
                                }
                        }
                }
        }

        private void setupContentBasedRouter(Node node) {
                // TODO Auto-generated method stub
                try {
                        Constructor c = ContentBasedRouterAction.class.getConstructor(new Class[] { ReceiverChannel.class, ReceiverChannel.class, PublishingChannel.class });
                        ReceiverChannel inputChannel = null;
                        ReceiverChannel controlChannel = null;
                        PublishingChannel invalidChannel = null;                        
                        NodeList list = node.getChildNodes();
                        for (int i = 0; i < list.getLength(); i++) {
                                Node item = list.item(i);
                                if (item.getNodeName().equals("inputchannel")) {
                                        inputChannel = getReceivingChannel(item.getAttributes().getNamedItem("id").getNodeValue());
                                        System.out.println("inputChannel: " + inputChannel);
                                }
                                if (item.getNodeName().equals("controlchannel")) {
                                        controlChannel = getReceivingChannel(item.getAttributes().getNamedItem("id").getNodeValue());
                                        System.out.println("controlChannel: " + controlChannel);
                                }
                                if (item.getNodeName().equals("invalidchannel")) {
                                        invalidChannel = getPublishingChannel(item.getAttributes().getNamedItem("id").getNodeValue());
                                        System.out.println("invalidChannel: " + invalidChannel);
                                }
                        }                      
                        c.newInstance(new Object[] { inputChannel, controlChannel, invalidChannel });
                } catch (SecurityException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (DOMException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (NoSuchMethodException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (ChannelException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }

        private PublishingChannel getPublishingChannel(String nodeValue) throws ChannelException {
                boolean multiplexMap = false;
                if (!multiplexMap)
                        return mgr.getSendingChannel(nodeValue);
                return null;
        }

        private ReceiverChannel getReceivingChannel(String nodeValue) throws ChannelException {
                // TODO Auto-generated method stub
                boolean multiplexMap = false;
                if (!multiplexMap)
                        return mgr.getReceivingChannel(nodeValue);
                return null;
        }

        public static void main(String[] args) throws Exception {
                System.out.println("Starting test Harness");
                MessagingManager.createManager("default", ConfigurationProvider.getInstance(new String[] { "conf/messagingMgr.config" }));
                MessagingManager mgr = MessagingManager.getManager();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document d = builder.parse(new File("conf/map1.xml"));
                ChannelMapLoader loader = new ChannelMapLoader(d, mgr);
                try {
                        loader.init();
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }

        public void runInitMessages(NodeList initSection) throws Exception {

                for (int i = 0; i < initSection.getLength(); i++) {
                        Node item = initSection.item(i);
                        NodeList itemChildren = item.getChildNodes();
                        for (int j = 0; j < itemChildren.getLength(); j++) {
                                Node child = itemChildren.item(j);
                                if (child.getNodeName().equals("send"))
                                        handleSendMessage(child);
                        }
                }
        }

        private void handleSendMessage(Node msg) throws Exception {
                // TODO Auto-generated method stub
                
                Node id = msg.getAttributes().getNamedItem("id");
                Node msgType = msg.getAttributes().getNamedItem("type");
                NodeList msgDetails = msg.getChildNodes();
                Node header = null;
                Node content = null;

                for (int i = 0; i < msgDetails.getLength(); i++) {
                        Node detailItem = msgDetails.item(i);
                        if (detailItem.getNodeName().equals("header"))
                                header = detailItem;
                        if (detailItem.getNodeName().equals("content"))
                                content = detailItem;
                }

                String type = msgType.getNodeValue();
                Message theMessage = null;
                if (type.equals("ObjectMessage")) {
                        MessageHeader msgheader = configureMessageHeader(header);
                        //System.out.println("Content: " + content.getNodeName());
                        Object msgContent = createObjectContent(content);
                        theMessage = new ObjectMessage(msgheader, msgContent);
                }
                if (theMessage == null) {
                        System.out.println("Message is null!!!");
                } else {
                        //System.out.println("A message is now being sent to " + id.getNodeValue());

                        getPublishingChannel(id.getNodeValue()).getPublishingQConnector().sendMessage(theMessage);
                }
        }

        private MessageHeader configureMessageHeader(Node header) {
                // TODO Auto-generated method stub
                return new MessageHeader();
        }

        private Object createObjectContent(Node content) throws Exception {
                for (int a = 0; a < content.getChildNodes().getLength(); a++) {
                        Node type = (content.getChildNodes().item(a));
                        if (type.getNodeName().equals("filterLink")) {
                                ChannelFilterLink cfl = new ChannelFilterLink();
                                NodeList cflDetails = type.getChildNodes();
                                for (int i = 0; i < cflDetails.getLength(); i++) {
                                        Node detailItem = cflDetails.item(i);
                                      //  System.out.println("detailItem " + detailItem.getNodeName());
                                        if (detailItem.getNodeName().equals("linkchannel")) {
                                                cfl.setChannelName(detailItem.getAttributes().getNamedItem("id").getNodeValue());
                                        }
                                        if (detailItem.getNodeName().equals("filter")) {
                                                Filter filter = null;
                                                String filterClassName = detailItem.getAttributes().getNamedItem("class").getNodeValue();
                                                filter = (Filter) Class.forName(filterClassName).newInstance();
                                                cfl.setFilter(filter);
                                        }
                                }
                                return cfl;
                        }
                }
                return null;
        }
}
