/*
 * osiris : osiris
 * 
 * 
 * ClassFilter.java
 * Created on 22-Jul-2003
 *
 */
package org.jini.projects.zenith.messaging.system;

import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.ServiceItemFilter;

/**
 * @author calum
 */
public class ClassFilter implements ServiceItemFilter {
	
	Class cl;
	public ClassFilter(Class cl) {
		this.cl = cl;
		
	}
	public boolean check(ServiceItem item) {
		//System.out.println("Checking " + cl.getName() + " against " + item.service.getClass().getName());
		
		if (cl.isInstance(item.service) )
			return true;
		else
			return false;
	}
}