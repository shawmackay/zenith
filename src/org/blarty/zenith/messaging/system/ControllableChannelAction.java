package org.blarty.zenith.messaging.system;

import org.blarty.zenith.messaging.messages.Message;

public interface ControllableChannelAction extends ChannelAction{
        public void controlDispatch(Message m);
}
