package org.jini.projects.zenith.messaging.test.items;

import net.jini.id.UuidFactory;

import org.jini.projects.zenith.messaging.channels.InvalidMessageChannel;
import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.components.DefaultControllableComponent;
import org.jini.projects.zenith.messaging.components.routers.ChannelFilterLink;
import org.jini.projects.zenith.messaging.components.routers.ContentBasedRouterAction;
import org.jini.projects.zenith.messaging.components.routers.Filter;
import org.jini.projects.zenith.messaging.messages.EventMessage;
import org.jini.projects.zenith.messaging.messages.ExceptionMessage;
import org.jini.projects.zenith.messaging.messages.MessageHeader;
import org.jini.projects.zenith.messaging.messages.ObjectMessage;
import org.jini.projects.zenith.messaging.messages.StringMessage;
import org.jini.projects.zenith.messaging.system.ChannelException;
import org.jini.projects.zenith.messaging.system.MessagingManager;
import org.jini.projects.zenith.messaging.system.UnsupportedActionException;
import org.jini.projects.zenith.messaging.test.TestItem;
import org.jini.projects.zenith.messaging.test.items.cbr.CBRFilter1;
import org.jini.projects.zenith.messaging.test.items.cbr.CBRFilter2;
import org.jini.projects.zenith.messaging.test.items.cbr.CBRFilter3;
import org.jini.projects.zenith.messaging.test.items.cbr.CBRListener1;
import org.jini.projects.zenith.messaging.test.items.cbr.CBRListener2;
import org.jini.projects.zenith.messaging.test.items.cbr.CBRListener3;

import com.sun.jndi.cosnaming.ExceptionMapper;

public class ContentBasedRouterTest implements TestItem {

        private Filter filter1;

        private Filter filter2;

        private Filter filter3;

        private MessagingManager mgr;

        private InvalidMessageChannel invalid;

        public void run(MessagingManager mgr) {
                System.out.println("Running CBR Test");
                // TODO Auto-generated method stub
                this.mgr = mgr;
                mgr.createChannel("routing");
                mgr.createChannel("filter1");
                mgr.createChannel("filter2");
                mgr.createChannel("filter3");
                mgr.createChannel("controlCBR");
                // Create the filters used by the CBR

                createFilters();
                invalid = setupInvalidChannel();
                setupContentBasedRouter(mgr, invalid);

                try {
                        setupListeners();
                        sendTestMessages();
                } catch (ChannelException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }

        private void sendTestMessages() throws ChannelException {
                // TODO Auto-generated method stub
                PublishingChannel routePublish = mgr.getSendingChannel("routing");
                
                
                //ExceptionMessage ex = ;
                for (int i = 0; i < 2000; i++) {
                        ObjectMessage o = new ObjectMessage(new MessageHeader(), UuidFactory.generate());
                        StringMessage s = new StringMessage(new MessageHeader(), "blah");
                        EventMessage evt = new EventMessage(new MessageHeader(), "event string", 1000, "system event");
                        ExceptionMessage ex = new ExceptionMessage(new MessageHeader(), new ChannelException("This shouldn't be delivered"), new StringMessage(new MessageHeader(), "Hello"));
                        ExceptionMessage ex2 = new ExceptionMessage(new MessageHeader(), new ChannelException("This shouldn't be delivered"), new StringMessage(new MessageHeader(), "Hello"));
                        Thread.yield();
                        
                        routePublish.getPublishingQConnector().sendMessage(o);
                        routePublish.getPublishingQConnector().sendMessage(s);
                        routePublish.getPublishingQConnector().sendMessage(evt);
                        // Send invalid message
                        routePublish.getPublishingQConnector().sendMessage(ex);
                        routePublish.getPublishingQConnector().sendMessage(ex2);
                }
                System.out.println("\nSend Harness Complete\n");
                try {
                        Thread.sleep(30000);
                } catch (Exception exc) {

                }
                System.out.println("Invalid Channel has: " + invalid.getOutstanding());
                System.out.println(filter1);
                System.out.println(filter2);
                System.out.println(filter3);
        }

        public void setupListeners() throws ChannelException {
                mgr.registerOnChannel("filter1", new CBRListener1());
                mgr.registerOnChannel("filter2", new CBRListener2());
                mgr.registerOnChannel("filter3", new CBRListener3());
        }

        private InvalidMessageChannel setupInvalidChannel() {
                InvalidMessageChannel invalid = new InvalidMessageChannel("invalid");
                mgr.addChannel(invalid);
                return invalid;
        }

        private void setupContentBasedRouter(MessagingManager mgr, InvalidMessageChannel invalid) {
                try {

                        DefaultControllableComponent component = new DefaultControllableComponent();
                        component.setChannelAction(new ContentBasedRouterAction());
                        component.setControlChannel(mgr.getReceivingChannel("controlCBR"));
                        component.setInvalidChannel(invalid);
                        component.setInputChannel(mgr.getReceivingChannel("routing"));

                        PublishingChannel pc = mgr.getSendingChannel("controlCBR");
                        pc.getPublishingQConnector().sendMessage(new ObjectMessage(new MessageHeader(), new ChannelFilterLink("filter1", filter1)));
                        pc.getPublishingQConnector().sendMessage(new ObjectMessage(new MessageHeader(), new ChannelFilterLink("filter2", filter2)));
                        pc.getPublishingQConnector().sendMessage(new ObjectMessage(new MessageHeader(), new ChannelFilterLink("filter3", filter3)));
                        Thread.sleep(2000);
                } catch (ChannelException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (UnsupportedActionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }

        private void createFilters() {
                filter1 = new CBRFilter1();
                filter2 = new CBRFilter2();
                filter3 = new CBRFilter3();
        }

}
