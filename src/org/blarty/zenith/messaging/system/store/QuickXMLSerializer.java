package org.jini.projects.zenith.messaging.system.store;

import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.jini.id.UuidFactory;

import org.jini.projects.zenith.messaging.messages.EventMessage;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.messages.MessageHeader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class QuickXMLSerializer {

        public static Document serializeObject(Object source) throws Exception {
                
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
               DocumentBuilder builder = factory.newDocumentBuilder();
              Document d =  builder.newDocument();
              Node node = d.createElement("serialized");
              d.appendChild(node);
                return serializeHelper(source, node,d, new IdentityHashMap());
        }

        private static Document serializeHelper(Object source, Node root, Document target, Map table) throws Exception{
               String id = Integer.toString(table.size());
               table.put(source,id);
               Class sourceClass =  source.getClass();
               Element oElt = target.createElement("object");
               oElt.setAttribute("class", sourceClass.getName());
               oElt.setAttribute("id", id);
               root.appendChild(oElt);
               if(!sourceClass.isArray()){
                       Field[] fields = getInstanceVariables(sourceClass);
                       for(int i=0;i<fields.length;i++){
                               if(!Modifier.isPublic(fields[i].getModifiers()))
                                       fields[i].setAccessible(true);
                               Element fElt = target.createElement("field");
                               fElt.setAttribute("name", fields[i].getName());
                               Class declClass = fields[i].getDeclaringClass();
                               fElt.setAttribute("declaringClass", declClass.getName());
                               Class fieldtype = fields[i].getType();
                               Object child = fields[i].get(source);
                               if(Modifier.isTransient(fields[i].getModifiers()))
                                               child = null;
                               System.out.println("Serializing " + fields[i].getName());
                               Element var = serializeVariable(fieldtype, child, target,table);
                               if(var!=null)
                               fElt.appendChild(var);
                               oElt.appendChild(fElt);
                       }
               } else {
                       Class componentType = sourceClass.getComponentType();
                       int length = Array.getLength(source);
                       oElt.setAttribute("length", Integer.toString(length));
                       for(int i=0;i<length;i++){
                               oElt.appendChild(serializeVariable(componentType, Array.get(source, i),target, table));
                       }
               }
                return target;
        }
        
        private static Element serializeVariable(Class fieldtype, Object child, Document target, Map table) throws Exception{
                if(child == null){
                        return null;
                }else if(!fieldtype.isPrimitive()) {
                        Element reference = target.createElement("reference");
                        if(table.containsKey(child)) {
                                reference.setTextContent(table.get(child).toString());
                        } else {
                                reference.setTextContent(Integer.toString(table.size()));
                                serializeHelper(child,reference,target,table);
                        }
                        return reference;
                } else {
                        Element value = target.createElement("value");
                        value.setTextContent(child.toString());
                        return value;
                }
                
                
        }

        private static Field[] getInstanceVariables(Class cls){
                List accum = new LinkedList();
                while(cls != null){
                        Field[] fields = cls.getDeclaredFields();
                        for(int i=0;i<fields.length;i++){
                                if(!Modifier.isStatic(fields[i].getModifiers()))
                                                accum.add(fields[i]);
                        }
                        cls = cls.getSuperclass();
                }
                Field[] retvalue = new Field[accum.size()];
                return (Field[]) accum.toArray(retvalue);
        }
        
        public static void main(String[] args) throws Exception{
                MessageHeader header = new MessageHeader();
                header.setReplyAddress("backhere");
                header.setDestinationAddress("overthere");
                header.setCorrelationID(UuidFactory.generate());
                header.setArbitraryField("time", new java.util.Date());
                
                Message m = new EventMessage(header,"Hello There", 0, "greeting");
               Document d= serializeObject(m);
               DOMSource source = new DOMSource(d);              
         
            StringWriter writer = new StringWriter();
            StreamResult xmlResult = new StreamResult(writer);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer trans = factory.newTransformer();
            trans.transform(source, xmlResult);
               //Scan for includer tags            
            System.out.println( writer.getBuffer().toString());
            
               
        }
}
