package org.blarty.zenith.messaging.components;

import org.blarty.zenith.messaging.system.ChannelAction;
import org.blarty.zenith.messaging.system.UnsupportedActionException;

public interface MessageComponent {
        public void setChannelAction(ChannelAction action) throws UnsupportedActionException;
}
