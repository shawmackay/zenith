/*
 * apollo2 : apollo.messaging.system
 * 
 * 
 * MessagingManagerIntf.java
 * Created on 26-Feb-2004
 * 
 * MessagingManagerIntf
 *
 */

package org.jini.projects.zenith.messaging.system;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.jini.projects.zenith.messaging.channels.MessageChannel;
import org.jini.projects.zenith.messaging.channels.connectors.PublishingQConnector;
import org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector;




/**
 * The Remote Interface for the Messaging Service
 * @author calum
 */
public interface MessagingService extends Remote{
	public void createChannel(String name) throws RemoteException;
	public void createChannel(MessageChannel channel) throws RemoteException;
	public void createSubscriptionChannel(String name) throws RemoteException;
	public ReceivingQConnector registerOnChannel(String name, MessagingListener listener) throws ChannelException, RemoteException;
	public PublishingQConnector getPublishingConnector(String name) throws ChannelException,RemoteException;
	public ReceivingQConnector getTemporaryChannel(MessagingListener listener) throws ChannelException, RemoteException;
	public void returnTemporaryChannel(String name) throws RemoteException;
}