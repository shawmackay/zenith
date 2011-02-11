/*
 * apollo2 : org.jini.projects.zenith.messaging.system.guarantees.impl
 * 
 * 
 * DeliveryEntry.java
 * Created on 06-Sep-2004
 * 
 * DeliveryEntry
 *
 */
package org.jini.projects.zenith.messaging.system.guarantees.impl;

import net.jini.entry.AbstractEntry;
import net.jini.id.Uuid;

/**
 * @author calum
 */
public class DeliveryEntry extends AbstractEntry{
	/**
     * 
     */
    private static final long serialVersionUID = 3257004375812552249L;
    public Uuid guaranteeID;
}
