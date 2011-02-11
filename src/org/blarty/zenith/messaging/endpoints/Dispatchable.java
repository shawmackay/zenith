package org.blarty.zenith.messaging.endpoints;

import net.jini.id.ReferentUuid;

import org.blarty.zenith.messaging.messages.Message;


/**
 * User: calum
 * Date: 23-Feb-2004
 * Time: 16:02:19
 */
public interface Dispatchable extends ReferentUuid{
    public void process(Message m);
    public boolean isAvailable();
    public void setAvailable(boolean avail);
    
}
