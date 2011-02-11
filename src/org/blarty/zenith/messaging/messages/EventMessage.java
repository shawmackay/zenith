package org.jini.projects.zenith.messaging.messages;

import java.io.InputStream;
import java.io.Serializable;

/**
 * User: calum Date: 24-Feb-2004 Time: 09:40:37
 */
public class EventMessage implements Message {
	/**
     * 
     */
    private static final long serialVersionUID = 3256727260278174770L;
    private MessageHeader header;
	private Object content;
	private int id;
	private String eventName;
	public EventMessage(MessageHeader header, Object content, int id,String eventName) {
		this.header = header;
		if (content instanceof Serializable)
			this.content = content;
		this.eventName = eventName;
		this.id = id;
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
	/**
	 * @return Returns the eventName.
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param header The header to set.
	 */
	public void setHeader(MessageHeader header) {
		this.header = header;
	}
}
