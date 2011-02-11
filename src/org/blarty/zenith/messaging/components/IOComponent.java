package org.jini.projects.zenith.messaging.components;

import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.system.ChannelAction;
import org.jini.projects.zenith.messaging.system.ChannelException;

public interface IOComponent extends MessageComponent{
        public void setInputChannel(ReceiverChannel input) throws ChannelException;
        public void setOutputChannel(PublishingChannel output) throws ChannelException;
      
}
