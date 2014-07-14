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

import java.nio.ByteBuffer;
import java.util.BitSet;

import ch.skyguide.message.structure.type.AbstractFieldSpecificationType;

//------------------------------------------------------------------------------

/*******************************************************************************
 * The class represents
 * 
 * @author R. Wilson
 */
public class AsterixFieldSpecificationType extends
		AbstractFieldSpecificationType {

	// --------------------------------------------------------------------------
	// public static final data members (constants)

	// --------------------------------------------------------------------------
	// public constructors

	/***************************************************************************
	 * Public defined constructor for a specification to be set.
	 * 
	 */
	public AsterixFieldSpecificationType() {
		super();
	}

	/***************************************************************************
	 * Public defined constructor taking a byte array to determine the field
	 * specification of the message.
	 * 
	 * @param inBuffer
	 *            Creates the Field Specification from a byte buffer.
	 */
	public AsterixFieldSpecificationType(ByteBuffer inBuffer) {
		super();

		if (inBuffer == null) {

			throw new NullPointerException(
					"field specification not initialized");
		}

		ByteBuffer fieldSpecification = inBuffer.slice();

		// ----------------------------------------------------------------------
		// Iterate through the field specification buffer byte for byte until
		// the last bit of the byte being set to zero.
		m_fieldSpecification = new BitSet();

		short field = 0;
		do {

			field = (short) (fieldSpecification.get() & 0xFF);
			int exponent = 0;
			// Process each byte
			for (int bit = 7; bit >= 0; bit--) {

				// Select only the desired bit.
				exponent = ((int) Math.pow(2, bit));
				if ((field & exponent) == exponent) {

					m_fieldSpecification.set(m_position++);
				} else {
					m_fieldSpecification.clear(m_position++);
				}
			}
		} while (((field & 0x01) == 0x01) && fieldSpecification.hasRemaining());

		inBuffer.position(inBuffer.position() + fieldSpecification.position());
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
	public AsterixFieldSpecificationType clone() {
		// ----------------------------------------------------------------------
		// Obtain an object by invoking the base class' clone method.
		AsterixFieldSpecificationType clonedSpecification;
		clonedSpecification = (AsterixFieldSpecificationType) super.clone();
		return clonedSpecification;
	}

	/***************************************************************************
	 * Enable (modify) a field specification
	 * 
	 * Precondition: fieldReferenceNumber > 0
	 * 
	 * @param fieldReferenceNumber
	 */
	@Override
	public final void enableField(int fieldReferenceNumber) {
		if (fieldReferenceNumber <= 0) {
			throw new IllegalArgumentException("FieldReferenceNumber "
					+ fieldReferenceNumber + " cannot be enabled");
		}

		m_fieldSpecification.set(determinePosition(fieldReferenceNumber));
	}

	/***************************************************************************
	 * Disable a field specification Precondition: fieldReferenceNumber > 0
	 * 
	 * @param fieldReferenceNumber
	 */
	@Override
	public final void disableField(int fieldReferenceNumber) {
		if (fieldReferenceNumber <= 0) {
			throw new IllegalArgumentException("FieldReferenceNumber "
					+ fieldReferenceNumber + " cannot be disabled");
		}

		m_fieldSpecification
				.set(determinePosition(fieldReferenceNumber), false);
	}

	/***************************************************************************
	 * Retrieves the number of bytes the field specification would occupy when
	 * converted to an array of bytes.
	 * 
	 * @return The number of bytes occupied by the byte array of the field
	 *         specification
	 */
	@Override
	public final int getBytes() {
	
		// Determine size of the array in bytes.
		int numberOfBytes = m_fieldSpecification.length() / 8;
		int modulas = m_fieldSpecification.length() % 8;
		int length = m_fieldSpecification.length();

		if (m_fieldSpecification.length() % 8 > 0) {

			numberOfBytes++;
		}
		
		// Minimum field specification is one byte.  
		if(m_fieldSpecification.isEmpty()) {
			
			numberOfBytes++;
		}

		return numberOfBytes;
	}

	/***************************************************************************
	 * Returns the FieldSpecification as a byte array.
	 * 
	 * @return The byte array containing the Field Specification.
	 */
	@Override
	public final byte[] toByteArray() {
		// Determine size of the array in bytes.
		final int numberOfBytes = getBytes();

		byte temp = 0;
		final ByteBuffer byteBuffer = ByteBuffer.allocate(numberOfBytes);

		for (int byteIndex = 0; byteIndex < numberOfBytes; byteIndex++) {

			temp = 0;

			for (int bitIndex = 7; bitIndex >= 1; bitIndex--) {

				if (m_fieldSpecification.get((byteIndex * 8) + (7 - bitIndex))) {

					temp |= (int) Math.pow(2, bitIndex);
				}
			}

			if (byteIndex + 1 < numberOfBytes) {

				temp |= 0x01;
			}

			byteBuffer.put(temp);
		}

		return byteBuffer.array();
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
	// private member functions

	/**
	 * Private helper method to determine the field specification {@code BitSet}
	 * position based on the field reference number.
	 * 
	 * @param fieldReferenceNumber
	 *            The {@code int} field reference number to determine the
	 *            position.
	 * 
	 * @return The {@code int} representing the position of the field reference
	 *         number within the field specification {@code BitSet}.
	 */
	private final int determinePosition(final int fieldReferenceNumber) {

		final int byteNumber = ((fieldReferenceNumber - 1) / 7) + 1;

		final int position = fieldReferenceNumber + byteNumber - 2;

		return position;
	}

	// --------------------------------------------------------------------------
	// private static data members

	// --------------------------------------------------------------------------
	// Static initialization block
}
