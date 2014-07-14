//------------------------------------------------------------------------------
// PROJECT:
//      Skyguide BEAM
// LAYER:
//      System Interaction
// COPYRIGHT:
//      Copyright (c) 1998-2007 Skyguide AG, Wangen bei Dubendorf, Switzerland
//      All rights reserved.
// REVISION:
//      $Id:$
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------

package ch.skyguide.communication.rdps.asterix.categories.cat062;

import java.nio.ByteBuffer;

//------------------------------------------------------------------------------

//------------------------------------------------------------------------------

/*******************************************************************************
 * Represents a container for the system track.
 * 
 * @author R. Wilson
 */
public class SystemTrack implements Cloneable {
	// --------------------------------------------------------------------------
	// public static final data members (constants)

	// --------------------------------------------------------------------------
	// public constructors

	/***************************************************************************
	 * Public defined constructor for the system track.
	 * 
	 * @param artasUnitIdentification
	 *            The unit identification for the artas tracker.
	 * 
	 * @param systemTrack
	 *            The system track from the unit.
	 */
	public SystemTrack(int artasUnitIdentification, int systemTrack) {
		m_artasUnitIdentification = ((artasUnitIdentification << 8) >>> 8);
		m_systemTrackNumber = (systemTrack << 20) >>> 20;
	}

	/***************************************************************************
	 * Public defined constructor for the system track.
	 * 
	 * @param buffer
	 * 		  The {@code ByteBuffer} containing the system track encoded
	 * 		  information.  The buffer may not be {@code null}.
	 * 
	 */
	public SystemTrack(final ByteBuffer buffer) {
		
		m_artasUnitIdentification = buffer.get();
		m_systemTrackNumber = buffer.asShortBuffer().get();
	}

	// --------------------------------------------------------------------------
	// public member functions

	/***************************************************************************
	 * Creates and returns a copy of this object. The method performs a
	 * "deep copy" of this object. By convention, the object returned by this
	 * method should be independent of this object (which is being cloned).
	 * Typically, this means copying any mutable objects that comprise the
	 * internal "deep structure" of the object being cloned and replacing the
	 * references to these objects with references to the copies.
	 * 
	 * See also description of class <code>Object</code>.
	 * 
	 * @see java.lang.Object
	 * @see java.lang.Cloneable
	 * 
	 * @return A "deep copy" of this object.
	 */
	@Override
	public SystemTrack clone() {
		// ----------------------------------------------------------------------
		// Obtain an object by invoking the base class' clone method.
		SystemTrack clonedSystemTrack;

		try {
			clonedSystemTrack = (SystemTrack) super.clone();
		} catch (CloneNotSupportedException e) {

			throw new UnsupportedOperationException(e.getMessage());
		}

		// ----------------------------------------------------------------------
		// Copy attributes.
		// Primative data types already covered.
		return clonedSystemTrack;
	}

	/***************************************************************************
	 * Retrieves the artas unit identification.
	 * 
	 * @return The artas unit identification number
	 */
	public int getArtasUnitIdentification() {
		return m_artasUnitIdentification;
	}

	/***************************************************************************
	 * Retrieves the system track.
	 * 
	 * @return The system track number.
	 */
	public int getSystemTrack() {
		return m_systemTrackNumber;
	}

	/***************************************************************************
	 * See description of superclass/interface.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[artasUnitIdentification : " + m_artasUnitIdentification
				+ ", systemTrackNumber : " + m_systemTrackNumber + "]";
	}

	// --------------------------------------------------------------------------
	// public static member functions

	// --------------------------------------------------------------------------
	// protected constructors

	// --------------------------------------------------------------------------
	// protected member functions

	// --------------------------------------------------------------------------
	// protected static member functions

	// --------------------------------------------------------------------------
	// protected data members

	// --------------------------------------------------------------------------
	// protected static data members

	// --------------------------------------------------------------------------
	// package constructors

	// --------------------------------------------------------------------------
	// package member functions

	// --------------------------------------------------------------------------
	// package static member functions

	// --------------------------------------------------------------------------
	// package data members

	// --------------------------------------------------------------------------
	// package static data members

	// --------------------------------------------------------------------------
	// private constructors

	// --------------------------------------------------------------------------
	// private member functions

	// --------------------------------------------------------------------------
	// private static member functions

	// --------------------------------------------------------------------------
	// private data members

	/**
	 * The ARTAS IUnit identification.
	 */
	private final int m_artasUnitIdentification;

	/**
	 * The system track number
	 */
	private final int m_systemTrackNumber;

	// --------------------------------------------------------------------------
	// private static data members

	// --------------------------------------------------------------------------
	// Static initialization block

}
