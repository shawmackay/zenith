package org.jini.projects.zenith.messaging.system;

import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.channels.connectors.ReceivingQConnector;
import org.jini.projects.zenith.messaging.messages.Message;

public  class ChannelDispatcher
                extends
                Thread {
        
        private boolean signalStop = false;
        
        private ReceiverChannel input;
        
        private ReceivingQConnector connector;
        
        private PublishingChannel invalid;
        
        private PublishingChannel output;
        
        private ChannelAction theAction;
        
        public ChannelDispatcher(ChannelAction action){
                super();
                theAction = action;
        }
        
        public void setInvalidChannel(PublishingChannel invalid){
                this.invalid = invalid;
        }
        
        public void setReceivingChannel(ReceiverChannel input) throws ChannelException{
                this.input = input;
                connector = MessagingManager.getManager().registerOnChannel(input.getName(),null);
        }
        
        public void setOutputChannel(PublishingChannel output) throws ChannelException{
                this.output = output;
        }
        
        public void setAction(ChannelAction action){
                this.theAction = action;
        }
        public synchronized void signalStop(){
                this.signalStop = true;
                connector = null;
        }
        
        public void run(){
                while (!signalStop) {
                        Message m = connector.receive(1000);
                        boolean consumed = false;
                        if (m != null){
                                consumed = theAction.dispatch(m);
                                if(!consumed)
                                        invalid.getPublishingQConnector().sendMessage(m);
                                
                        }
                }
                System.out.println("ChannelDispatcher complete");
        }
}
