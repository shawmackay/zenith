package org.blarty.zenith.messaging.test.items;

/*
* PubSubTest.java
*
* Created Mon Apr 04 11:34:39 BST 2005
*/
import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;
import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.messages.MessageHeader;
import org.blarty.zenith.messaging.messages.StringMessage;
import org.blarty.zenith.messaging.system.ChannelException;
import org.blarty.zenith.messaging.system.MessagingListener;
import org.blarty.zenith.messaging.system.MessagingManager;

/**
*
* @author  calum
*
*/

public class PubSubTest implements org.blarty.zenith.messaging.test.TestItem{
	public void run(final MessagingManager mgr){
		mgr.createSubscriptionChannel("TestChannel");
		PublishingQConnector send = null;
		try {
			send = mgr.getSendingChannel("TestChannel").getPublishingQConnector();
		} catch (ChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mgr.createChannel("MyReplyChannel");
		MessageHeader header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		header.setGuaranteed(true);
		//Register some receivers
		ReceivingQConnector s1;
		ReceivingQConnector s2;
		try {
			mgr.registerOnChannel("MyReplyChannel", new MessagingListener() {
				/*
				* @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
				*/
				public void messageReceived(Message m) {
					// TODO Complete method stub for messageReceived
					System.out.println("Asynch Reply received: " + m.toString());
				}
			});
			s1 = mgr.registerOnChannel("TestChannel", new MessagingListener() {
				/*
				* (non-Javadoc)
				*
				* @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
				*/
				public void messageReceived(Message m) {
					// TODO Auto-generated method stub
					System.out.println("Message Received (1): - " + m.toString());
				}
			});
			s2 = mgr.registerOnChannel("TestChannel", new MessagingListener() {
				/*
				* (non-Javadoc)
				*
				* @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
				*/
				public void messageReceived(Message m) {
					// TODO Auto-generated method stub
					System.out.println("Message Received (2): - " + m.toString());
					String reply = m.getHeader().getReplyAddress();
					if (reply != null) {
						try {
							PublishingQConnector replyChannel = mgr.getPublishingConnector(reply);
							MessageHeader header = new MessageHeader();
							header.setCorrelationID(m.getHeader().getRequestID());
							Message replyMsg = new StringMessage(header, "This is a reply");
							System.out.println("Sending Reply");
							replyChannel.sendMessage(replyMsg);
						} catch (ChannelException e) {
							// URGENT Handle ChannelException
							e.printStackTrace();
						}
					} else
					System.out.println("Reply address is null");
				}
			});
		} catch (ChannelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
		System.out.println("Listener registered");
		send.sendMessage(new StringMessage(header, "Hello Message"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "a"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "b"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "c"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "d"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "e"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "f"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		header = new MessageHeader();
		header.setReplyAddress("MyReplyChannel");
		send.sendMessage(new StringMessage(header, "g"));
		System.out.println("All messages added");
	}
}
