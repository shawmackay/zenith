/*
 * apollo2 : org.blarty.zenith.messaging.system.guarantees.impl
 * 
 * 
 * FileGuarantor.java
 * Created on 08-Nov-2004
 * 
 * FileGuarantor
 *
 */
package org.blarty.zenith.messaging.system.guarantees.impl;

import java.util.HashMap;

import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.guarantees.Guarantee;
import org.blarty.zenith.messaging.system.guarantees.Guarantor;


/**
 * @author calum
 */
public class FileGuarantor implements Guarantor{
	private HashMap mesgMap = new HashMap();
	public void complete(Guarantee guarantee) {
		// TODO Complete method stub for complete
		if (mesgMap.containsKey(guarantee)){
			System.out.println("Message has been delivered");
			mesgMap.remove(guarantee);
		}
	}
	public int getOutstandingDeliveries(Guarantee guarantee) {
		// TODO Complete method stub for getOutstandingDeliveries
		return 0;
	}
	public Guarantee guarantee(Message m) {
		// TODO Complete method stub for guarantee
		return null;
	}
	public boolean isDelivered(Guarantee guarantee) {
		// TODO Complete method stub for isDelivered
		return false;
	}
}
