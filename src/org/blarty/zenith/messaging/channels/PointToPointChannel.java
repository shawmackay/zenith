/*
 * Created on 22-Feb-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */

package org.blarty.zenith.messaging.channels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.blarty.zenith.messaging.channels.connectors.MessageReceiver;
import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;
import org.blarty.zenith.messaging.channels.connectors.PublishingQConnectorImpl;
import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnectorImpl;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.ChannelException;
import org.blarty.zenith.messaging.system.ChannelPublisher;
import org.blarty.zenith.messaging.system.MessagingListener;
import org.blarty.zenith.messaging.system.store.StoreSystem;




/**
 * @author Calum
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PointToPointChannel implements MessageChannel, PublishingChannel, ReceiverChannel {
	List queue;
	String name;
	MessagingListener listener;
	MessageReceiver currentSubscriber;
	private boolean guaranteed;
	Logger l = Logger.getLogger("org.blarty.zenith.messaging.channels");

	public PointToPointChannel(String name) {
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

	public  void sendTheMessage(Message m) {
		if (queue == null) {
			queue = Collections.synchronizedList(new ArrayList());
		}
        
		if (currentSubscriber != null){			
			//System.out.println("Point-to-point channel is sending to subscriber; Message: " +m.getHeader().getRequestID());
                Thread.yield();
                l.finest("Channel "  + name  + " is forwarding on Msg: " + m.getHeader().getRequestID() + " to the listener & subscriber in response to Msg: " + m.getHeader().getCorrelationID());
			((MessageReceiver) currentSubscriber).addMessage(m);
             l.finest("Channel "  + name  + " has added  Msg: " + m.getHeader().getRequestID() + " to the subscriber list in response to Msg: " + m.getHeader().getCorrelationID());
		}else {
                
                Thread.yield();
			//System.out.println("Point-to-point channel is sending message to queue; Message: " +m.getHeader().getRequestID());
                l.finest("Channel "  + name  + " has added  Msg: " + m.getHeader().getRequestID() + " to the queue in response to Msg: " + m.getHeader().getCorrelationID());
			queue.add(m);
			l.finest("Added ");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see apollo.messaging.channels.ReceiverChannel#setReceivingEndpoint(apollo.messaging.endpoints.ReceivingEndpoint)
	 */
	public ReceivingQConnector setReceivingListener(MessagingListener listener) throws ChannelException {
		// TODO Auto-generated method stub
		// System.out.println("Set receving listener on channel: " +
		// this.getName());
            
		currentSubscriber = new ReceivingQConnectorImpl(name,listener);
		return (ReceivingQConnector) currentSubscriber;
	}

	/*
	 * @see apollo.messaging.channels.ReceiverChannel#createSynchronousRegistration()
	 */
	public ReceivingQConnector createSynchronousRegistration() throws ChannelException {
		currentSubscriber = new ReceivingQConnectorImpl(name,null);
		return (ReceivingQConnector) currentSubscriber;
	}
    
    private PublishingQConnector theConnector = new PublishingQConnectorImpl(new ChannelPublisher() {
            /*
             * @see apollo.messaging.system.ChannelPublisher#sendMessage(apollo.messaging.messages.Message)
             */
            public synchronized void sendMessage(Message m) {
                if(guaranteed){
                    l.finest("Channel is storing a message");
                    try {
                        StoreSystem sys = new StoreSystem(System.getProperty("org.blarty.zenith.messaging.system.store.dir"));
                        sys.store(name, m);
                        l.finest("Channel " + name + " has stored the Message with ID " + m.getHeader().getRequestID());
                    } catch (IOException e) {
                        // TODO Handle IOException
                        e.printStackTrace();
                    }
                }
                // TODO Complete method stub for sendMessage
                if (m == null) {
                    System.out.println("Why is message null!!!");
                }
                if(m.getHeader().getCorrelationID()!=null)
                l.finest("Channel "  + name  + " is forwarding on Msg: " + m.getHeader().getRequestID() + " to the listener in response to Msg: " + m.getHeader().getCorrelationID());
                else
                        l.finest("Channel "  + name  + " is forwarding on Msg: " + m.getHeader().getRequestID() + " to the listener");
                //if(m.getHeader().getReplyAddress()==null)
                //   System.out.println("Reply address is null");
                //System.out.println("Point-to-point channel is sending/storing Message: " +m.getHeader().getRequestID());
                sendTheMessage(m);

            }
            
            public String toString(){
                    return getDetails();
            }
        });

	/*
	 * @see apollo.messaging.channels.PublishingChannel#getPublishingQConnector()
	 */
	public PublishingQConnector getPublishingQConnector() {
		// TODO Complete method stub for getPublishingQConnector
		return theConnector;
	}

	/*
	 * @see org.blarty.zenith.messaging.channels.MessageChannel#getOutstanding()
	 */
	public int getOutstanding() {
		// TODO Complete method stub for getOutstanding
		if (queue != null)
			return queue.size();
		else
			return 0;
	}

	/*
	 * @see org.blarty.zenith.messaging.channels.ReceiverChannel#removeListenerFor(org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector)
	 */
	public boolean removeListenerFor(ReceivingQConnector connect) {
		if (connect.equals(currentSubscriber)) {
			currentSubscriber = null;
			return true;
		} else
			return false;
	}

	public boolean isGuaranteed() {
		// TODO Complete method stub for isGuaranteed
		return guaranteed;
	}

	public void setGuaranteed(boolean guaranteeDelivery) {
		// TODO Complete method stub for setGuaranteed
		this.guaranteed = guaranteeDelivery;
	}
    
    public String getDetails(){
            StringBuffer buffer = new StringBuffer( "P-P :  " + name +"\n");
            for(int i=0;i<queue.size();i++){
			Object l = queue.get(i);
                    buffer.append("\t[" + l.toString() + "]\n");
            }
            return buffer.toString();
    }
}
