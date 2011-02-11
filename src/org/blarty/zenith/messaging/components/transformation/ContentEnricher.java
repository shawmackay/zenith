package org.blarty.zenith.messaging.components.transformation;

import org.blarty.zenith.messaging.components.IOComponent;

public interface ContentEnricher extends IOComponent{
        public void setExternalSource(Object source);
        public Object getExternalSource();
}
