/*
* Created on 22-Feb-2004
*
* To change the template for this generated file go to Window - Preferences -
* Java - Code Generation - Code and Comments
*/

package org.jini.projects.zenith.messaging.channels;

import java.util.ArrayList;
import java.util.List;

import org.jini.projects.zenith.messaging.channels.connectors.MessageReceiver;
import org.jini.projects.zenith.messaging.channels.connectors.PublishingQConnector;
import org.jini.projects.zenith.messaging.channels.connectors.PublishingQConnectorImpl;
import org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnectorImpl;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.system.ChannelException;
import org.jini.projects.zenith.messaging.system.ChannelPublisher;
import org.jini.projects.zenith.messaging.system.MessagingListener;

/**
* @author Calum
*
* To change the template for this generated type comment go to Window -
* Preferences - Java - Code Generation - Code and Comments
*/
public class InvalidMessageChannel implements MessageChannel, PublishingChannel, ReceiverChannel {
	List queue;
	String name;
	ReceivingQConnector currentSubscriber;
	
	public InvalidMessageChannel(String name) {
		this.name = name;
	}
	
	/*
	* (non-Javadoc)
	*
	* @see apollo.messaging.channels.MessageChannel#getName()
	*/
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public void sendTheMessage(Message m) {
		if (queue == null) {
			queue = new ArrayList();
		}
		
		if(currentSubscriber!=null){
		((MessageReceiver) currentSubscriber).addMessage(m);
		} else {
		queue.add(m);
		}
		//System.out.println("A message has been routed to the Invalid Message Channel");
	}
	
	public PublishingQConnector getPublishingQConnector() {
		// TODO Complete method stub for getPublishingQConnector
		return new PublishingQConnectorImpl(new ChannelPublisher() {/*
			* @see apollo.messaging.system.ChannelPublisher#sendMessage(apollo.messaging.messages.Message)
			*/
			public void sendMessage(Message m) {
				// TODO Complete method stub for sendMessage
				sendTheMessage(m);
			}
		});
	}
	/* @see org.jini.projects.zenith.messaging.channels.MessageChannel#getOutstanding()
	*/
	public int getOutstanding() {
		// TODO Complete method stub for getOutstanding
		return queue.size();
	}
	public boolean isGuaranteed() {
		// TODO Complete method stub for isGuaranteed
		return false;
	}
	public void setGuaranteed(boolean guaranteeDelivery) {
		// TODO Complete method stub for setGuaranteed
		
	}

	public ReceivingQConnector setReceivingListener(MessagingListener listener) throws ChannelException {
		// TODO Auto-generated method stub
		// System.out.println("Set receving listener on channel: " +
		// this.getName());
		currentSubscriber = new ReceivingQConnectorImpl(name,listener);
		
		return (ReceivingQConnector) currentSubscriber;
	}

	public boolean removeListenerFor(ReceivingQConnector connect) {
		if (connect.equals(currentSubscriber)) {
			connect = null;
			return true;
		} else
			return false;
	}

	public ReceivingQConnector createSynchronousRegistration() throws ChannelException {
		currentSubscriber = new ReceivingQConnectorImpl(name,null);
		return (ReceivingQConnector) currentSubscriber;
	}
}
