/*
 * osiris : osiris
 * 
 * 
 * NameFilter.java
 * Created on 22-Jul-2003
 *
 */
package org.jini.projects.zenith.messaging.system;

import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.lookup.entry.Name;

/**
 * @author calum
 */
public class NameFilter extends ClassFilter {
	
	String name;

	public NameFilter(String name, Class cl) {
		super(cl);
		
		this.name = name;
	}
	/**
	 * @see net.jini.lookup.ServiceItemFilter#check(net.jini.core.lookup.ServiceItem)
	 */
	public boolean check(ServiceItem item) {
		if (!super.check(item))
			return false;
		Entry[] attr = item.attributeSets;
		for (int i = 0; i < attr.length; i++)
			if (attr[i] instanceof Name) {
				Name n = (Name) attr[i];
				//System.out.print("\t Checking Name: [" + this.name +"] against [" + n.name + "]");
				if (n.name.equals(this.name)){
					//System.out.println("..yes");
					return true;
				}
				//else
				
					//System.out.println("...no");
			}
		return false;
	}
}