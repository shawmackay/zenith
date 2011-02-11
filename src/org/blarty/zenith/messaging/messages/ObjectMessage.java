package org.blarty.zenith.messaging.messages;

import java.io.InputStream;

/**
 * User: calum Date: 24-Feb-2004 Time: 09:40:37
 */
public class ObjectMessage implements Message {
	/**
     * 
     */
    private static final long serialVersionUID = 3257007652889704756L;
    private MessageHeader header;
	private Object content;

	public ObjectMessage(MessageHeader header, Object content) {
		this.header = header;
		//if (content instanceof Serializable)
			this.content = content;
	}

	public MessageHeader getHeader() {
		return header;
	}

	public InputStream getContentStream() {
		return null; //To change body of implemented methods use File |
						 // Settings | File Templates.
	}

	public byte[] getMessageContentAsByte() {
		return new byte[0]; //To change body of implemented methods
									  // use File | Settings | File Templates.
	}

	public Object getMessageContent() {
		return content;
	}

	public String getMessageContentAsString() {
		if (content != null)
			return content.toString();
		else
			return "<void>";
	}
}
