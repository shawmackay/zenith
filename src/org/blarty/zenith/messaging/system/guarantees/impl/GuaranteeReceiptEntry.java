/*
 * apollo2 : org.jini.projects.zenith.messaging.system.guarantees.impl
 * 
 * 
 * GuaranteeReceiptEntry.java
 * Created on 06-Sep-2004
 * 
 * GuaranteeReceiptEntry
 *
 */
package org.jini.projects.zenith.messaging.system.guarantees.impl;

import java.util.ArrayList;
import java.util.List;

import net.jini.entry.AbstractEntry;
import net.jini.id.Uuid;

import org.jini.projects.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public class GuaranteeReceiptEntry extends AbstractEntry {
	/**
     * 
     */
    private static final long serialVersionUID = 3258408417835168306L;
    public Uuid ID;
	private List destinations;
	public String sender;
	public Message message;
	
	public GuaranteeReceiptEntry(Uuid ID, String sender, String[] dests, Message m){
		this.ID = ID;
			this.sender = sender;
		
		if (dests.length>0){
			this.destinations = new ArrayList();
			for(int i=0;i<dests.length;i++)
				destinations.add(new ReceiverStatus(dests[i], false));
		} else
			this.destinations = null;
	}
	
	public void delivered(String receiver){
		boolean processed=false;
		for(int i=0;i<destinations.size();i++){
			ReceiverStatus rs = (ReceiverStatus) destinations.get(i);
			if(rs.getReceiver().equals(receiver)){
				rs.setDelivered(true);
				processed = true;
			}	
		}
		if(!processed){
			System.out.println("Delivery not requested in this receipt");
		}
	}
	
}
