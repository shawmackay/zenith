package org.blarty.zenith.messaging.channels;

import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;


/**
 * 
 * Used to obtain a connector to a publishing Channel, in 
 * order to send messages to it
 * @author Calum
 *
 */
public interface PublishingChannel extends MessageChannel {
		
        public PublishingQConnector getPublishingQConnector();
}
