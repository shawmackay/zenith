/*
 * apollo2 : apollo.messaging.channels.connectors.transports.jini
 * 
 * 
 * ConnectorCreator.java
 * Created on 26-Feb-2004
 * 
 * ConnectorCreator
 *
 */
package org.jini.projects.zenith.messaging.channels.connectors.transports.jini;

import java.rmi.Remote;

import net.jini.core.event.RemoteEventListener;
import net.jini.id.Uuid;

import org.jini.glyph.chalice.builder.ProxyCreator;
import org.jini.projects.zenith.messaging.channels.connectors.transports.jini.constrainable.PublishingQConnectorProxy;
import org.jini.projects.zenith.messaging.channels.connectors.transports.jini.constrainable.ReceivingQConnectorProxy;
import org.jini.projects.zenith.messaging.channels.connectors.transports.jini.constrainable.RemoteMessagingListenerProxy;

/**
 * @author calum
 */
public class ConnectorCreator implements ProxyCreator {
	/* @see utilities20.export.builder.ProxyCreator#create(java.rmi.Remote, net.jini.id.Uuid)
	 */
	public Remote create(Remote in, Uuid ID) {
		// TODO Complete method stub for create
		if(in instanceof RemotePublishingQConnector)
            return PublishingQConnectorProxy.create((RemotePublishingQConnector) in, ID);
        if(in instanceof RemoteReceivingQConnector)
            return ReceivingQConnectorProxy.create((RemoteReceivingQConnector) in, ID);
        if(in instanceof RemoteMessagingListener)
            return RemoteMessagingListenerProxy.create((RemoteEventListener) in, ID);
        return null;
	}
}
