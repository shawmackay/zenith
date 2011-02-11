package org.blarty.zenith.messaging.components.transformation;

import org.blarty.zenith.messaging.components.IOComponent;

import org.blarty.zenith.messaging.messages.Message;

public interface EnvelopeUnwrapper extends IOComponent{
        public Object unwrap(Message m);
}
