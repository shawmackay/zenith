package org.jini.projects.zenith.messaging.components.transformation;

import org.jini.projects.zenith.messaging.components.IOComponent;
import org.jini.projects.zenith.messaging.messages.Message;

public interface EnvelopeWrapper extends IOComponent {
        public Message wrap(Object content);
}
