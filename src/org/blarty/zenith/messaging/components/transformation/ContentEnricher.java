package org.jini.projects.zenith.messaging.components.transformation;

import org.jini.projects.zenith.messaging.components.IOComponent;

public interface ContentEnricher extends IOComponent{
        public void setExternalSource(Object source);
        public Object getExternalSource();
}
