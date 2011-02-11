package org.blarty.zenith.messaging.components.transformation;

import org.blarty.zenith.messaging.components.IOComponent;
import org.blarty.zenith.messaging.messages.Message;

public interface ClaimCheck extends IOComponent{
        public void createClaim(Message m);
}
