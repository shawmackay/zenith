package org.jini.projects.zenith.messaging.messages;

import java.io.Serializable;

/**
 *Represents a response from a message call
 */
public class Response implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 3904961958338836791L;
    private Object objResponse;

	public void setResponseObject(Object objResponse) {
		this.objResponse = objResponse;
	}

	public Object getResponseObject() {
		return objResponse;
	}
}
