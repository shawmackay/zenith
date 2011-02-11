/*
 * Created on 22-Feb-2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */

package org.blarty.zenith.messaging.system;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationException;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.lease.UnknownLeaseException;
import net.jini.core.lookup.ServiceID;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.id.Uuid;
import net.jini.id.UuidFactory;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.LookupCache;
import net.jini.lookup.ServiceDiscoveryEvent;
import net.jini.lookup.ServiceDiscoveryListener;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.lookup.ServiceItemFilter;

import org.jini.glyph.chalice.DefaultExporterManager;
import org.jini.glyph.chalice.ExporterManager;
import org.blarty.zenith.messaging.broker.BrokerEvent;
import org.blarty.zenith.messaging.broker.MessageBroker;
import org.blarty.zenith.messaging.channels.MessageChannel;
import org.blarty.zenith.messaging.channels.PointToPointChannel;
import org.blarty.zenith.messaging.channels.PublishSubscribeChannel;
import org.blarty.zenith.messaging.channels.PublishingChannel;
import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;
import org.blarty.zenith.messaging.channels.connectors.ReceivingQConnector;

import com.sun.jini.constants.TimeConstants;

/**
 * @author Calum
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class MessagingManager implements ServiceDiscoveryListener {
    private static Map mgrs;

    private static MessagingManager mgr;

    ServiceID messagingSvcID;

    private Map brokerData;

    private LookupCache cache;

    MessageBroker broker;

    ServiceID brokerID;

    LookupDiscoveryManager ldm;

    ServiceDiscoveryManager sdm;

    LeaseRenewalManager lrm = new LeaseRenewalManager();

    String name;

    Logger l = Logger.getLogger("org.blarty.zenith.messaging.system");

    private int retries = 6;

    private int retryTime = 500;
    static {
        mgrs = new HashMap();
    }

    public void overrideMessagingSvcID(ServiceID newID){
    	messagingSvcID = newID;
    }
    
    /*
     * TODO: Add routing out to other messge managers on the network
     */
    private MessagingManager(String name, Configuration config) {
        try {
            this.name = name;
            l.info("Creating manager " + name);            
            String[] groups = (String[]) config.getEntry("messagingManager", "sdmLookupGroups", String[].class);
            ldm = new LookupDiscoveryManager(groups, null, null);
            sdm = new ServiceDiscoveryManager(ldm, null);
            cache = sdm.createLookupCache(new ServiceTemplate(null, null, null), null, this);
        } catch (RemoteException e) {
            // URGENT Handle RemoteException
            e.printStackTrace();
        } catch (ConfigurationException e) {
            // URGENT Handle ConfigurationException
            l.warning("Brokering is not available");
            e.printStackTrace();
        } catch (IOException e) {
            // URGENT Handle IOException
            e.printStackTrace();
        }
    }

    protected String getName() {
        return name;
    }

    public static MessagingManager getManager() {
        return getManager("default");
    }

    public static void createManager(String name, Configuration config) {
        mgr = new MessagingManager(name, config);
        System.out.println("Creating Manager \"" + name + "\"");
      
        mgrs.put(name, mgr);
    }

    public ReceiverChannel getTemporaryChannel(){
                Uuid channel = UuidFactory.generate();
                mgr.createChannel("tmp" + channel.toString());
                try {
                        ReceiverChannel input = mgr.getReceivingChannel("tmp" + channel.toString());
                        return input;
                } catch (ChannelException e) {
                        // URGENT Handle ChannelException
                        e.printStackTrace();
                }
                return null;
    }
    
    public void returnTemporaryChannel(MessageChannel channel){
                returnTemporaryChannel(channel.getName());
    }
        
    public void returnTemporaryChannel(String name){
                try {
                        while (removeChannel(name) == false) {
                                System.out.println("Channel has existing messages");
                                try {
                                        Object o = new Object();
                                        synchronized (this) {
                                                wait(1000);
                                        }
                                } catch (Exception ex) {
                                        ex.printStackTrace();
                                }
                        }

                } catch (ChannelException e) {
                        // URGENT Handle ChannelException
                        e.printStackTrace();
                }
    }
    
    public static MessagingManager getManager(String name) {
        if (mgrs.containsKey(name)) {
            return (MessagingManager) mgrs.get(name);
        } else {
            return null;
        }
    }

    public void setMessagingServiceID(ServiceID id) {
        l.fine("Setting the MessagingService ID to " + id + " for manager " + name);
        synchronized (this) {
            messagingSvcID = id;
            try {
                notify();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private HashMap channels = new HashMap();

    public MessageChannel createChannel(String name) {
        l.finest("Creating channel:  " + name);
        PointToPointChannel channel =new PointToPointChannel(name); 
        channels.put(name, channel);
        try {
            if (broker != null)
                broker.channelAdded(messagingSvcID, name);
        } catch (RemoteException e) {
            // URGENT Handle RemoteException
            e.printStackTrace();
        }
        return channel;
    }

    public void addChannel(MessageChannel channel) {
        l.finest("Binding channel:  " + channel.getName() + " [" + channel.getClass().getName());
        channels.put(channel.getName(), channel);
        try {
            if (broker != null)
                broker.channelAdded(messagingSvcID, channel.getName());
        } catch (RemoteException e) {
            // URGENT Handle RemoteException
            e.printStackTrace();
        }
    }

    public void createSubscriptionChannel(String name) {
        l.finest("Creating Publish-Subscribe channel: " + name);
        channels.put(name, new PublishSubscribeChannel(name));
        try {
            if (broker != null)
                broker.channelAdded(messagingSvcID, name);
        } catch (RemoteException e) {
            // URGENT Handle RemoteException
            e.printStackTrace();
        }
    }

    public MessageChannel getChannel(String name) {
        return (MessageChannel) channels.get(name);
    }

    public ReceiverChannel getReceivingChannel(String name) throws ChannelException {
        if (channels.containsKey(name))
            if (channels.get(name) instanceof ReceiverChannel)
                return (ReceiverChannel) channels.get(name);
            else
                throw new ChannelException("Channel is wrong type");
        else
            throw new ChannelException("Requested Channel is undefined");
    }

    public PublishingChannel getSendingChannel(String name) throws ChannelException {
        if (name == null)
            System.out.println("Blergh!");
        if (channels.containsKey(name))
            if (channels.get(name) instanceof PublishingChannel)
                return (PublishingChannel) channels.get(name);
            else
                throw new ChannelException("Channel is wrong type");
        else
            throw new ChannelException("Requested Channel is undefined - " + name);
    }

    public ReceivingQConnector registerOnChannel(String name, MessagingListener listener) throws ChannelException {
        if (channels.containsKey(name))
            if (channels.get(name) instanceof ReceiverChannel) {
                ReceiverChannel channel = (ReceiverChannel) channels.get(name);
                ReceivingQConnector s = channel.setReceivingListener(listener);
                return s;
            } else {
                try {
                    /*
                     * Next --
                     * 
                     * Query broker data, to see if the channel exists anywhere,
                     * if so, connect to the Remote Service, and return a Remote
                     * PublishingQConnector
                     */
                    l.finer("Checking broker data ");
                    Object item = broker.getServiceForChannel(name);
                    if (item != null) {
                        System.out.println("Got a return from the broker - checking");
                        if (item instanceof ServiceID) {
                            if (!(messagingSvcID.equals(item))) {
                                System.out.println("Service ID != my Service ID - continue checking");
                                ServiceItem svcItem = cache.lookup(new MessagingServiceIDFilter((ServiceID) item));
                                MessagingService svc = (MessagingService) svcItem;
                                l.finer("Found a channel named " + name + " in service " + item);
                               ReceivingQConnector s = svc.registerOnChannel(name, null);
                                s.setListener(listener);
                                return s;
                            }
                        }
                        // For a list to exists there must be at least two
                        // of
                        // them
                        if (item instanceof List) {
                            List list = (List) item;
                            Random r = new Random(System.currentTimeMillis());
                            int index = r.nextInt(list.size());
                            ServiceID id = (ServiceID) list.get(index);
                            if (!(messagingSvcID.equals(id))) {
                                ServiceItem svcItem = cache.lookup(new MessagingServiceIDFilter((ServiceID) item));
                                MessagingService svc = (MessagingService) svcItem;
                                l.finer("Found a channel named " + name + " in service " + id);
                                ReceivingQConnector s = svc.registerOnChannel(name, null);
                                s.setListener(listener);
                                return s;
                            }
                        }
                    }
                } catch (RemoteException e) {
                    // URGENT Handle RemoteException
                    e.printStackTrace();
                } catch (ChannelException e) {
                    // URGENT Handle ChannelException
                    e.printStackTrace();
                }
            }
        throw new ChannelException("Channel is wrong type");
    }

    public boolean removeChannel(String channelName) throws ChannelException {
        if (channels.containsKey(channelName)) {
            MessageChannel channel = (MessageChannel) channels.get(channelName);
            if (channel.getOutstanding() != 0)
                return false;
            else {
                channels.remove(channelName);

                return true;
            }
        }
        throw new ChannelException("No such channel registered: " + channelName);
    }

    public PublishingQConnector getPublishingConnector(String name) throws ChannelException {
        for (int i = 0; i < retries; i++) {
            if (channels.containsKey(name)) {
                if (channels.get(name) instanceof PublishingChannel) {
                    PublishingChannel channel = (PublishingChannel) channels.get(name);
                    PublishingQConnector s = channel.getPublishingQConnector();
                    return s;
                }
            } else {
                try {
                    /*
                     * Next --
                     * 
                     * Query broker data, to see if the channel exists anywhere,
                     * if so, connect to the Remote Service, and return a Remote
                     * PublishingQConnector
                     */
                    l.info("Checking broker data for channel " + name);
                    Object item = broker.getServiceForChannel(name);
                    if (item != null) {
                        if (item instanceof ServiceID) {
                            if (!(messagingSvcID.equals(item))) {
                                ServiceItem svcItem = cache.lookup(new MessagingServiceIDFilter((ServiceID) item));
                                if (svcItem != null) {
                                    MessagingService svc = (MessagingService) svcItem.service;
                                    l.finer("Found a channel named " + name + " in service " + item);
                                    if(svc==null)
                                        System.out.println("\t\t****Messaging Service obtained from cache is null");
                                    PublishingQConnector s = svc.getPublishingConnector(name);
                                    return s;
                                } else {
                                    System.out.println("Matching Service could not be found - " + item);
                                    
                                }
                            }
                        }
                        // For a list to exists there must be at least two of
                        // them
                        if (item instanceof List) {
                            List list = (List) item;
                            Random r = new Random(System.currentTimeMillis());
                            int index = r.nextInt(list.size());
                            ServiceID id = (ServiceID) list.get(index);
                            if (!(messagingSvcID.equals(id))) {
                                ServiceItem svcItem = cache.lookup(new MessagingServiceIDFilter((ServiceID) item));
                                MessagingService svc = (MessagingService) svcItem;
                                l.finer("Found a channel named " + name + " in service " + id);
                                PublishingQConnector s = svc.getPublishingConnector(name);
                                return s;
                            }
                        }
                    }
                } catch (RemoteException e) {
                    // URGENT Handle RemoteException
                    e.printStackTrace();
                } catch (ChannelException e) {
                    // URGENT Handle ChannelException
                    e.printStackTrace();
                }
            }
            try {
                System.out.println("Retrying....");
                synchronized (this) {
                    wait(retryTime);
                }
            } catch (Exception ex) {

            }
        }
        throw new ChannelException("Requested Channel is undefined: " + name);
    }

    class MessagingServiceIDFilter implements ServiceItemFilter {
        ServiceID idToCheck;

        public MessagingServiceIDFilter(ServiceID idToCheck) {
            this.idToCheck = idToCheck;
        }

        /*
         * @see net.jini.lookup.ServiceItemFilter#check(net.jini.core.lookup.ServiceItem)
         */
        public boolean check(ServiceItem item) {
            // TODO Complete method stub for check
            // System.out.println("Checking [" + idToCheck +"] against external
            // service [" + item.serviceID+"]");
            if (item.serviceID.equals(idToCheck))
                return true;
            return false;
        }
    }

    public boolean containsChannel(String name) {
        if (channels.containsKey(name))
            return true;
        try {
            if (broker != null)
                if (broker.getServiceForChannel(name) != null)
                    return true;
        } catch (RemoteException e) {
            // URGENT Handle RemoteException
            e.printStackTrace();
        }
        System.out.println("Channel " + name + " cannot be found in either local or brokered channels!");
        return false;
    }

    public boolean isPointToPoint(String name) throws ChannelException {
        if (containsChannel(name)) {
            return channels.get(name) instanceof PointToPointChannel;
        }
        throw new ChannelException("Requested Channel is undefined");
    }

    public boolean isPublishSubscribe(String name) throws ChannelException {
        if (containsChannel(name)) {
            return channels.get(name) instanceof PublishSubscribeChannel;
        }
        throw new ChannelException("Requested Channel is undefined");
    }

    // public class BrokerLookup implements DiscoveryListener {
    // /*
    // * @see
    // net.jini.discovery.DiscoveryListener#discarded(net.jini.discovery.DiscoveryEvent)
    // */
    // public void discarded(DiscoveryEvent e) {
    // // TODO Complete method stub for discarded
    // }
    //
    // /*
    // * @see
    // net.jini.discovery.DiscoveryListener#discovered(net.jini.discovery.DiscoveryEvent)
    // */
    // public void discovered(DiscoveryEvent e) {
    // try {
    // Object ob = e.getRegistrars()[0].lookup(new ServiceTemplate(null, new
    // Class[]{MessagingBroker.class}, null));
    // broker = (MessagingBroker) ob;
    // brokerData = broker.getCurrentlyRegisteredChannels();
    // } catch (RemoteException e1) {
    // // URGENT Handle RemoteException
    // e1.printStackTrace();
    // }
    // }
    // }
    /*
     * @see net.jini.lookup.ServiceDiscoveryListener#serviceAdded(net.jini.lookup.ServiceDiscoveryEvent)
     */
    public void serviceAdded(ServiceDiscoveryEvent event) {
        // TODO Complete method stub for serviceAdded
        if (event.getPostEventServiceItem().service instanceof MessageBroker)
            if (broker == null) {
                broker = (MessageBroker) event.getPostEventServiceItem().service;
                setupBroker(broker);
            }
    }

    private class BrokerCleanupHook implements Runnable {
        private Lease brokerLease;

        private LeaseRenewalManager lrm;

        public BrokerCleanupHook(Lease brokerLease, LeaseRenewalManager theLRM) {
            this.brokerLease = brokerLease;
            lrm = theLRM;
        }

        public void run() {
            try {
                lrm.cancel(brokerLease);
                System.out.println("Broker Lease cancelled");
            } catch (UnknownLeaseException e) {
                // TODO Handle UnknownLeaseException
                e.printStackTrace();
            } catch (RemoteException e) {
                // TODO Handle RemoteException
                e.printStackTrace();
            }
        }
    }

    private void setupBroker(MessageBroker broker) {
        try {
            ExporterManager mgr = DefaultExporterManager.getManager("messaging");
            if (mgr == null)
                System.out.println("Manager is null");
            RemoteEventListener rel = (RemoteEventListener) DefaultExporterManager.getManager("messaging").exportProxy(new BrokerListener(), "Broker", UuidFactory.generate());
            brokerData = broker.getCurrentlyRegisteredChannels();
            EventRegistration reg = broker.registerInterest(3 * TimeConstants.MINUTES, rel);
            lrm.renewFor(reg.getLease(), Lease.FOREVER, null);
            Runtime.getRuntime().addShutdownHook(new Thread(new BrokerCleanupHook(reg.getLease(), lrm)));
            l.info("Registered with a broker");
            while (messagingSvcID == null) {
                l.info("Waiting for messaging serviceID to be set for manager " + name);
                try {
                    synchronized (this) {
                        wait(1000);
                    }
                } catch (InterruptedException e1) {
                    // URGENT Handle InterruptedException
                    e1.printStackTrace();
                }
            }
            synchronized (channels) {
                Set s = channels.keySet();
                l.finer("Number of channels is: " + s.size());

                for (Iterator iter = s.iterator(); iter.hasNext();) {
                    broker.channelAdded(messagingSvcID, (String) iter.next());
                }
            }
        } catch (ExportException e) {
            // URGENT Handle ExportException
            e.printStackTrace();
        } catch (LeaseDeniedException e) {
            // URGENT Handle LeaseDeniedException
            e.printStackTrace();
        } catch (RemoteException e) {
            // URGENT Handle RemoteException
            e.printStackTrace();
        }
    }

    /*
     * @see net.jini.lookup.ServiceDiscoveryListener#serviceChanged(net.jini.lookup.ServiceDiscoveryEvent)
     */
    public void serviceChanged(ServiceDiscoveryEvent event) {
        // TODO Complete method stub for serviceChanged
    }

    /*
     * @see net.jini.lookup.ServiceDiscoveryListener#serviceRemoved(net.jini.lookup.ServiceDiscoveryEvent)
     */
    public void serviceRemoved(ServiceDiscoveryEvent event) {
        // TODO Complete method stub for serviceRemoved
        if (event.getPreEventServiceItem().serviceID.equals(brokerID)) {
            broker = null;
            ServiceItem item = cache.lookup(new ClassFilter(MessageBroker.class));
            if (item != null) {
                broker = (MessageBroker) item.service;
                setupBroker(broker);
            }
        }
    }

    class BrokerListener implements RemoteEventListener {
        /*
         * @see net.jini.core.event.RemoteEventListener#notify(net.jini.core.event.RemoteEvent)
         */
        public void notify(RemoteEvent theEvent) throws UnknownEventException, RemoteException {
            l.finest("Event received");
            if (theEvent instanceof BrokerEvent) {
                BrokerEvent ev = (BrokerEvent) theEvent;
                System.out.println("MessagingService ID: " + messagingSvcID);
                if (!ev.getChangingService().equals(messagingSvcID)) {
                    ServiceID serviceChanged = ev.getChangingService();
                    String channelName = ev.getChangingChannel();
                    l.info("Channel map changed in service " + serviceChanged + " putting to local broker cache - Channel " + channelName + (ev.getChangeType() == BrokerEvent.ADDED ? " added" : " removed"));
                    switch (ev.getChangeType()) {
                    case BrokerEvent.ADDED:
                        l.finer("Adding a channel to the local cache");
                        if (brokerData.containsKey(channelName)) {
                            Object ob = brokerData.get(channelName);
                            if (ob instanceof ServiceID) {
                                List arr = new ArrayList();
                                arr.add(ob);
                                arr.add(serviceChanged);
                                brokerData.put(channelName, arr);
                            }
                            if (ob instanceof List) {
                                List arr = (List) ob;
                                arr.add(serviceChanged);
                            }
                        } else
                            brokerData.put(channelName, serviceChanged);
                        break;
                    case BrokerEvent.REMOVED:
                        l.finer("Removing a channel from the local cache");
                        if (brokerData.containsKey(channelName)) {
                            Object ob = brokerData.get(channelName);
                            if (ob instanceof ServiceID) {
                                brokerData.remove(channelName);
                            }
                            if (ob instanceof List) {
                                List arr = (List) ob;
                                arr.remove(serviceChanged);
                            }
                            System.out.println("Removed channel " + channelName + " from local broker cache");
                        } else
                            System.out.println("Channel " + channelName + " was not stored in this cache");
                        break;
                    }
                }
            }
        }
    }
}
