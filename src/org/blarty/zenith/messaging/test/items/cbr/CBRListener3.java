/**
 * 
 */
package org.jini.projects.zenith.messaging.test.items.cbr;

import org.jini.projects.zenith.messaging.messages.EventMessage;
import org.jini.projects.zenith.messaging.system.MessagingListener;

public class CBRListener3 implements MessagingListener {
        public void messageReceived(org.jini.projects.zenith.messaging.messages.Message m) {
              //  System.out.println("Event Message received " + ((EventMessage) m).getEventName() + " @ " + System.currentTimeMillis());
        }
}