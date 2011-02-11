/*
 * Apollo : org.blarty.zenith.router
 * 
 * 
 * RouterServiceImpl.java
 * Created on 29-Jul-2003
 *
 */
package org.blarty.zenith.router;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.jini.id.Uuid;

import org.blarty.zenith.exceptions.NoSuchSubscriberException;
import org.blarty.zenith.messaging.messages.Message;


/**
 * @author calum
 */
public class RouterServiceImpl implements RouterService {
	private Map joints = new HashMap();
	private Map namespaces = new TreeMap();
	/**
	 * 
	 */
	private Logger l = Logger.getLogger("Router");
	private Random r = new Random(System.currentTimeMillis());
	public RouterServiceImpl() {
		super();
		// TODO Complete constructor stub for RouterServiceImpl
	}

	/* (non-Javadoc)
	 * @see org.blarty.zenith.router.RouterService#sendMessage(org.blarty.zenith.message.Message)
	 */
	public void sendMessage(String channelName, Message m) throws NoSuchSubscriberException, RemoteException {
		// TODO Complete method stub for sendMessage
		ArrayList availableBuses = new ArrayList();
		Iterator iter = joints.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			RouterJoint point = (RouterJoint) entry.getValue();
			if (point.hostsTopic(channelName)) {
				l.info("Found an available Router Node Point");
				availableBuses.add(point);
			}
		}
		if (availableBuses.size() > 0) {
			if (availableBuses.size() == 1){
				//Send to the only available bus
                Message wrapMesg = wrapMessage(m);
                System.out.println("Sending Message now");  
				((RouterJoint) availableBuses.get(0)).sendMessage(channelName,wrapMesg);
            }
			else {
				//Choose a bus at random
				int choice = r.nextInt(availableBuses.size());
				//l.info("Choosing avialbleBus "  +choice+ " of " + availableBuses.size());
				((RouterJoint) availableBuses.get(choice)).sendMessage(channelName,wrapMessage(m));
			}

		}
		l.warning("Topic [" + channelName +"] does not exist and/or cannot be routed to");
		
	}

	/* (non-Javadoc)
	 * @see org.blarty.zenith.router.RouterService#createNodePoint(org.blarty.zenith.router.RouterJoint)
	 */
	public void createNodePoint(RouterJoint point) throws RemoteException {
		// TODO Complete method stub for createNodePoint
		l.info("A RouterJoint is being created as a node point");
		//synchronized (joints) {
		try {
			Uuid jointID = point.getID();
			String ns = point.getNameSpace();
			if (namespaces.containsKey(ns)) {
				List l = (List) namespaces.get(ns);
				l.add(jointID);
			} else {
				List l = new ArrayList();
				l.add(jointID);
				namespaces.put(ns, l);
			}
			joints.put(jointID, point);
		} catch (Exception ex) {
			System.out.println("Err: " + ex.getMessage());
			ex.printStackTrace();
		}
		//}
		l.info("Node Point created");
		displayRouter();
	}

	/* (non-Javadoc)
	 * @see org.blarty.zenith.router.RouterService#sendDirectedMessage(net.jini.id.Uuid, org.blarty.zenith.message.Message)
	 */
	public void sendDirectedMessage(Uuid subscriber, Message m) throws NoSuchSubscriberException, RemoteException {
		// TODO Complete method stub for sendDirectedMessage
		ArrayList availableBuses = new ArrayList();
		Iterator iter = joints.entrySet().iterator();
		while (iter.hasNext()) {
			System.out.println("Checking a bus");
			Object ob = iter.next();
			System.out.println(ob.getClass().getName());
			RouterJoint point = (RouterJoint) ((Map.Entry)ob).getValue();
			if (point.hostsSubscriber(subscriber)) {
				l.info("Found an available Router Node Point containing the subscriber");
				point.sendDirectedMessage(subscriber, m);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.blarty.zenith.router.RouterService#deregisterNodePoint(org.blarty.zenith.router.RouterJoint)
	 */
	public void deregisterNodePoint(RouterJoint point) throws RemoteException {
		// TODO Complete method stub for deregisterNodePoint
		l.info("A Node point is being deregistered");
		Uuid jointID = point.getID();
		String ns = point.getNameSpace();
		synchronized (joints) {
			if (joints.containsKey(jointID)) {

				joints.remove(jointID);
				List nslist = (List) namespaces.get(ns);
				nslist.remove(jointID);
				displayRouter();
				l.info("Node Point removed");
			} else
				l.warning("An attempt has been made to deregister a non-existant NodePoint");
		}

	}

	private void displayRouter() {
		String spacer = "                                    ";
		System.out.println(spacer + "Entry");
		System.out.println(spacer + "  |");

		int spacerlen = spacer.length();
		synchronized (joints) {
			Set s = joints.keySet();
			ArrayList l = new ArrayList(s);
			for (int i = 0; i < l.size(); i++) {
				String str = l.get(i).toString();
				if (i % 2 == 0) {
					System.out.print(str);
					for (int j = 0; j < (spacerlen - str.length()); j++)
						System.out.print("-");
					System.out.println("--|");
				} else {
					System.out.print(spacer + "  |");
					for (int j = 0; j < (spacerlen - str.length()) + 2; j++)
						System.out.print("-");
					System.out.println(str);
				}
			}
		}
		System.out.println("Registered namespaces: " + namespaces);
	}
	/* (non-Javadoc)
	 * @see org.blarty.zenith.router.RouterService#sendDirectedMessage(net.jini.id.Uuid, org.blarty.zenith.message.Message, java.lang.String)
	 */
	public void sendDirectedMessage(Uuid subscriber, Message m, String namespace) throws NoSuchSubscriberException, RemoteException {
		ArrayList availableBuses = new ArrayList();
		List nsList = (List) namespaces.get(namespace);
		
		Iterator iter = nsList.iterator();
		while (iter.hasNext()) {
			RouterJoint point = (RouterJoint) iter.next();
			if (point.hostsSubscriber(subscriber)) {
				l.info("Found an available Router Node Point, in the " + namespace +" namespace, containing the subscriber");
				point.sendDirectedMessage(subscriber, m);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.blarty.zenith.router.RouterService#sendMessage(org.blarty.zenith.message.Message, java.lang.String)
	 */
	public void sendMessage(String channelName, Message m, String namespace) throws NoSuchSubscriberException, RemoteException {
		ArrayList availableBuses = new ArrayList();
		List nsList = (List) namespaces.get(namespace);

		Iterator iter = nsList.iterator();
		while (iter.hasNext()) {
			Uuid entry = (Uuid) iter.next();
			RouterJoint point = (RouterJoint) joints.get(entry);
			if (point.hostsTopic(channelName)) {
				l.info("Found an available Router Node Point");
				availableBuses.add(point);
			}
		}
		if (availableBuses.size() > 0) {
			if (availableBuses.size() == 1){
			     
			 	//Send to the only available bus
				((RouterJoint) availableBuses.get(0)).sendMessage(channelName,m);
            }else {
				//Choose a bus at random
				int choice = r.nextInt(availableBuses.size());
				l.info("Choosing avialbleBus "  +choice+ " of " + availableBuses.size());
				((RouterJoint) availableBuses.get(choice)).sendMessage(channelName,m);
			}

		}
		l.warning("Topic [" + channelName + "] does not exist in the " + namespace + " namespace and/or cannot be routed to");
		
	}
	
	private Message wrapMessage(Message m){
        System.out.println("Wrapping a message....");
        if(m==null)
            System.out.println("....but it is null?");
        m.getHeader().setArbitraryField("routeable", "no");
		return m;
    }
}
