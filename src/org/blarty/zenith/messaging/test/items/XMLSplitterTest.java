package org.jini.projects.zenith.messaging.test.items;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.channels.connectors.PublishingQConnector;
import org.jini.projects.zenith.messaging.components.ControllableComponent;
import org.jini.projects.zenith.messaging.components.DefaultControllableComponent;
import org.jini.projects.zenith.messaging.components.routers.XMLSplitter;
import org.jini.projects.zenith.messaging.components.routers.splitters.SplitPoint;
import org.jini.projects.zenith.messaging.components.routers.splitters.XPathSplitPoint;
import org.jini.projects.zenith.messaging.components.routers.splitters.XSLTSplitPoint;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.messages.MessageHeader;
import org.jini.projects.zenith.messaging.messages.ObjectMessage;
import org.jini.projects.zenith.messaging.messages.StringMessage;
import org.jini.projects.zenith.messaging.system.MessagingListener;
import org.jini.projects.zenith.messaging.system.MessagingManager;
import org.jini.projects.zenith.messaging.test.TestItem;
/**
*
* @author  calum
*
*/

public class XMLSplitterTest implements TestItem{
	

	/**
	* run
	* @param aMessagingManager
	*/
	public void run(MessagingManager  mgr){
		try{
			ReceiverChannel input = (ReceiverChannel) mgr.createChannel("mySplitter");
            ReceiverChannel control = (ReceiverChannel) mgr.createChannel("controlSplitter");
			mgr.createChannel("acceptOrders");
			mgr.createChannel("acceptAddresses");
			mgr.createChannel("acceptItems");
            mgr.createChannel("acceptCount");
            
            
            String stylesheet = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"2.0\"><" +
                    "xsl:output method=\"text\"/>" +
                    " <xsl:template match=\"/\">" +
                    "<xsl:value-of select=\"count(//delivery)\"/>" +
                    "</xsl:template>       " +
                    "</xsl:stylesheet>";

			
			List<SplitPoint> arr = new ArrayList<SplitPoint>();
		arr.add(new XPathSplitPoint("acceptOrders","/dispatch-note/delivery/order"));
			arr.add(new XPathSplitPoint("acceptAddresses","/dispatch-note/delivery/address"));
			//arr.add(new XPathSplitPoint("acceptItems","//item"));
           arr.add(new XSLTSplitPoint("acceptCount",stylesheet));
            
           ControllableComponent component = new DefaultControllableComponent();
           component.setChannelAction(new XMLSplitter());
           component.setControlChannel(control);
           component.setInputChannel(input);
           for(SplitPoint s :arr){
                   PublishingQConnector connector = mgr.getPublishingConnector("controlSplitter");
                   connector.sendMessage(new ObjectMessage(new MessageHeader(),s));
           }
			
			mgr.registerOnChannel("acceptOrders", new MessagingListener() {
                    int count=0;
					/*
					* (non-Javadoc)
					*
					* @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
					*/
					public void messageReceived(Message m) {
						// TODO Auto-generated method stub
						System.out.println("Order Received(" + ++count + "): - "  + m.getMessageContentAsString());
					}
				});
			
			mgr.registerOnChannel("acceptCount", new MessagingListener() {
                    
                    int count=0;
					/*
					* (non-Javadoc)
					*
					* @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
					*/
					public void messageReceived(Message m) {
						// TODO Auto-generated method stub
						System.out.println("COUNT Received (" + ++count + "): - "  + m.toString());
					}
				});
			
mgr.registerOnChannel("acceptAddresses", new MessagingListener() {
                    
                    int count=0;
                    /*
                    * (non-Javadoc)
                    *
                    * @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
                    */
                    public void messageReceived(Message m) {
                        // TODO Auto-generated method stub
                        System.out.println("Address Received (" + ++count + "): - " + m.getMessageContentAsString() );
                    }
                });
            
			mgr.registerOnChannel("acceptItems", new MessagingListener() {
                    int count=0;
					/*
					* (non-Javadoc)
					*
					* @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
					*/
					public void messageReceived(Message m) {
						// TODO Auto-generated method stub
						System.out.println("Item Received (" + ++count + "): - " + m.toString() );
					}
				});
			BufferedReader rdr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("splittertest.xml")));
			StringBuffer buffer = new StringBuffer();
			String line = rdr.readLine();
			while(line!=null){
				buffer.append(line);
				line=rdr.readLine();
			}
			rdr.close();
			//System.out.println(buffer.toString());
			
			PublishingQConnector send = mgr.getSendingChannel("mySplitter").getPublishingQConnector();
			send.sendMessage(new StringMessage(new MessageHeader(), buffer.toString()));
           
            
		}catch(Exception ex){
			System.err.println("Caught Exception: "+ ex.getClass().getName() + "; Msg: " + ex.getMessage());
			ex.printStackTrace();
		}

	}

}
