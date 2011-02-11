/*
 * MessageAudit.java
 *
 * Created on 21 July 2006, 15:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jini.projects.zenith.messaging.system;

import java.util.HashMap;
import java.util.Map;
import net.jini.id.Uuid;
import org.jini.projects.zenith.messaging.messages.Message;

/**
 *
 * @author calum.mackay
 */
public class MessageAudit {
    
    private Map<Uuid, Message> msgAudit = new HashMap<Uuid, Message>();
    
    private Map<Uuid, Uuid> responseMap = new HashMap<Uuid,Uuid>();
    
    private static MessageAudit audit;
    
       
    
    /** Creates a new instance of MessageAudit */
    private  MessageAudit() {
        
    }
    
    public static MessageAudit getMessageAuditer(){
     if(audit==null)
         audit = new MessageAudit();
     return audit;
    }
    
    public void addMessage(Message m){
        msgAudit.put(m.getHeader().getRequestID(), m);
        if(m.getHeader().getCorrelationID()!=null)
            responseMap.put(m.getHeader().getCorrelationID(), m.getHeader().getRequestID());
    }
    
    public boolean hasResponseFor(Uuid request){
        return responseMap.containsKey(request);
    }
    
    public Message getResponseFor(Uuid request){
        Uuid responseID = responseMap.get(request);
        return msgAudit.get(responseID);
    }
}
