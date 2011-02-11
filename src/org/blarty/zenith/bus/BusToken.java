package org.blarty.zenith.bus;

// Created as: 13-Jan-2003 : enclosing_type :BusToken.java
// In org.blarty.zenith.bus
/**
 * Represents a clients interest in a certain topic, obtained using the
 * subscribe() method
 * @author calum

 */
public interface BusToken {
	public Object getToken();
}
