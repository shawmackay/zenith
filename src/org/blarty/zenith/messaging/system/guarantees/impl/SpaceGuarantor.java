/*
 * apollo2 : org.jini.projects.zenith.messaging.system.guarantees.impl
 * 
 * 
 * SpaceGuarantor.java
 * Created on 06-Sep-2004
 * 
 * SpaceGuarantor
 *
 */

package org.jini.projects.zenith.messaging.system.guarantees.impl;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.transaction.CannotAbortException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.UnknownTransactionException;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.lease.LeaseListener;
import net.jini.lease.LeaseRenewalEvent;
import net.jini.lease.LeaseRenewalManager;
import net.jini.space.JavaSpace;

import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.system.guarantees.Guarantee;
import org.jini.projects.zenith.messaging.system.guarantees.Guarantor;

/**
 * Use a space (i.e. distributed shared memory) as the gaurantor repository
 * 
 * @author calum
 */
public class SpaceGuarantor implements Guarantor {

	JavaSpace space;
	TransactionManager tm;
	LeaseRenewalManager lrm;
	Logger log = Logger.getLogger("org.jini.projects.zenith.messaging.system.guarantees");

	public SpaceGuarantor(JavaSpace space) {
		this.space = space;
	}

	public int getOutstandingDeliveries(Guarantee guarantee) {
		// TODO Complete method stub for getOutstandingDeliveries

		int numOutstanding = 0;
		try {
			GuaranteeEntry template = new GuaranteeEntry(guarantee.getID(), null, null);
			GuaranteeReceiptEntry receiptTemplate = new GuaranteeReceiptEntry(guarantee.getID(),null,null,null);
			Transaction tc = buildTransaction();
			numOutstanding = 0;
			GuaranteeEntry taken = null;
			do {
				taken = (GuaranteeEntry) space.takeIfExists(template, tc, 5000);
				if (taken != null)
					numOutstanding++;
			} while (taken != null);
			tc.abort();
		} catch (LeaseDeniedException e) {
			// TODO Handle LeaseDeniedException
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Handle RemoteException
			e.printStackTrace();
		} catch (UnknownTransactionException e) {
			// TODO Handle UnknownTransactionException
			e.printStackTrace();
		} catch (CannotAbortException e) {
			// TODO Handle CannotAbortException
			e.printStackTrace();
		} catch (UnusableEntryException e) {
			// TODO Handle UnusableEntryException
			e.printStackTrace();
		} catch (TransactionException e) {
			// TODO Handle TransactionException
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Handle InterruptedException
			e.printStackTrace();
		}
		return numOutstanding;
	}

	public Guarantee guarantee(Message m) {
		// TODO Complete method stub for guarantee

		return null;
	}

	public boolean isDelivered(Guarantee guarantee) {

		return false;
	}

	public void complete(Guarantee guarantee) {

	}

	private Transaction buildTransaction() throws LeaseDeniedException, RemoteException {

		Transaction.Created txf = TransactionFactory.create(tm, 20000L);
		lrm.renewFor(txf.lease, Lease.FOREVER, new LeaseProblemListener());
		return txf.transaction;
	}

	
	
	class LeaseProblemListener implements LeaseListener {

		public void notify(LeaseRenewalEvent e) {
			// TODO Complete method stub for notify
			log.info("LeaseListener: " + e.getException().getMessage());

		}
	}
}
