/*
 * zenith : org.blarty.zenith.messaging.routers
 *
 *
 * RecipientList.java
 * Created on 01-Apr-2005
 *
 * RecipientList
 *
 */

package org.blarty.zenith.messaging.components;

import java.util.logging.Logger;

import org.blarty.zenith.messaging.channels.PublishingChannel;
import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.ChannelAction;
import org.blarty.zenith.messaging.system.ChannelDispatcher;
import org.blarty.zenith.messaging.system.ChannelException;
import org.blarty.zenith.messaging.system.ControllableChannelAction;
import org.blarty.zenith.messaging.system.MessagingListener;
import org.blarty.zenith.messaging.system.UnsupportedActionException;

/**
 * @author calum
 */
public class DefaultControllableComponent implements ControllableComponent {
        ReceiverChannel inputChannel;

        ReceiverChannel controlChannel;

        PublishingChannel invalidChannel;

        Logger log = Logger.getLogger("org.blarty.zenith.messaging.routers");

        private ChannelDispatcher currentDispatcher;

        private ChannelAction theAction;

        public DefaultControllableComponent() {

        }

        public void setControlChannel(ReceiverChannel control) {

                controlChannel = control;
                try {
                        controlChannel.setReceivingListener(new MessagingListener() {
                                public void messageReceived(Message m) {
                                        ((ControllableChannelAction) theAction).controlDispatch(m);
                                }
                        });
                } catch (ChannelException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }

        public void setChannelAction(ChannelAction action) throws UnsupportedActionException {
                // TODO Auto-generated method stub
                if (!(action instanceof ControllableChannelAction))
                        throw new UnsupportedActionException("Expecting a ConfigurableChannelAction");
                theAction = action;
        }

        public void setInputChannel(ReceiverChannel input) throws ChannelException {
                // TODO Auto-generated method stub
                inputChannel = input;
                System.out.println("Setting channel dispatcher");
                if (currentDispatcher != null) {
                        System.out.println("Existing channel dispatcher...stopping");
                        currentDispatcher.signalStop();
                        while (currentDispatcher.isAlive())
                                Thread.yield();
                }
                currentDispatcher = new ChannelDispatcher(theAction);

                currentDispatcher.setReceivingChannel(inputChannel);

                currentDispatcher.setInvalidChannel(invalidChannel);
                currentDispatcher.start();
        }

        public void setInvalidChannel(PublishingChannel invalid) {
                // TODO Auto-generated method stub
                invalidChannel = invalid;
        }
}
