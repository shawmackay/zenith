package org.jini.projects.zenith.messaging.system;

import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.messages.Message;

public interface ChannelAction {
        public boolean dispatch(Message m);
}
