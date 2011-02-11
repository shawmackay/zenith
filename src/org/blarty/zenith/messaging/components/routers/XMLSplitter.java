package org.jini.projects.zenith.messaging.components.routers;

/*
 * XMLSplitter.java
 *
 * Created Mon Apr 04 09:23:28 BST 2005
 */

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.components.routers.splitters.SplitPoint;
import org.jini.projects.zenith.messaging.components.routers.splitters.XPathSplitPoint;
import org.jini.projects.zenith.messaging.components.routers.splitters.XSLTSplitPoint;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.messages.MessageHeader;
import org.jini.projects.zenith.messaging.messages.ObjectMessage;
import org.jini.projects.zenith.messaging.messages.StringMessage;
import org.jini.projects.zenith.messaging.system.ControllableChannelAction;
import org.jini.projects.zenith.messaging.system.MessagingManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author calum
 * 
 */

public class XMLSplitter implements ControllableChannelAction {
        List<SplitPoint> splitPaths;

        Logger log = Logger.getLogger("org.jini.projects.zenith.messaging.routers");

      

        private TransformerFactory factory = TransformerFactory.newInstance();

        private Templates templ;

        private XPathFactory xpf = XPathFactory.newInstance();

        private XPath xp = xpf.newXPath();

        public XMLSplitter() {
                try {
                        splitPaths = new ArrayList<SplitPoint>();
                        templ = factory.newTemplates(new StreamSource(new StringReader(getCopySheet())));

                } catch (Exception e) {
                        log.log(Level.SEVERE, "Error binding MessageFilter", e);
                }
        }

        public void controlDispatch(Message m) {
                // TODO Auto-generated method stub
                ObjectMessage msg = (ObjectMessage) m;
                SplitPoint point = (SplitPoint) msg.getMessageContent();
                splitPaths.add(point);
        }

        public boolean dispatch(Message m) {
                try {
                        Document d = createNode(m.getMessageContentAsString());
                        HashMap<SplitPoint, List<String>> returnSet = new HashMap<SplitPoint, List<String>>();
                        DOMSource domS = new DOMSource(d);
                        for (SplitPoint point : splitPaths) {
                                try {
                                        // System.out.println(point.getSplitDefinition());

                                        if (point instanceof XPathSplitPoint) {
                                                XPathExpression xpathExp = xp.compile(point.getSplitDefinition());
                                                if (domS == null)
                                                        System.out.println("DOMSource is null");

                                                NodeList eval = (NodeList) xpathExp.evaluate(d, XPathConstants.NODESET);

                                                for (int i = 0; i < eval.getLength(); i++) {
                                                        String fragment = transformFragment(eval.item(i));
                                                        addReturnFragment(returnSet, point, fragment);
                                                }
                                        }
                                        if (point instanceof XSLTSplitPoint) {
                                                String output = transformXSLTSplitPoint(d, (XSLTSplitPoint) point);
                                                addReturnFragment(returnSet, point, output);
                                        }
                                } catch (Exception ex) {
                                        log.log(Level.INFO, "splitBoundary not matched");
                                        System.err.println(ex.getMessage());
                                        ex.printStackTrace();
                                }
                        }
                        for (Map.Entry<SplitPoint, List<String>> entr : returnSet.entrySet()) {
                                try {
                                        SplitPoint point = (SplitPoint) entr.getKey();
                                        List<String> fragments = entr.getValue();
                                        PublishingChannel ch = MessagingManager.getManager().getSendingChannel(point.getDestination());
                                        for (int i = 0; i < fragments.size(); i++) {
                                                String fragment = fragments.get(i);

                                                MessageHeader header = new MessageHeader();
                                                header.setCorrelationID(m.getHeader().getRequestID());
                                                header.setDestinationAddress(point.getDestination());
                                                ch.getPublishingQConnector().sendMessage(new StringMessage(header, fragment));
                                        }
                                } catch (Exception ex) {
                                        System.err.println("Caught Exception: " + ex.getClass().getName() + "; Msg: " + ex.getMessage());
                                        ex.printStackTrace();
                                }
                        }
                        return true;
                } catch (Exception ex) {
                        System.err.println("Caught Exception: " + ex.getClass().getName() + "; Msg: " + ex.getMessage());
                        ex.printStackTrace();
                }
                return false;
        };

        private void addReturnFragment(HashMap<SplitPoint, List<String>> returnSet, SplitPoint point, String fragment) {
                List<String> addToList = null;
                if (returnSet.containsKey(point)) {
                        addToList = returnSet.get(point);

                } else {
                        addToList = new ArrayList<String>();
                        returnSet.put(point, addToList);
                }
                addToList.add(fragment);
        }

        private Document createNode(String message) throws ParserConfigurationException, SAXException, IOException {
                javax.xml.parsers.DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                return db.parse(new InputSource(new StringReader(message)));
        }

        private String transformFragment(Node fragment) throws TransformerException, TransformerConfigurationException {
              //  long start = System.currentTimeMillis();
                Source xmlsource = new DOMSource(fragment);
                StringWriter writer = new StringWriter();
                StreamResult xmlResult = new StreamResult(writer);
                Transformer trans = templ.newTransformer();

                trans.transform(xmlsource, xmlResult);
                //long end = System.currentTimeMillis();
                // System.out.println("Identity Transformation took " + (end
                // -start) +"ms");
                return writer.getBuffer().toString();

        }

        private String transformXSLTSplitPoint(Node document, XSLTSplitPoint point) throws TransformerException, TransformerConfigurationException {

                Source xmlsource = new DOMSource(document);
                Source xslSource = new StreamSource(new StringReader(point.getSplitDefinition()));
                Transformer trans = factory.newTransformer(xslSource);
                StringWriter writer = new StringWriter();
                StreamResult xmlResult = new StreamResult(writer);
                trans.transform(xmlsource, xmlResult);
                return writer.getBuffer().toString();
        }

        private String getCopySheet() {
                return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" + "<xsl:output method=\"xml\" indent=\"yes\"/>" + "<xsl:template match=\"@*|node()\"> " + "<xsl:copy><xsl:apply-templates select=\"@*|node()\"/></xsl:copy>" + "</xsl:template>" + "</xsl:stylesheet>";
        }

}
