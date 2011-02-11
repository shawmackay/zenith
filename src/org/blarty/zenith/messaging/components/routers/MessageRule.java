/*
 * zenith : org.blarty.zenith.messaging.routers
 * 
 * 
 * MessageRule.java
 * Created on 01-Apr-2005
 * 
 * MessageRule
 *
 */
package org.blarty.zenith.messaging.components.routers;

import org.blarty.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface MessageRule extends java.io.Serializable {
	public boolean evaluate(Message m);
	
	public String getDestination();
}
