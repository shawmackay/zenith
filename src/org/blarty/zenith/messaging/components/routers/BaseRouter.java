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
package org.jini.projects.zenith.messaging.components.routers;

import org.jini.projects.zenith.messaging.components.ControllableComponent;
import org.jini.projects.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface BaseRouter extends ControllableComponent{
    public void route(Message m);
}
