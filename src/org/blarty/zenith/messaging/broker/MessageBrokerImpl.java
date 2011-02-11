/*
 * apollo2 : org.jini.projects.zenith.messaging.system
 * 
 * 
 * MessagingBrokerImpl.java
 * Created on 09-Mar-2004
 * 
 * MessagingBrokerImpl
 *
 */

package org.jini.projects.zenith.messaging.broker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.lookup.ServiceID;
import net.jini.lookup.LookupCache;
import net.jini.lookup.ServiceDiscoveryEvent;
import net.jini.lookup.ServiceDiscoveryListener;
import net.jini.lookup.ServiceDiscoveryManager;

import org.jini.projects.zenith.messaging.broker.leasing.MesgLandlord;
import org.jini.projects.zenith.messaging.system.MessagingService;

/**
 * Simple Implementation for the Message Broker. The broker maintains a list of
 * channels, and what service they are running in a Logical channel may be
 * spread across multiple services. For instance, the 'Services' channel may
 * exist in a number of different Messaging Managers.
 * 
 * @author calum
 */
public class MessageBrokerImpl implements MessageBroker, ServiceDiscoveryListener {
	Random r = new Random(System.currentTimeMillis());
	private long EVENT_TYPE_ID = 3440378797293423L;
	MesgLandlord landlord;
	ArrayList registrations;
	Map serviceChannels;
	Map channels;
	Logger log = Logger.getLogger("org.jini.projects.zenith.messaging.broker");
	ServiceDiscoveryManager sdm;
	LookupCache cache;
	private long currentEventNum = 0L;

	public MessageBrokerImpl() {
		channels = new HashMap();
		serviceChannels = new HashMap();
		landlord = new MesgLandlord();
	}

	// Only convert to a HashMap if more than one channel with the same name is
	// created
public void channelAdded(ServiceID messageService, String channelName) throws RemoteException {
		Object ob = channels.get(channelName);
		// System.out.println("Adding Channel: " + channelName);
		if (ob != null) {
			// Check if we currently have a single entrypoint for a channel
			if (ob instanceof ServiceID) {
				ArrayList arr = new ArrayList();
				arr.add(ob);
				arr.add(messageService);
				channels.put(channelName, arr);
				fireEvents(channelName, messageService, BrokerEvent.ADDED);
			} else if (ob instanceof ArrayList) {
				((ArrayList) ob).add(messageService);
				fireEvents(channelName, messageService, BrokerEvent.ADDED);
			} else
				log.warning("Unknown channel registration Value object");
		} else
			channels.put(channelName, messageService);
		
		ob = serviceChannels.get(messageService);
		if (ob != null) {
			// Check if we currently have a single entrypoint for a channel
			if (ob instanceof String) {
				ArrayList arr = new ArrayList();
				arr.add(ob);
				arr.add(channelName);
				serviceChannels.put(messageService, arr);				
			} else if (ob instanceof ArrayList) {
				((ArrayList) ob).add(channelName);				
			} else
				log.warning("Unknown channel registration Value object");
		} else
			serviceChannels.put(messageService, channelName);
	}
	/*
	 * @see org.jini.projects.zenith.messaging.system.MessagingBroker#channelRemoved(net.jini.core.lookup.ServiceID,
	 *           java.lang.String)
	 */
	public void channelRemoved(ServiceID messageService, String channelName) throws RemoteException {
		// TODO Complete method stub for channelRemoved
		Object ob = channels.get(channelName);
		if (ob != null) {
			if (ob instanceof ServiceID) {
				if (ob.equals(messageService)) {
					System.out.println("Removing channel definition completely: " + channelName);
					channels.remove(channelName);
					fireEvents(channelName, messageService, BrokerEvent.REMOVED);
				} else if (ob instanceof ArrayList) {
					ArrayList arr = (ArrayList) ob;
					if (arr.remove(messageService))
						System.out.println("Channel link removed for " + channelName + ": " + arr.size() + " remaining");
					fireEvents(channelName, messageService, BrokerEvent.REMOVED);
				}
			}
		}

		ob = serviceChannels.get(messageService);
		if (ob != null) {
			if (ob instanceof String) {
				if (ob.equals(channelName)) {					
					serviceChannels.remove(messageService);
				} else if (ob instanceof ArrayList) {
					ArrayList arr = (ArrayList) ob;
					if (arr.remove(channelName))
						System.out.println("serviceChannel link removed");
				}
			}
		}
	}

	public void fireEvents(String channelName, ServiceID service, int changeType) {
		System.out.println("Firing events to other messaging managers");
		Iterator regs = landlord.getRegistered().values().iterator();
		currentEventNum++;
		while (regs.hasNext()) {
			RemoteEventListener rel = (RemoteEventListener) regs.next();
			Thread t = new Thread(new Notifier(service, channelName, changeType, rel));
			t.start();
		}
	}

