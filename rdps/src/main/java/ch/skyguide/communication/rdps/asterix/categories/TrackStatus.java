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
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultExtendedDataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/*******************************************************************************
 * Class representing the container for the Track Status Data Item.
 * 
 * @author R. Wilson
 */
public final class TrackStatus 
	extends AbstractLoggingNonCompoundDataItem {

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
	public TrackStatus(final short dataCategory, final int dataItemIdentifier,
			final String description, final String units) {
		
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
	public NonCompoundField createAssociatedDataField(int repetitionFactor) {
		
		return new DefaultFixedDataField(getSizeFactor(), this, true);
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
	public NonCompoundField createAssociatedDataField(ByteBuffer inBuffer) {
		
		final List<NonCompoundField> dataFields = new ArrayList<>();
		NonCompoundField dataField = null;

		do {

			dataField = createAssociatedDataField(1);
			dataField.setFieldExtensionBit();
			dataFields.add(dataField);
			dataField.decode(inBuffer);
		} while ((inBuffer.get(m_sizeFactor - 1) & 0x1) == 0x1);

		dataField.clearFieldExtensionBit(); // reset the last one.

		return new DefaultExtendedDataField(this, dataFields);
	}

	/***************************************************************************
	 * Packs main datafield structure.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param 	monoSensor
	 *            The {@code boolean} indicating a mono sensor for {@code true}
	 *            and multi-senor {@code false}.
	 * 
	 * @param 	spiPresent
	 *            The {@code boolean} indicating a SPI was transmitted from 
	 *            sensor in last report, {@code true}, otherwise {@code false}.
	 * 
	 * @param 	mostReliableHeight
	 *            The {@code boolean} indicating the most reliable height 
	 *            determination method. Geometric altitude more reliable is
	 *            {@code true}, otherwise {@code false} indicates barometric
	 *            altitude more reliable.
	 * 
	 * @param	source
	 * 			An enumeration {@code SourceType} representing the calculated
	 * 			track altitude source.
	 * 
	 * @param	confirmedTrack
	 * 			A {@code boolean} indicating the track is confirmed or 
	 * 			tentative.  Note confirmed is {@code false} and tentative
	 * 			is {@code true}.
	 * 
	 * @return The {@code NonCompoundField} packed with the track status.
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter systemTracks is null.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public NonCompoundField 
	pack(final boolean monoSensor, final boolean spiPresent, 
		 final boolean mostReliableHeight, final SourceType source, 
		 final boolean confirmedTrack) {
		
		// Validate the precondition.
		
		try {

			final List<NonCompoundField> dataFields = new ArrayList<>();

			final NonCompoundField dataField =
				structure(monoSensor, 
						   spiPresent, 
						   mostReliableHeight, source, confirmedTrack);
			
			dataField.clearFieldExtensionBit();
			dataFields.add(dataField);

			NonCompoundField extendedDataField 
				= new DefaultExtendedDataField(this, dataFields);

			return extendedDataField;
			
		} catch (BufferOverflowException exception) { // The buffer should not
														// be filled after
														// reaching its limit.
			logError(BUFFER_OVERFLOW_MESSAGE);
			throw exception;
		}
	}

	/***************************************************************************
	 * Packs main and first extended data field structure.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param 	monoSensor
	 *            The {@code boolean} indicating a mono sensor for {@code true}
	 *            and multi-senor {@code false}.
	 * 
	 * @param 	spiPresent
	 *            The {@code boolean} indicating a SPI was transmitted from 
	 *            sensor in last report, {@code true}, otherwise {@code false}.
	 * 
	 * @param 	mostReliableHeight
	 *            The {@code boolean} indicating the most reliable height 
	 *            determination method. Geometric altitude more reliable is
	 *            {@code true}, otherwise {@code false} indicates barometric
	 *            altitude more reliable.
	 * 
	 * @param	source
	 * 			An enumeration {@code SourceType} representing the calculated
	 * 			track altitude source.
	 * 
	 * @param	confirmedTrack
	 * 			A {@code boolean} indicating the track is confirmed or 
	 * 			tentative.  Note confirmed is {@code false} and tentative
	 * 			is {@code true}.
	 * 
	 * @param 	simulatedTrack
	 *            The {@code boolean} indicating the track is simulated or 
	 *            actual.  A simulated track is {@code true} and actual track
	 *            is {@code false}.
	 * 
	 * @param 	lastMessageTransmitted
	 *            The {@code boolean} indicating it is the last message being
	 *            transmitted to the user as {@code true}, otherwise 
	 *            {@code false}.
	 * 
	 * @param 	firstMessageTransmitted
	 *            The {@code boolean} indicating the message is the first
	 *            transmitted to the user.  A {@code true} is indicates first
	 *            message, otherwise {@code false}.
	 * 
	 * @param	flightPlanCorrelated
	 *            The {@code boolean} indicating the track is correlated with 
	 *            a flight plan.  A {@code true} is indicates correlated track, 
	 *            otherwise {@code false}.
	 * 
	 * @param	adsbInconsistent
	 * 			A {@code boolean} indicating the track ADSB information is 
	 * 			inconsistent with other surveillance information. A {@code true}
	 * 			indicates inconsistent, otherwise {@code false}.
	 * 
	 * @param	slaveTrackPromotion
	 * 			A {@code boolean} indicating the promotion of the slave track
	 * 			to primary. A {@code true} indicates promotion, otherwise 
	 * 			{@code false}.
	 * 
	 * @param	complimentaryService
	 * 			A {@code boolean} indicating complimentary or background 
	 * 			service. A {@code true} for background and {@code false} for
	 * 			complimentary service.
	 *
	 * @return The {@code NonCompoundField} packed with the track status.
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter systemTracks is null.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public NonCompoundField 
	pack(final boolean monoSensor, final boolean spiPresent, 
		 final boolean mostReliableHeight, final SourceType source, 
		 final boolean confirmedTrack, final boolean simulatedTrack, 
	     final boolean lastMessageTransmitted, 
      	 final boolean firstMessageTransmitted, 
      	 final boolean flightPlanCorrelated, 
         final boolean adsbInconsistent, 
         final boolean slaveTrackPromotion, 
         final boolean complimentaryService) {
		
		// Validate the precondition.
		
		try {

			final List<NonCompoundField> dataFields = new ArrayList<>();

			NonCompoundField dataField =
				structure(monoSensor, 
						   spiPresent, 
						   mostReliableHeight, source, confirmedTrack);
			
			dataField.setFieldExtensionBit();
			dataFields.add(dataField);

			dataField =
					firstExtended(simulatedTrack, 
							lastMessageTransmitted, 
							firstMessageTransmitted, 
							flightPlanCorrelated, 
							adsbInconsistent, 
							slaveTrackPromotion, 
							complimentaryService);
				
			dataField.clearFieldExtensionBit();
			dataFields.add(dataField);

			NonCompoundField extendedDataField 
			= new DefaultExtendedDataField(this, dataFields);

			return extendedDataField;
			
		} catch (BufferOverflowException exception) { // The buffer should not
														// be filled after
														// reaching its limit.
			logError(BUFFER_OVERFLOW_MESSAGE);
			throw exception;
		}
	}

	/***************************************************************************
	 * Packs main and first extended data field structure.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param 	monoSensor
	 *            The {@code boolean} indicating a mono sensor for {@code true}
	 *            and multi-senor {@code false}.
	 * 
	 * @param 	spiPresent
	 *            The {@code boolean} indicating a SPI was transmitted from 
	 *            sensor in last report, {@code true}, otherwise {@code false}.
	 * 
	 * @param 	mostReliableHeight
	 *            The {@code boolean} indicating the most reliable height 
	 *            determination method. Geometric altitude more reliable is
	 *            {@code true}, otherwise {@code false} indicates barometric
	 *            altitude more reliable.
	 * 
	 * @param	source
	 * 			An enumeration {@code SourceType} representing the calculated
	 * 			track altitude source.
	 * 
	 * @param	confirmedTrack
	 * 			A {@code boolean} indicating the track is confirmed or 
	 * 			tentative.  Note confirmed is {@code false} and tentative
	 * 			is {@code true}.
	 * 
	 * @param 	simulatedTrack
	 *            The {@code boolean} indicating the track is simulated or 
	 *            actual.  A simulated track is {@code true} and actual track
	 *            is {@code false}.
	 * 
	 * @param 	lastMessageTransmitted
	 *            The {@code boolean} indicating it is the last message being
	 *            transmitted to the user as {@code true}, otherwise 
	 *            {@code false}.
	 * 
	 * @param 	firstMessageTransmitted
	 *            The {@code boolean} indicating the message is the first
	 *            transmitted to the user.  A {@code true} is indicates first
	 *            message, otherwise {@code false}.
	 * 
	 * @param	flightPlanCorrelated
	 *            The {@code boolean} indicating the track is correlated with 
	 *            a flight plan.  A {@code true} is indicates correlated track, 
	 *            otherwise {@code false}.
	 * 
	 * @param	adsbInconsistent
	 * 			A {@code boolean} indicating the track ADSB information is 
	 * 			inconsistent with other surveillance information. A {@code true}
	 * 			indicates inconsistent, otherwise {@code false}.
	 * 
	 * @param	slaveTrackPromotion
	 * 			A {@code boolean} indicating the promotion of the slave track
	 * 			to primary. A {@code true} indicates promotion, otherwise 
	 * 			{@code false}.
	 * 
	 * @param	complimentaryService
	 * 			A {@code boolean} indicating complimentary or background 
	 * 			service. A {@code true} for background and {@code false} for
	 * 			complimentary service.
	 *
	 * @param 	amalgamation
	 *            The {@code boolean} indicating track was formed from an 
	 *            amalgamation process, {@code true} for amalgamated, otherwise
	 *            {@code false}.
	 * 
	 * @param 	mode4Interrogation
	 *            An enumeration {@code MODE4} indicating the mode 4 
	 *            Interrogation state of the track.
	 * 
	 * @param 	militaryEmergency
	 *            The {@code boolean} indicating if the track is a military
	 *            emergency. A {@code true} indicates an emergency, otherwise 
	 *            {@code false}.
	 * 
	 * @param 	militaryIdentification
	 *            The {@code boolean} indicating if the track is a military
	 *            identification in last message. A {@code true} indicates 
	 *            identification present, otherwise {@code false}.
	 * 
	 * @param	noMode5Interrogation
	 *            An enumeration {@code MODE5} indicating the mode 5 
	 *            Interrogation state of the track.
	 * 
	 * @return The {@code NonCompoundField} packed with the track status.
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter systemTracks is null.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public NonCompoundField 
	pack(final boolean monoSensor, final boolean spiPresent, 
		 final boolean mostReliableHeight, final SourceType source, 
		 final boolean confirmedTrack, final boolean simulatedTrack, 
	     final boolean lastMessageTransmitted, 
      	 final boolean firstMessageTransmitted, 
      	 final boolean flightPlanCorrelated, 
         final boolean adsbInconsistent, 
         final boolean slaveTrackPromotion, 
         final boolean complimentaryService,
         final boolean amalgamation, final MODE4 mode4Interrogation, 
	     final boolean militaryEmergency, final boolean militaryIdentification, 
	     final MODE5 noMode5Interrogation) {
		
		// Validate the precondition.
		
		try {

			final List<NonCompoundField> dataFields = new ArrayList<>();

			NonCompoundField dataField =
				structure(monoSensor, 
						   spiPresent, 
						   mostReliableHeight, source, confirmedTrack);
			
			dataField.setFieldExtensionBit();
			dataFields.add(dataField);

			dataField =
				firstExtended(simulatedTrack, 
						lastMessageTransmitted, 
						firstMessageTransmitted, 
						flightPlanCorrelated, 
						adsbInconsistent, 
						slaveTrackPromotion, 
						complimentaryService);
				
			dataField.setFieldExtensionBit();
			dataFields.add(dataField);

			dataField = secondExtended(amalgamation, 
							mode4Interrogation, 
							militaryEmergency, 
							militaryIdentification, 
							noMode5Interrogation);
				
			dataField.clearFieldExtensionBit();
			dataFields.add(dataField);

			final NonCompoundField extendedDataField 
				= new DefaultExtendedDataField(this, dataFields);

			return extendedDataField;
			
		} catch (BufferOverflowException exception) { // The buffer should not
														// be filled after
														// reaching its limit.
			logError(BUFFER_OVERFLOW_MESSAGE);
			throw exception;
		}
	}

	/***************************************************************************
	 * Packs main and first extended data field structure.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param 	monoSensor
	 *            The {@code boolean} indicating a mono sensor for {@code true}
	 *            and multi-senor {@code false}.
	 * 
	 * @param 	spiPresent
	 *            The {@code boolean} indicating a SPI was transmitted from 
	 *            sensor in last report, {@code true}, otherwise {@code false}.
	 * 
	 * @param 	mostReliableHeight
	 *            The {@code boolean} indicating the most reliable height 
	 *            determination method. Geometric altitude more reliable is
	 *            {@code true}, otherwise {@code false} indicates barometric
	 *            altitude more reliable.
	 * 
	 * @param	source
	 * 			An enumeration {@code SourceType} representing the calculated
	 * 			track altitude source.
	 * 
	 * @param	confirmedTrack
	 * 			A {@code boolean} indicating the track is confirmed or 
	 * 			tentative.  Note confirmed is {@code false} and tentative
	 * 			is {@code true}.
	 * 
	 * @param 	simulatedTrack
	 *            The {@code boolean} indicating the track is simulated or 
	 *            actual.  A simulated track is {@code true} and actual track
	 *            is {@code false}.
	 * 
	 * @param 	lastMessageTransmitted
	 *            The {@code boolean} indicating it is the last message being
	 *            transmitted to the user as {@code true}, otherwise 
	 *            {@code false}.
	 * 
	 * @param 	firstMessageTransmitted
	 *            The {@code boolean} indicating the message is the first
	 *            transmitted to the user.  A {@code true} is indicates first
	 *            message, otherwise {@code false}.
	 * 
	 * @param	flightPlanCorrelated
	 *            The {@code boolean} indicating the track is correlated with 
	 *            a flight plan.  A {@code true} is indicates correlated track, 
	 *            otherwise {@code false}.
	 * 
	 * @param	adsbInconsistent
	 * 			A {@code boolean} indicating the track ADSB information is 
	 * 			inconsistent with other surveillance information. A {@code true}
	 * 			indicates inconsistent, otherwise {@code false}.
	 * 
	 * @param	slaveTrackPromotion
	 * 			A {@code boolean} indicating the promotion of the slave track
	 * 			to primary. A {@code true} indicates promotion, otherwise 
	 * 			{@code false}.
	 * 
	 * @param	complimentaryService
	 * 			A {@code boolean} indicating complimentary or background 
	 * 			service. A {@code true} for background and {@code false} for
	 * 			complimentary service.
	 *
	 * @param 	amalgamation
	 *            The {@code boolean} indicating track was formed from an 
	 *            amalgamation process, {@code true} for amalgamated, otherwise
	 *            {@code false}.
	 * 
	 * @param 	mode4Interrogation
	 *            An enumeration {@code MODE4} indicating the mode 4 
	 *            Interrogation state of the track.
	 * 
	 * @param 	militaryEmergency
	 *            The {@code boolean} indicating if the track is a military
	 *            emergency. A {@code true} indicates an emergency, otherwise 
	 *            {@code false}.
	 * 
	 * @param 	militaryIdentification
	 *            The {@code boolean} indicating if the track is a military
	 *            identification in last message. A {@code true} indicates 
	 *            identification present, otherwise {@code false}.
	 * 
	 * @param	noMode5Interrogation
	 *            An enumeration {@code MODE5} indicating the mode 5 
	 *            Interrogation state of the track.
	 * 
	 * @param 	coastingTrack
	 *            The {@code boolean} indicating the track is simulated or 
	 *            actual.  A simulated track is {@code true} and actual track
	 *            is {@code false}.
	 * 
	 * @param 	psrTrack
	 *            The {@code boolean} indicating it is the last message being
	 *            transmitted to the user as {@code true}, otherwise 
	 *            {@code false}.
	 * 
	 * @param 	ssrTrack
	 *            The {@code boolean} indicating the message is the first
	 *            transmitted to the user.  A {@code true} is indicates first
	 *            message, otherwise {@code false}.
	 * 
	 * @param	modeSTrack
	 *            The {@code boolean} indicating the track is correlated with 
	 *            a flight plan.  A {@code true} is indicates correlated track, 
	 *            otherwise {@code false}.
	 * 
	 * @param	adsbTrack
	 * 			A {@code boolean} indicating the track ADSB information is 
	 * 			inconsistent with other surveillance information. A {@code true}
	 * 			indicates inconsistent, otherwise {@code false}.
	 * 
	 * @param	specialUseCode
	 * 			A {@code boolean} indicating the promotion of the slave track
	 * 			to primary. A {@code true} indicates promotion, otherwise 
	 * 			{@code false}.
	 * 
	 * @param	assignedCodeConflict
	 * 			A {@code boolean} indicating complimentary or background 
	 * 			service. A {@code true} for background and {@code false} for
	 * 			complimentary service.
	 * 
	 * @return The {@code NonCompoundField} packed with the track status.
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter systemTracks is null.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	public NonCompoundField 
	pack(final boolean monoSensor,
		 final boolean spiPresent,
		 final boolean mostReliableHeight,
		 final SourceType source, 
		 final boolean confirmedTrack,
		 final boolean simulatedTrack, 
	     final boolean lastMessageTransmitted, 
      	 final boolean firstMessageTransmitted, 
      	 final boolean flightPlanCorrelated, 
         final boolean adsbInconsistent, 
         final boolean slaveTrackPromotion, 
         final boolean complimentaryService,
         final boolean amalgamation,
         final MODE4 mode4Interrogation, 
	     final boolean militaryEmergency,
	     final boolean militaryIdentification, 
	     final MODE5 noMode5Interrogation,
	     final boolean coastingTrack, 
	     final boolean psrTrack, 
     	 final boolean ssrTrack, 
     	 final boolean modeSTrack, 
         final boolean adsbTrack, 
         final boolean specialUseCode, 
         final boolean assignedCodeConflict) {
		
		try {

			final List<NonCompoundField> dataFields = new ArrayList<>();

			NonCompoundField dataField =
				structure(monoSensor, 
						   spiPresent, 
						   mostReliableHeight, source, confirmedTrack);
			
			dataField.setFieldExtensionBit();
			dataFields.add(dataField);

			dataField =
				firstExtended(simulatedTrack, 
						lastMessageTransmitted, 
						firstMessageTransmitted, 
						flightPlanCorrelated, 
						adsbInconsistent, 
						slaveTrackPromotion, 
						complimentaryService);
				
			dataField.setFieldExtensionBit();
			dataFields.add(dataField);

			dataField = secondExtended(amalgamation, 
							mode4Interrogation, 
							militaryEmergency, 
							militaryIdentification, 
							noMode5Interrogation);
				
			dataField.setFieldExtensionBit();
			dataFields.add(dataField);

			dataField = thirdExtended(coastingTrack, 
									  psrTrack, 
									  ssrTrack, 
									  modeSTrack, 
									  adsbTrack, 
									  specialUseCode, 
									  assignedCodeConflict);
				
			dataField.clearFieldExtensionBit();
			dataFields.add(dataField);

			final NonCompoundField extendedDataField 
				= new DefaultExtendedDataField(this, dataFields);

			return extendedDataField;
			
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
		StringBuilder str = new StringBuilder(getDescription() + "={");
			str.append(field.toString() + ";");
		str.append("}");
		return str.toString();
	}

	/***************************************************************************
	 * Unpack the data field into {@code List} of {@code Byte} containing the
	 * main and extended field data structure.
	 * 
	 * @param dataField
	 *            The data field to be unpacked.
	 * 
	 * @return A {@code List} of {@code Byte}s from the data field.
	 * 
	 * @throws BufferUnderflowException
	 *             if more elements shall be read after reaching the buffer's
	 *             limit.
	 */
	public List<Byte> unpack(NonCompoundField dataField) {
		
		final ByteBuffer tempBuffer = dataField.encode();
		final List<Byte> dataFieldStructure = new ArrayList<>();

		try {

			boolean fieldExtensionBit = true;

			while (fieldExtensionBit) {

				byte field = tempBuffer.get();

				dataFieldStructure.add(field);

				// Check if extended beyond this field.
				fieldExtensionBit = ((field & 0x1) == 0x1);
			}
		} catch (BufferUnderflowException exception) { // Trying to read after
														// reaching the limit of
														// the buffer.
			logError(BUFFER_UNDERFLOW_MESSAGE);
			throw exception;
		}

		return dataFieldStructure;
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
	 * Creates the structure of the main data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param 	simulatedTrack
	 *            The {@code boolean} indicating the track is simulated or 
	 *            actual.  A simulated track is {@code true} and actual track
	 *            is {@code false}.
	 * 
	 * @param 	lastMessageTransmitted
	 *            The {@code boolean} indicating it is the last message being
	 *            transmitted to the user as {@code true}, otherwise 
	 *            {@code false}.
	 * 
	 * @param 	firstMessageTransmitted
	 *            The {@code boolean} indicating the message is the first
	 *            transmitted to the user.  A {@code true} is indicates first
	 *            message, otherwise {@code false}.
	 * 
	 * @param	flightPlanCorrelated
	 *            The {@code boolean} indicating the track is correlated with 
	 *            a flight plan.  A {@code true} is indicates correlated track, 
	 *            otherwise {@code false}.
	 * 
	 * @param	adsbInconsistent
	 * 			A {@code boolean} indicating the track ADSB information is 
	 * 			inconsistent with other surveillance information. A {@code true}
	 * 			indicates inconsistent, otherwise {@code false}.
	 * 
	 * @param	slaveTrackPromotion
	 * 			A {@code boolean} indicating the promotion of the slave track
	 * 			to primary. A {@code true} indicates promotion, otherwise 
	 * 			{@code false}.
	 * 
	 * @param	complimentaryService
	 * 			A {@code boolean} indicating complimentary or background 
	 * 			service. A {@code true} for background and {@code false} for
	 * 			complimentary service.
	 * 
	 * @return The {@code NonCompoundField} packed with the first extent.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	private NonCompoundField 
	firstExtended(final boolean simulatedTrack, 
			      final boolean lastMessageTransmitted, 
		      	  final boolean firstMessageTransmitted, 
		      	  final boolean flightPlanCorrelated, 
		          final boolean adsbInconsistent, 
		          final boolean slaveTrackPromotion, 
		          final boolean complimentaryService) {
		
		// Validate the precondition.
		
		try {

			final NonCompoundField dataField = createAssociatedDataField(1);
			
			final BitSet bsTrackStatus = new BitSet();

			bsTrackStatus.set(0, simulatedTrack);
			bsTrackStatus.set(1, lastMessageTransmitted);
			bsTrackStatus.set(2, firstMessageTransmitted);
			bsTrackStatus.set(3, flightPlanCorrelated);
			bsTrackStatus.set(4, adsbInconsistent);
			bsTrackStatus.set(5, slaveTrackPromotion);
			bsTrackStatus.set(6, complimentaryService);
			
			ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
					.getFieldLengthIndicator());

			if(bsTrackStatus.isEmpty()) {
				
				tempBuffer.put((byte)0);
			}
			else {
				tempBuffer.put(bsTrackStatus.toByteArray());
			}

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
	 * Creates the structure of the main data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param 	amalgamation
	 *            The {@code boolean} indicating track was formed from an 
	 *            amalgamation process, {@code true} for amalgamated, otherwise
	 *            {@code false}.
	 * 
	 * @param 	mode4Interrogation
	 *            An enumeration {@code MODE4} indicating the mode 4 
	 *            Interrogation state of the track.
	 * 
	 * @param 	militaryEmergency
	 *            The {@code boolean} indicating if the track is a military
	 *            emergency. A {@code true} indicates an emergency, otherwise 
	 *            {@code false}.
	 * 
	 * @param 	militaryIdentification
	 *            The {@code boolean} indicating if the track is a military
	 *            identification in last message. A {@code true} indicates 
	 *            identification present, otherwise {@code false}.
	 * 
	 * @param	noMode5Interrogation
	 *            An enumeration {@code MODE5} indicating the mode 5 
	 *            Interrogation state of the track.
	 * 
	 * @return The {@code NonCompoundField} packed with the main structure.
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter systemTracks is null.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	private NonCompoundField 
	secondExtended(final boolean amalgamation, final MODE4 mode4Interrogation, 
		      final boolean militaryEmergency, final boolean militaryIdentification, 
		      final MODE5 noMode5Interrogation) {
		
		// Validate the precondition.
		
		try {

			final NonCompoundField dataField = createAssociatedDataField(1);
			
			final BitSet bsTrackStatus = new BitSet();

			bsTrackStatus.set(0, amalgamation);

			bsTrackStatus.set(1, (mode4Interrogation.ordinal() & 0x2) == 0x2);
			bsTrackStatus.set(2, (mode4Interrogation.ordinal() & 0x1) == 0x1);
			
			bsTrackStatus.set(3, militaryEmergency);
			bsTrackStatus.set(4, militaryIdentification);
		
			bsTrackStatus.set(5, (noMode5Interrogation.ordinal() & 0x2) == 0x2);
			bsTrackStatus.set(6, (noMode5Interrogation.ordinal() & 0x1) == 0x1);
			
			ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
					.getFieldLengthIndicator());

			tempBuffer.put(bsTrackStatus.toByteArray());

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
	 * Creates the structure of the main data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param 	monoSensor
	 *            The {@code boolean} indicating a mono sensor for {@code true}
	 *            and multi-senor {@code false}.
	 * 
	 * @param 	spiPresent
	 *            The {@code boolean} indicating a SPI was transmitted from 
	 *            sensor in last report, {@code true}, otherwise {@code false}.
	 * 
	 * @param 	mostReliableHeight
	 *            The {@code boolean} indicating the most reliable height 
	 *            determination method. Geometric altitude more reliable is
	 *            {@code true}, otherwise {@code false} indicates barometric
	 *            altitude more reliable.
	 * 
	 * @param	source
	 * 			An enumeration {@code SourceType} representing the calculated
	 * 			track altitude source.
	 * 
	 * @param	confirmedTrack
	 * 			A {@code boolean} indicating the track is confirmed or 
	 * 			tentative.  Note confirmed is {@code false} and tentative
	 * 			is {@code true}.
	 * 
	 * @return The {@code NonCompoundField} packed with the main structure.
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter systemTracks is null.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	private NonCompoundField 
	structure(final boolean monoSensor, final boolean spiPresent, 
		      final boolean mostReliableHeight, final SourceType source, 
		      final boolean confirmedTrack) {
		
		// Validate the precondition.
		
		try {

			final NonCompoundField dataField = createAssociatedDataField(1);
			
			final BitSet bsTrackStatus = new BitSet();

			bsTrackStatus.set(0, monoSensor);
			bsTrackStatus.set(1, spiPresent);
			bsTrackStatus.set(2, mostReliableHeight);
			
			int sourceOrdinal = source.ordinal();
			bsTrackStatus.set(3, (sourceOrdinal & 0x1) == 0x1);
			bsTrackStatus.set(4, (sourceOrdinal & 0x2) == 0x2);
			bsTrackStatus.set(5, (sourceOrdinal & 0x4) == 0x4);
		
			bsTrackStatus.set(6, confirmedTrack);
			
			ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
					.getFieldLengthIndicator());

			if(bsTrackStatus.isEmpty()) {
				
				tempBuffer.put((byte)0);
			}
			else {
				tempBuffer.put(bsTrackStatus.toByteArray());
			}

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
	 * Creates the thrid extent of the data field.
	 * 
	 * Remark: The {@code ReadOnlyBufferException} exception is not handled
	 * since the used {@code ByteBuffer} is not read only.
	 * 
	 * @param 	coastingTrack
	 *            The {@code boolean} indicating the track is simulated or 
	 *            actual.  A simulated track is {@code true} and actual track
	 *            is {@code false}.
	 * 
	 * @param 	psrTrack
	 *            The {@code boolean} indicating it is the last message being
	 *            transmitted to the user as {@code true}, otherwise 
	 *            {@code false}.
	 * 
	 * @param 	ssrTrack
	 *            The {@code boolean} indicating the message is the first
	 *            transmitted to the user.  A {@code true} is indicates first
	 *            message, otherwise {@code false}.
	 * 
	 * @param	modeSTrack
	 *            The {@code boolean} indicating the track is correlated with 
	 *            a flight plan.  A {@code true} is indicates correlated track, 
	 *            otherwise {@code false}.
	 * 
	 * @param	adsbTrack
	 * 			A {@code boolean} indicating the track ADSB information is 
	 * 			inconsistent with other surveillance information. A {@code true}
	 * 			indicates inconsistent, otherwise {@code false}.
	 * 
	 * @param	specialUseCode
	 * 			A {@code boolean} indicating the promotion of the slave track
	 * 			to primary. A {@code true} indicates promotion, otherwise 
	 * 			{@code false}.
	 * 
	 * @param	assignedCodeConflict
	 * 			A {@code boolean} indicating complimentary or background 
	 * 			service. A {@code true} for background and {@code false} for
	 * 			complimentary service.
	 * 
	 * @return The {@code NonCompoundField} packed with the first extent.
	 * 
	 * @throws BufferOverflowException
	 *             if elements shall be added after the buffer reached its
	 *             limit.
	 */
	private NonCompoundField 
	thirdExtended(final boolean coastingTrack, 
			      final boolean psrTrack, 
		      	  final boolean ssrTrack, 
		      	  final boolean modeSTrack, 
		          final boolean adsbTrack, 
		          final boolean specialUseCode, 
		          final boolean assignedCodeConflict) {
		
		// Validate the precondition.
		
		try {

			final NonCompoundField dataField = createAssociatedDataField(1);
			
			final BitSet bsTrackStatus = new BitSet();

			bsTrackStatus.set(7, coastingTrack);
			bsTrackStatus.set(6, psrTrack);
			bsTrackStatus.set(5, ssrTrack);
			bsTrackStatus.set(4, modeSTrack);
			bsTrackStatus.set(3, adsbTrack);
			bsTrackStatus.set(2, specialUseCode);
			bsTrackStatus.set(1, assignedCodeConflict);
			
			ByteBuffer tempBuffer = ByteBuffer.allocate(dataField
					.getFieldLengthIndicator());

			tempBuffer.put(bsTrackStatus.toByteArray());

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

	// -------------------------------------------------------------------------
	// private static member functions

	// -------------------------------------------------------------------------
	// private data members

	// -------------------------------------------------------------------------
	// private static data members

	// -------------------------------------------------------------------------
	// Static initialization block
	public enum SourceType { 
	
		NO_SOURCE,
		GNSS,
		RADAR_3D,
		TRIANGULATION,
		HEIGHT_FROM_COVERAGE,
		SPEED_LOOKUP_TABLE,
		DEFAULT_HEIGHT,
		MULTILATERATION;
	}
	
	public enum MODE4 {
		
		NO_MODE4_INTERROGATION,
		FRIENDLY_TARGET,
		UNKOWN_TARGET,
		NO_REPLY
	}

	public enum MODE5 {
		
		NO_MODE5_INTERROGATION,
		FRIENDLY_TARGET,
		UNKOWN_TARGET,
		NO_REPLY
	}
}
