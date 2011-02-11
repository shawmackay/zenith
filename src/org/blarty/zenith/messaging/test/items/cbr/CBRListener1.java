/**
 * 
 */
package org.blarty.zenith.messaging.test.items.cbr;

import org.blarty.zenith.messaging.system.MessagingListener;

public class CBRListener1 implements MessagingListener {
        public void messageReceived(org.blarty.zenith.messaging.messages.Message m) {
                //System.out.println("Object Message received " + m.getMessageContent() + " @ " + System.currentTimeMillis());
        }
}