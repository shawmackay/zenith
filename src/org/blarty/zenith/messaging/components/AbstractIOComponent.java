package org.blarty.zenith.messaging.components;

import org.blarty.zenith.messaging.channels.PublishingChannel;
import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.system.ChannelAction;
import org.blarty.zenith.messaging.system.ChannelDispatcher;
import org.blarty.zenith.messaging.system.ChannelException;

public abstract class AbstractIOComponent implements IOComponent {

        private ReceiverChannel input;
        
        private PublishingChannel output;
        
        private ChannelDispatcher currentDispatcher;
        
        public void setInputChannel(ReceiverChannel input) throws ChannelException {
                // TODO Auto-generated method stub
                this.input = input;
        }

        public void setOutputChannel(PublishingChannel output) throws ChannelException {
                // TODO Auto-generated method stub
                this.output = output;
        }
        
        public void setChannelAction(ChannelAction action) {
                System.out.println("Setting channel dispatcher");
                if (currentDispatcher != null) {
                        System.out.println("Existing channel dispatcher...stopping");
                        currentDispatcher.signalStop();
                        while (currentDispatcher.isAlive())
                                Thread.yield();
                }
                currentDispatcher = new ChannelDispatcher(action);
        }
        
}
