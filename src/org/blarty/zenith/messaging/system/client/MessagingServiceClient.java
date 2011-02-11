/*
 * apollo2 : apollo.messaging.system.client
 * 
 * 
 * MessagingServiceClient.java
 * Created on 26-Feb-2004
 * 
 * MessagingServiceClient
 *
 */

package org.blarty.zenith.messaging.system.client;

import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscoveryManager;

import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;
import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.messages.MessageHeader;
import org.blarty.zenith.messaging.messages.StringMessage;
import org.blarty.zenith.messaging.system.ChannelException;
import org.blarty.zenith.messaging.system.MessagingListener;
import org.blarty.zenith.messaging.system.MessagingService;


/**
 * @author calum
 */
public class MessagingServiceClient implements DiscoveryListener {
	LookupDiscoveryManager ldm;
	private ReceivingQConnector rqc;
	private PublishingQConnector pqc;

	public MessagingServiceClient() {
		try {
			ldm = new LookupDiscoveryManager(new String[]{"uk.co.cwa.jini2"}, null, this);
			synchronized (this) {
				wait(0);
			}
		} catch (IOException e) {
			// TODO Handle IOException
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Handle InterruptedException
			e.printStackTrace();
		}
	}

	/*
	 * @see net.jini.discovery.DiscoveryListener#discovered(net.jini.discovery.DiscoveryEvent)
	 */
	public void discovered(DiscoveryEvent e) {
		ServiceRegistrar reg = e.getRegistrars()[0];
		try {
			MessagingService mySvc = (MessagingService) reg.lookup(new ServiceTemplate(null, new Class[]{MessagingService.class}, null));
			pqc = mySvc.getPublishingConnector("Simple1");
			System.out.println("Getting receiver");
			rqc = mySvc.registerOnChannel("MyReturn",null);
			rqc.setListener(new MessagingListener() {
				/* @see org.blarty.zenith.messaging.system.MessagingListener#messageReceived(org.blarty.zenith.messaging.messages.Message)
				 */
				public void messageReceived(Message m) {
					// TODO Complete method stub for messageReceived
                    System.out.println("This is the message I have received from the server:\n" + m.getMessageContentAsString());
				}
			});
			if (rqc == null)
				System.out.println("Receiver is null!!!!");
			if (pqc == null)
				System.out.println("Connector is null!!!!");
            MessageHeader hdr = new MessageHeader();
            hdr.setReplyAddress("MyReturn");
			pqc.sendMessage(new StringMessage(hdr                                                                                                                                                         , "Hello!!!!"));
			Thread.sleep(10000);
			Message m = rqc.receive(0);
            if(m!=null)
            	System.out.println("Message Received:  = " + m.toString());
		} catch (RemoteException e1) {
			// URGENT Handle RemoteException
			e1.printStackTrace();
		} catch (ChannelException e1) {
			// URGENT Handle ChannelException
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// URGENT Handle InterruptedException
			e1.printStackTrace();
		}
	}

	
    public void messageReceived(Message m) {
        // TODO Complete method stub for messageReceived
        System.out.println("Async Reply received = " + m.toString());
    }
    
	/*
	 * @see net.jini.discovery.DiscoveryListener#discarded(net.jini.discovery.DiscoveryEvent)
	 */
	public void discarded(DiscoveryEvent e) {
		// TODO Complete method stub for discarded
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		new MessagingServiceClient();
	}
}
