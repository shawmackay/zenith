/*
 * Created on 22-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.jini.projects.zenith.messaging.endpoints;

import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.channels.ReceiverChannel;

/**
 * @author Calum
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface MessageEndpoint {
        
	public String getType();
    public void init();
}
