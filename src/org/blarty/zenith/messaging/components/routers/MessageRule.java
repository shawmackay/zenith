/*
 * zenith : org.jini.projects.zenith.messaging.routers
 * 
 * 
 * MessageRule.java
 * Created on 01-Apr-2005
 * 
 * MessageRule
 *
 */
package org.jini.projects.zenith.messaging.components.routers;

import org.jini.projects.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface MessageRule extends java.io.Serializable {
	public boolean evaluate(Message m);
	
	public String getDestination();
}
