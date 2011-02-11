package org.blarty.zenith.resources.threads;
/***********************************************
The Closed exception is thrown if you try to used an explicitly
closed queue. See close.
 */

public class Closed extends RuntimeException {
	/**
     * 
     */
    private static final long serialVersionUID = 3258132466186269744L;
    private final WorkQueue blocking_Queue;
	public Closed(WorkQueue blocking_Queue) {
		super("Tried to access closed Blocking_queue");
		this.blocking_Queue = blocking_Queue;
	}
}