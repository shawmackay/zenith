package org.blarty.zenith.messaging.components.sysmgmt;

import org.blarty.zenith.messaging.channels.PublishingChannel;
import org.blarty.zenith.messaging.components.MessageComponent;
import org.blarty.zenith.messaging.components.routers.Filter;

public interface Detour extends MessageComponent{
        public void setDetourWhen(Filter decision, PublishingChannel detourChannel);
}
