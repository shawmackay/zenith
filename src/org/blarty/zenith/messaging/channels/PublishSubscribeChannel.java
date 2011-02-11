/*
 * Created on 22-Feb-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */

package org.blarty.zenith.messaging.channels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.blarty.zenith.messaging.channels.connectors.MessageReceiver;
import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;
import org.blarty.zenith.messaging.channels.connectors.PublishingQConnectorImpl;
import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnectorImpl;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.ChannelException;
import org.blarty.zenith.messaging.system.ChannelPublisher;
import org.blarty.zenith.messaging.system.MessagingListener;


/**
 * @author Calum
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PublishSubscribeChannel implements MessageChannel, PublishingChannel, ReceiverChannel {
	List queue;
	String name;
	ArrayList subscriptions;

	public PublishSubscribeChannel(String name) {
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
		//synchronized (queue) {
		///queue.add(m);
		for (int i = 0; i < subscriptions.size(); i++) {
			MessageReceiver s = (MessageReceiver) subscriptions.get(i);
			System.out.println("Sending message to " + i);
			s.addMessage(m);
		}
		//queue.remove(m);
		//}
	}

	/**
	 * Creates a QConnector that allows an endpoint to register for items on a
	 * queue
	 */
	public ReceivingQConnector setReceivingListener(MessagingListener listener) throws ChannelException {
		// TODO Auto-generated method stub
		if (this.subscriptions == null)
			subscriptions = new ArrayList();
		ReceivingQConnector s = new ReceivingQConnectorImpl(name,listener);
		subscriptions.add(s);
		return s;
	}

	
	/*
	 * @see apollo.messaging.channels.ReceiverChannel#createSynchronousRegistration()
	 */
	public ReceivingQConnector createSynchronousRegistration() throws ChannelException {
		ReceivingQConnector s = new ReceivingQConnectorImpl(name,null);
		subscriptions.add(s);
		return s;
	}

	public PublishingQConnector getPublishingQConnector() {
		// TODO Complete method stub for getPublishingQConnector
		return new PublishingQConnectorImpl(new ChannelPublisher() {
			/*
			 * @see apollo.messaging.system.ChannelPublisher#sendMessage(apollo.messaging.messages.Message)
			 */
			public void sendMessage(Message m) {
				// TODO Complete method stub for sendMessage
				sendTheMessage(m);
			}
		});
	}
    
    /* @see org.blarty.zenith.messaging.channels.MessageChannel#getOutstanding()
     */
    public int getOutstanding() {
        // TODO Complete method stub for getOutstanding
        return queue.size();
    }
    
   
	/* @see org.blarty.zenith.messaging.channels.ReceiverChannel#removeListenerFor(org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector)
	 */
	public boolean removeListenerFor(ReceivingQConnector connect) {
		// TODO Complete method stub for removeListenerFor
        synchronized(subscriptions){
         Iterator iter = subscriptions.iterator();
         boolean removed=false;
         
         while(iter.hasNext()){
            ReceivingQConnector item = (ReceivingQConnector) iter.next();
            if(item.equals(connect)){
                iter.remove();
                removed=true;
            }
         }
         return removed;
        }        
	}
	public boolean isGuaranteed() {
		// TODO Complete method stub for isGuaranteed
		return false;
	}
	public void setGuaranteed(boolean guaranteeDelivery) {
		// TODO Complete method stub for setGuaranteed

	}
}
