/*
 * apollo2 : apollo.messaging.system
 * 
 * 
 * MessagingServiceImpl.java
 * Created on 26-Feb-2004
 * 
 * MessagingServiceImpl
 *
 */

package org.blarty.zenith.messaging.system;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import net.jini.id.UuidFactory;

import org.jini.glyph.chalice.DefaultExporterManager;
import org.jini.glyph.chalice.ExporterManager;
import org.blarty.zenith.messaging.channels.MessageChannel;
import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;
import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.blarty.zenith.messaging.channels.connectors.transports.jini.JiniPublishingQConnectorImpl;
import org.blarty.zenith.messaging.channels.connectors.transports.jini.JiniReceivingQConnectorImpl;
import org.blarty.zenith.messaging.channels.connectors.transports.jini.RemotePublishingQAdapter;
import org.blarty.zenith.messaging.channels.connectors.transports.jini.RemotePublishingQConnector;
import org.blarty.zenith.messaging.channels.connectors.transports.jini.RemoteReceivingQAdapter;
import org.blarty.zenith.messaging.channels.connectors.transports.jini.RemoteReceivingQConnector;



/**
 * @author calum
 */
public class MessagingServiceImpl implements MessagingService{
	MessagingManager mgr = MessagingManager.getManager();
	
	ExporterManager emgr = DefaultExporterManager.getManager("messaging","msgexportmgr.config");
	Logger log = Logger.getLogger("org.blarty.zenith.messaging.system");
    
	/*
	 * @see apollo.messaging.system.MessagingService#createChannel(java.lang.String)
	 */
	public MessagingServiceImpl() {
				
	}

	public void createChannel(String name) throws RemoteException {
		// TODO Complete method stub for createChannel
		mgr.createChannel(name);
	}

	/*
	 * @see apollo.messaging.system.MessagingService#createChannel(apollo.messaging.channels.MessageChannel)
	 */
	public void createChannel(MessageChannel channel) throws RemoteException {
		mgr.addChannel(channel);
	}

	/*
	 * @see apollo.messaging.system.MessagingService#createSubscriptionChannel(java.lang.String)
	 */
	public void createSubscriptionChannel(String name) throws RemoteException {
		// TODO Complete method stub for createSubscriptionChannel
		mgr.createSubscriptionChannel(name);
	}

	/*
	 * @see apollo.messaging.system.MessagingService#registerOnChannel(java.lang.String,
	 *           apollo.messaging.system.MessagingListener)
	 */
	public ReceivingQConnector registerOnChannel(String name, MessagingListener listener) throws ChannelException, RemoteException {
		log.finest("registering on Channel");
		RemoteReceivingQConnector rqc = new JiniReceivingQConnectorImpl(mgr.registerOnChannel(name, null));
		Remote proxy = emgr.exportProxy(rqc, "Connectors", UuidFactory.generate());
		log.finest("Creating Messaging Gateway");
		ReceivingQConnector adapter = new RemoteReceivingQAdapter((RemoteReceivingQConnector) proxy);
		return adapter;
	}

	/*
	 * @see apollo.messaging.system.MessagingService#getPublishingConnector(java.lang.String)
	 */
	public PublishingQConnector getPublishingConnector(String name) throws ChannelException, RemoteException {
		// TODO Complete method stub for getPublishingConnector
		log.finest("Getting Connector Proxy....");
		PublishingQConnector pc = mgr.getPublishingConnector(name);
		if (pc == null) {
			log.warning("Requested Connector " + name + " is null!");
		} else
			log.finest("Requested Connector " + name + " is not null!");
		Remote proxy = emgr.exportProxy(new JiniPublishingQConnectorImpl(pc), "Connectors", UuidFactory.generate());
		if (proxy == null)
			log.severe("Proxy to be returned will be null");
		RemotePublishingQAdapter adapter = new RemotePublishingQAdapter((RemotePublishingQConnector) proxy);
		return adapter;
	}

	public ReceivingQConnector getTemporaryChannel(MessagingListener listener) throws ChannelException, RemoteException {
		ReceiverChannel channel = mgr.getTemporaryChannel();
		ReceivingQConnector rqc = mgr.registerOnChannel(channel.getName(),listener);
		RemoteReceivingQConnector remoterqc = new JiniReceivingQConnectorImpl(rqc);
		Remote proxy = emgr.exportProxy(remoterqc, "Connectors", UuidFactory.generate());
		log.finest("Creating Messaging Gateway");
		RemoteReceivingQAdapter adapter = new RemoteReceivingQAdapter((RemoteReceivingQConnector) proxy);
		return adapter;
	}

	public void returnTemporaryChannel(String name) throws RemoteException{
		// TODO Complete method stub for returnTemporaryChannel
		mgr.returnTemporaryChannel(name);
		
	}
	
	
}
