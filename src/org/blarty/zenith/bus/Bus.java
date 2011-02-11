package org.jini.projects.zenith.bus;



import net.jini.id.Uuid;

import org.jini.projects.zenith.messaging.messages.Message;



// Created as: 13-Jan-2003 : enclosing_type :Bus.java
// In org.jini.projects.zenith.bus
/**
 * 
 * @author calum

 */
public interface Bus {
	public String getName();	
	public void addLink(String name, Bus linkBus);
	public void sendMessage(String channelName, Message mesg);
	public void sendDirectedMessage(Uuid identity,Message mesg);
	//public BusToken subscribe(String topic, Subscriber callback);
	//public void unsubscribe(BusToken tok);
	//public void unsubscribe(Uuid subscriber);
	public boolean hostsTopic(String topic);
	public boolean hostsSubscriber(Uuid identity);
}
