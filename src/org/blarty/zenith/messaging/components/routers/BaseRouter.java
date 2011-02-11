/*
 * Apollo : apollo.messaging.routers
 * 
 * 
 * ContentBasedRouter.java
 * Created on 23-Feb-2004
 * 
 * ContentBasedRouter
 *
 */
package org.blarty.zenith.messaging.components.routers;

import org.blarty.zenith.messaging.components.ControllableComponent;
import org.blarty.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface BaseRouter extends ControllableComponent{
    public void route(Message m);
}
