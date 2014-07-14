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
 * Represents the MRT Source Data Item
 * 
 * @author R. Wilson
 */
public class MrtSource extends AbstractLoggingNonCompoundDataItem {

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
	public MrtSource(short dataCategory, int dataItemIdentifier,
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
	 * @param siteIdentification
	 * 		  The site Identification number to uniquely identify the site.
	 *
	 * @param mvIdentification
	 * 		  The MV Identification number to uniquely identify the MV
	 * .
	 * @param artasUnit
	 * 		  The ARTAS Unit number delivering the original flow.
	 * 
	 * @return The {@code NonCompoundField} containing the packed data.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public NonCompoundField pack(final short siteIdentification, 
								 final short mvIdentification, 
								 final short artasUnit) {
		
		if(siteIdentification < 0 || siteIdentification > 15) {
			
			throw new IllegalArgumentException("Site Identification shall "
					+ "be in range of 0 to 15, illegal value: " 
					+ siteIdentification);
		}
		
		if(mvIdentification < 0 || mvIdentification > 31) {
			
			throw new IllegalArgumentException("Mv Identification shall "
					+ "be in range of 0 to 31, illegal value: " 
					+ mvIdentification);
		}
		
		if(artasUnit < 0 || artasUnit > 31) {
			
			throw new IllegalArgumentException("ARTAS Unit shall "
					+ "be in range of 0 to 31, illegal value: " 
					+ artasUnit);
		}

		try {
			
			final NonCompoundField dataField = createAssociatedDataField(1);
			final ByteBuffer tempBuffer
				= ByteBuffer.allocate(dataField.getFieldLengthIndicator());

			int temp = 0;

			temp |= (siteIdentification << 12);
			temp |= (mvIdentification << 7);
			temp |= (artasUnit << 2);

			tempBuffer.putShort((short) temp);
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
	 * Unpack the MRT source from the data field.
	 * 
	 * @param dataField
	 *            The data field containing the track number.
	 * 
	 * @return The {@code MrtSourceContainer} from the data field.
	 * 
	 * @throws BufferUnderflowException
	 *             if more elements shall be read after reaching the buffer's
	 *             limit.
	 */
	public MrtSourceContainer unpack(NonCompoundField dataField) {
		try {

			final ByteBuffer tempBuffer = dataField.encode();

			int temp = tempBuffer.getShort();
			
			short siteIdentification = (short)((temp << 16) >>> 28);
			short mvIdentification = (short)((temp << 20) >>> 27);
			short artasUnit = (short)((temp << 25) >>> 27);

			return new MrtSourceContainer(siteIdentification, 
										  mvIdentification, 
										  artasUnit);
			
		} catch (BufferUnderflowException exception) { // Trying to read after
														// reaching the limit of
														// the buffer.
			logError(BUFFER_UNDERFLOW_MESSAGE);
			throw exception;
		}
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
	
	static public class MrtSourceContainer {
		
		public MrtSourceContainer(final short siteIdentification, 
								  final short mvIdentification, 
								  final short artasUnit) {
			
			m_siteIdentification = siteIdentification;
			m_mvIdentification = mvIdentification;
			m_artasUnit = artasUnit;
		}
		
		public final short getSiteIdentification() {
			
			return m_siteIdentification;
		}
		
		public final short getMvIdentification() {
			
			return m_mvIdentification;
		}
		
		public final short getArtasUnit() {
			
			return m_artasUnit;
		}
		
		private short m_siteIdentification;
		private short m_mvIdentification;
		private short m_artasUnit;
	}

}
