/*
 * Apollo : apollo.messaging.channels
 *
 *
 * Subscriber.java
 * Created on 23-Feb-2004
 *
 * Subscriber
 *
 */

package org.jini.projects.zenith.messaging.channels.connectors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.system.MessagingListener;
import org.jini.projects.zenith.messaging.system.store.StoreSystem;

/**
 * Implements an connector to a queue that receives messages asynchronously and
 * can be used to take messages from the queue synchronously. Connectors contain
 * their own queues that are filled by the Channel
 * 
 * @author calum
 */
public class ReceivingQConnectorImpl implements ReceivingQConnector,MessageReceiver {
        MessagingListener listener;

        Vector<Message> queue = new Vector<Message>();

        Dispatcher dispatch = new Dispatcher();

        Thread t = new Thread(dispatch);

        Logger l = Logger.getLogger("org.jini.projects.zenith.messaging.channels.connectors");

        static StoreSystem sys;

        String channelName;
        static {
                try {
                        sys = new StoreSystem(System.getProperty("org.jini.projects.zenith.messaging.system.store.dir"));
                } catch (IOException e) {

                        System.out.println("Err: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        public ReceivingQConnectorImpl(String channelName, MessagingListener listener) {
                this.listener = listener;
                if (listener != null)
                        t.start();
                this.channelName = channelName;
        }

        public String getChannelName() {
                // TODO Complete method stub for getChannelName
                return channelName;
        }

        public synchronized void addMessage(Message m) {
                // System.out.println("ReceivingQConnector Impl obtained Msg:" +
                // m.getHeader().getRequestID());
                // synchronized (queue) {
                queue.add(m);
                // }
                // System.out.println("ReceivingQConnector Impl added Msg:" +
                // m.getHeader().getRequestID() + " to queue");
                if (m.getHeader().getCorrelationID() != null)
                        l.finest("ChannelConnector on Channel " + channelName + " is forwarding on Msg: " + m.getHeader().getRequestID() + " to the dispatcher in response to Msg: " + m.getHeader().getCorrelationID());
                else
                        l.finest("Channel " + channelName + " is forwarding on Msg: " + m.getHeader().getRequestID() + " to the dispatcher");
                if (m.getHeader().isGuaranteed()) {
                        l.finest("Received message is guaranteed");
                        try {
                                sys.store(m.getHeader().getDestinationAddress(), m);
                                l.finest("Received Message is stored for: " + m.getHeader().getDestinationAddress());
                        } catch (IOException e) {
                                System.out.println("Err: " + e.getMessage());
                                e.printStackTrace();
                        }
                }
                l.finest("Message added: ");

                synchronized (dispatch) {
                        dispatch.notify();
                }
        }

        public Message receive() {
                if (queue.size() > 0)
                        return queue.remove(0);
                return null;

        }

        public Message receive(int timeout) {
                if(queue.size()>0)                      
                if (queue.get(0) != null)
                        return queue.remove(0);
                synchronized (this) {
                        try {
                                wait(timeout);
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                }
                if(queue.size()>0)                      
                        if (queue.get(0) != null)
                                return queue.remove(0);
                return null;
        }

        public class Dispatcher implements Runnable {

                private List dispatchQueue = Collections.synchronizedList(new ArrayList());

                public void go() {

                }

                public void run() {
                        for (;;) {

                                if (listener != null) {

                                        try {
                                                
                                                if (queue.size()>0) {
                                                        Message m = queue.remove(0);
                                                        listener.messageReceived(m);
                                                        if (m.getHeader().isGuaranteed()) {
                                                                l.finest("Guaranteed Message: Item delivered - removing");
                                                                try {
                                                                        sys.remove(m);
                                                                } catch (IOException e) {
                                                                        System.out.println("Err: " + e.getMessage());
                                                                        e.printStackTrace();
                                                                }

                                                        }
                                                } else {
                                                        synchronized (this) {
                                                                wait(0);
                                                        }
                                                }
                                        } catch (InterruptedException e) {
                                                // TODO Auto-generated catch
                                                // block
                                                e.printStackTrace();
                                        }
                                }
                        }
                }
        }

        /*
         * @see apollo.messaging.channels.connectors.ReceivingQConnector#setListener(apollo.messaging.system.MessagingListener)
         */
        public void setListener(MessagingListener listener) {
                // TODO Complete method stub for setListener

                boolean startThread = false;
                if (this.listener == null)
                        startThread = true;
                this.listener = listener;
                if (startThread)
                        t.start();
        }

        /*
         * @see apollo.messaging.channels.connectors.ReceivingQConnector#getReceiver()
         */
        public MessageReceiver getReceiver() {
                // TODO Complete method stub for getReceiver
                return this;
        }
}
