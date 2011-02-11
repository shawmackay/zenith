/*
 * apollo2 : org.jini.projects.zenith.messaging.channels.connectors.transports.jini
 * 
 * 
 * RemoteReceivingQAdapter.java
 * Created on 09-Mar-2004
 * 
 * RemoteReceivingQAdapter
 *
 */
package org.jini.projects.zenith.messaging.channels.connectors.transports.jini;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;
import net.jini.export.Exporter;
import net.jini.id.UuidFactory;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.http.HttpServerEndpoint;

import org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.jini.projects.zenith.messaging.channels.connectors.transports.jini.constrainable.RemoteMessagingListenerProxy;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.system.MessagingListener;


/**
 * @author calum
 */
public class RemoteReceivingQAdapter implements ReceivingQConnector, Serializable {
	
    /**
     * 
     */
    private static final long serialVersionUID = 3257003250530988857L;
    RemoteReceivingQConnector backend;
    private MessagingListener theListener;
    private Remote messageProxy;
    private Remote exported;
	private Exporter exporter;
	private ListenerWrapper wrap;
	private String channelName;
    public RemoteReceivingQAdapter(RemoteReceivingQConnector backend){
        this.backend = backend;
        try {
			channelName = backend.getChannelName();
		} catch (RemoteException e) {
			// TODO Handle RemoteException
			e.printStackTrace();
		}
    }
    
    

	public String getChannelName() {
		// TODO Complete method stub for getChannelName
		return channelName;
	}
	/*
	 * @see org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector#setListener(org.jini.projects.zenith.messaging.system.MessagingListener)
	 */
	public void setListener(MessagingListener listener) {
		// TODO Complete method stub for setListener
        try {
            exporter = new BasicJeriExporter(HttpServerEndpoint.getInstance(0), new BasicILFactory());
            wrap = new ListenerWrapper();
			Remote listen = exporter.export(wrap);
            exported = listen;
            theListener = listener;
            messageProxy = RemoteMessagingListenerProxy.create((RemoteMessagingListener)listen, UuidFactory.generate());
            System.out.println("Setting remote listener");
            backend.setListener((MessagingListener) messageProxy);
        } catch (ExportException e) {
            // URGENT Handle ExportException
            e.printStackTrace();
        } catch (RemoteException e) {
			// URGENT Handle RemoteException
			e.printStackTrace();
		}      
	}

	/*
	 * @see org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector#receive()
	 */
	public Message receive() {
		// TODO Complete method stub for receive
		System.out.println("receive() called");
		return null;
	}

	/*
	 * @see org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector#receive(int)
	 */
	public Message receive(int timeout) {
		// TODO Complete method stub for receive
		System.out.println("receive(int) called");
		return null;
	}
    
    private class ListenerWrapper implements RemoteMessagingListener {
        
            /*
			 * @see net.jini.core.event.RemoteEventListener#notify(net.jini.core.event.RemoteEvent)
			 */
            public void notify(RemoteEvent theEvent) throws UnknownEventException, RemoteException {
                // TODO Complete method stub for notify
            	System.out.println("Notified from Neon");
                theListener.messageReceived((Message) theEvent.getSource());
            }

            /*
			 * @see apollo.messaging.system.MessagingListener#messageReceived(apollo.messaging.messages.Message)
			 */
            
        }
        
}
