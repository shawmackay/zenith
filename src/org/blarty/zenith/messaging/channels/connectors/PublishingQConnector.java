/*
 * apollo2 : apollo.messaging.channels.connectors
 * 
 * 
 * PublishingQConnector.java
 * Created on 26-Feb-2004
 * 
 * PublishingQConnector
 *
 */
package org.blarty.zenith.messaging.channels.connectors;

import org.blarty.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface PublishingQConnector {
    public void sendMessage(Message m);
}
