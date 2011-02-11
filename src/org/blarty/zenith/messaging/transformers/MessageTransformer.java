/*
 * zenith : org.blarty.zenith.messaging.transformers
 * 
 * 
 * MessageTransformer.java
 * Created on 19-Apr-2005
 * 
 * MessageTransformer
 *
 */
package org.blarty.zenith.messaging.transformers;

import org.blarty.zenith.messaging.messages.Message;

/**
 * @author calum
 */
public interface MessageTransformer {
	public Object transform(Message toTransform);
}
