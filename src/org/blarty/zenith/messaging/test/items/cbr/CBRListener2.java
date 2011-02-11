/**
 * 
 */
package org.blarty.zenith.messaging.test.items.cbr;

import org.blarty.zenith.messaging.system.MessagingListener;

public class CBRListener2 implements MessagingListener {
        public void messageReceived(org.blarty.zenith.messaging.messages.Message m) {
               // System.out.println("String Message received " + m.getMessageContent() + " @ " + System.currentTimeMillis());
        }
}