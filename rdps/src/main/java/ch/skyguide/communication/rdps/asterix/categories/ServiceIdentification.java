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
 * Represents an integer embedded in an asterix message.
 * 
 * @author R. Wilson
 */
public class ServiceIdentification extends AbstractLoggingNonCompoundDataItem {
	// --------------------------------------------------------------------------
	// public static final data members (constants)

	// --------------------------------------------------------------------------
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
	public ServiceIdentification(short dataCategory, int dataItemIdentifier,
			String description, String units) {
		super(dataCategory, dataItemIdentifier, 1, description, units);
	}

	// --------------------------------------------------------------------------
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
	public NonCompoundField createAssociatedDataField(int repetitionFactor) {
		return new DefaultFixedDataField(m_sizeFactor, this, false);
	}

	/***************************************************************************
	 * Create the associated data field based on the information contained in
	 * the byte buffer.
	 * 
	 * @param inBuffer
	 *            The input buffer containing the information.
	 * 
	 * @return The <code>NonCompoundField</code> created from the data item and
	 *         the input buffer information.
	 * 
	 */
	@Override
	public NonCompoundField createAssociatedDataField(ByteBuffer inBuffer) {
		return createAssociatedDataField(1);
	}

	/***************************************************************************
	 * Packs the data into the data field.
	 * 
	 * Remark: The <code>ReadOnlyBufferException</code> exception is not handled
	 * since the used <code>ByteBuffer</code> is not read only.
	 * 
	 * @param backgroundSelected
	 *            The parameter is true when the back ground service is
	 *            selected, otherwise false.
	 * 
	 * @param complementaryService1Selected
	 *            The parameter is true when the back complementary service 1 is
	 *            selected, otherwise false.
	 * 
	 * @param complementaryService2Selected
	 *            The parameter is true when the back complementary service 2 is
	 *            selected, otherwise false.
	 * 
	 * @param complementaryService3Selected
	 *            The parameter is true when the back complementary service 3 is
	 *            selected, otherwise false.
	 * 
	 * @param complementaryService4Selected
	 *            The parameter is true when the back complementary service 4 is
	 *            selected, otherwise false.
	 * 
	 * @param complementaryService5Selected
	 *            The parameter is true when the back complementary service 5 is
	 *            selected, otherwise false.
	 * 
	 * @return The packed <code>NonCompoundField</code>.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public NonCompoundField pack(final byte serviceIdentification) {

		NonCompoundField dataField = createAssociatedDataField(1);

		final ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
				.getFieldLengthIndicator());

		try {

			tempBuffer.put(serviceIdentification);
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
		return getDescription() + "={"
				+ unpack((NonCompoundField) field) + "}";
	}

	/***************************************************************************
	 * Unpacks the service identification into a {@code byte} identifying the
	 * service.
	 * 
	 * @param dataField
	 *            The data field to be unpacked.
	 * 
	 * @return The {@code byte} containing the service identification.
	 * 
	 * @throws BufferUnderflowException
	 *             if more elements shall be read after reaching the buffer's
	 *             limit.
	 */
	public byte unpack(NonCompoundField dataField) {
		
		final ByteBuffer tempBuffer = dataField.encode();

		try {
			
			return tempBuffer.get();

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

}
