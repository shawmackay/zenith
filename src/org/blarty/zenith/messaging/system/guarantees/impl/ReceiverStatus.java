/*
 * apollo2 : org.blarty.zenith.messaging.system.guarantees.impl
 * 
 * 
 * ReceiverStatus.java
 * Created on 06-Sep-2004
 * 
 * ReceiverStatus
 *
 */
package org.blarty.zenith.messaging.system.guarantees.impl;

import java.io.Serializable;

/**
 * @author calum
 */
public class ReceiverStatus implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 3906090040139657783L;
    private String receiver;
	private boolean delivered;
	
	/**
	 * @param receiver
	 * @param delivered
	 */
	public ReceiverStatus(String receiver, boolean delivered) {
		super();
		this.receiver = receiver;
		this.delivered = delivered;
	}
	
	
	public boolean isDelivered() {
		return delivered;
	}
	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
	public String getReceiver() {
		return receiver;
	}
}
