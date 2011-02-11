/*
 * apollo2 : apollo.messaging.channels.connectors.transports.jini
 * 
 * 
 * RemoteMessagingListener.java
 * Created on 26-Feb-2004
 * 
 * RemoteMessagingListener
 *
 */
package org.blarty.zenith.messaging.channels.connectors.transports.jini;

import java.rmi.RemoteException;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;

/**
 * @author calum
 */
public interface RemoteMessagingListener extends RemoteEventListener{
	/* @see net.jini.core.event.RemoteEventListener#notify(net.jini.core.event.RemoteEvent)
	 */
	public void notify(RemoteEvent theEvent) throws UnknownEventException, RemoteException ;
	/* @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
	 */	
}
