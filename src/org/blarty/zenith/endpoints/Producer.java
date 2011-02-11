package org.jini.projects.zenith.endpoints;

import net.jini.id.Uuid;

// Created as: 13-Jan-2003 : enclosing_type :Producer.java
// In apollo.producer
/**
 * @author calum
 *  
 */
public interface Producer {

    /**
	 * Obtain the identity of the producer for this message
	 * 
	 * @return
	 */
    public Uuid getProducerIdentity();
    
    /**
     * Obtain the localised area of interest for the producer
     * 
     * @author calum
     *
     */
    
    public String getNamespace();
}
