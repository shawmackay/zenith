package org.jini.projects.zenith.messaging.components.sysmgmt;

import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.components.MessageComponent;
import org.jini.projects.zenith.messaging.components.routers.Filter;

public interface Detour extends MessageComponent{
        public void setDetourWhen(Filter decision, PublishingChannel detourChannel);
}
