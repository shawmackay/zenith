package org.jini.projects.zenith.messaging.components.routers.splitters;

/*
* XPathSplitPoint.java
*
* Created Mon Apr 04 09:25:10 BST 2005
*/

/**
* Defines an XPath Query that should be run against a set of XML to produce a <b>Node Set</b>.
* For Split points that query data that can return values other than Node Sets (i.e. count(...) returning xs:integer)
* see XSLTSplitPoint
* @author  calum
*
*/

public class XPathSplitPoint implements SplitPoint{
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
	

	/**
	* XPathSplitPoint
	* @param destination
	* @param splitPoint
	* @param repeating
	*/
	public XPathSplitPoint(String destination, String splitPoint, boolean repeating){
		this.destination=destination;
		this.splitPoint=splitPoint;
		this.repeating=repeating;
	}

	public XPathSplitPoint(String destination, String splitPoint){
		this.destination=destination;
		this.splitPoint=splitPoint;
		this.repeating=false;
	}

}
