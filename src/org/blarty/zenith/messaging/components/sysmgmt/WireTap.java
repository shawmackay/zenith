package org.jini.projects.zenith.messaging.components.sysmgmt;

import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.components.MessageComponent;

public interface WireTap extends MessageComponent{
        public void setOn(ReceiverChannel input, PublishingChannel tapChannel);
}
