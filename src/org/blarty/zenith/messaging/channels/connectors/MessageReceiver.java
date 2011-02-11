/*
 * apollo2 : apollo.messaging.channels.connectors
 * 
 * 
 * MessageReceiver.java
 * Created on 26-Feb-2004
 * 
 * MessageReceiver
 *
 */
package org.blarty.zenith.messaging.channels.connectors;

import org.blarty.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface MessageReceiver {
    public abstract void addMessage(Message m);
}
