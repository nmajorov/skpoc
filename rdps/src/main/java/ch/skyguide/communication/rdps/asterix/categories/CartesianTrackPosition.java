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

import java.math.BigInteger;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/**
 * Represents the Data Item with the Cartesian track position of the flight.
 * 
 * @author R. Wilson
 */
public final class CartesianTrackPosition 
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
	 * @param sizeFactor
	 *            The size factor of the data field.
	 * 
	 * @param description
	 *            Description of the data item and contents.
	 * 
	 * @param units
	 *            Units of the data item.
	 *            
	 * @throws	IllegalArgumentException
	 * 			Thrown when the size factor isn't either 6 or 8 bytes.
	 */
	public CartesianTrackPosition(final short dataCategory, 
								  final int dataItemIdentifier,
								  final int sizeFactor,
								  final String description, 
								  final String units)
	throws IllegalArgumentException {
		
		super(dataCategory, dataItemIdentifier, sizeFactor, description, units);
		
		if(sizeFactor != 6 && sizeFactor != 8) {
			
			throw new IllegalArgumentException("Size factor can only be "
					+ "6 or 8.");
		}
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
	 * @param rRepetitionFactor
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
	 * Pack the Cartesian x and y component of the position into the data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param xComponent
	 *            The {@code int} containing the x-component of the flight's
	 *            position in a resolution of 0.5m.
	 * 
	 * @param yComponent
	 *            The {@code int} containing the y-component of the flight's
	 *            position in a resolution of 0.5m.
	 * 
	 * @return The {@code NonCompoundField} containing the packed data.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public final NonCompoundField pack(final int xComponent, 
									   final int yComponent) {
		
		final NonCompoundField dataField = createAssociatedDataField(1);
		final ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
				.getFieldLengthIndicator());

		try {

			if(getSizeFactor()/2 > 3) {

				tempBuffer.putInt(xComponent);
				tempBuffer.putInt(yComponent);
				tempBuffer.flip();
			}
			else {
				
				byte[] byteArrayX = new byte[3];
				byteArrayX[2] = (byte)((xComponent<<24)>>>24);
				byteArrayX[1] = (byte)((xComponent<<16)>>>24);
				byteArrayX[0] = (byte)((xComponent<<8)>>24);
				tempBuffer.put(byteArrayX);

				byte[] byteArrayY = new byte[3];
				byteArrayY[2] = (byte)((yComponent<<24)>>>24);
				byteArrayY[1] = (byte)((yComponent<<16)>>>24);
				byteArrayY[0] = (byte)((yComponent<<8)>>24);
				tempBuffer.put(byteArrayY);
				
				tempBuffer.flip();
			}

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
	public final CartesianPosition unpack(final NonCompoundField dataField) {
		
	
		try {
				final ByteBuffer tempBuffer = dataField.encode();
				
				int xComponent = 0;
				int yComponent = 0;
				
				if(getSizeFactor()/2 > 3) {
					
					xComponent = tempBuffer.getInt();
					yComponent = tempBuffer.getInt();
				}
				else {

					byte[] tempX = new byte[3];
					tempBuffer.get(tempX);
					xComponent = new BigInteger(tempX).intValue();
					
					byte[] tempY = new byte[3];
					tempBuffer.get(tempY);
					yComponent = new BigInteger(tempY).intValue();
				}
				
				return new CartesianPosition(xComponent, 
											 yComponent);

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
	
	public final static class CartesianPosition {
		
		public CartesianPosition(final int xComponent, final int yComponent) {
			
			m_xComponent = xComponent;
			m_yComponent = yComponent;
		}
		
		public final int getXComponent() {
			
			return m_xComponent;
		}
		
		public final int getYComponent() {
			
			return m_yComponent;
		}
		
		private final int m_xComponent;
		
		private final int m_yComponent;
	}

}
