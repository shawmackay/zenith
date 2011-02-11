package org.blarty.zenith.endpoints;

import net.jini.id.Uuid;

import org.blarty.zenith.messaging.messages.Message;





// Created as: 13-Jan-2003 : enclosing_type :Subscriber.java
// In org.blarty.zenith.endpoints
/**
 * All subscribers to a topic must be able to show some form
 * of unique identity and the ability to handle a message.
 * A single Subscriber may be subscribed to multiple topics, and the 
 * <code>handleMessage</code> method has to differentiate between them.
 * @author calum
 */
public interface Subscriber {
    public String getName();
    
		/**
		 * Send a message to a subscriber, this will be one of three kinds of message
		 * <ul>
		 * <li>Topic based - sent to all subscribers on a particular topic.</li>
		 * <li>Broadcast - sent across all topics and all subscribers </li>
		 * <li>Directed - sent to a single subscriber using it's identity</li>
		 * </ul>
		 * @param msg
		 * @return
		 */
		
		public void handleMessage(Message msg);
		/**
		 * Obtain the identity of the subscriber
		 * @return
		 */
		public Uuid getSubscriberIdentity();
        
        public boolean isLocked();
        
        public boolean isBusy();
}
