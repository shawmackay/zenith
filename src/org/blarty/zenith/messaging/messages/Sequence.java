/*
 * Apollo : apollo.messaging.messages
 * 
 * 
 * Sequence.java
 * Created on 23-Feb-2004
 * 
 * Sequence
 *
 */
package org.blarty.zenith.messaging.messages;

import java.io.Serializable;

/**
 * @author calum
 */
public class Sequence implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 3257847666888095798L;
    private int sequenceIdentifier;
    private int positionInSequence;
    private int totalPartsInSequence;
	
	/**
	 * @param sequenceIdentifier
	 * @param positionInSequence
	 * @param totalPartsInSequence
	 */
	public Sequence(int sequenceIdentifier, int positionInSequence, int totalPartsInSequence) {
		super();
		this.sequenceIdentifier = sequenceIdentifier;
		this.positionInSequence = positionInSequence;
		this.totalPartsInSequence = totalPartsInSequence;
	}
	/**
	 * @return Returns the parts position in the overall Sequence.
	 */
	public int getPositionInSequence() {
		return this.positionInSequence;
	}
	/**
	 * @return Returns the sequence Identifier.
	 */
	public int getSequenceIdentifier() {
		return this.sequenceIdentifier;
	}
	/**
	 * @return Returns the total number of parts that makes up this Sequence.
	 */
	public int getTotalPartsInSequence() {
		return this.totalPartsInSequence;
	}
}
