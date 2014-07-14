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
import java.util.BitSet;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/*******************************************************************************
 * Represents the Status of an SDPS service.
 * 
 * @author R. Wilson
 */
public class SDPSConfigurationAndStatus extends
		AbstractLoggingNonCompoundDataItem {
	// -------------------------------------------------------------------------
	// public static final data members (constants)

	// -------------------------------------------------------------------------
	// public constructors

	/***************************************************************************
	 * The public defined constructor.
	 * 
	 * @param dataCategory
	 *            The data category for this data item
	 * 
	 * @param dataItemIdentifier
	 *            The data item identifier.
	 * 
	 * @param sizeFactor
	 *            Size factor of the item in bytes.
	 * 
	 * @param description
	 *            Description of the data item and contents.
	 * 
	 * @param units
	 *            Units of the data item.
	 */
	public SDPSConfigurationAndStatus(final short dataCategory,
									  final int dataItemIdentifier, 
									  final String description, 
									  final String units) {
		
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
	 * @return The {@code NonCompoundField} created from the data item and the
	 *         input buffer information.
	 * 
	 */
	@Override
	public final NonCompoundField 
	createAssociatedDataField(final ByteBuffer inBuffer) {
		
		return createAssociatedDataField(1);
	}

	/***************************************************************************
	 * Packs the data into the data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param nogoStatus
	 *            An enumerator {@code NoGoStatus} indicating the operational
	 *            status of the connected system.
	 * 
	 * @param overload
	 *            A {@code boolean} where {@code true} indicates the system is
	 *            overloaded, otherwise {@code false}.
	 * 
	 * @param invalidTimeSource
	 *            A {@code boolean} where {@code true} indicates the time source
	 *            is not valid, otherwise {@code false}.
	 * 
	 * @param ps_status
	 *            An enumerator {@code ProcessingSystemStatus} indicating the
	 *            processing system selected.
	 * 
	 * @return The packed {@code NonCompoundField}.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public final NonCompoundField pack(final NoGoStatus nogoStatus,
									   final boolean overload, 
									   final boolean invalidTimeSource,
									   final ProcessingSystemStatus ps_status) {

		BitSet bitSet = new BitSet(8);

		// first two bit by default zero.

		switch (ps_status) {

		case NOT_APPLICABLE:
			bitSet.set(2, false);
			bitSet.set(3, false);
			break;

		case SDPS1_SELECTED:
			bitSet.set(2, false);
			bitSet.set(3, true);
			break;

		case SDPS2_SELECTED:
			bitSet.set(2, true);
			bitSet.set(3, false);
			break;

		case SDPS3_SELECTED:
			bitSet.set(2, true);
			bitSet.set(3, true);
			break;

		default:
			bitSet.set(2, false);
			bitSet.set(3, false);
			break;
		}

		bitSet.set(4, invalidTimeSource);
		bitSet.set(5, overload);

		switch (nogoStatus) {

		case OPERATIONAL:
			bitSet.set(6, false);
			bitSet.set(7, false);
			break;

		case DEGRADED:
			bitSet.set(6, false);
			bitSet.set(7, true);
			break;

		case NOT_CURRENTLY_CONNECTED:
			bitSet.set(6, true);
			bitSet.set(7, false);
			break;

		case UNKNOWN:
			bitSet.set(6, true);
			bitSet.set(7, true);
			break;

		default:
			bitSet.set(6, false);
			bitSet.set(7, false);
			break;
		}
		NonCompoundField dataField = createAssociatedDataField(1);

		ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
				.getFieldLengthIndicator());

		try {

			tempBuffer.put(bitSet.toByteArray());
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
		
		return getDescription() + "={"
				+ unpack((NonCompoundField) field).toString() + "}";
	}

	/***************************************************************************
	 * Unpacks the configuration and status into a bit set containing the nogo
	 * status, processing system status, overload and invalid time source
	 * status.
	 * 
	 * @param dataField
	 *            The data field to be unpacked.
	 * 
	 * @return The {@code BitSet} containing the NoGo status in bits 7/8,
	 *         overload in bit 6, invalid time source in bit 5 and processing
	 *         system status in bits 3/4. Note bits 1/2 are not set.
	 * 
	 * @throws BufferUnderflowException
	 *             if more elements shall be read after reaching the buffer's
	 *             limit.
	 */
	public final BitSet unpack(final NonCompoundField dataField) {

		try {

			final ByteBuffer tempBuffer = dataField.encode();

			final BitSet configAndStatus = BitSet.valueOf(tempBuffer);

			return configAndStatus;

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

	public static enum NoGoStatus {

		OPERATIONAL, DEGRADED, NOT_CURRENTLY_CONNECTED, UNKNOWN
	};

	public static enum ProcessingSystemStatus {

		NOT_APPLICABLE, SDPS1_SELECTED, SDPS2_SELECTED, SDPS3_SELECTED
	};
}
