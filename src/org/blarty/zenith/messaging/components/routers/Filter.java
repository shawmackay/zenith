/*
 * Apollo : apollo.messaging.routers
 * 
 * 
 * Filter.java
 * Created on 23-Feb-2004
 * 
 * Filter
 *
 */
package org.jini.projects.zenith.messaging.components.routers;

import org.jini.projects.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface Filter {
    boolean check(Message m);
}
