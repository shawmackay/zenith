/*
 * Apollo : org.blarty.zenith.bus.constrainable
 * 
 * 
 * RouterJointProxy.java
 * Created on 29-Jul-2003
 *
 */
package org.blarty.zenith.bus.constrainable;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.id.ReferentUuids;
import net.jini.id.Uuid;

import org.blarty.zenith.exceptions.NoSuchSubscriberException;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.router.RouterJoint;



/**
 * @author calum
 */
public class RouterJointProxy implements RouterJoint, Serializable{
	private static final long serialVersionUID = 3L;
		static Logger log = Logger.getLogger("org.blarty.zenith");
		RouterJoint backend;
		Uuid proxyID;

		Uuid nodeID;
		public static RouterJointProxy create(RouterJoint server, Uuid id) {
			if (server instanceof RemoteMethodControl) {
				log.info("Registering a secure proxy");
				return new RouterJointProxy.ConstrainableProxy(server, id, null);
			} else
				return new RouterJointProxy(server, id);
		}

		public RouterJointProxy(RouterJoint backend, Uuid proxyID) {
			if (backend==null)
			     System.out.println("Backend is null");
			this.backend = backend;			
			this.proxyID = proxyID;
			
			
			
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

		/**
		 * @param point
		 * @throws RemoteException
		 */
		

	 static class ConstrainableProxy extends RouterJointProxy implements RemoteMethodControl {
			private static final long serialVersionUID = 4L;
			private ConstrainableProxy(RouterJoint server, Uuid id,MethodConstraints methodConstraints) {
				super(constrainServer(server, methodConstraints), id);

			}
			public RemoteMethodControl setConstraints(MethodConstraints constraints) {
				return new RouterJointProxy.ConstrainableProxy(backend, proxyID, constraints);
			}

			/** {@inheritDoc} */
			public MethodConstraints getConstraints() {
				return ((RemoteMethodControl) backend).getConstraints();
			}

			private static RouterJoint constrainServer(RouterJoint server, MethodConstraints methodConstraints) {
				return (RouterJoint) ((RemoteMethodControl) server).setConstraints(methodConstraints);
			}

		}

	/**
	 * @return
	 */
	public Uuid getID() throws RemoteException{

		if (backend==null)
			System.out.println("Backend is null");		
		return backend.getID();
	}

	/**
	 * @param subscriberIdentity
	 * @return
	 * @throws RemoteException
	 */
	public boolean hostsSubscriber(Uuid subscriberIdentity) throws RemoteException {
		return backend.hostsSubscriber(subscriberIdentity);
	}

	/**
	 * @param topic
	 * @return
	 * @throws RemoteException
	 */
	public boolean hostsTopic(String topic) throws RemoteException {
		return backend.hostsTopic(topic);
	}

	/**
	 * @param subscriber
	 * @param m
	 * @return
	 * @throws NoSuchSubscriberException
	 * @throws RemoteException
	 */
	public void sendDirectedMessage(Uuid subscriber, Message m) throws NoSuchSubscriberException, RemoteException {
		backend.sendDirectedMessage(subscriber, m);
	}

	/**
	 * @param m
	 * @return
	 * @throws NoSuchSubscriberException
	 * @throws RemoteException
	 */
	public void sendMessage(String channelName, Message m) throws NoSuchSubscriberException, RemoteException {
		backend.sendMessage(channelName,m);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return backend.toString();
	}

	/* (non-Javadoc)
	 * @see org.blarty.zenith.router.RouterJoint#getNameSpace()
	 */
	public String getNameSpace() throws RemoteException {
		// URGENT Complete method stub for getNameSpace
		return backend.getNameSpace();
	}

}
