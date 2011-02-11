/**
 * 
 */
package org.blarty.zenith.messaging.test.items.cbr;

import org.blarty.zenith.messaging.messages.EventMessage;
import org.blarty.zenith.messaging.system.MessagingListener;

public class CBRListener3 implements MessagingListener {
        public void messageReceived(org.blarty.zenith.messaging.messages.Message m) {
              //  System.out.println("Event Message received " + ((EventMessage) m).getEventName() + " @ " + System.currentTimeMillis());
        }
}