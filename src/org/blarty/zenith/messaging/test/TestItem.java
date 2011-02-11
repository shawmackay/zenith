package org.jini.projects.zenith.messaging.test;

/*
* TestItem.java
*
* Created Mon Apr 04 11:33:36 BST 2005
*/
import org.jini.projects.zenith.messaging.system.MessagingManager;
/**
*
* @author  calum
*
*/

public interface TestItem{
	public void run(final MessagingManager mgr);
}
