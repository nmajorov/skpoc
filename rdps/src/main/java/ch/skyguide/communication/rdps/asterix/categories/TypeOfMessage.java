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
public class TypeOfMessage extends AbstractLoggingNonCompoundDataItem {

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
	public TypeOfMessage(final short dataCategory, final int dataItemIdentifier,
			final String description, final String units) {

		super(dataCategory, dataItemIdentifier, 1, description, units);
	}

	// -------------------------------------------------------------------------
	// public member functions

	/***************************************************************************
	 * Creates the associated data fields.
	 * 
	 * @param repetitionFactor
	 *            The repetition factor for the data item.
	 * 
	 * @return The data field associated with the data items.
	 * 
	 */
	@Override
	public final NonCompoundField 
	createAssociatedDataField(final int repetitionFactor) {
		
		return new DefaultFixedDataField(m_sizeFactor, this, false);
	}

	/***************************************************************************
	 * Create the associated data field based on the information contained in
	 * the byte buffer.
	 * 
	 * @param inBuffer
	 *            The input buffer containing the information.
	 * 
	 * @return The {@code NonCompoundField} created from the data item and
	 *         the input buffer information.
	 * 
	 */
	@Override
	public final NonCompoundField 
	createAssociatedDataField(ByteBuffer inBuffer) {

		return createAssociatedDataField(1);
	}

	/***************************************************************************
	 * Pack the track number into the data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param familyOfMessage
	 *            The family of asterix message.
	 * 
	 * @param natureOfMessage
	 *            The nature of the asterix message.
	 * 
	 * @return The {@code NonCompoundField} containing the packed data.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public final NonCompoundField pack(final byte familyOfMessage, 
								 	   final byte natureOfMessage) {
		
		NonCompoundField dataField = createAssociatedDataField(1);
		ByteBuffer tempBuffer 
			= ByteBuffer.allocate(dataField.getFieldLengthIndicator());

		try {

			byte temp = 0;
			temp |= (natureOfMessage << 4) >> 4;
			temp |= familyOfMessage << 4;
			tempBuffer.put(temp);
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
	public final String toUnpackedString(DataField field) {

		StringBuilder str = new StringBuilder(getDescription() + "={");
		TypeOfMessageContainer container = unpack((NonCompoundField) field);

		str.append("FamilyOfMessage : "
				+ Byte.valueOf(container.getFamilyOfMessage()).intValue()
				+ ", ");
		str.append("NatureOfMessage : "
				+ Byte.valueOf(container.getNatureOfMessage()).intValue());
		str.append("}");
		return str.toString();
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
	public final TypeOfMessageContainer 
	unpack(final NonCompoundField dataField) {
		

		try {

			final ByteBuffer tempBuffer = dataField.encode();

			byte temp = tempBuffer.get();

			TypeOfMessageContainer container 
			= new TypeOfMessageContainer((byte) (temp >> 4),
										 (byte) ((temp << 4) >> 8));

			return container;

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
