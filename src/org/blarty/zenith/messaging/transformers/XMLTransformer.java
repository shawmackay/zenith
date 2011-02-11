/*
 * zenith : org.blarty.zenith.messaging.transformers
 * 
 * 
 * XMLTransformer.java
 * Created on 19-Apr-2005
 * 
 * XMLTransformer
 *
 */

package org.blarty.zenith.messaging.transformers;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.blarty.zenith.messaging.messages.EventMessage;
import org.blarty.zenith.messaging.messages.ExceptionMessage;
import org.blarty.zenith.messaging.messages.InvocationMessage;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.messages.MessageHeader;
import org.blarty.zenith.messaging.messages.ObjectMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author calum
 */
public class XMLTransformer implements MessageTransformer {

	public Object transform(Message toTransform) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder build = factory.newDocumentBuilder();
			Document doc = build.newDocument();
			Element mesgElem = doc.createElement("message");
			doc.appendChild(mesgElem);
			addElement(mesgElem, "type", toTransform.getClass().getName(), doc);
			Element header = doc.createElement("header");
			MessageHeader mHeader = toTransform.getHeader();
			mesgElem.appendChild(header);
			addElement(header, "requestid", mHeader.getRequestID(), doc);
			addElement(header, "created", mHeader.getCreatedTime(), doc);
			addElement(header, "destination", mHeader.getDestinationAddress(), doc);
			addElement(header, "reply", mHeader.getReplyAddress(), doc);
			addElement(header, "absolutettl", mHeader.getAbsoluteTTL(), doc);
			addElement(header, "relativettl", mHeader.getRelativeTTL(), doc);
			addElement(header, "correlated", mHeader.getCorrelationID(), doc);
			Element seq = doc.createElement("sequence");
			header.appendChild(seq);
			if (mHeader.getSequence() != null) {
				addElement(seq, "sequenceid", mHeader.getSequence().getSequenceIdentifier(), doc);
				addElement(seq, "sequenceloc", mHeader.getSequence().getPositionInSequence(), doc);
				addElement(seq, "sequencesize", mHeader.getSequence().getTotalPartsInSequence(), doc);
			}
			addElement(header, "version", mHeader.getCreatedTime(), doc);
			Element content = doc.createElement("content");
			mesgElem.appendChild(content);
			if (toTransform instanceof InvocationMessage) {
				Element invoke = doc.createElement("invocation");
				content.appendChild(invoke);
				InvocationMessage invo = (InvocationMessage) toTransform;
				addElement(invoke, "methodname", invo.getMethodName(), doc);
				addElement(invoke, "methodsignature", invo.getMethodSignature(), doc);
				Element params = doc.createElement("params");
				invoke.appendChild(params);
				Object[] mParams = invo.getParameters();
				for (Object o : mParams) {
					Element param = doc.createElement("param");
					param.setAttribute("type", param.getClass().getName());
					param.setAttribute("value", param.toString());
					params.appendChild(param);
				}
			}
			if (toTransform instanceof ObjectMessage) {
				Element invoke = doc.createElement("object");
				content.appendChild(invoke);
				ObjectMessage o = (ObjectMessage) toTransform;
				if (o.getMessageContent() != null) {
					invoke.setAttribute("type", o.getMessageContent().getClass().getName());
					invoke.setAttribute("value", o.getMessageContent().toString());
				} else
					invoke.setAttribute("type", "null");
			}
			if (toTransform instanceof ExceptionMessage) {
				Element invoke = doc.createElement("exception");
				content.appendChild(invoke);
				ExceptionMessage o = (ExceptionMessage) toTransform;
				invoke.setAttribute("type", o.getMessageContent().getClass().getName());
				invoke.setAttribute("value", ((Exception) o.getMessageContent()).getMessage());
				Element stackt = doc.createElement("stacktrace");
				invoke.appendChild(stackt);
				StringWriter writer = new StringWriter();
				Exception e = (Exception) o.getMessageContent();
				e.printStackTrace(new PrintWriter(writer));
				stackt.appendChild(doc.createTextNode(writer.toString()));
			}
			if (toTransform instanceof EventMessage) {
				Element invoke = doc.createElement("event");
				content.appendChild(invoke);
				EventMessage o = (EventMessage) toTransform;
				invoke.setAttribute("eventname", o.getEventName());
				invoke.setAttribute("triggervalue", o.getMessageContent().toString());
			}

			Source xmlsource = new DOMSource(doc);
			Source xsloutsource = new StreamSource(new StringReader(getStyleSheet()));
			StringWriter writer = new StringWriter();
			StreamResult xmlResult = new StreamResult(writer);
			TransformerFactory transfactory = TransformerFactory.newInstance();
			Transformer trans = transfactory.newTransformer(xsloutsource);
			trans.transform(xmlsource, xmlResult);			
			return writer.getBuffer().toString();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private void addElement(Element parent, String elementName, Object elementValue, Document doc) {
		Element elem = doc.createElement(elementName);
		if (elementValue != null)
			elem.appendChild(doc.createTextNode(elementValue.toString()));
		parent.appendChild(elem);
	}

	private String getStyleSheet() {
		// A simple identity stylesheet renders a DOM tree ut as an XML string
		// which can then be passed back
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=" + "\"http://www.w3.org/1999/XSL/Transform\">" + "<xsl:output method=\"xml\" indent=\"yes\"/>" + "<xsl:template match=\"@*|node()\">" + "<xsl:copy>" + "<xsl:apply-templates select=\"@*|node()\"/>" + "</xsl:copy>" + "</xsl:template>" + "</xsl:stylesheet>";
	}

}
