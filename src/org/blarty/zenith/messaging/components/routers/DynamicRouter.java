/*
 * Apollo : apollo.messaging.routers
 * 
 * 
 * DynamicRouter.java
 * Created on 23-Feb-2004
 * 
 * DynamicRouter
 *
 */
package org.blarty.zenith.messaging.components.routers;

import org.blarty.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface DynamicRouter extends BaseRouter {
    public void manageControlMsg(Message m);
}
