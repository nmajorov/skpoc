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

/*******************************************************************************
 * @author R. Wilson
 */
public class TimeOfMessage extends AbstractLoggingNonCompoundDataItem {

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
	public TimeOfMessage(final short dataCategory, final int dataItemIdentifier,
						 final String description, final String units) {
		
		super(dataCategory, dataItemIdentifier, 3, description, units);
	}

	// -------------------------------------------------------------------------
	// public member functions

	/***************************************************************************
	 * Creates the associated data fields.
	 * 
	 * @param inBuffer
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
	 * @param repetitionFactor
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
	 * Pack the data into the data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param timeSinceMidnight
	 *            The time since midnight in 1/128s to pack into the data field.
	 * 
	 * @return The {@code NonCompoundField} containing the packed data.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public final NonCompoundField pack(final int timeSinceMidnight) {
		
		final NonCompoundField dataField = createAssociatedDataField(1);
		
		try {

			final ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
					.getFieldLengthIndicator());

			tempBuffer.put((byte) (0xff & (timeSinceMidnight >> 16)));
			tempBuffer.put((byte) (0xff & (timeSinceMidnight >> 8)));
			tempBuffer.put((byte) (0xff & (timeSinceMidnight)));

			tempBuffer.flip();
			dataField.decode(tempBuffer);
			// 11111111 00000000 00000000 00000000

			return dataField;
			
		} catch (BufferOverflowException exception) { // The buffer should not
														// be filled after
														// reaching its limit.
			logError(BUFFER_OVERFLOW_MESSAGE);
			throw exception;
		}
	}

	/***************************************************************************
	 * See description of superclass/interface.
	 * 
	 * @see ch.skyguide.message.structure.dataitem.genericstructure.dataitem.DataItem#toUnpackedString(ch.skyguide.message.structure.datafield.genericstructure.datafield.DataField)
	 */
	@Override
	public final String toUnpackedString(DataField field) {
		
		int seconds = unpack((NonCompoundField) field) / 128;
		int modulo = seconds % 3600;
		String hour = String.valueOf((seconds - modulo) / 3600);
		seconds = modulo;
		modulo = seconds % 60;
		String minute = String.valueOf((seconds - modulo) / 60);
		String second = String.valueOf(modulo);
		return getDescription() + "={" + hour + ":" + minute + ":" + second
				+ "}";
	}

	/***************************************************************************
	 * Unpack the data field into a date object.
	 * 
	 * @param dataField
	 *            The {@code NonCompoundField} to unpack.
	 * 
	 * @return The {@code int} containing the time since midnight in 1/128
	 *         of a second.
	 * 
	 * @throws BufferUnderflowException
	 *             if more elements shall be read after reaching the buffer's
	 *             limit.
	 */
	public final int unpack(final NonCompoundField dataField) {
		
		try {
			
			int secondsSinceMidnight = 0;
			final ByteBuffer tempBuffer = dataField.encode();

			secondsSinceMidnight = (tempBuffer.get() & 0xFF) << 16;
			secondsSinceMidnight |= (tempBuffer.get() & 0xFF) << 8;
			secondsSinceMidnight |= (tempBuffer.get() & 0xFF);

			return secondsSinceMidnight;
			
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

}
