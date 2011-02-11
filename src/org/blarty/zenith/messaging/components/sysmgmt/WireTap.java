package org.blarty.zenith.messaging.components.sysmgmt;

import org.blarty.zenith.messaging.channels.PublishingChannel;
import org.blarty.zenith.messaging.channels.ReceiverChannel;
import org.blarty.zenith.messaging.components.MessageComponent;

public interface WireTap extends MessageComponent{
        public void setOn(ReceiverChannel input, PublishingChannel tapChannel);
}
