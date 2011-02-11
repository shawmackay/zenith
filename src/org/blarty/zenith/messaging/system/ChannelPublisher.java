/*
 * apollo2 : apollo.messaging.system
 * 
 * 
 * ChannelPublisher.java
 * Created on 26-Feb-2004
 * 
 * ChannelPublisher
 *
 */
package org.jini.projects.zenith.messaging.system;

import org.jini.projects.zenith.messaging.messages.Message;

/**
 * 
 * Supplied by a channel, when a user requests a PublishingQConnector, in order
 * to send the message on to the Channel
 * @author calum
 */
public interface ChannelPublisher {
    public void sendMessage(Message m);
}