	/*
	 * @see org.jini.projects.zenith.messaging.system.MessagingBroker#registerInterest(net.jini.core.event.RemoteEventListener)
	 */
	public EventRegistration registerInterest(long leaseTime, RemoteEventListener mylistener) throws LeaseDeniedException, RemoteException {
		log.info("Registering for events");
		if (mylistener == null)
			System.out.println("WHy is myListener null????");
		Lease l = landlord.newLease(mylistener, leaseTime);
		EventRegistration evReg = new EventRegistration(EVENT_TYPE_ID, null, l, currentEventNum);
		log.info("Event Registration Created");
		return evReg;
	}

	class Notifier implements Runnable {
		ServiceID service;
		String channelName;
		int changeType;
		RemoteEventListener rel;

		public Notifier(ServiceID service, String channelName, int changeType, RemoteEventListener rel) {
			this.service = service;
			this.channelName = channelName;
			this.changeType = changeType;
			this.rel = rel;
		}

		public void run() {
			//			
			log.fine("Notifying");
			try {
				rel.notify(new BrokerEvent("Producer", EVENT_TYPE_ID, currentEventNum, null, service, channelName, changeType));
			} catch (RemoteException e) {
				// URGENT Handle RemoteException
				e.printStackTrace();
			} catch (UnknownEventException e) {
				// URGENT Handle UnknownEventException
				e.printStackTrace();
			}
		}
	}

	/*
	 * @see net.jini.lookup.ServiceDiscoveryListener#serviceAdded(net.jini.lookup.ServiceDiscoveryEvent)
	 */
	public void serviceAdded(ServiceDiscoveryEvent event) {
		Object ob = event.getPostEventServiceItem().service;
		ServiceID id = event.getPostEventServiceItem().serviceID;
		System.out.println("Svc Added:ID: " + id +"; type: " + ob.getClass().getName());
	}

	/*
	 * @see net.jini.lookup.ServiceDiscoveryListener#serviceRemoved(net.jini.lookup.ServiceDiscoveryEvent)
	 */
	public void serviceRemoved(ServiceDiscoveryEvent event) {
		// TODO Complete method stub for serviceRemoved
		Object ob = event.getPreEventServiceItem().service;
		ServiceID id = event.getPreEventServiceItem().serviceID;
		System.out.println("Svc Removed:ID: " + id +"; type: " + ob.getClass().getName());
		if (ob instanceof MessagingService) {
			removeAllForService(id);
			for (Iterator iter = this.channels.entrySet().iterator(); iter.hasNext();) {
				Map.Entry item = (Map.Entry) iter.next();
				if (item.getValue() instanceof ServiceID) {
					if (((ServiceID) item.getValue()).equals(id)) {
						System.out.println("Purging global channel entry for channel " + item.getKey());
						iter.remove();
					}
				}
				if (item.getValue() instanceof List) {
					List l = (List) item.getValue();
					for (Iterator listIter = l.iterator(); listIter.hasNext();) {
						ServiceID listEntry = (ServiceID) listIter.next();
						if (listEntry.equals(id)) {
							System.out.println("Removing serviceID " + id + " from global channel " + item.getKey());
							listIter.remove();
						}
					}
				}
			}
		}
	}

	public void removeAllForService(ServiceID messagingService) {
		Object ob = serviceChannels.get(messagingService);
		try {
			if (ob != null) {
				if (ob instanceof String) {
					channelRemoved(messagingService, (String) ob);
				} else if (ob instanceof ArrayList) {
					ArrayList arr = (ArrayList) ob;
					for (Iterator iter = arr.iterator(); iter.hasNext();) {
						String channelName = (String) iter.next();
						channelRemoved(messagingService, channelName);
					}

				}
				log.info("All channels for Service: " + messagingService + ", removed");
			}
		} catch (RemoteException e) {
			// TODO Handle RemoteException
			e.printStackTrace();
		}
	}

	/*
	 * @see net.jini.lookup.ServiceDiscoveryListener#serviceChanged(net.jini.lookup.ServiceDiscoveryEvent)
	 */
	public void serviceChanged(ServiceDiscoveryEvent event) {
		// TODO Complete method stub for serviceChanged
	}

	/*
	 * @see org.jini.projects.zenith.messaging.broker.MessagingBroker#getCurrentlyRegisteredChannels()
	 */
	public Map getCurrentlyRegisteredChannels() throws RemoteException {
		// TODO Complete method stub for getCurrentlyRegisteredChannels
		return channels;
	}

	/*
	 * @see org.jini.projects.zenith.messaging.broker.MessagingBroker#getServiceForChannel(java.lang.String)
	 */
	public ServiceID getServiceForChannel(String channelName) throws RemoteException {
		// TODO Complete method stub for getServiceForChannel
		Object ob = channels.get(channelName);
		System.out.println("Getting a Messaging Service ID that contains the " + channelName + " channel");
		if (ob != null) {
			if (ob instanceof List) {
				System.out.println("List found : Returning random service ID");
				List l = (List) ob;
				return (ServiceID) l.get(r.nextInt(l.size()));
			}
			if (ob instanceof ServiceID) {
				System.out.println("Single instance found - returning it");
				return (ServiceID) ob;
			}
		}
		System.out.println("No such channel registered");
		return null;
	}
}
