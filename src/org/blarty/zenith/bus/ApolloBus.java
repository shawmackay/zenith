package org.jini.projects.zenith.bus;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Logger;

import net.jini.id.Uuid;

import org.jini.projects.zenith.messaging.channels.PublishSubscribeChannel;
import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.channels.connectors.PublishingQConnector;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.system.ChannelException;
import org.jini.projects.zenith.messaging.system.MessagingListener;
import org.jini.projects.zenith.messaging.system.MessagingManager;


// Created as: 13-Jan-2003 : enclosing_type :ApolloBus.java
// In org.jini.projects.zenith.bus
/**
* @author calum
*
*/
public class ApolloBus implements Bus {
	String name;
	String dat;
	Random rand = new Random(System.currentTimeMillis());
	Logger l = Logger.getLogger("org.jini.projects.zenith.Bus");
	//HashMap attachedBuses = new HashMap();
	MessagingManager mgr = MessagingManager.getManager();
	
	public ApolloBus(String namespace) {
		this.name = namespace;
		try {
			dat = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
	}
	
	/**
	* @see org.jini.projects.zenith.bus.Bus#subscribe(java.lang.String)
	*/
	public BusToken subscribe(String topic, MessagingListener callback) {
		ReceiverChannel channel = null;
		try {
			channel = mgr.getReceivingChannel(topic);
		} catch (ChannelException cex) {
		}
		if (channel == null)
			mgr.createSubscriptionChannel(toString());
		try {
			channel = mgr.getReceivingChannel(topic);
		} catch (ChannelException cex) {
		}
		try {
			channel.setReceivingListener(callback);
		} catch (ChannelException e) {
			System.out.println("Err: " + e.getMessage());
			e.printStackTrace();
		}
		//return subscr.addSubscriber(callback);
		return null;
	}
	
	/**
	* @see org.jini.projects.zenith.bus.Bus#unsubscribe(org.jini.projects.zenith.bus.BusToken)
	*/
	/*
	* public void unsubscribe(BusToken tok) { String tokstr = (String)
	* tok.getToken(); String topic = tokstr.substring(tokstr.lastIndexOf('|') + 1,
	* tokstr.lastIndexOf(':')); l.finest("Finding Topic: " + topic);
	* MessageList subscr = findSubscription(topic); String sub = (String)
	* tok.getToken(); String id = sub.substring(sub.lastIndexOf(':') + 1);
	* l.finest("ID:" + id); Integer in = (Integer) subscriptionCount.get(id);
	* l.finest("Subscriber is currently subscribed to " + in.intValue() + "
	* topics"); subscr.removeSubscriber(tok); if (in.intValue() == 1) { //This
	* is the last subscription = remove it from the // allsubscribers list
	* subscriptionCount.remove(id); allSubscribers.remove(id);
	* l.finest("Subscriptions deleted"); } else { in = new
	* Integer(in.intValue() - 1); subscriptionCount.put(id, in);
	* l.finest("Subscriber is now currently subscribed to " + in.intValue() + "
	* topics"); } System.out.println(subscr.getSubscribers()); }
	*
	* public void unsubscribe(Uuid subscriber) { if
	* (subscriptionCount.containsKey(subscriber.toString())) { ArrayList list =
	* (ArrayList) subscriptionCount.get(subscriber.toString());
	* l.finest("Subscriber " + subscriber + "is subscribed to:"); for (int i = 0;
	* i < list.size(); i++) { MessageList mlist = findSubscription((String)
	* list.get(i)); mlist.removeSubscriber(subscriber); }
	* allSubscribers.remove(subscriber.toString()); } else
	* l.warning("Subscriber does not exist!");
	*/
	public static void main(String[] args) {
	}
	
	private PublishingQConnector getPublishingChannel(String topic) {
		if (mgr.containsChannel(topic)) {
			try {
				return mgr.getPublishingConnector(topic);
			} catch (ChannelException e) {
				// URGENT Handle ChannelException
				e.printStackTrace();
			}
			return null;
		} else
		return null;
	}
	
	/**
	* @see org.jini.projects.zenith.bus.Bus#sendMessage
	*/
	public void sendMessage(String channelName, Message mesg) {
		l.finest("Checking channels");
		if (mgr.containsChannel(channelName)) {
			PublishingQConnector subscr = getPublishingChannel(channelName);
			// If we have a local subscription i.e. within this bus,use it
			//Otherwise scan for any attached buses
			if (mgr.getChannel(channelName) instanceof PublishSubscribeChannel)
				System.out.println("Sending PUB SUB!");
			if (subscr != null)
				subscr.sendMessage(mesg);
		}
	}
	
	/*
	* (non-Javadoc)
	*
	* @see org.jini.projects.zenith.bus.Bus#sendDirectedMessage(net.jini.id.Uuid,
	*           org.jini.projects.zenith.message.Message)
	*/
	public void sendDirectedMessage(Uuid identity, Message mesg) {
		l.finest("Checking channels");
		if (mgr.containsChannel(identity.toString())) {
			PublishingQConnector subscr = getPublishingChannel(identity.toString());
			// If we have a local subscription i.e. within this bus,use it
			//Otherwise scan for any attached buses
			if (mgr.getChannel(identity.toString()) instanceof PublishSubscribeChannel)
				System.out.println("Sending PUB SUB!");
			if (subscr != null)
				subscr.sendMessage(mesg);
		}		
		
	}
	
	/*
	* (non-Javadoc)
	*
	* @see org.jini.projects.zenith.bus.Bus#hostsSubscriber(net.jini.id.Uuid)
	*/
	public boolean hostsSubscriber(Uuid identity) {
		// TODO Complete method stub for hostsSubscriber
		l.finest("Looking for channel: " + identity.toString());
		return mgr.containsChannel(identity.toString());
	}
	
	/*
	* (non-Javadoc)
	*
	* @see org.jini.projects.zenith.bus.Bus#hostsTopic(java.lang.String)
	*/
	//URGENT Need to check for cyclical calls.
	public boolean hostsTopic(String topic) {
		return mgr.containsChannel(topic);
	}
	
	//	public boolean hostsTopicNonCyclical(String topic, ArrayList pastTopics)
	// {
	//		// TODO Complete method stub for hostsTopic
	//		//l.info("Bus - Registered topics: " + topics);
	//		if (topics.containsKey(topic.toLowerCase())) {
	//			MessageList list = (MessageList) topics.get(topic.toLowerCase());
	//			if (list.getNumSubscribers() != 0) {
	//				return true;
	//			} else {
	//				l.info("Topic registered, but no subscribers");
	//				return false;
	//			}
	//		}
	//		return false;
	//	}
	/*
	* (non-Javadoc)
	*
	* @see org.jini.projects.zenith.bus.Bus#addLink(org.jini.projects.zenith.bus.Bus)
	*/
	public void addLink(String name, Bus linkBus) {
		// TODO Complete method stub for addLink
		//attachedBuses.put(name, linkBus);
	}
	
	/*
	* (non-Javadoc)
	*
	* @see org.jini.projects.zenith.bus.Bus#getName()
	*/
	public String getName() {
		// URGENT Complete method stub for getName
		return this.name;
	}
}
