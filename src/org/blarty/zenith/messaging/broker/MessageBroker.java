/*
 * apollo2 : org.blarty.zenith.messaging.system
 * 
 * 
 * MessagingBroker.java
 * Created on 09-Mar-2004
 * 
 * MessagingBroker
 *
 */
package org.blarty.zenith.messaging.broker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.lookup.ServiceID;

/**
 * Maintains a global list of channels.
 * Used for maintaining a list of channels between systems.  
 * MessagingManagers can automatically route messages to different systems
 * If any of the following conditions apply:
 * <ul>
 * <li> A channel does not exist in the originating MessagingManager</li>
 * <li> A channel does exist in the originating MessagingManager, but is overloaded</li>
 * <li> A channel distributes load both locally and remotely</li>
    </ul>
 * @author calum
 */
public interface MessageBroker extends Remote{
    
    
    /**
     * Informs the broker that a service, contains the following channel
     * @param messageService the serviceID of the Messaging Service
     * @param channelName name of channel added
     */
    public void channelAdded(ServiceID messageService, String channelName) throws RemoteException;
    /**
     * Informs the broker that a service, has removed the following channel
     * @param messageService the serviceID of the Messaging Service
     * @param channelName name of channel added
     */
    public void channelRemoved(ServiceID messageService, String channelName) throws RemoteException;
    /**
     * Every MessagingManager should register with the broker service so that new channels
     * created by other Managers, can be 'attached' into the local Bus.  
     * @param mylistener
     * @return
     */ 
    public EventRegistration registerInterest(long leaseTime, RemoteEventListener mylistener) throws LeaseDeniedException,RemoteException;
    /**
     * Returns a map of the current channel list and services holding the channels.
     * All MessagingServices should call this method on the Broker to get the current list of channels.
     * @return the channel list
     * @throws RemoteException
     */
    public Map getCurrentlyRegisteredChannels() throws RemoteException;
    
    public ServiceID getServiceForChannel(String channelName) throws RemoteException;
}
