/*
 * Apollo : apollo.messaging.endpoints
 * 
 * 
 * MessageDispatcher.java Created on 23-Feb-2004
 * 
 * MessageDispatcher
 *  
 */

package org.blarty.zenith.messaging.endpoints;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import net.jini.id.Uuid;

import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.messages.ObjectMessage;
import org.blarty.zenith.messaging.system.ChannelException;
import org.blarty.zenith.messaging.system.MessagingListener;
import org.blarty.zenith.messaging.system.MessagingManager;
/**
 * @author calum
 */
public class MessageDispatcher {
	ArrayList dispatchers;
	String inputChannelName;
	Logger log = Logger.getLogger("org.blarty.zenith.messaging.endpoints");

	public MessageDispatcher(ReceiverChannel input, ReceiverChannel controlChannel) {
		dispatchers = new ArrayList();
		inputChannelName = input.getName();
		try {
			//TODO: Change for Control Channel to allow adding of dispatchers.
			controlChannel.setReceivingListener(new MessagingListener() {
				public void messageReceived(Message m) {
					//TODO: Need to ensure that distribution logic pushes
					// message properly
					if (m instanceof ObjectMessage) {
						Object data = m.getMessageContent();
						if (data instanceof Dispatchable)
							addDispatcher((Dispatchable) data);
					}
				}
			});
			buildSyncCheckThread();
		} catch (ChannelException e) {
			System.out.println("Err: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void buildSyncCheckThread() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				// TODO Complete method stub for run
				ReceivingQConnector rec = null;
				try {
					rec = MessagingManager.getManager().registerOnChannel(inputChannelName, null);
				} catch (ChannelException e) {
					// URGENT Handle ChannelException
					e.printStackTrace();
				}
				for (;;) {
					Message m = rec.receive(100);
					boolean consumed = false;
					while (m != null && !consumed) {
						for (int i = 0; i < dispatchers.size(); i++) {
							Dispatchable dispatcher = (Dispatchable) dispatchers.get(i);
							if (dispatcher.isAvailable()) {
								dispatcher.setAvailable(false);
								log.finest("Sending to:  " + i);
								Thread t = new Thread(new DispatchThread(dispatcher, m));
								t.start();
								consumed = true;
								break;
							}
						}
					}
				}
			}
		});
		t.start();
	}

	public void addDispatcher(Dispatchable processor) {
		dispatchers.add(processor);
	}

	public synchronized void removeDispatcher(Uuid processorID) {
		Iterator iter = dispatchers.iterator();
		boolean removed = false;
		while (iter.hasNext()) {
			Dispatchable processor = (Dispatchable) iter.next();
			if (processorID.equals(processor.getReferentUuid())) {
				iter.remove();
				removed = true;				
			}
		}
		if (!removed)
			System.out.println("Dispatcher could not find processor");
	}

	class DispatchThread implements Runnable {
		Dispatchable proc;
		Message workingMsg;

		public DispatchThread(Dispatchable proc, Message m) {
			this.proc = proc;
			workingMsg = m;
		}

		public void run() {
			proc.process(workingMsg);
			proc.setAvailable(true);
		}
	}
}
