/*
 * Apollo : apollo.messaging.messages
 * 
 * 
 * KVPair.java
 * Created on 24-Feb-2004
 * 
 * KVPair
 *
 */
package org.jini.projects.zenith.messaging.messages;

import java.io.Serializable;

/**
 * Implements a simple key-value pair
 * Useful when using a String message transformation
 * can't be reconstructed into Objects.
 * @author calum
 */
public class KVPair implements Serializable{
   
    /**
     * 
     */
    private static final long serialVersionUID = 4050485603563090743L;
    String key;
    Object value;
    public KVPair(String key, Object value){
    	this.key = key;
        this.value= value;
    }
	/**
	 * @return Returns the key.
	 */
	public String getKey() {
		return this.key;
	}
	/**
	 * @param key The key to set.
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return this.value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
    
    public String toString(){
    	return "KV:" + key + ":" + value;
    }
}
