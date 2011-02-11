/*
 * apollo2 : apollo.messaging.channels.connectors.transports.jini
 * 
 * 
 * JiniReceivingQConnectorImpl.java
 * Created on 26-Feb-2004
 * 
 * JiniReceivingQConnectorImpl
 *
 */
package org.jini.projects.zenith.messaging.channels.connectors.transports.jini;

import java.rmi.RemoteException;

import org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.system.MessagingListener;


/**
 * @author calum
 */
public class JiniReceivingQConnectorImpl implements RemoteReceivingQConnector {
    ReceivingQConnector delegate;
    
    public String getChannelName() {
		// TODO Complete method stub for getChannelName
		return delegate.getChannelName();
	}
	public JiniReceivingQConnectorImpl(ReceivingQConnector delegate){
        this.delegate = delegate;
        
    }
	/* @see apollo.messaging.channels.connectors.transports.jini.JiniReceivingQConnector#setListener(apollo.messaging.system.MessagingListener)
	 */
	public void setListener(MessagingListener listener) throws RemoteException {
		// TODO Complete method stub for setListener
        delegate.setListener(listener);
	}

	/* @see apollo.messaging.channels.connectors.transports.jini.JiniReceivingQConnector#receive()
	 */
	public Message receive() throws RemoteException {
		// TODO Complete method stub for receive
        System.out.println("Delegating receive");
		return delegate.receive();
	}

	/* @see apollo.messaging.channels.connectors.transports.jini.JiniReceivingQConnector#receive(int)
	 */
	public Message receive(int timeout) throws RemoteException {
		// TODO Complete method stub for receive
		return delegate.receive(timeout);
	}
}
