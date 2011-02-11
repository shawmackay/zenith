package org.jini.projects.zenith.messaging.system;

import org.jini.projects.zenith.messaging.messages.Message;

public interface ControllableChannelAction extends ChannelAction{
        public void controlDispatch(Message m);
}
