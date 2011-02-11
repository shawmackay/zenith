/*
 * Created on 22-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

package org.blarty.zenith.messaging.endpoints;

import java.util.ArrayList;

import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.MessagingListener;


/**
 * @author Calum
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReceivingPushEndpointImpl implements ReceivingEndpoint {
	ReceiverChannel channel;
	MessagingListener listener;
	ArrayList tQueue = new ArrayList();
	Thread t;
	public ReceivingPushEndpointImpl(ReceiverChannel channel, MessagingListener listener) {
		this.channel = channel;
		this.listener = listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see apollo.messaging.endpoints.ReceivingEndpoint#receive(apollo.messaging.messages.Message)
	 */
	public boolean receive(Message m) {
		// TODO Auto-generated method stub
		listener.messageReceived(m);
		tQueue.add(m);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see apollo.messaging.endpoints.MessageEndpoint#getType()
	 */
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see apollo.messaging.endpoints.MessageEndpoint#init()
	 */
	public void init() {
		// TODO Complete method stub for init
        Thread t = new Thread(new Dispatcher());
        t.start();
	}

	public class Dispatcher implements Runnable {
		public void run() {
			for (;;) {
				while (tQueue.size() > 0) {
					listener.messageReceived((Message) tQueue.get(0));
					tQueue.remove(0);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				synchronized (this) {
					try {
						wait(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
