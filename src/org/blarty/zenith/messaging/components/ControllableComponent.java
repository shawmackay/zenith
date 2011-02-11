package org.jini.projects.zenith.messaging.components;

import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.system.ChannelException;

public interface ControllableComponent extends MessageComponent{
        public void setInputChannel(ReceiverChannel input) throws ChannelException;
        public void setControlChannel(ReceiverChannel control) throws ChannelException;
        public void setInvalidChannel(PublishingChannel invalid) throws ChannelException;
}
