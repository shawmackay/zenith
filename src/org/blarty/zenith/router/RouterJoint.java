/*
 * Apollo : org.jini.projects.zenith.router
 * 
 * 
 * RouterJoint.java
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
 * @author calum
 */
public interface RouterJoint extends Remote{
	public String getNameSpace() throws RemoteException;
	public Uuid getID() throws RemoteException;
	public boolean hostsSubscriber(Uuid subscriberIdentity) throws RemoteException;
	public boolean hostsTopic(String topic) throws RemoteException;
	public void sendMessage(String channelName,Message m) throws NoSuchSubscriberException, RemoteException;
	public void sendDirectedMessage(Uuid subscriber,Message m) throws NoSuchSubscriberException, RemoteException;
}
