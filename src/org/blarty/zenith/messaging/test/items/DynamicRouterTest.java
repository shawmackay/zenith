/*
 * Apollo : apollo.messaging.test.router.simple
 * 
 * 
 * DynamicRouterTest.java
 * Created on 23-Feb-2004
 * 
 * DynamicRouterTest
 *
 */

package org.jini.projects.zenith.messaging.test.items;

import org.jini.projects.zenith.messaging.channels.InvalidMessageChannel;
import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.channels.connectors.PublishingQConnector;
import org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.jini.projects.zenith.messaging.components.ControllableComponent;
import org.jini.projects.zenith.messaging.components.DefaultControllableComponent;
import org.jini.projects.zenith.messaging.components.routers.DynamicRouterAction;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.messages.MessageHeader;
import org.jini.projects.zenith.messaging.messages.StringMessage;
import org.jini.projects.zenith.messaging.system.ChannelException;
import org.jini.projects.zenith.messaging.system.MessagingManager;
import org.jini.projects.zenith.messaging.system.UnsupportedActionException;
import org.jini.projects.zenith.messaging.test.TestItem;


/**
 * @author calum
 */
public class DynamicRouterTest implements TestItem{
        public void run(MessagingManager mgr) {
		//Setup the Manager and the various channels;
		
		mgr.createChannel("ChannelA");
		mgr.createChannel("ChannelB");
		mgr.createChannel("ChannelC");
		mgr.createChannel("ChannelD");
		mgr.createChannel("RouterControl");
		mgr.createChannel("InputChannel");
		mgr.addChannel(new InvalidMessageChannel("invalid"));
		try {
                
                 ControllableComponent component = new DefaultControllableComponent();
                 component.setChannelAction(new DynamicRouterAction());
                 component.setInvalidChannel(mgr.getSendingChannel("invalid"));
                 component.setControlChannel(mgr.getReceivingChannel("RouterControl"));
                 component.setInputChannel(mgr.getReceivingChannel("InputChannel"));		
                 
			PublishingQConnector inputChannel = mgr.getSendingChannel("InputChannel").getPublishingQConnector();
			PublishingQConnector controlChannel = mgr.getSendingChannel("RouterControl").getPublishingQConnector();
			controlChannel.sendMessage(new StringMessage(new MessageHeader(), "A:ChannelA"));
			controlChannel.sendMessage(new StringMessage(new MessageHeader(), "B:ChannelB"));
			//            try {
			//				Thread.sleep(1000);
			//			} catch (InterruptedException e1) {
			//				// URGENT Handle InterruptedException
			//				e1.printStackTrace();
			//			}
			inputChannel.sendMessage(new StringMessage(new MessageHeader(), "ABCDE"));
			//controlChannel.sendMessage(new StringMessage(new
			// MessageHeader(), "A:ChannelC"));
			System.out.println("Woken Up");
			inputChannel.sendMessage(new StringMessage(new MessageHeader(), "AAAAA"));
			System.out.println("Getting Data");
			inputChannel.sendMessage(new StringMessage(new MessageHeader(), "AAAAA"));
			inputChannel.sendMessage(new StringMessage(new MessageHeader(), "AAAAA"));
			inputChannel.sendMessage(new StringMessage(new MessageHeader(), "AAAAA"));
			ReceiverChannel endLine = mgr.getReceivingChannel("ChannelA");
			ReceivingQConnector connector = endLine.createSynchronousRegistration();
			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
                e.printStackTrace();
			}
            
            inputChannel.sendMessage(new StringMessage(new MessageHeader(), "AAAAA4"));
            System.out.println("Receiving ");
			Message m = connector.receive(1000);
			if (m != null)
				System.out.println(m.getMessageContentAsString());
			else
				System.out.println("No message available at present time");
            m = connector.receive(1000);
            if (m != null)
                System.out.println(m.getMessageContentAsString());
            else
                System.out.println("No message available at present time");
            System.exit(0);
		} catch (ChannelException e) {
			// URGENT Handle ChannelException
			e.printStackTrace();
		} catch (UnsupportedActionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } 
	}

	public static void main(String[] args) {
		new DynamicRouterTest();
	}
}
