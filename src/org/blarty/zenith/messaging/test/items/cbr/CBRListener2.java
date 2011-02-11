/**
 * 
 */
package org.jini.projects.zenith.messaging.test.items.cbr;

import org.jini.projects.zenith.messaging.system.MessagingListener;

public class CBRListener2 implements MessagingListener {
        public void messageReceived(org.jini.projects.zenith.messaging.messages.Message m) {
               // System.out.println("String Message received " + m.getMessageContent() + " @ " + System.currentTimeMillis());
        }
}