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

package ch.skyguide.communication.rdps.asterix.categories.cat243;

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
 * Represents the Flow Status Data Item
 * 
 * @author R. Wilson
 */
public class FlowStatus extends AbstractLoggingNonCompoundDataItem {

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
	public FlowStatus(short dataCategory, int dataItemIdentifier,
			String description, String units) {
		
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
	 * Pack the flow status into the data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param resynchronizationFlag
	 * 		  A {@code boolean} indicating the current strip is being
	 * 		  resynchronized.
	 *
	 * @param newUpdateCycleFlag
	 * 		  A {@code boolean} indicating a new update cycle has started.
	 * .
	 * @param stripNumber
	 * 		  The number of the screen strip being updated.
	 * 
	 * @return The {@code NonCompoundField} containing the packed data.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public NonCompoundField pack(final boolean resynchronizationFlag, 
								 final boolean newUpdateCycleFlag, 
								 final short stripNumber) {
		
		if(stripNumber < 0 || stripNumber > 255) {
			
			throw new IllegalArgumentException("Strip number shall "
					+ "be in range of 0 to 255, illegal value: " 
					+ stripNumber);
		}

		try {
			
			final NonCompoundField dataField = createAssociatedDataField(1);
			final ByteBuffer tempBuffer
				= ByteBuffer.allocate(dataField.getFieldLengthIndicator());

			byte temp = 0;

			if(resynchronizationFlag) {
			
				temp |= 0xFF;
			}
			
			if(newUpdateCycleFlag) {

				temp |= 0x40;
			}
			
			tempBuffer.put(temp);
			tempBuffer.put((byte)stripNumber);
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
	public String toUnpackedString(DataField field) {
		
		return getDescription() + "={" + unpack((NonCompoundField) field) + "}";
	}

	/***************************************************************************
	 * Unpack the Flow Status from the data field.
	 * 
	 * @param dataField
	 *            The data field containing the flow status.
	 * 
	 * @return The {@code FlowStatusContainer} from the data field.
	 * 
	 * @throws BufferUnderflowException
	 *             if more elements shall be read after reaching the buffer's
	 *             limit.
	 */
	public FlowStatusContainer unpack(NonCompoundField dataField) {
		try {

			final ByteBuffer tempBuffer = dataField.encode();

			byte temp = tempBuffer.get();
			
			boolean resynchronizationFlag = (temp & 0xFF) == 0xFF;
			boolean newUpdateCycleFlag = (temp & 0x40) == 0x40;
			short stripNumber = (short)tempBuffer.get();

			return new FlowStatusContainer(resynchronizationFlag, 
										   newUpdateCycleFlag, 
										   stripNumber);
			
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
	
	static public class FlowStatusContainer {
		
		public FlowStatusContainer(final boolean resynchronizationFlag, 
								   final boolean newUpdateCycleFlag, 
								   final short stripNumber) {
			
			m_resynchronizationFlag = resynchronizationFlag;
			m_newUpdateCycleFlag = newUpdateCycleFlag;
			m_stripNumber = stripNumber;
		}
		
		public final boolean getResynchronizationFlag() {
			
			return m_resynchronizationFlag;
		}
		
		public final boolean getNewUpdateCycleFlag() {
			
			return m_newUpdateCycleFlag;
		}
		
		public final short getStripNumber() {
			
			return m_stripNumber;
		}
		
		private final boolean m_resynchronizationFlag;
		private final boolean m_newUpdateCycleFlag;
		private final short m_stripNumber;
	}

}
