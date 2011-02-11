package org.blarty.zenith.messaging.components.routers.splitters;

/*
* RegexSplitPoint.java
*
* Created Mon Apr 04 09:26:04 BST 2005
*/

/**
*
* @author  calum
*
*/

public class RegexSplitPoint implements SplitPoint{

	private String destination;
	private String splitPoint;
	private boolean repeating;
	/**
	* getDestination
	* @return String
	*/
	public String getDestination(){
		return destination;
	}
	/**
	* setDestination
	* @param destination
	*/
	public void setDestination(String destination){
		this.destination=destination;
	}
	/**
	* getSplitPoint
	* @return String
	*/
	public String getSplitDefinition(){
		return splitPoint;
	}
	/**
	* setSplitPoint
	* @param splitPoint
	*/
	public void setSplitDefinition(String splitPoint){
		this.splitPoint=splitPoint;
	}
	

}
