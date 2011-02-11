/**
 * 
 */
package org.jini.projects.zenith.messaging.test.items.cbr;

import org.jini.projects.zenith.messaging.system.MessagingListener;

public class CBRListener1 implements MessagingListener {
        public void messageReceived(org.jini.projects.zenith.messaging.messages.Message m) {
                //System.out.println("Object Message received " + m.getMessageContent() + " @ " + System.currentTimeMillis());
        }
}