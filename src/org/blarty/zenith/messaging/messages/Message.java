
package org.jini.projects.zenith.messaging.messages;

import java.io.Serializable;

/**
 * The basic message interface.
 * @author Calum
 */
public interface Message extends Serializable {
	public MessageHeader getHeader();	
    public Object getMessageContent();    
    public String getMessageContentAsString();
}
