/*
 * apollo2 : org.jini.projects.zenith.messaging.broker
 * 
 * 
 * BrokerEvent.java
 * Created on 09-Mar-2004
 * 
 * BrokerEvent
 *
 */
package org.jini.projects.zenith.messaging.broker;

import java.rmi.MarshalledObject;

import net.jini.core.event.RemoteEvent;
import net.jini.core.lookup.ServiceID;

/**
 * @author calum
 */
public class BrokerEvent extends RemoteEvent {
    
   /**
     * 
     */
    private static final long serialVersionUID = 3688792470498914359L;
public static final int ADDED=1;
   public static final int REMOVED=2;
    
    private ServiceID theChangingService;
    private String theChangingChannel;
   private int theChangeType;
	
	public BrokerEvent(Object source, long eventID, long seqNum, MarshalledObject handback, ServiceID service, String channelName, int EventType) {
		super(source, eventID, seqNum, handback);
		// URGENT Complete constructor stub for BrokerEvent
        this.theChangingService = service;
        this.theChangingChannel = channelName;
        this.theChangeType = EventType;
	}
	    /**
 * @return Returns the theChangeType.
 */
public int getChangeType() {
	return this.theChangeType;
} 

	/**
	 * @return Returns the theChangingChannel.
	 */
	public String getChangingChannel() {
		return this.theChangingChannel;
	}
	/**
	 * @return Returns the theChangingService.
	 */
	public ServiceID getChangingService() {
		return this.theChangingService;
	}
}
