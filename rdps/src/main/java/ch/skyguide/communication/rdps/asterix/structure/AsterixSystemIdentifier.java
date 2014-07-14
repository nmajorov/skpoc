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

package ch.skyguide.communication.rdps.asterix.structure;

//------------------------------------------------------------------------------

//------------------------------------------------------------------------------

/*******************************************************************************
 * Represents the unique codes for an asterix server, comprising of the Source
 * Area Code and the Server Identification Code.
 * 
 * @author R. Wilson
 */
public final class AsterixSystemIdentifier {
	
	// -------------------------------------------------------------------------
	// public static final data members (constants)

	// -------------------------------------------------------------------------
	// public constructors

	/***************************************************************************
	 * Defined constructor take a System Area Code (SAC) and a System
	 * Identification Code (SIC).
	 * 
	 * @param systemAreaCode
	 *            Identifies the state in which the system operates.
	 * 
	 * @param systemIdentificationCode
	 *            The unique system identification code within the state.
	 */
	public AsterixSystemIdentifier(final byte systemAreaCode,
								   final byte systemIdentificationCode) {
		
		m_systemIdentificationCode = systemIdentificationCode;
		m_systemAreaCode = systemAreaCode;
	}

	// -------------------------------------------------------------------------
	// public member functions

	/***************************************************************************
	 * Retrieve the System Identification Code.
	 * 
	 * @return A {@code byte} containing the system identification code.
	 */
	public final byte getSystemIdentificationCode() {
		return m_systemIdentificationCode;
	}

	/***************************************************************************
	 * Retrieves the Source Area Code.
	 * 
	 * @return A {@code byte} containing the system area code.
	 */
	public final byte getSystemAreaCode() {
		return m_systemAreaCode;
	}
	
	/**
	 * Extracts the system area code and system identification code as a 
	 * byte array.
	 * 
	 * @return An array of {@code byte}s containing two bytes with the system 
	 * 		   area code as the first byte and the system identification code
	 * 		   as the second.
	 */
	public final byte[] toByteArray() {
		
		return new byte[]{getSystemAreaCode(), getSystemIdentificationCode()};
	}

	/***************************************************************************
	 * See description of superclass/interface.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "[systemIdentificationCode="
				+ Byte.valueOf(m_systemIdentificationCode).intValue()
				+ ", systemAreaCode="
				+ Byte.valueOf(m_systemAreaCode).intValue() + "]";
	}

	// -------------------------------------------------------------------------
	// public static member functions

	// -------------------------------------------------------------------------
	// protected constructors

	// -------------------------------------------------------------------------
	// protected member functions

	// -------------------------------------------------------------------------
	// protected static member functions

	// -------------------------------------------------------------------------
	// protected data members

	// -------------------------------------------------------------------------
	// protected static data members

	// -------------------------------------------------------------------------
	// package constructors

	// -------------------------------------------------------------------------
	// package member functions

	// -------------------------------------------------------------------------
	// package static member functions

	// -------------------------------------------------------------------------
	// package data members

	// -------------------------------------------------------------------------
	// package static data members

	// -------------------------------------------------------------------------
	// private constructors

	// -------------------------------------------------------------------------
	// private member functions

	// -------------------------------------------------------------------------
	// private static member functions

	// -------------------------------------------------------------------------
	// private data members

	/**
	 * The ASTERIX System Identification code.
	 */
	private final byte m_systemIdentificationCode;

	/**
	 * The ASTERIX System Area Code
	 */
	private final byte m_systemAreaCode;

	// -------------------------------------------------------------------------
	// private static data members

	// -------------------------------------------------------------------------
	// Static initialization block

}
