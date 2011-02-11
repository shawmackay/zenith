/*
 * zenith : org.blarty.zenith.messaging.channels
 * 
 * 
 * DeadLetterChannel.java
 * Created on 01-Apr-2005
 * 
 * DeadLetterChannel
 *
 */

package org.blarty.zenith.messaging.channels;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;
import org.blarty.zenith.messaging.channels.connectors.PublishingQConnectorImpl;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.ChannelPublisher;
import org.blarty.zenith.messaging.system.store.StoreSystem;

/**
 * @author calum
 */
public class DeadLetterChannel implements MessageChannel, PublishingChannel {
	private String name;
	private java.util.List queue;
	private boolean guaranteed;
	Logger l = Logger.getLogger("org.blarty.zenith.messaging.channels");
	public DeadLetterChannel(String name) {
		this.name = name;
	}

	public String getName() {
		// TODO Complete method stub for getName
		return null;
	}



	public void setGuaranteed(boolean guaranteeDelivery) {
		guaranteed = guaranteeDelivery;
	}

	public boolean isGuaranteed() {
		// TODO Complete method stub for isGuaranteed
		return guaranteed;
	}

	public int getOutstanding() {
		// TODO Complete method stub for getOutstanding
		return queue.size();
	}

	public PublishingQConnector getPublishingQConnector() {
		// TODO Complete method stub for getPublishingQConnector

		// TODO Complete method stub for getPublishingQConnector
		return new PublishingQConnectorImpl(new ChannelPublisher() {
			/*
			 * @see apollo.messaging.system.ChannelPublisher#sendMessage(apollo.messaging.messages.Message)
			 */
			public void sendMessage(Message m) {
				if (guaranteed) {
					l.finest("Channel is storing a message");
					try {
						StoreSystem sys = new StoreSystem(System.getProperty("org.blarty.zenith.messaging.system.store.dir") + File.separator + "DeadLetters");
						sys.store(name, m);
						l.finest("Channel has stored the Message");
					} catch (IOException e) {
						// TODO Handle IOException
						e.printStackTrace();
					}
				}
				// TODO Complete method stub for sendMessage
				if (m == null) {
					System.out.println("Why is message null!!!");
				}
				// if(m.getHeader().getReplyAddress()==null)
				// System.out.println("Reply address is null");
				queue.add(m);
			}
		});
	}

}
