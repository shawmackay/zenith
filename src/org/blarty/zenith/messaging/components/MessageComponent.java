package org.jini.projects.zenith.messaging.components;

import org.jini.projects.zenith.messaging.system.ChannelAction;
import org.jini.projects.zenith.messaging.system.UnsupportedActionException;

public interface MessageComponent {
        public void setChannelAction(ChannelAction action) throws UnsupportedActionException;
}
