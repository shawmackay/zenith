package org.jini.projects.zenith.messaging.components.transformation;

import org.jini.projects.zenith.messaging.components.IOComponent;
import org.jini.projects.zenith.messaging.messages.Message;

public interface ClaimCheck extends IOComponent{
        public void createClaim(Message m);
}
