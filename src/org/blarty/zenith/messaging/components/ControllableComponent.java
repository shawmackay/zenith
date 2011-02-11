package org.blarty.zenith.messaging.components;

import org.blarty.zenith.messaging.channels.PublishingChannel;
import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.system.ChannelException;

public interface ControllableComponent extends MessageComponent{
        public void setInputChannel(ReceiverChannel input) throws ChannelException;
        public void setControlChannel(ReceiverChannel control) throws ChannelException;
        public void setInvalidChannel(PublishingChannel invalid) throws ChannelException;
}
