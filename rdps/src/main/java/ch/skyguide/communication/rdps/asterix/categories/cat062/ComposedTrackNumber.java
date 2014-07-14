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

package ch.skyguide.communication.rdps.asterix.categories.cat062;

//------------------------------------------------------------------------------

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultExtendedDataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;
import ch.skyguide.message.structure.dataitem.AbstractNonCompoundDataItem;

//------------------------------------------------------------------------------

/**
 * Represents the composed Track Number data item which is an Identification of
 * a system track.
 * 
 * Extendible data item, comprising a first part of three octets (Master Track
 * Number), followed by three-octet extents (Slave Tracks Numbers).
 * 
 * @author R. Wilson
 */
public class ComposedTrackNumber extends AbstractNonCompoundDataItem {

	// -------------------------------------------------------------------------
	// public static final data members (constants)

	// -------------------------------------------------------------------------
	// public constructors

	/***************************************************************************
	 * Public defined constructor
	 * 
	 * @param dataCategory
	 *            The assigned data category for the item.
	 * 
	 * @param dataItemIdentifier
	 *            The identifier for the data item.
	 * 
	 * @param description
	 *            The description of the data item.
	 * 
	 * @param units
	 *            The units of the data item.
	 * 
	 */
	public ComposedTrackNumber(final short dataCategory, 
							   final int dataItemIdentifier,
							   final String description, 
							   final String units) {
		
		super(dataCategory, dataItemIdentifier, 3, description, units);
	}

	// -------------------------------------------------------------------------
	// public member functions

	/***************************************************************************
	 * Creates the association with a data field.
	 * 
	 * @param repetitionFactor
	 *            A repetition factor for how many times it is to repeat.
	 * 
	 * @return The {@code NonCompoundField} associated with the data item.
	 * 
	 */
	@Override
	public final NonCompoundField 
	createAssociatedDataField(final int repetitionFactor) {
		
		return new DefaultFixedDataField(m_sizeFactor, this, true);
	}

	/***************************************************************************
	 * Creates the associated data field with the information in the byte
	 * buffer.
	 * 
	 * @param inBuffer
	 *            The input buffer containing the information to be encoded into
	 *            the data field.
	 * 
	 * @return The {@code NonCompoundField} associated with the data item.
	 * 
	 */
	@Override
	public final NonCompoundField 
	createAssociatedDataField(final ByteBuffer inBuffer) {
		
		List<NonCompoundField> dataFields = new ArrayList<>();
		NonCompoundField dataField = null;
		inBuffer.mark();
		
		do {

			dataField = createAssociatedDataField(1);
			dataFields.add(dataField);
			dataField.decode(inBuffer);
		
		} while (dataField.isFieldExtended());

		inBuffer.reset();

		return new DefaultExtendedDataField(this, dataFields);
	}

	/***************************************************************************
	 * Packs a {@code List} of {@code SystemTrack}s into an extendable data
	 * field to produce a composed track number.
	 *  
	 * @param 	systemTracks
	 *          The {@code List} of {@code SystemTrack}s to be packed into the
	 *          data field.  The list may not be {@code null}.
	 * 
	 * @return The {@code NonCompoundField} packed with the extendable data
	 *         item.
	 * 
	 */
	public final NonCompoundField pack(final List<SystemTrack> systemTracks) {
		
		List<NonCompoundField> dataFields = new ArrayList<>();

		NonCompoundField dataField = null;
		ByteBuffer tempBuffer = ByteBuffer.allocate(getSizeFactor());

		for (SystemTrack systemTrack : systemTracks) {

			tempBuffer.clear();

			tempBuffer.put((byte)systemTrack.getArtasUnitIdentification());
			tempBuffer.asShortBuffer().put((short)systemTrack.getSystemTrack());
			
			tempBuffer.flip();
			dataField = createAssociatedDataField(1);
			dataField.decode(tempBuffer);
			dataField.setFieldExtensionBit();
		}

		if (dataField != null) {
			dataField.clearFieldExtensionBit();
		}

		return new DefaultExtendedDataField(this, dataFields);
	}

	/***************************************************************************
	 * See description of superclass/interface.
	 * 
	 * @see ch.skyguide.message.structure.dataitem.genericstructure.dataitem.DataItem#toUnpackedString(ch.skyguide.message.structure.datafield.genericstructure.datafield.DataField)
	 */
	@Override
	public String toUnpackedString(DataField field) {
		
		StringBuilder str = new StringBuilder(getDescription() + "={");
		
		for (SystemTrack track : unpack((NonCompoundField) field)) {
			str.append(track.toString() + ",");
		}
		
		str.append("}");
		return str.toString();
	}

	/***************************************************************************
	 * Unpack the data field into a {@code List} of {@code SystemTrack}
	 * objects. 
	 * 
	 * @param dataField
	 *            The data field to be unpacked.
	 * 
	 * @return A {@code List} of {@code SystemTrack}s from the data field.
	 * 
	 */
	public List<SystemTrack> unpack(NonCompoundField dataField) {
		
		ByteBuffer tempBuffer = dataField.encode();
		List<SystemTrack> systemTracks = new ArrayList<>();
		
		for (NonCompoundField field : createAssociatedDataFields(tempBuffer)) {
			
			systemTracks.add(new SystemTrack(field.encode()));
		}
		
		return systemTracks;
	}

	@Override
	protected void logError(String arg0) {
		
		// TODO: rjw, talk with GFO as weird with the logging.
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

	/***************************************************************************
	 * Creates the associated data field with the information in the byte
	 * buffer.
	 * 
	 * @param inBuffer
	 *            The input buffer containing the information to be encoded into
	 *            the data field.
	 * 
	 * @return The {@code List} of associated with the data fields.
	 * 
	 */
	private final List<NonCompoundField> 
	createAssociatedDataFields(final ByteBuffer inBuffer) {
		
		List<NonCompoundField> dataFields = new ArrayList<>();
		NonCompoundField dataField = null;
		inBuffer.mark();
		
		do {

			dataField = createAssociatedDataField(1);
			dataFields.add(dataField);
			dataField.decode(inBuffer);
		
		} while (dataField.isFieldExtended());

		inBuffer.reset();

		return dataFields;
	}

	// -------------------------------------------------------------------------
	// private static member functions

	// -------------------------------------------------------------------------
	// private data members

	// -------------------------------------------------------------------------
	// private static data members

	// -------------------------------------------------------------------------
	// Static initialization block

}
