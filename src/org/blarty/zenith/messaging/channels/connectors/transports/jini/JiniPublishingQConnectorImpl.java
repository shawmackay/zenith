/*
 * apollo2 : apollo.messaging.channels.connectors.transports.jini
 * 
 * 
 * JiniPublishingQConnectorImpl.java
 * Created on 26-Feb-2004
 * 
 * JiniPublishingQConnectorImpl
 *
 */
package org.jini.projects.zenith.messaging.channels.connectors.transports.jini;

import java.rmi.RemoteException;

import org.jini.projects.zenith.messaging.channels.connectors.PublishingQConnector;
import org.jini.projects.zenith.messaging.messages.Message;


/**
 * @author calum
 */
public class JiniPublishingQConnectorImpl implements RemotePublishingQConnector {
    
    PublishingQConnector delegate;
    
    public JiniPublishingQConnectorImpl(PublishingQConnector delegate){
    	this.delegate = delegate;
    }
	
	public void sendMessage(Message m) throws RemoteException {
		delegate.sendMessage(m);
	}
}
