/*
 * Apollo : org.jini.projects.zenith.bus
 * 
 * 
 * BusJoint.java
 * Created on 29-Jul-2003
 *
 */
package org.jini.projects.zenith.bus;

import java.rmi.RemoteException;

import net.jini.id.Uuid;
import net.jini.id.UuidFactory;

import org.jini.projects.zenith.exceptions.NoSuchSubscriberException;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.router.RouterJoint;


/**
 * @author calum
 */
public class BusJoint implements RouterJoint{
	Bus b;
	Uuid jointID;
	String namespace;
	/**
	 * 
	 */
	public BusJoint(Bus mesgBus) throws RemoteException{		
		if (jointID==null){		
			jointID = UuidFactory.generate();
			System.out.println("Joint ID initialised: " + jointID.toString());
		}
		this.b = mesgBus;
		this.namespace = b.getName();
	}
	
	

	/* (non-Javadoc)
	 * @see org.jini.projects.zenith.router.RouterJoint#hostsSubscriber(net.jini.id.Uuid)
	 */
	public boolean hostsSubscriber(Uuid subscriberIdentity) throws RemoteException {
		
		return b.hostsSubscriber(subscriberIdentity);
	}

	/* (non-Javadoc)
	 * @see org.jini.projects.zenith.router.RouterJoint#hostsTopic(java.lang.String)
	 */
	public boolean hostsTopic(String topic) throws RemoteException {
		
		return b.hostsTopic(topic);
	}

	/**
	 * @param identity
	 * @param mesg
	 * @return
	 */
	public void sendDirectedMessage(Uuid identity, Message mesg) throws NoSuchSubscriberException, RemoteException{
		 b.sendDirectedMessage(identity, mesg);
	}

	/**
	 * @param mesg
	 * @return
	 */
	public void sendMessage(String channelName, Message mesg) throws NoSuchSubscriberException, RemoteException{
        System.out.println("Received a message");
        b.sendMessage(channelName,mesg);
	}

	/* (non-Javadoc)
	 * @see org.jini.projects.zenith.router.RouterJoint#getID()
	 */
	public Uuid getID() throws RemoteException{
		// TODO Complete method stub for getID
		
		System.out.println("Getting ID: " + jointID);	
		return jointID;
	}

	/* (non-Javadoc)
	 * @see org.jini.projects.zenith.router.RouterJoint#getNameSpace()
	 */
	public String getNameSpace() throws RemoteException {		
		return this.namespace;
	}

}
