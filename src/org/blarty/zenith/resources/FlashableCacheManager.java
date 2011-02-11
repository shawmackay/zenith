/*
 * FlashableCacheManager.java
 *
 * Created on April 8, 2002, 10:21 AM
 */

package org.blarty.zenith.resources;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages a set of Flashable Object instances. The manager can be told
 * to update certain objects. The criteria for object selection and midification is
 * a Regular Expression_type matched against object keys. Any items that match the pattern
 * are <i>'flashed'</i> i.e. told to update.
 *
 * @author  calum
 */
public class FlashableCacheManager extends CacheManager {
	static boolean DEBUG = true; //ResourceManager.DEBUG

	/** Creates a new instance of FlashableCacheManager */
	public FlashableCacheManager(boolean createThread) {
		super(createThread);
	}

	/**
	 * Update the single object with the given identifier
	 */
	public void flashCache(Object identifier) {
		Flashable flashOb = (Flashable) cacheHashMap.get(identifier);
		flashOb.applyFlash();
	}

	/**
	 * Update all the objects in the cache which match the given pattern.
	 * Returns the number of objects that were updated. The pattern must be a valid regular expression.
	 * @see java.util.regex.Pattern
	 */

	public int flashPatternCache(String pattern) {
	
		java.util.Set keySet = cacheHashMap.keySet();
		java.util.Iterator keys = keySet.iterator();
		int numflashed = 0;
		while (keys.hasNext()) {
			Object key = keys.next();

			if (key instanceof String) {
				String name = String.valueOf(key);
				
				FlashCachedObject fco = (FlashCachedObject) cacheHashMap.get(key);
				if (fco.isExpired()) {
					cacheHashMap.remove(key);

				} else {
					//TODO :: Build the cache Key
					
					StringBuffer cakey = new StringBuffer();
					Pattern p = Pattern.compile(cakey.toString());
					Matcher m = p.matcher(key.toString());

					if (m.matches()) {
						
						fco.applyFlash();
						numflashed++;
					} 
				}
			}
		}
		return numflashed;
	}

}
