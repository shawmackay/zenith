/*
 * apollo2 : apollo.messaging.system.constrainable
 * 
 * 
 * MessagingServiceProxy.java
 * Created on 26-Feb-2004
 * 
 * MessagingServiceProxy
 *
 */
package org.jini.projects.zenith.messaging.system.constrainable;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.id.ReferentUuid;
import net.jini.id.ReferentUuids;
import net.jini.id.Uuid;

import org.jini.projects.zenith.messaging.channels.MessageChannel;
import org.jini.projects.zenith.messaging.channels.connectors.PublishingQConnector;
import org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.jini.projects.zenith.messaging.system.ChannelException;
import org.jini.projects.zenith.messaging.system.MessagingListener;
import org.jini.projects.zenith.messaging.system.MessagingService;


/**
 * @author calum
 */
public class MessagingServiceProxy implements MessagingService, ReferentUuid, Serializable {
     private static final long serialVersionUID = 2L;
	 	static Logger log = Logger.getLogger("org.jini.projects.zenith");
        final MessagingService backend;
        final Uuid proxyID;

        public static MessagingServiceProxy create(MessagingService server, Uuid id) {
            if (server instanceof RemoteMethodControl  ) {
                log.info("Registering a secure proxy");
                return new MessagingServiceProxy.ConstrainableProxy(server, id, null);
            } else
                return new MessagingServiceProxy(server, id);
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
    
        public MessagingServiceProxy(MessagingService backend, Uuid ID) {
         this.backend = backend;
         this.proxyID = ID;
        }
    
    
	/* @see apollo.messaging.system.MessagingService#createChannel(java.lang.String)
	 */
	public void createChannel(String name) throws RemoteException {
		// TODO Complete method stub for createChannel
	}

	/* @see apollo.messaging.system.MessagingService#createChannel(apollo.messaging.channels.MessageChannel)
	 */
	public void createChannel(MessageChannel channel) throws RemoteException {
		// TODO Complete method stub for createChannel
	}

	/* @see apollo.messaging.system.MessagingService#createSubscriptionChannel(java.lang.String)
	 */
	public void createSubscriptionChannel(String name) throws RemoteException {
		// TODO Complete method stub for createSubscriptionChannel
	}

	/* @see apollo.messaging.system.MessagingService#registerOnChannel(java.lang.String, apollo.messaging.system.MessagingListener)
	 */
	public ReceivingQConnector registerOnChannel(String name, MessagingListener listener) throws ChannelException, RemoteException {
		// TODO Complete method stub for registerOnChannel
		return backend.registerOnChannel(name, listener);
	}

	/* @see apollo.messaging.system.MessagingService#getPublishingConnector(java.lang.String)
	 */
	public PublishingQConnector getPublishingConnector(String name) throws ChannelException, RemoteException {
		// TODO Complete method stub for getPublishingConnector
		return backend.getPublishingConnector(name);
	}

	
    
    
     final static class ConstrainableProxy extends MessagingServiceProxy implements RemoteMethodControl {
        private static final long serialVersionUID = 4L;
        private ConstrainableProxy(MessagingService server, Uuid id, MethodConstraints methodConstraints) {
            super(constrainServer(server, methodConstraints), id);

        }
        public RemoteMethodControl setConstraints(MethodConstraints constraints) {
            return new MessagingServiceProxy.ConstrainableProxy(backend, proxyID, constraints);
        }

        /** {@inheritDoc} */
        public MethodConstraints getConstraints() {
            return ((RemoteMethodControl) backend).getConstraints();
        }

        private static MessagingService constrainServer(MessagingService server, MethodConstraints methodConstraints) {
            return (MessagingService) ((RemoteMethodControl) server).setConstraints(methodConstraints);
        }

    }




	public ReceivingQConnector getTemporaryChannel(MessagingListener listener) throws ChannelException, RemoteException {
		// TODO Complete method stub for getTemporaryChannel
		return backend.getTemporaryChannel(listener);
	}

	public void returnTemporaryChannel(String name) throws RemoteException{
		// TODO Complete method stub for returnTemporaryChannel
		backend.returnTemporaryChannel(name);
	}
    
}
