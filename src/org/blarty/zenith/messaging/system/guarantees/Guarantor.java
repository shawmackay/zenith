/*
 * apollo2 : org.blarty.zenith.messaging.system
 * 
 * 
 * Guarantor.java
 * Created on 06-Sep-2004
 * 
 * Guarantor
 *
 */
package org.blarty.zenith.messaging.system.guarantees;

import org.blarty.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface Guarantor {
	public Guarantee guarantee(Message m);
	public boolean isDelivered(Guarantee guarantee);
	public int getOutstandingDeliveries(Guarantee guarantee);
	public void complete(Guarantee guarantee);
}
