/*
 * apollo2 : org.blarty.zenith.messaging.system.guarantees.impl
 * 
 * 
 * GuaranteeEntry.java
 * Created on 06-Sep-2004
 * 
 * GuaranteeEntry
 *
 */
package org.blarty.zenith.messaging.system.guarantees.impl;

import net.jini.entry.AbstractEntry;
import net.jini.id.Uuid;

import org.blarty.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public class GuaranteeEntry extends AbstractEntry {
	/**
     * 
     */
    private static final long serialVersionUID = 3257566226338493752L;
    public Uuid guaranteeID;
	public Message message;
	public String destination;
	public GuaranteeEntry(){
		this.guaranteeID=null;
		this.message = null;
		this.destination = null;		
	}
	
	
	/**
	 * @param guaranteeID
	 * @param message
	 * @param destination
	 */
	public GuaranteeEntry(Uuid guaranteeID, Message message, String destination) {
		super();
		this.guaranteeID = guaranteeID;
		this.message = message;
		this.destination = destination;
	}
}
