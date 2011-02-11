/*
 * Created on 22-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.blarty.zenith.messaging.channels;

import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.blarty.zenith.messaging.system.ChannelException;
import org.blarty.zenith.messaging.system.MessagingListener;



/**
 * @author Calum
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface ReceiverChannel extends MessageChannel {
	public ReceivingQConnector setReceivingListener(MessagingListener listener) throws ChannelException;
    public boolean removeListenerFor(ReceivingQConnector connect);
    public ReceivingQConnector createSynchronousRegistration() throws ChannelException;	
    
}
