/*
 * Created on 04-Mar-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package org.blarty.zenith.resources.threads;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * @author calum
 */

public class WorkQueue {
	private LinkedList elements = new LinkedList();
	private boolean closed = false;

	/***********************************************
	Enqueue an object
	 **/

	public synchronized final void enqueue(Object new_element) throws Closed {
		if (closed)
			throw new Closed(this);
		elements.addLast(new_element);
		notify();
	}

	/***********************************************
	Dequeues an element; blocks if the queue is empty (until something
	is enqueued). Be careful of nested-monitor lockout if you call this
	function. You must ensure that there's a way to get something into
	the queue that does not involve calling a synchronized method of
	whatever class is blocked, waiting to dequeue something.
	@see dequeue	
	@see enqueue
	@return s the dequeued object always
	 **/

	public synchronized final Object dequeue() throws InterruptedException, Closed {
		try {
			while (elements.size() <= 0) {
				wait();
				if (closed)
					throw new Closed(this);
			}
			return elements.removeFirst();
		} catch (NoSuchElementException e) // Shouldn't happen
			{
			throw new Error("Internal error (com.holub.asynch.Blocking_queue)");
		}
	}

	/***********************************************

	The is_empty() method is inherently unreliable in a multithreade
	situation. In code like the following, it's possible for a thread
	to sneak in after the test but before the dequeue operation and steal
	the element you thought you were dequeueing.
	
	Blocking_queue queue = new Blocking_queue();
	//...
	if( !some_queue.is_empty() )
		some_queue.dequeue();
	
	To do the foregoing reliably, you must synchronize on the
	queue as follows:
	
	Blocking_queue queue = new Blocking_queue();
	//...
	synchronized( queue )
	{   if( !some_queue.is_empty() )
			some_queue.dequeue();
	}
	
	The same effect can be achieved if the test/dequeue operation
	is done inside a synchronized method, and the only way to
	add or remove queue elements is from other synchronized methods.
	 */

	public synchronized final boolean is_empty() {
		return elements.size() > 0;
	}

	/* Releasing a blocking queue causes all threads that are blocked
	 * [waiting in dequeue() for items to be enqueued] to be released.
	 * The dequeue() call will throw a Blocking_queue.Closed runtime
	 * exception instead of returning normally in this case.
	 * Once a queue is closed, any attempt to enqueue() an item will
	 * also result in a Blocking_queue.Closed exception toss.
	 */

	public synchronized void close() {
		closed = true;
		notifyAll();
	}
}