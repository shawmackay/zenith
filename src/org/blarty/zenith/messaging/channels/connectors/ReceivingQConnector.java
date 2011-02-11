/*
 * apollo2 : apollo.messaging.channels.connectors
 * 
 * 
 * ReceivingQConnector.java
 * Created on 26-Feb-2004
 * 
 * ReceivingQConnector
 *
 */

package org.blarty.zenith.messaging.channels.connectors;

import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.MessagingListener;


/**
 * @author calum
 */
public interface ReceivingQConnector {
	
	public String getChannelName();
    
    public void setListener(MessagingListener listener);

	public abstract Message receive();

	public abstract Message receive(int timeout);
}