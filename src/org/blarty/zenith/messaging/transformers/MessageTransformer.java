/*
 * zenith : org.jini.projects.zenith.messaging.transformers
 * 
 * 
 * MessageTransformer.java
 * Created on 19-Apr-2005
 * 
 * MessageTransformer
 *
 */
package org.jini.projects.zenith.messaging.transformers;

import org.jini.projects.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface MessageTransformer {
	public Object transform(Message toTransform);
}
