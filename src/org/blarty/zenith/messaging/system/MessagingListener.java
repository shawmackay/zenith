/*
 * Created on 22-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.jini.projects.zenith.messaging.system;

import org.jini.projects.zenith.messaging.channels.MessageChannel;
import org.jini.projects.zenith.messaging.messages.Message;

/**
 * @author Calum
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface MessagingListener {
	public void messageReceived(Message m);
   // public void setcurrentMessagingChannel(MessageChannel channel);
}
