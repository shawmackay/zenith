/*
 * apollo2 : apollo.messaging.channels.connectors.transports.jini.constrainable
 * 
 * 
 * RemoteMessagingListenerProxy.java
 * Created on 26-Feb-2004
 * 
 * RemoteMessagingListenerProxy
 *
 */
package org.blarty.zenith.messaging.channels.connectors.transports.jini.constrainable;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.id.ReferentUuids;
import net.jini.id.Uuid;

import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.MessagingListener;


/**
 * @author calum
 */
public class RemoteMessagingListenerProxy implements RemoteEventListener, Serializable, MessagingListener {
        private static long MESSAGE_EVENT_ID=3478381L;
     private static final long serialVersionUID = 2L;
         private long seqID=0;
        final RemoteEventListener backend;
        final Uuid proxyID;

        public static RemoteMessagingListenerProxy create(RemoteEventListener server, Uuid id) {
            if (server instanceof RemoteMethodControl ) {
                Logger.getAnonymousLogger().info("Registering a secure proxy");
                return new RemoteMessagingListenerProxy.ConstrainableProxy(server, id, null);
            } else
                return new RemoteMessagingListenerProxy(server, id);
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

        public RemoteMessagingListenerProxy(RemoteEventListener backend, Uuid proxyID) {
            this.backend = backend;
            this.proxyID = proxyID;
        }

        final static class ConstrainableProxy extends RemoteMessagingListenerProxy implements RemoteMethodControl {
            private static final long serialVersionUID = 4L;
            private ConstrainableProxy(RemoteEventListener server, Uuid id, MethodConstraints methodConstraints) {
                super(constrainServer(server, methodConstraints), id);

            }
            public RemoteMethodControl setConstraints(MethodConstraints constraints) {
                return new RemoteMessagingListenerProxy.ConstrainableProxy(backend, proxyID, constraints);
            }

            /** {@inheritDoc} */
            public MethodConstraints getConstraints() {
                return ((RemoteMethodControl) backend).getConstraints();
            }

            private static RemoteEventListener constrainServer(RemoteEventListener server, MethodConstraints methodConstraints) {
                return (RemoteEventListener) ((RemoteMethodControl) server).setConstraints(methodConstraints);
            }

        }
    
	/* @see net.jini.core.event.RemoteEventListener#notify(net.jini.core.event.RemoteEvent)
	 */
	public void notify(RemoteEvent theEvent) throws UnknownEventException, RemoteException {
		// TODO Complete method stub for notify
        backend.notify(theEvent);
	}

	/* @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
	 */
	public void messageReceived(Message m) {
		try {
			// TODO Complete method stub for messageReceived
			notify(new RemoteEvent(m,MESSAGE_EVENT_ID,seqID++,null));
		} catch (RemoteException e) {
			// URGENT Handle RemoteException
			e.printStackTrace();
		} catch (UnknownEventException e) {
			// URGENT Handle UnknownEventException
			e.printStackTrace();
		}
	}
}
