/*
 * apollo2 : apollo.messaging.channels.connectors.transports.jini.constrainable
 * 
 * 
 * PublishingQConnectorProxy.java
 * Created on 26-Feb-2004
 * 
 * PublishingQConnectorProxy
 *
 */
package org.blarty.zenith.messaging.channels.connectors.transports.jini.constrainable;

import java.io.Serializable;
import java.rmi.RemoteException;

import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.id.ReferentUuid;
import net.jini.id.ReferentUuids;
import net.jini.id.Uuid;

import org.blarty.zenith.messaging.channels.connectors.transports.jini.RemoteReceivingQConnector;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.MessagingListener;


/**
 * @author calum
 */
public class ReceivingQConnectorProxy implements RemoteReceivingQConnector, Serializable, ReferentUuid{
    
    private static final long serialVersionUID = 2L;

    final RemoteReceivingQConnector backend;
    final Uuid proxyID;

    public static ReceivingQConnectorProxy create(RemoteReceivingQConnector server, Uuid id) {
        if (server instanceof RemoteMethodControl ) {            
            return new ReceivingQConnectorProxy.ConstrainableProxy(server, id, null);
        } else
            return new ReceivingQConnectorProxy(server, id);
    }
    
    public Uuid getReferentUuid() {
        return proxyID;
    }

    /** Proxies for servers with the same proxyID have the same hash code. */
    public int hashCode() {
        return proxyID.hashCode();
    }

    /**
     * Proxies for servers with the same <code>proxyID</code> are
     * considered equal.
     */
    public boolean equals(Object o) {
        return ReferentUuids.compare(this, o);
    }

    public ReceivingQConnectorProxy(RemoteReceivingQConnector backend, Uuid proxyID) {
        this.backend = backend;
        this.proxyID = proxyID;
    }

	
    
    final static class ConstrainableProxy extends ReceivingQConnectorProxy implements RemoteMethodControl {
        private static final long serialVersionUID = 4L;
        private ConstrainableProxy(RemoteReceivingQConnector server, Uuid id, MethodConstraints methodConstraints) {
            super(constrainServer(server, methodConstraints), id);

        }
        public RemoteMethodControl setConstraints(MethodConstraints constraints) {
            return new ReceivingQConnectorProxy.ConstrainableProxy(backend, proxyID, constraints);
        }

        /** {@inheritDoc} */
        public MethodConstraints getConstraints() {
            return ((RemoteMethodControl) backend).getConstraints();
        }

        private static RemoteReceivingQConnector constrainServer(RemoteReceivingQConnector server, MethodConstraints methodConstraints) {
            return (RemoteReceivingQConnector) ((RemoteMethodControl) server).setConstraints(methodConstraints);
        }

    }
    
	
	public String getChannelName() throws RemoteException{
		// TODO Complete method stub for getChannelName
		return backend.getChannelName();
	}

	/* @see apollo.messaging.channels.connectors.ReceivingQConnector#receive()
	 */
	public Message receive() {
        try {
			return backend.receive();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
        return null;
	}
	/* @see apollo.messaging.channels.connectors.ReceivingQConnector#receive(int)
	 */
	public Message receive(int timeout) {
		try {			
			return backend.receive(timeout);
		} catch (RemoteException e) {			
			e.printStackTrace();
		}
        return null;
	}
	
	/* @see apollo.messaging.channels.connectors.ReceivingQConnector#setListener(apollo.messaging.system.MessagingListener)
	 */
	public void setListener(MessagingListener listener) {
		try {
			// TODO Complete method stub for setListener
			backend.setListener(listener);
		} catch (RemoteException e) {
			// URGENT Handle RemoteException
			e.printStackTrace();
		}
	}
}
