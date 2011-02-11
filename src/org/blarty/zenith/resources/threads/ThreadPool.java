/*
 * Created on 04-Mar-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package org.jini.projects.zenith.resources.threads;

/**
 * @author calum
 */

/**
  |     
A generic implementation of a thread pool. Use it like this:

ThreadPool pool = new ThreadPool();
pool.execute
(   new Runnable()
{   public void run()
{   // execute this function on an existing
// thread from the pool.
}
}
);


The size of the thread pool can expand automatically to accommodate
requests for execution. That is, if a thread is available in the
pool, it's used to execute the Runnable object, otherwise a new
thread can be created (and added to the pool) to execute the request.
A maximum count can be specified to limit the number of threads in
the pool, however.  Each thread pool also forms a thread group (all
threads in the pool are in the group). In practice this means that
the security manager controls whether a thread in the pool can access
threads in other groups, but it also gives you an easy mechanism to
make the entire group a daemon.



(c) 1999, Allen I. Holub.


You may not distribute this code except in binary form,
incorporated into a java .class file. You may use this code freely
for personal purposes, but you may not incorporate it into any

commercial product without securing my express permission in
writing.

*/

public class ThreadPool extends ThreadGroup {
	private final WorkQueue pool = new WorkQueue();
	private /*final*/
	int maximum_size;
	private int pool_size;
	private boolean has_closed = false;

	private static int group_number = 0;
	private static int thread_id = 0;

	/**********************************************
	These are the objects that wait to be activated. They are
	typically blocked on an empty queue. You post a Runnable
	object to the queue to release a thread, which will execute
	the run() method from that object. All pooled-thread objects
	will be members of the thread group that comprises the thread
	pool.
	 */

	private class Pooled_thread extends Thread {
		public Pooled_thread() {
			super(ThreadPool.this, "T" + thread_id);
		}

		public void run() {
			try {
				while (!has_closed) {
					((Runnable) (pool.dequeue())).run();
				}
			} catch (InterruptedException e) { /* ignore it, stop thread */
			} catch (Closed e) { /* ignore it, stop thread */
			}
		}
	}

	/**********************************************
	Create a thread pool with initial_thread_count threads in it. The
	pool can expand to contain additional threads if they are needed.
	@param initial_thread_count  The initial thread count.  If the initial count is greater than
	the maximum, it is silently truncated to the maximum.
	@param maximum_thread_count specifies the maximum number of threads that can be in
	the pool. A maximum of 0 indicates that the pool will be permitted to grow
	indefinitely.
	 */

	public ThreadPool(int initial_thread_count, int maximum_thread_count) {
		super("ThreadPool" + group_number++);
		maximum_size = (maximum_thread_count > 0) ? maximum_thread_count : Integer.MAX_VALUE;
		pool_size = Math.min(initial_thread_count, maximum_size);
		for (int i = pool_size; --i >= 0;)
			new Pooled_thread().start();
	}

	/**********************************************
	Create a dynamic Thread pool as if you had used
	ThreadPool(0, true);
	 **/

	public ThreadPool() {
		super("ThreadPool" + group_number++);
		this.maximum_size = 0;
	}

	/**********************************************
	Execute the run() method of the Runnable object on a thread
	in the pool. A new thread is created if the pool is empty and
	the number of threads in the pool is not at the maximum.
	@throws ThreadPool.Closed
	if you try to execute an action on a pool to which a close()
	request has been sent.
	 */

	public synchronized void execute(Runnable action) throws Closed {
		// You must synchronize on the pool because the Pooled_thread's
		// run method is asynchronously dequeueing elements. If we
		// didn't synchronize, it would be possible for the is_empty
		// query to return false, and then have a Pooled_thread sneak
		// in and put a thread on the queue (by doing a blocking dequeue).
		// In this scenario, the number of threads in the pool could
		// exceed the maximum specified in the constructor. The
		// test for pool_size < maximum_size is outside the synchronized
		// block because I didn't want to incur the overhead of
		// synchronization unnecessarily. This means that I could
		// occasionally create an extra thread unnecessarily, but
		// the pool size will never exceed the maximum.

		if (has_closed)
			throw new Closed();

		if (pool_size < maximum_size)
			synchronized (pool) {
				if (pool.is_empty()) {
					++pool_size;
					new Pooled_thread().start(); // Add thread to pool
				}
			}

		pool.enqueue(action); // Attach action to it.
	}

	/**********************************************
	Objects of class ThreadPool.Closed are thrown if you try to
	execute an action on a closed ThreadPool.
	 */

	public class Closed extends RuntimeException {
		/**
         * 
         */
        private static final long serialVersionUID = 3258126972939679793L;

        Closed() {
			super("Tried to execute operation on a closed ThreadPool");
		}
	}

	/**********************************************
Kill all the threads waiting in the thread pool, and arrange
	for all threads that came out of the pool, but which are working,
	to die natural deaths when they're finished with whatever they're
	doing. Actions that have been passed to execute() but which
	have not been assigned to a thread for execution are discarded.
	
	No further operations are permitted on a closed pool, though
	closing a closed pool is a harmless no-op.
	 */

	public synchronized void close() {
		has_closed = true;
		pool.close(); // release all waiting threads
	}

	/* ============================================================== */

	public static class Test {
		private static ThreadPool pool = new ThreadPool(10, 10);

		public static void main(String[] args) {
			Test test_bed = new Test();
			test_bed.fire("hello");
			test_bed.fire("world");

			// Give the threads a chance to start before closing the pool
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
			}

			pool.close();
		}

		private void fire(final String id) {
			pool.execute(new Runnable() {
				public void run() {
					System.out.println("Starting " + id);

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}

					System.out.println("Stopping " + id);
				}
			});
		}
	}
}
