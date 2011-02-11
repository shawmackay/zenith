/*
 * Created on 22-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

package org.jini.projects.zenith.messaging.messages;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import net.jini.id.Uuid;
import net.jini.id.UuidFactory;



/**
 * Stores message properties such as return address, correlation ID etc.
 *
 * @author Calum 
 */
public class MessageHeader implements Serializable{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -3241416023711016382L;
    private HashMap properties;
    
    public MessageHeader() {
        properties = new HashMap();
        net.jini.id.Uuid requestID = UuidFactory.generate();
        properties.put("requestID", requestID);
    }
    
    public void setReplyAddress(String address) {
        properties.put("replyaddress",address);
    }
    
    public void setGuaranteed(boolean guaranteed){
        properties.put("guaranteed",new Boolean(guaranteed));
    }
    
    public void setDestinationAddress(String destination){
    	properties.put("destination", destination);
    }
    
    public String getDestinationAddress(){
    	if (properties.containsKey("destination"))
            return (String) properties.get("destination");
        else {
            return null;
        }
    }
    
    public String getReplyAddress(){
        if (properties.containsKey("replyaddress"))
            return (String) properties.get("replyaddress");
        else {
            return null;
        }
    }
    
    public Uuid getRequestID() {
        if (properties.containsKey("requestID"))
            return (Uuid) properties.get("requestID");
        else {
            
            return null;
        }
    }
    
    public void setCorrelationID(Uuid ID) {
        properties.put("correlationID", ID);
    }
    
    public Uuid getCorrelationID() {
        if (properties.containsKey("correlationID"))
            return (Uuid) properties.get("correlationID");
        else
            return null;
    }
    
    public Sequence getSequence() {
        if (properties.containsKey("sequence"))
            return (Sequence) properties.get("sequence");
        else
            return null;
    }
    
    public Date getCreatedTime() {
        return (Date) properties.get("createdtime");
    }
    
    public boolean isGuaranteed(){
        if (properties.containsKey("guaranteed"))
            return ((Boolean) properties.get("guaranteed")).booleanValue();
        else
            return false;
    }
    
    public Long getRelativeTTL() {
        return (Long) properties.get("relativeTTL");
    }
    
    public Date getAbsoluteTTL() {
        return (Date) properties.get("absoluteTTL");
    }
    
    public void setRelativeTTL(long relTTL) {
        properties.put("relativeTTL", new Long(relTTL));
    }
    
    public void setAbsoluteTTL(Date absTTL) {
        properties.put("absoluteTTL", absTTL);
    }
    
    public void setSequence(Sequence seq) {
        properties.put("sequence", seq);
    }
    
    public void setVersion(String version) {
        properties.put("version", version);
    }
    
    public String getVersion() {
        if (properties.containsKey("version"))
            return (String) properties.get("version");
        else
            return null;
    }
    
    public Object getArbitraryField(String name){
        return properties.get(name);
    }
    
    public void setArbitraryField(String  name, Object data){
        properties.put(name, data);
    }
    
    public void showArbitraryFields(){
        System.out.println(properties);
    }
}
