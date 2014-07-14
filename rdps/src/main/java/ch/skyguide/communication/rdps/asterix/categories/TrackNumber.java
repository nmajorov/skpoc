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

package ch.skyguide.communication.rdps.asterix.categories;

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
 * @author R. Wilson
 */
public class TrackNumber extends AbstractLoggingNonCompoundDataItem {

	// --------------------------------------------------------------------------
	// public static final data members (constants)

	// --------------------------------------------------------------------------
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
	public TrackNumber(short dataCategory, int dataItemIdentifier,
			String description, String units) {
		super(dataCategory, dataItemIdentifier, 2, description, units);
	}

	// --------------------------------------------------------------------------
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
	public NonCompoundField createAssociatedDataField(ByteBuffer p_inBuffer) {
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
	public NonCompoundField createAssociatedDataField(int p_iRepetitionFactor) {
		return new DefaultFixedDataField(m_sizeFactor, this, false);
	}

	/***************************************************************************
	 * Pack the track number into the data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param numberingRestarted
	 *            Indicating if the numbering has restarted.
	 * 
	 * @param trackNumber
	 *            The track number to be encoded in the data field.
	 * 
	 * @return The {@code NonCompoundField} containing the packed data.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public NonCompoundField pack(boolean numberingRestarted, int trackNumber) {
		NonCompoundField dataField = createAssociatedDataField(1);
		ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
				.getFieldLengthIndicator());

		try {

			int temp = 0;

			if (numberingRestarted) {

				temp |= 0x1;
				temp = temp << 13;
			}

			temp |= (trackNumber << 12) >>> 12;
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
	public String toUnpackedString(DataField field) {
		return getDescription() + "={" + unpack((NonCompoundField) field) + "}";
	}

	/***************************************************************************
	 * Unpack the track number from the data field.
	 * 
	 * @param dataField
	 *            The data field containing the track number.
	 * 
	 * @return The track number from the data field.
	 * 
	 * @throws BufferUnderflowException
	 *             if more elements shall be read after reaching the buffer's
	 *             limit.
	 */
	public int unpack(NonCompoundField dataField) {
		ByteBuffer tempBuffer = dataField.encode();
		@SuppressWarnings("unused")
		boolean numberingRestarted = false;
		int trackNumber = 0;

		try {
			int temp = tempBuffer.getShort();
			// TODO: rjw: numberingRestarted should be given back, too.
			numberingRestarted = ((temp & 0x1000) == 0x1000);
			trackNumber = (temp << 12) >>> 12;
		} catch (BufferUnderflowException exception) { // Trying to read after
														// reaching the limit of
														// the buffer.
			logError(BUFFER_UNDERFLOW_MESSAGE);
			throw exception;
		}

		return trackNumber;
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

	// --------------------------------------------------------------------------
	// private static data members

	// --------------------------------------------------------------------------
	// Static initialization block

}
