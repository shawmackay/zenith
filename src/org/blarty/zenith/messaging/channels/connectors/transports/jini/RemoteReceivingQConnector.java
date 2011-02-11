/*
 * apollo2 : apollo.messaging.channels.connectors.transports.jini
 * 
 * 
 * JiniReceivingQConnector.java
 * Created on 26-Feb-2004
 * 
 * JiniReceivingQConnector
 *
 */
package org.jini.projects.zenith.messaging.channels.connectors.transports.jini;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.system.MessagingListener;


/**
 * @author calum
 */
public interface RemoteReceivingQConnector extends Remote{
       
		public String getChannelName() throws RemoteException;
        
        public void setListener(MessagingListener listener) throws RemoteException;

        public abstract Message receive() throws RemoteException;

        public abstract Message receive(int timeout) throws RemoteException;
}
