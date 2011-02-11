/*
 * apollo2 : apollo.messaging.channels.connectors
 * 
 * 
 * PublishingQConnectorImpl.java
 * Created on 26-Feb-2004
 * 
 * PublishingQConnectorImpl
 *
 */
package org.blarty.zenith.messaging.channels.connectors;

import java.io.IOException;
import java.util.logging.Logger;

import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.ChannelPublisher;
import org.blarty.zenith.messaging.system.MessageAudit;
import org.blarty.zenith.messaging.system.store.StoreSystem;


/**
 * @author calum
 */
public class PublishingQConnectorImpl implements PublishingQConnector {
    Logger l = Logger.getLogger("org.blarty.zenith.messaging.channels.connectors"); 
    ChannelPublisher callback;
    
    public PublishingQConnectorImpl(ChannelPublisher channelCallback){
    	this.callback = channelCallback;
    }
    static StoreSystem sys;

    static {
        try {
            sys = new StoreSystem(System.getProperty("org.blarty.zenith.messaging.system.store.dir"));
        } catch (IOException e) {

            System.out.println("Err: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
	/* @see apollo.messaging.channels.connectors.PublishingQConnector#sendMessage(apollo.messaging.messages.Message)
	 */
	public void sendMessage(Message m) {
        l.finer("Publishing a message ");
        if(m == null)
            l.finer("and it is null");
        else
            l.finest("and it is not null");
        if (m.getHeader().isGuaranteed()) {
            l.finest("Received message is guaranteed");
            try {
                MessageAudit.getMessageAuditer().addMessage(m);
                sys.store(m.getHeader().getDestinationAddress(),m);
                l.finest("Publishing Queue has stored the message");
            } catch (IOException e) {

                System.out.println("Err: " + e.getMessage());
                e.printStackTrace();
            }
        }
		callback.sendMessage(m);
	}
    
    public String toString(){
            return callback.toString();
    }
}
