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

import org.blarty.zenith.messaging.channels.connectors.transports.jini.RemotePublishingQConnector;
import org.blarty.zenith.messaging.messages.Message;


/**
 * @author calum
 */
public class PublishingQConnectorProxy implements RemotePublishingQConnector, Serializable,ReferentUuid{
    
    private static final long serialVersionUID = 2L;

    final RemotePublishingQConnector backend;
    final Uuid proxyID;

    public static PublishingQConnectorProxy create(RemotePublishingQConnector server, Uuid id) {
        if (server instanceof RemoteMethodControl ) {            
            return new PublishingQConnectorProxy.ConstrainableProxy(server, id, null);
        } else
            return new PublishingQConnectorProxy(server, id);
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

    public PublishingQConnectorProxy(RemotePublishingQConnector backend, Uuid proxyID) {
        this.backend = backend;
        this.proxyID = proxyID;
    }

	/* @see apollo.messaging.channels.connectors.PublishingQConnector#sendMessage(apollo.messaging.messages.Message)
	 */
	public void sendMessage(Message m) {
		try {
			// TODO Complete method stub for sendMessage
			backend.sendMessage(m);
		} catch (RemoteException e) {
			// URGENT Handle RemoteException
			e.printStackTrace();
		}
	}
    
    final static class ConstrainableProxy extends PublishingQConnectorProxy implements RemoteMethodControl {
        private static final long serialVersionUID = 4L;
        private ConstrainableProxy(RemotePublishingQConnector server, Uuid id, MethodConstraints methodConstraints) {
            super(constrainServer(server, methodConstraints), id);

        }
        public RemoteMethodControl setConstraints(MethodConstraints constraints) {
            return new PublishingQConnectorProxy.ConstrainableProxy(backend, proxyID, constraints);
        }

        /** {@inheritDoc} */
        public MethodConstraints getConstraints() {
            return ((RemoteMethodControl) backend).getConstraints();
        }

        private static RemotePublishingQConnector constrainServer(RemotePublishingQConnector server, MethodConstraints methodConstraints) {
            return (RemotePublishingQConnector) ((RemoteMethodControl) server).setConstraints(methodConstraints);
        }

    }
    
}
