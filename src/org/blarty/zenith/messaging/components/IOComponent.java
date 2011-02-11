package org.blarty.zenith.messaging.components;

import org.blarty.zenith.messaging.channels.PublishingChannel;
import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.system.ChannelAction;
import org.blarty.zenith.messaging.system.ChannelException;

public interface IOComponent extends MessageComponent{
        public void setInputChannel(ReceiverChannel input) throws ChannelException;
        public void setOutputChannel(PublishingChannel output) throws ChannelException;
      
}
