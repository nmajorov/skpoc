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

package ch.skyguide.communication.rdps.asterix.categories.cat065;

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
 * Batch Number field of the cat065
 * 
 * @author Guillaume Fouillet, Ross Wilson
 */
public class BatchNumber extends AbstractLoggingNonCompoundDataItem {

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
	public BatchNumber(final short dataCategory, final int dataItemIdentifier,
					   final String description) {

		super(dataCategory, dataItemIdentifier, 1, description, "n/a");
	}

	// -------------------------------------------------------------------------
	// public member functions

	/***************************************************************************
	 * Creates the associated data fields.
	 * 
	 * @param p_iRepetitionFactor
	 *            The repetition factor for the data item.
	 * 
	 * @return The data field associated with the data items.
	 * 
	 */
	@Override
	public final NonCompoundField 
	createAssociatedDataField(final int p_iRepetitionFactor) {
		
		return new DefaultFixedDataField(m_sizeFactor, this, false);
	}

	/***************************************************************************
	 * Create the associated data field based on the information contained in
	 * the byte buffer.
	 * 
	 * @param p_inBuffer
	 *            The input buffer containing the information.
	 * 
	 * @return The {@code NonCompoundField} created from the data item and the
	 *         input buffer information.
	 * 
	 */
	@Override
	public final NonCompoundField 
	createAssociatedDataField(final ByteBuffer p_inBuffer) {
	
		return createAssociatedDataField(1);
	}

	/***************************************************************************
	 * Pack the batch number into the data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param batchNumber
	 *            The batch number to be encoded in the data field.
	 * 
	 * @return The {@code NonCompoundField} containing the packed data.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public final NonCompoundField pack(final int batchNumber) {

		// can only accept the only bytes.
		final int extractedBatchNumber = (batchNumber << 8) >> 8;

		final NonCompoundField dataField = createAssociatedDataField(1);

		final ByteBuffer tempBuffer 
			= ByteBuffer.allocate(dataField.getFieldLengthIndicator());

		try {

			tempBuffer.putShort((short) extractedBatchNumber);
			tempBuffer.flip();
			dataField.decode(tempBuffer);

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
	public final String toUnpackedString(final DataField field) {
		
		return getDescription() + "={" + unpack((NonCompoundField) field) + "}";
	}

	/***************************************************************************
	 * Unpack the batch number from the data field.
	 * 
	 * @param dataField
	 *            The data field containing the track number.
	 * 
	 * @return The batch number from the data field.
	 * 
	 * @throws BufferUnderflowException
	 *             if more elements shall be read after reaching the buffer's
	 *             limit.
	 */
	public final int unpack(final NonCompoundField dataField) {
		
		try {

			final ByteBuffer tempBuffer = dataField.encode();
			
			if (tempBuffer.position() > 0) {
				tempBuffer.flip();
			}

			final int batchNumber = tempBuffer.get();

			return batchNumber;
			
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
