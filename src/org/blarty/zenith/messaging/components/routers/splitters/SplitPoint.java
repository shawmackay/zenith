package org.blarty.zenith.messaging.components.routers.splitters;

/*
* SplitPoint.java
*
* Created Mon Apr 04 09:24:13 BST 2005
*/

/**
*
* @author  calum
*
*/

public interface SplitPoint{
	public String getSplitDefinition();
	public String getDestination();

	public void setSplitDefinition(String boundary);
	public void setDestination(String destination);
	
}
