/*
 * Created on 22-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.jini.projects.zenith.messaging.channels;

/**
 * @author Calum
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface MessageChannel {
	public String getName();	
	public void setGuaranteed(boolean guaranteeDelivery);
	public boolean isGuaranteed();
    public int getOutstanding();
}
