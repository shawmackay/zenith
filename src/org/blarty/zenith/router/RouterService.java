/*
 * Apollo : org.jini.projects.zenith.router
 * 
 * 
 * RouterService.java
 * Created on 29-Jul-2003
 *
 */
package org.jini.projects.zenith.router;

import java.rmi.Remote;
import java.rmi.RemoteException;

import net.jini.id.Uuid;

import org.jini.projects.zenith.exceptions.NoSuchSubscriberException;
import org.jini.projects.zenith.messaging.messages.Message;


/**
 * The main interface for routing a message
 * @author calum
 */
public interface RouterService extends Remote {
	public void sendMessage(String channelName, Message m) throws NoSuchSubscriberException, RemoteException;
    public void sendMessage(String channelName, Message m, String namespace) throws NoSuchSubscriberException, RemoteException;
	public void createNodePoint(RouterJoint point) throws RemoteException;
	public void deregisterNodePoint(RouterJoint point) throws RemoteException;
	public void sendDirectedMessage(Uuid subscriber,Message m) throws NoSuchSubscriberException, RemoteException;
	public void sendDirectedMessage(Uuid subscriber,Message m, String namespace) throws NoSuchSubscriberException, RemoteException;	
	
	//public RouterDetails getRouterDetails() throws RemoteException; 
}
