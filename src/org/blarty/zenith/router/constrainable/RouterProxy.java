/*
 * Apollo : org.jini.projects.zenith.router.constrainable
 * 
 * 
 * RouterProxy.java
 * Created on 29-Jul-2003
 *
 */
package org.jini.projects.zenith.router.constrainable;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import net.jini.core.constraint.MethodConstraints;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.id.ReferentUuids;
import net.jini.id.Uuid;

import org.jini.projects.zenith.exceptions.NoSuchSubscriberException;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.router.RouterJoint;
import org.jini.projects.zenith.router.RouterService;


/**
 * @author calum
 */
public class RouterProxy implements RouterService, Serializable {
	private static final long serialVersionUID = 2L;
	static Logger log = Logger.getLogger("org.jini.projects.zenith");
	final RouterService backend;
	final Uuid proxyID;

	public static RouterProxy create(RouterService server, Uuid id) {
		if (server instanceof RemoteMethodControl) {
			log.info("Registering a secure proxy");
			return new RouterProxy.ConstrainableProxy(server, id, null);
		} else
			return new RouterProxy(server, id);
	}

	public RouterProxy(RouterService backend, Uuid proxyID) {
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
	public void createNodePoint(RouterJoint point) throws RemoteException {
		backend.createNodePoint(point);
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
	public void sendMessage(String channelName ,Message m) throws NoSuchSubscriberException, RemoteException {
		backend.sendMessage(channelName,m);
	}

	final static class ConstrainableProxy extends RouterProxy implements RemoteMethodControl {
		private static final long serialVersionUID = 4L;
		private ConstrainableProxy(RouterService server, Uuid id, MethodConstraints methodConstraints) {
			super(constrainServer(server, methodConstraints), id);

		}
		public RemoteMethodControl setConstraints(MethodConstraints constraints) {
			return new RouterProxy.ConstrainableProxy(backend, proxyID, constraints);
		}

		/** {@inheritDoc} */
		public MethodConstraints getConstraints() {
			return ((RemoteMethodControl) backend).getConstraints();
		}

		private static RouterService constrainServer(RouterService server, MethodConstraints methodConstraints) {
			return (RouterService) ((RemoteMethodControl) server).setConstraints(methodConstraints);
		}

	}
	/* (non-Javadoc)
	 * @see org.jini.projects.zenith.router.RouterService#deregisterNodePoint(org.jini.projects.zenith.router.RouterJoint)
	 */
	public void deregisterNodePoint(RouterJoint point) throws RemoteException {
		// TODO Complete method stub for deregisterNodePoint
		backend.deregisterNodePoint(point);

	}

	/* (non-Javadoc)
	 * @see org.jini.projects.zenith.router.RouterService#sendDirectedMessage(net.jini.id.Uuid, org.jini.projects.zenith.message.Message, java.lang.String)
	 */
	public void  sendDirectedMessage(Uuid subscriber, Message m, String namespace) throws NoSuchSubscriberException, RemoteException {
		// URGENT Complete method stub for sendDirectedMessage
		backend.sendDirectedMessage(subscriber, m,namespace);
	}

	/* (non-Javadoc)
	 * @see org.jini.projects.zenith.router.RouterService#sendMessage(org.jini.projects.zenith.message.Message, java.lang.String)
	 */
	public void sendMessage(String channelName ,Message m, String namespace) throws NoSuchSubscriberException, RemoteException {
		// URGENT Complete method stub for sendMessage
		backend.sendMessage(channelName,m, namespace);
	}

    
}
