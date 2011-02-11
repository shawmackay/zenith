/*
 * apollo2 : org.blarty.zenith.messaging.channels.connectors.transports.jini
 * 
 * 
 * RemotePublishingQAdapter.java
 * Created on 09-Mar-2004
 * 
 * RemotePublishingQAdapter
 *
 */
package org.blarty.zenith.messaging.channels.connectors.transports.jini;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;
import org.blarty.zenith.messaging.messages.Message;


/**
 * @author calum
 */
public class RemotePublishingQAdapter implements PublishingQConnector, Serializable {
    
	/**
     * 
     */
    private static final long serialVersionUID = 3258417248304445496L;
    RemotePublishingQConnector backend;
  
    public RemotePublishingQAdapter(RemotePublishingQConnector backend){    	
    	this.backend = backend;
    }
    
	public void sendMessage(Message m) {
		try {
			backend.sendMessage(m);
		} catch (RemoteException e) {
			// URGENT Handle RemoteException
			e.printStackTrace();
		}
	}
}
