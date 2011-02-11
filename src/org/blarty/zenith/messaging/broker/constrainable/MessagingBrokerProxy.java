/*
 * apollo2 : org.jini.projects.zenith.messaging.broker.constrainable
 * 
 * 
 * MessagingBrokerProxy.java
 * Created on 09-Mar-2004
 * 
 * MessagingBrokerProxy
 *
 */
package org.jini.projects.zenith.messaging.broker.constrainable;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.logging.Logger;

import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.lookup.ServiceID;
import net.jini.id.ReferentUuids;
import net.jini.id.Uuid;

import org.jini.projects.zenith.messaging.broker.MessageBroker;


/**
 * @author calum
 */
public class MessagingBrokerProxy implements MessageBroker, Serializable {
     private static final long serialVersionUID = 2L;

     final MessageBroker backend;
     final Uuid proxyID;

     public static MessagingBrokerProxy create(MessageBroker server, Uuid id) {
         if (server instanceof RemoteMethodControl  ) {
             Logger.getAnonymousLogger().info("Registering a secure proxy");
             return new MessagingBrokerProxy.ConstrainableMessagingBrokerProxy(server, id, null);
         } else
             return new MessagingBrokerProxy(server, id);
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
 
     public MessagingBrokerProxy(MessageBroker backend, Uuid ID) {
      this.backend = backend;
      this.proxyID = ID;
     }
 
     final static class ConstrainableMessagingBrokerProxy extends MessagingBrokerProxy implements RemoteMethodControl {
        private static final long serialVersionUID = 4L;
        public ConstrainableMessagingBrokerProxy(MessageBroker server, Uuid id, MethodConstraints methodConstraints) {
            super(constrainServer(server, methodConstraints), id);

        }
        public RemoteMethodControl setConstraints(MethodConstraints constraints) {
            return new MessagingBrokerProxy.ConstrainableMessagingBrokerProxy(backend, proxyID, constraints);
        }

        /** {@inheritDoc} */
        public MethodConstraints getConstraints() {
            return ((RemoteMethodControl) backend).getConstraints();
        }

        private static MessageBroker constrainServer(MessageBroker server, MethodConstraints methodConstraints) {
            return (MessageBroker) ((RemoteMethodControl) server).setConstraints(methodConstraints);
        }

    }
     
	/**
	 * @param messageService
	 * @param channelName
	 * @throws RemoteException
	 */
	public void channelAdded(ServiceID messageService, String channelName) throws RemoteException {
		backend.channelAdded(messageService, channelName);
	}
	/**
	 * @param messageService
	 * @param channelName
	 * @throws RemoteException
	 */
	public void channelRemoved(ServiceID messageService, String channelName) throws RemoteException {
		backend.channelRemoved(messageService, channelName);
	}
	/**
	 * @param leaseTime
	 * @param mylistener
	 * @return
	 * @throws LeaseDeniedException
	 * @throws RemoteException
	 */
	public EventRegistration registerInterest(long leaseTime, RemoteEventListener mylistener) throws LeaseDeniedException, RemoteException {
		return backend.registerInterest(leaseTime, mylistener);
	}
	/* @see java.lang.Object#toString()
	 */
	public String toString() {
		return backend.toString();
	}
	/**
	 * @return
	 * @throws RemoteException
	 */
	public Map getCurrentlyRegisteredChannels() throws RemoteException {
		return backend.getCurrentlyRegisteredChannels();
	}
	/**
	 * @param channelName
	 * @return
	 * @throws RemoteException
	 */
	public ServiceID getServiceForChannel(String channelName) throws RemoteException {
		return backend.getServiceForChannel(channelName);
	}
}
