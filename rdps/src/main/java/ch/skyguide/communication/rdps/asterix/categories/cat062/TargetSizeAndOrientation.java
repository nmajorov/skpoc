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
 * Represents the Target size defined as length and width of the detected 
 * target and orientation.
 * 
 * Variable length Data Item comprising a first part of one octet, followed by 
 * one-octet extents as necessary.
 * 
 * @author R. Wilson
 */
public class TargetSizeAndOrientation extends AbstractNonCompoundDataItem {

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
	public TargetSizeAndOrientation(final short dataCategory, 
							   final int dataItemIdentifier,
							   final String description, 
							   final String units) {
		
		super(dataCategory, dataItemIdentifier, 1, description, units);
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

	public final NonCompoundField pack(final short length) {
		
		final List<Short> sizeAndOrientations = new ArrayList<Short>();
		sizeAndOrientations.add(length);
		
		return pack(sizeAndOrientations);
	}

	public final NonCompoundField pack(final short length,
									   final short orientation) {
		
		final List<Short> sizeAndOrientations = new ArrayList<Short>();
		sizeAndOrientations.add(length);
		sizeAndOrientations.add(orientation);
		
		return pack(sizeAndOrientations);
	}

	public final NonCompoundField pack(final short length,
									   final short orientation,
									   final short width) {
		
		final List<Short> sizeAndOrientations = new ArrayList<Short>();
		sizeAndOrientations.add(length);
		sizeAndOrientations.add(orientation);
		sizeAndOrientations.add(width);
		
		return pack(sizeAndOrientations);
	}

	public final NonCompoundField pack(final List<Short> sizeAndOrientations) {
		
		List<NonCompoundField> dataFields = new ArrayList<>();

		NonCompoundField dataField = null;
		ByteBuffer tempBuffer = ByteBuffer.allocate(getSizeFactor());

		for (short sizeAndOrientation : sizeAndOrientations) {

			tempBuffer.clear();

			tempBuffer.put((byte)sizeAndOrientation);
			
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
		
		for (Short sizeAndOrientation : unpack((NonCompoundField) field)) {
			str.append(sizeAndOrientation.toString() + ",");
		}
		
		str.append("}");
		return str.toString();
	}

	/***************************************************************************
	 * Unpack the data field into a {@code List} of {@code Short}
	 * objects. 
	 * 
	 * @param dataField
	 *            The data field to be unpacked.
	 * 
	 * @return A {@code List} of {@code Short}s from the data field.
	 * 
	 */
	public List<Short> unpack(NonCompoundField dataField) {
		
		ByteBuffer tempBuffer = dataField.encode();
		List<Short> sizeAndOrientations = new ArrayList<Short>();
		
		for (NonCompoundField field : createAssociatedDataFields(tempBuffer)) {
			
			sizeAndOrientations.add(Short.valueOf((short)field.encode().get()));
		}
		
		return sizeAndOrientations;
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
