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

package ch.skyguide.communication.rdps.asterix.categories.cat242;

//------------------------------------------------------------------------------

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/**
 * Represents the Asterix I242/040 Data Item for the Local Track Number.
 * 
 * @author R. Wilson
 */
public final class LocalTrackNumber 
extends AbstractLoggingNonCompoundDataItem {

	// -------------------------------------------------------------------------
	// public static final data members (constants)

	// -------------------------------------------------------------------------
	// public constructors

	/***************************************************************************
	 * Public defined constructor
	 * 
	 * @param dataCategory
	 *            The data category for this data item
	 * 
	 * @param dataItemIdentifier
	 *            The data item identifier.
	 * 
	 * @param description
	 *            Description of the data item and contents.
	 * 
	 * @param units
	 *            Units of the data item.
	 */
	public LocalTrackNumber(final short dataCategory, 
							final int dataItemIdentifier,
							final String description, 
							final String units) {
		
		super(dataCategory, dataItemIdentifier, 2, description, units);
	}

	// -------------------------------------------------------------------------
	// public member functions

	/***************************************************************************
	 * Creates the associated data fields.
	 * 
	 * @param p_inBuffer
	 *            The input buffer containing the information.
	 * 
	 * @return The data field associated with the data items.
	 * 
	 */
	@Override
	public final NonCompoundField 
	createAssociatedDataField(final ByteBuffer inBuffer) {
		
		return createAssociatedDataField(1);
	}

	/***************************************************************************
	 * Create the associated data field based on the information contained in
	 * the byte buffer.
	 * 
	 * @param p_iRepetitionFactor
	 *            The repetition factor for the data item.
	 * 
	 * @return The {@code NonCompoundField} created from the data item and
	 *         the input buffer information.
	 * 
	 */
	@Override
	public final NonCompoundField 
	createAssociatedDataField(final int repetitionFactor) {
		
		return new DefaultFixedDataField(m_sizeFactor, this, false);
	}

	/***************************************************************************
	 * Pack the track number into the data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param localTrackNumber
	 *            The local track number to be encoded in the data field.
	 * 
	 * @param valid
	 *        Indicating whether the local track number is valid or invalid. 
	 *        True for valid and false for invalid local track number.
	 * 
	 * @return The {@code NonCompoundField} containing the packed data.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public final NonCompoundField pack(final int localTrackNumber,
									   final boolean valid) {
		
		final NonCompoundField dataField = createAssociatedDataField(1);
		final ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
				.getFieldLengthIndicator());

		try {

			int temp = 0;

			if (valid) {

				temp |= 0x1;
				temp = temp << 15;
			}

			temp |= (localTrackNumber << 8) >>> 8;
			tempBuffer.putShort((short) temp);
			tempBuffer.flip();
			dataField.decode(tempBuffer);
		} catch (BufferOverflowException exception) { // The buffer should not
														// be filled after
														// reaching its limit.
			logError(BUFFER_OVERFLOW_MESSAGE);
			throw exception;
		}

		return dataField;
	}

	/***************************************************************************
	 * See description of superclass/interface.
	 * 
	 * @see ch.skyguide.message.structure.dataitem.genericstructure.dataitem.DataItem#toUnpackedString(ch.skyguide.message.structure.datafield.genericstructure.datafield.DataField)
	 */
	@Override
	public final String toUnpackedString(final DataField field) {
		
		return getDescription() + "={" + unpack((NonCompoundField) field) + "}";
	}

	/***************************************************************************
	 * Unpack the local track number from the data field.
	 * 
	 * @param dataField
	 *            The data field containing the track number.
	 * 
	 * @return The local track number container from the data field.
	 * 
	 * @throws BufferUnderflowException
	 *             if more elements shall be read after reaching the buffer's
	 *             limit.
	 */
	public final LocalTrackNumberContainer
	unpack(NonCompoundField dataField) {
		
		final ByteBuffer tempBuffer = dataField.encode();

		boolean valid = false;
		int localTrackNumber = 0;

		try {
			
			int temp = tempBuffer.getShort();

			valid = ((temp & 0x100000) == 0x100000);
			localTrackNumber = (temp << 8) >>> 8;
			
			return new LocalTrackNumberContainer(localTrackNumber, valid);
			
		} catch (BufferUnderflowException exception) { // Trying to read after
														// reaching the limit of
														// the buffer.
			logError(BUFFER_UNDERFLOW_MESSAGE);
			throw exception;
		}
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

	// -------------------------------------------------------------------------
	// private static data members

	// -------------------------------------------------------------------------
	// Static initialization block
	
	public static class LocalTrackNumberContainer {
		
		public LocalTrackNumberContainer(final int localTrackNumber, 
										 final boolean valid) {
			
			m_localTrackNumber = localTrackNumber;
			m_valid = valid;
		}
		
		public final int getLocalTrackNumber() {
			
			return m_localTrackNumber;
		}
		
		public final boolean getValid() {
			
			return m_valid;
		}
		
		private final int m_localTrackNumber;
		
		private final boolean m_valid;
	}

}
