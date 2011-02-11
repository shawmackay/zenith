/*
 * Created on 22-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.blarty.zenith.messaging.endpoints;

import org.blarty.zenith.messaging.messages.Message;

/**
 * @author Calum
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface PublishingEndpoint extends MessageEndpoint {
	public void sendMessage(Message m);
}
