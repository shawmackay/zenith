package org.jini.projects.zenith.bus;

// Created as: 14-Jan-2003 : enclosing_type :BusTokenImpl.java
// In org.jini.projects.zenith.bus
/**
 * 
 * @author calum

 */
public class BusTokenImpl implements BusToken{
	
	private String token;
	
	public BusTokenImpl(String token){
		this.token = token;
	}
	
	/**
	 * @see org.jini.projects.zenith.bus.BusToken#getToken()
	 */
	public Object getToken() {
		return token;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof BusToken){
			String var = ((BusToken) obj).getToken().toString();
			if (var.equals(token))
				return true;
		}
		return false;
	}
}
