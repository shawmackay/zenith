/*
 * apollo2 : org.jini.projects.zenith.messaging.broker.constrainable
 * 
 * 
 * BrokerCreator.java
 * Created on 10-Mar-2004
 * 
 * BrokerCreator
 *
 */

package org.jini.projects.zenith.messaging.broker.constrainable;

import java.rmi.Remote;

import net.jini.core.constraint.RemoteMethodControl;
import net.jini.core.event.RemoteEventListener;
import net.jini.id.Uuid;

import org.jini.glyph.chalice.builder.ProxyCreator;
import org.jini.projects.zenith.messaging.broker.MessageBroker;
import org.jini.projects.zenith.messaging.channels.connectors.transports.jini.constrainable.RemoteMessagingListenerProxy;

/**
 * @author calum
 */
public class BrokerCreator implements ProxyCreator {
	/*
	 * @see utilities20.export.builder.ProxyCreator#create(java.rmi.Remote,
	 *           net.jini.id.Uuid)
	 */
	public Remote create(Remote in, Uuid ID) {
		if (in instanceof MessageBroker)
			if (in instanceof RemoteMethodControl) {
				return new MessagingBrokerProxy.ConstrainableMessagingBrokerProxy((MessageBroker) in, ID, null);
			} else
				return new MessagingBrokerProxy((MessageBroker) in, ID);
         if(in instanceof RemoteEventListener)
            return RemoteMessagingListenerProxy.create((RemoteEventListener) in, ID);
        System.out.println("Invalid instance class: " + in.getClass().getName());
        for (int i=0;i<in.getClass().getInterfaces().length;i++)
            System.out.println("\t"+ in.getClass().getInterfaces()[i].getName());
		return null;
	}
}
