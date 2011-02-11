/*
 * Created on 22-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.blarty.zenith.messaging.messages;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Calum
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class StringMessage implements Message {
	/**
     * 
     */
    private static final long serialVersionUID = 3689347732511207480L;
    String text;
	MessageHeader header;
        
        public StringMessage(){
            text="unknown";
        }
        
	public StringMessage(MessageHeader header, String message){
		text = message;
        this.header = header;
	}
	
	public String toString(){
		return text;
	}
	
	public MessageHeader getHeader(){
        if(header==null)
        	System.out.println("The message header is null");
		return header;
	}
	
	
	/* (non-Javadoc)
	 * @see apollo.messaging.messages.Message#getMessageContent()
	 */
	public byte[] getMessageContentAsByte() {
		// TODO Auto-generated method stub
        return text.getBytes();
	}

    public InputStream getContentStream() {
        return new ByteArrayInputStream(text.getBytes());

    }

    public Object getMessageContent() {
        return text;
    }


	/* @see apollo.messaging.messages.Message#getMessageAsString()
	 */
	public String getMessageContentAsString() {
		return text;
	}
}
