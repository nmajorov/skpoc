// -----------------------------------------------------------------------------
// PROJECT:
// Skyguide VC2 Proto
//
// REVISION:
// $Id:$
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------

package ch.skyguide.ei.prototype.converter;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.camel.util.ObjectHelper;

import aero.fixm.model.base.LocationPointType;
import aero.fixm.model.foundation.AirspeedMeasureType;
import aero.fixm.model.foundation.GeographicLocation;
import aero.fixm.model.foundation.TrueAirspeedType;
import aero.fixm.model.foundation.UnitOfMeasureType;
import ch.skyguide.communication.rdps.asterix.categories.CartesianTrackPosition;
import ch.skyguide.communication.rdps.asterix.categories.DataCategoryType;
import ch.skyguide.communication.rdps.asterix.categories.IdentificationTag;
import ch.skyguide.communication.rdps.asterix.categories.ServiceIdentification;
import ch.skyguide.communication.rdps.asterix.categories.TimeOfMessage;
import ch.skyguide.communication.rdps.asterix.categories.TrackMode3A;
import ch.skyguide.communication.rdps.asterix.categories.TrackNumber;
import ch.skyguide.communication.rdps.asterix.categories.TypeOfMessage;
import ch.skyguide.communication.rdps.asterix.categories.TypeOfMessageContainer;
import ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedData;
import ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataContainer;
import ch.skyguide.communication.rdps.asterix.categories.cat062.CalculatedTrackVelocity;
import ch.skyguide.communication.rdps.asterix.categories.cat062.ComposedTrackNumber;
import ch.skyguide.communication.rdps.asterix.categories.cat062.MeasuredInformation;
import ch.skyguide.communication.rdps.asterix.categories.cat062.ModeOfMovement;
import ch.skyguide.communication.rdps.asterix.categories.cat062.ModeOfMovementContainer;
import ch.skyguide.communication.rdps.asterix.categories.cat062.SystemTrack;
import ch.skyguide.communication.rdps.asterix.categories.cat062.SystemTrackUpdateAges;
import ch.skyguide.communication.rdps.asterix.categories.cat062.TargetSizeAndOrientation;
import ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAges;
import ch.skyguide.communication.rdps.asterix.categories.cat062.TrackVelocity;
import ch.skyguide.communication.rdps.asterix.categories.cat065.BatchNumber;
import ch.skyguide.communication.rdps.asterix.categories.cat065.ServiceStatusReport;
import ch.skyguide.communication.rdps.asterix.structure.AsterixMessage;
import ch.skyguide.communication.rdps.asterix.structure.AsterixSystemIdentifier;
import ch.skyguide.communication.rdps.asterix.structure.AsterixUserApplicationProfileManager;
import ch.skyguide.fixm.extension.flight.enroute.AsterixRecordType;
import ch.skyguide.fixm.extension.flight.enroute.CalculatedAccelerationType;
import ch.skyguide.fixm.extension.flight.enroute.CalculatedTrackVelocityType;
import ch.skyguide.fixm.extension.flight.enroute.DataSourceIdentifier;
import ch.skyguide.fixm.extension.flight.enroute.FieldReferenceType;
import ch.skyguide.fixm.extension.flight.enroute.FieldSpecificationType;
import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixDataBlock;
import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixMessage;
import ch.skyguide.fixm.extension.flight.enroute.FixmSdpsService;
import ch.skyguide.fixm.extension.flight.enroute.FixmSystemTrackData;
import ch.skyguide.fixm.extension.flight.enroute.Identification;
import ch.skyguide.fixm.extension.flight.enroute.MeasuredPositionType;
import ch.skyguide.fixm.extension.flight.enroute.PointType;
import ch.skyguide.fixm.extension.flight.enroute.SystemTrackUpdateAgesType;
import ch.skyguide.fixm.extension.flight.enroute.TargetSizeAndOrientationType;
import ch.skyguide.fixm.extension.flight.enroute.TrackDataAgesType;
import ch.skyguide.fixm.extension.flight.enroute.TrackStatusType;
import ch.skyguide.fixm.extension.flight.enroute.TrajectoryIntentDataType;
import ch.skyguide.fixm.extension.flight.enroute.TurnDirectionType;
import ch.skyguide.fixm.extension.flight.enroute.VehicleFleetIdentificationType;
import ch.skyguide.message.structure.GenericDataBlock;
import ch.skyguide.message.structure.GenericRecord;
import ch.skyguide.message.structure.container.DataItemContainer;
import ch.skyguide.message.structure.container.DefaultSpecificationDataItemContainer;
import ch.skyguide.message.structure.datafield.CompoundField;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;
import ch.skyguide.message.structure.dataitem.AbstractUniversalExtendedDataItem;
import ch.skyguide.message.structure.dataitem.AbstractUniversalFixedDataItem;
import ch.skyguide.message.structure.dataitem.DataItem;
import ch.skyguide.message.structure.uap.UserApplicationProfile;
import ch.skyguide.util.data.FixedLengthBitSet;


//------------------------------------------------------------------------------

//------------------------------------------------------------------------------

/**
 * Convert Eurocontrol ASTERIX data format to the FIXM canonical model xml
 * format.
 * 
 * @author Ross Wilson
 *
 */
public class AsterixToFixmDataConverter {
	
	/**
	 * A method to transform from Asterix Messages to the FIXM Format.
	 * 
	 * @pre		inputBuffer shall be initialized, exceptional.
	 *  
	 * @param	inputBuffer
	 * 			The {@code ByteBuffer} to be transformed to the FIXM 
	 * 			format.  The input buffer may not be {@code null}.
	 * 
	 * @return 	The transformed {@code AsterixMessage} to the FIXM 
	 * 			{@code AsterixMessageType}.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the precondition is violated.
	 */
	public final FixmAsterixMessage 
	transformToFixm(final ByteBuffer inputBuffer)
	throws IllegalArgumentException
	{
	
		// ---------------------------------------------------------------------
		// Validate the precondition
		if(inputBuffer == null) {

			throw new IllegalArgumentException("Precondition failure: Input "
					+ "buffer is not initialized.");
		}
			
		// ---------------------------------------------------------------------
		// Decode the asterix message and transform to FIXM.
        final AsterixMessage inputAsterixMessage 
        	= new AsterixMessage(new ArrayList<GenericDataBlock>());
      
        inputAsterixMessage.decode(inputBuffer);
		
		// ---------------------------------------------------------------------
		// Transform the Asterix message to a FIXM message.
		final FixmAsterixMessage 
				fixmAsterixMessage 
					= new FixmAsterixMessage();
		
		final List<FixmAsterixDataBlock> 
			fixmAsterixDataBlocks = fixmAsterixMessage.getFixmAsterixDataBlocks();
		
		final List<GenericDataBlock> candidateDeadLetterDataBlocks 
			= new ArrayList<GenericDataBlock>();
		
		// ---------------------------------------------------------------------
		// Iterate through the data block from the various Asterix categories.
		for(GenericDataBlock dataBlock : inputAsterixMessage.getDataBlocks()) {
			
			final short dataCategory = dataBlock.getDataCategory();
			
			switch(dataCategory) {
			
				// -------------------------------------------------------------
				// Transform Asterix CAT062
				case 62:
					fixmAsterixDataBlocks.add(transform62BlockToFixm(dataBlock));
				break;

				// -------------------------------------------------------------
				// Transform Asterix CAT065
				case 65:
					fixmAsterixDataBlocks.add(transform65BlockToFixm(dataBlock));
				break;
				
				default:
					candidateDeadLetterDataBlocks.add(dataBlock);
				break;
			}
		}
		
		//----------------------------------------------------------------------
		// Data blocks are to be added to the deadletter queue.
		
		// ---------------------------------------------------------------------
		// Resultant transformed Asterix message in the FIXM format.
		return fixmAsterixMessage;
	}

	/**
	 * Private helper method to transform an Asterix CAT062 
	 * {@code GenericDataBlock}s to a FIXM {@code AsterixDataBlock}.
	 * 
	 * @pre		dataBlock may not be {@code null}.
	 * 
	 * @param	dataBlock
	 * 			The {@code GenericDataBlock} to be converted to FIXM format.
	 * 			The dataBlock may not be {@code null}.
	 * 
	 * @return	A FIXM {@code AsterixDataBlock} containing the Asterix
	 * 			data block in FIXM format.
	 */
	private final FixmAsterixDataBlock 
	transform62BlockToFixm(GenericDataBlock dataBlock) {
		
		// ---------------------------------------------------------------------
		// Validate preconditions.
		ObjectHelper.notNull(dataBlock, "dataBlock");
		
		// ---------------------------------------------------------------------
		// Setup and fill the FIXM Data Block.
		final FixmAsterixDataBlock fixmAsterixDataBlock 
			= new FixmAsterixDataBlock();
		
		fixmAsterixDataBlock.setDataCategory(dataBlock.getDataCategory());
		fixmAsterixDataBlock
			.setLengthIndicator(dataBlock.getFieldLengthIndicator());
		
		final UserApplicationProfile uap = 
				AsterixUserApplicationProfileManager.getInstance()
					.getUserApplicationProfile(DataCategoryType
											.CAT062.getByteRepresentation());

		// ---------------------------------------------------------------------
		// Convert the Asterix Records to FIXM Records.
		transform62RecordsToFixm(fixmAsterixDataBlock.getFixmAsterixRecords(), 
						 dataBlock.getRecords(), 
						 uap);
		
		// ---------------------------------------------------------------------
		// Resulting FIXM Data block.
		return fixmAsterixDataBlock;
	}
	
	/**
	 * Private helper method to transform the CAT062 records to 
	 * {@code Collection} of FIXM {@code AsterixRecord}s. 
	 * 
	 * @pre		uap and records are not {@code null}.
	 * 
	 * @param 	fixmAsterixRecords
	 * 		  	The {@code List} of FIXM {@code AsterixRecords}s to be 
	 * 			filled with the converted from Asterix Records.  The records may
	 * 			not be {@code null}.
	 * 
	 * @param 	records
	 * 		  	The {@code List} of Asterix {@code GenericRecord}s to be 
	 * 			converted from Asterix to FIXM format.  The records may not be 
	 * 			{@code null}.
	 * 
	 * @param 	uap
	 * 		  	The {@code UserApplicationProfile} containing meta information
	 * 		  	concerning each Asterix Data Field.  The uap may not be 
	 * 		  	{@code null}.
	 * 
	 * @return 	A {@code Collection} containing the FIXM  
	 * 			{@code AsterixRecord}.
	 */
	private final void 
	transform62RecordsToFixm(final List<AsterixRecordType> fixmAsterixRecords,
							 final List<GenericRecord> records,
							 final UserApplicationProfile uap) {
		
		// ---------------------------------------------------------------------
		// Validate preconditions
		ObjectHelper.notNull(fixmAsterixRecords, "fixmAsterixRecords");
		ObjectHelper.notNull(uap, "uap");
		ObjectHelper.notNull(records, "records");
				
		// ---------------------------------------------------------------------
		// Iterate through asterix records and convert to FIXM format
		for(GenericRecord record : records) {
			
			final FieldSpecificationType fieldSpecification 
				= new FieldSpecificationType();
			
			final List<FieldReferenceType> fieldReferences 
				= fieldSpecification.getFieldReferences();
			
			final FixmSystemTrackData fixmAsterixRecord 
				= new FixmSystemTrackData();
			fixmAsterixRecord.setFieldSpecification(fieldSpecification);
			
			//------------------------------------------------------------------
			// Setup Field Reference Numbers
			for(int i = 1; i < 35; i++) {
				
				FieldReferenceType fieldReference = new FieldReferenceType();
				fieldReference.setPresent(record.getDataField(i)!= null);
				fieldReference.setFrn(i);
				
				if(fieldReference.isPresent()) {
				
					fieldReference.setDataItem(record.getDataField(i)
										   .getAssociatedDataItem()
										   .getDataItemReferenceNumber());
				}
				
				fieldReferences.add(fieldReference);
			}
			
			// -----------------------------------------------------------------
			// FRN 1:  Data Item I062/010 Data Source Identifier, 2 bytes
			// Identification of the system sending the data
			if(record.getDataField(1) != null) {

				final DataItem dataItem 
					= record.getDataField(1).getAssociatedDataItem();
				
				final DataField dataField = record.getDataField(1);
				
				final AsterixSystemIdentifier identifier 
					= ((IdentificationTag)dataItem)
						.unpack((NonCompoundField)dataField);
				
				DataSourceIdentifier dataSourceId = new DataSourceIdentifier();
				dataSourceId.setSystemAreaCode(identifier.getSystemAreaCode());
				dataSourceId
					.setSystemIdentificationCode(identifier
							.getSystemIdentificationCode());
				
				fixmAsterixRecord.setDataSourceIdentifier(dataSourceId);
			}
			
			// -----------------------------------------------------------------
			// FRN 2:  Spare
			
			// -----------------------------------------------------------------
			// FRN 3:  Data Item I062/015 Service Identification, 1 byte
			// Identification of the service provided to one or more users.
			if(record.getDataField(3) != null) {

				final DataItem dataItem 
					= record.getDataField(3).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(3);
				
				final byte serviceIdentification 
					= ((ServiceIdentification)dataItem)
						.unpack((NonCompoundField)dataField);

				fixmAsterixRecord
					.setServiceIdentifier((short)serviceIdentification);
			}
			
			// -----------------------------------------------------------------
			// FRN 4:  Data Item I062/070 Time Of Track Information, 3 bytes
			// Absolute time stamping of the information provided in the track
			// message, in the form of elapsed time since last midnight,
			// expressed as UTC. Units are 1/128 s
			if(record.getDataField(4) != null) {

				final DataItem dataItem 
					= record.getDataField(4).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(4);

				final int timeOfMessage = 
					((TimeOfMessage)dataItem)
						.unpack((NonCompoundField)dataField);

				fixmAsterixRecord.setTimeOfTrackInformation(timeOfMessage);
			}
		
			// -----------------------------------------------------------------
			// FRN 5:  Data Item I062/105 Calculated Track Position (WGS-84),
			// 8 bytes
			// Calculated Position in WGS-84 Co-ordinates with a resolution of
			// 180/2-25 degrees.
			if(record.getDataField(5) != null) {

				final DataItem dataItem 
				= record.getDataField(5).getAssociatedDataItem();

				final DataField dataField = record.getDataField(5);
	
				final ByteBuffer calculatedTrackPosition = 
					((AbstractUniversalFixedDataItem)dataItem)
						.unpack((NonCompoundField)dataField);
				
				LocationPointType location = new LocationPointType();
				location.setLocation(new GeographicLocation());

				// TODO : rjw cheating with the conversions.
				List<Double> positions = location.getLocation().getPos();
				positions
					.add(Double.valueOf(calculatedTrackPosition
							.asIntBuffer().get()));
				positions
					.add(Double.valueOf(calculatedTrackPosition
							.asIntBuffer().get()));
				
				fixmAsterixRecord
					.setCalculatedTrackPositionWGS84Coordinates(location);
			}

			// -----------------------------------------------------------------
			// FRN 6:  Data Item I062/100 Calculated Track Position (Cartesian)
			// 6 bytes
			// Calculated position in Cartesian co-ordinates with a resolution 
			// of 0.5m, in two’s complement form.
			if(record.getDataField(6) != null) {

				final DataItem dataItem 
				= record.getDataField(6).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(6);
				
				final CartesianTrackPosition.CartesianPosition calculatedTrackPosition = 
						((CartesianTrackPosition)dataItem)
							.unpack((NonCompoundField)dataField);
					
				LocationPointType location = new LocationPointType();
				location.setLocation(new GeographicLocation());

				// TODO : rjw cheating with the conversions.
				List<Double> positions = location.getLocation().getPos();
				
				positions
					.add(Double.valueOf(calculatedTrackPosition
							.getXComponent()));
				positions
					.add(Double.valueOf(calculatedTrackPosition
							.getYComponent()));
				
				fixmAsterixRecord.setCalculatedTrackPositionCartesian(location);
			}

			// -----------------------------------------------------------------
			// FRN 7: Data Item I062/185 Calculated Track Velocity (Cartesian)
			// 4 bytes
			// Calculated track velocity expressed in Cartesian co-ordinates,
			// in two’s complement form.  Units is 0.25 m/s
			if(record.getDataField(7) != null) {

				final DataItem dataItem 
					= record.getDataField(7).getAssociatedDataItem();
				
				final DataField dataField = record.getDataField(7);
			
				final TrackVelocity trackVelocity 
					= ((CalculatedTrackVelocity)dataItem)
						.unpack((NonCompoundField)dataField);
				final CalculatedTrackVelocityType fixmCalTrackVelocity 
					= new CalculatedTrackVelocityType();
				
				fixmCalTrackVelocity.setVx(trackVelocity.getXVelocity());
				fixmCalTrackVelocity.setVy(trackVelocity.getYVelocity());

				fixmAsterixRecord
				.setCalculatedTrackVelocityCartesian(fixmCalTrackVelocity);
			}
			
			// -----------------------------------------------------------------
			// FRN 8:  Data Item I062/210 Calculated Acceleration (Cartesian)
			// 2 bytes
			// Calculated Acceleration of the target expressed in Cartesian
			// co-ordinates, in two’s complement form. 0.25 m/s-2
			if(record.getDataField(8) != null) {

				final DataItem dataItem 
					= record.getDataField(8).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(8);
				
				final ByteBuffer calculatedCartesianAcceleration 
					= ((AbstractUniversalFixedDataItem)dataItem)
						.unpack((NonCompoundField)dataField);
	
				
				fixmAsterixRecord
				.setCalculatedAccelerationCartesian(new CalculatedAccelerationType());
				fixmAsterixRecord.getCalculatedAccelerationCartesian()
					.setAx(calculatedCartesianAcceleration.get());
				fixmAsterixRecord.getCalculatedAccelerationCartesian()
					.setAy(calculatedCartesianAcceleration.get());
			}

			// -----------------------------------------------------------------
			// FRN 9:  Data Item I062/060 Track Mode 3/A Code, 2 bytes
			// Mode-3/A code converted into octal representation.
			if(record.getDataField(9) != null) {

				final DataItem dataItem 
					= record.getDataField(9).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(9);

				final int trackMode3A 
					= ((TrackMode3A)dataItem)
						.unpack((NonCompoundField)dataField);
				
				fixmAsterixRecord.setTrackMode3ACode(trackMode3A);
			}

			// -----------------------------------------------------------------
			// FRN 10:  Data Item I062/245 Target Identification, 7 bytes
			// Target (aircraft or vehicle) identification in 8 characters.
			if(record.getDataField(10) != null) {

				final DataItem dataItem 
					= record.getDataField(10).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(10);

				final ByteBuffer targetIdentification 
					= ((AbstractUniversalFixedDataItem)dataItem)
						.unpack((NonCompoundField)dataField);
				
				BitSet bsTargetId = BitSet.valueOf(targetIdentification);
				BitSet character = bsTargetId.get(48, 43);
				CharBuffer buf = CharBuffer.allocate(7);
				buf.put((char)character.toByteArray()[0]);				
				
				character = bsTargetId.get(42, 37);
				buf.put((char)character.toByteArray()[0]);				
				
				character = bsTargetId.get(36, 31);
				buf.put((char)character.toByteArray()[0]);				

				character = bsTargetId.get(30, 25);
				buf.put((char)character.toByteArray()[0]);				

				character = bsTargetId.get(24, 19);
				buf.put((char)character.toByteArray()[0]);				

				character = bsTargetId.get(18, 13);
				buf.put((char)character.toByteArray()[0]);				

				character = bsTargetId.get(12, 7);
				buf.put((char)character.toByteArray()[0]);				

				character = bsTargetId.get(6, 1);
				buf.put((char)character.toByteArray()[0]);
				buf.rewind();

				fixmAsterixRecord.setTargetIdentification(buf.toString());
			}

			// -----------------------------------------------------------------
			// FRN 11:  Data Item I062/380 Aircraft Derived Data 1+ Bytes
			// Data derived directly by the aircraft.
			// Compound Data Item, comprising a primary subfield of up to four
			// octets, followed by the indicated subfields.
			if(record.getDataField(11) != null) {

				final DataItem dataItem 
					= record.getDataField(11).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(11);
				
				final AircraftDerivedDataContainer aircraftDerivedData 
					= (AircraftDerivedDataContainer)((AircraftDerivedData)dataItem)
						.unpack((CompoundField)dataField);
	
				ch.skyguide.fixm.extension.flight.enroute.AircraftDerivedData fixmAircraftDerivedData 
				= new ch.skyguide.fixm.extension.flight.enroute.AircraftDerivedData();
				final FixedLengthBitSet primaryField 
					= aircraftDerivedData.getPrimaryField();
				
				if(primaryField.get(0)) {
					fixmAircraftDerivedData
					.setTargetAddress((long)aircraftDerivedData
									  .getTargetAddress().toInt());
				}
				
				if(primaryField.get(1)) {
					
					fixmAircraftDerivedData
					.setTargetIdentification(new Identification());
				}
				
				if(primaryField.get(2)) {
					fixmAircraftDerivedData.setTargetIdentification(new Identification());
					fixmAircraftDerivedData.getTargetIdentification()
					.setValue(aircraftDerivedData.getTargetIdentification());
				}
				
				if(primaryField.get(3)) {
					fixmAircraftDerivedData
					.setMagneticHeading((long)aircraftDerivedData
										.getMagneticHeading());
				}
				
				if(primaryField.get(4)) {

					final TrueAirspeedType airspeed = new TrueAirspeedType();
					
					if(aircraftDerivedData.getIASMNoMachNumberFlag()) {
						
						airspeed.setUom(AirspeedMeasureType.MACH);
					}
					else {
					
						airspeed.setUom(AirspeedMeasureType.KNOTS);
					}
					
					UnitOfMeasureType value = new UnitOfMeasureType();
					value.setValue(aircraftDerivedData.getIASMNoSpeed());
					airspeed.setValue(value);
					
					fixmAircraftDerivedData.setIndicatedAirspeedMachNo(airspeed);
				}
				
				if(primaryField.get(5)) {
					TrueAirspeedType airspeed = new TrueAirspeedType();
					airspeed.setUom(AirspeedMeasureType.KNOTS);
					UnitOfMeasureType value = new UnitOfMeasureType();
					value.setValue(aircraftDerivedData.getIASMNoSpeed());
					airspeed.setValue(value);
					
					fixmAircraftDerivedData.setTrueAirspeed(airspeed);
				}

				
				if(primaryField.get(6)) {
					
					fixmAircraftDerivedData
					.setSelectedAltitude(aircraftDerivedData.getSALSelectedAltitude());;
				}

				if(primaryField.get(7)) {
					
					fixmAircraftDerivedData
					.setFinalStateSelectedAltitude(aircraftDerivedData
							.getFinalStateSelectedAltitude());
				}

				//TODO: rjw, determine what's wrong with the field 8 in aircraft derived data.
//				if(primaryField.get(8)) {
//				
//					fixmAircraftDerivedData
//					.setTrajectoryIntentStatus((short)aircraftDerivedData
//										.getTrajectoryIntentStatus()[0]);
//				}
				
				if(primaryField.get(9)) {
					
					ByteBuffer buffer 
						= ByteBuffer.wrap(aircraftDerivedData
										  .getTrajectoryIntentStatus());
					
					byte repititionFactor = buffer.get();
					TrajectoryIntentDataType trajectory 
						= new TrajectoryIntentDataType();
					for(int i = 0; i < repititionFactor; i++) {
						
						buffer.mark();
						trajectory
							.setTrajectoryChangePointAvailable((buffer.get()>>7)==1);
						buffer.reset().mark();
						trajectory
							.setTrajectoryChangePointCompliance((buffer.get()<<1)>>6==1);
						buffer.reset();
						trajectory
							.setTrajectoryChangePointNumber((short)((buffer.get()<<2)>>2));
						
						trajectory.setAltitude(buffer.getShort());
						
						ByteBuffer temp 
							= ByteBuffer.allocate(8).put(buffer.get(new byte[3]));
						temp.rewind();
						trajectory
							.setLatitude(BigInteger.valueOf(temp.getLong()));
						
						temp 
							= ByteBuffer.allocate(8).put(buffer.get(new byte[3]));
						temp.rewind();
						trajectory
							.setLongitude(BigInteger.valueOf(temp.getLong()));
						
						buffer.mark();
						trajectory
							.setPointType(PointType.values()[buffer.get()>>4]);
						buffer.reset();
						
						buffer.mark();
						trajectory
							.setTurnDirection(TurnDirectionType.values()[(buffer.get()<<4)>>6]);
						buffer.reset();

						buffer.mark();
						trajectory
							.setTurnRadiusAvailability(((buffer.get()<<6)>>7)==1);
						buffer.reset();

						trajectory
						.setTimeOverPointAvailability(((buffer.get()<<6)>>7)==1);
						
						temp = ByteBuffer.allocate(8).put(buffer.get(new byte[3]));
						temp.rewind();
						trajectory.setTimeOverPoint(temp.getLong());
						
						trajectory
							.setTrajectoryChangePointTurnRadius(buffer.getShort());
					}
					
					// TODO: work out error and complete conversion.
					fixmAircraftDerivedData.setTrajectoryIntentData(trajectory);
				}

				fixmAsterixRecord.setAircraftDerivedData(fixmAircraftDerivedData);
			}

			// -----------------------------------------------------------------
			// FRN 12:  Data Item I062/040 Track Number, 2 bytes
			// Identification of a track
			if(record.getDataField(12) != null) {

				final DataItem dataItem 
					= record.getDataField(12).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(12);
				
				final int trackNumber 
					= ((TrackNumber)dataItem)
						.unpack((NonCompoundField)dataField);
	
				fixmAsterixRecord.setTrackNumber(trackNumber);
			}

			// -----------------------------------------------------------------
			// FRN 13:  Data Item I062/080 Track Status, 1+ bytes
			// Status of a track.
			// Variable length data item comprising a first part of one Octet,
			// followed by 1-Octet extents as necessary.
			if(record.getDataField(13) != null) {

				final DataItem dataItem 
					= record.getDataField(13).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(13);
				
				final List<Number> trackStatus 
					= ((AbstractUniversalExtendedDataItem)dataItem)
						.unpack((NonCompoundField)dataField);
				
				fixmAsterixRecord.setTrackStatus(new TrackStatusType());
				List<Short> fixmTrackStatus 
					= fixmAsterixRecord.getTrackStatus().getValues();
				
				for (Number trackStatusField : trackStatus) {
				
					fixmTrackStatus
					.add(Short.valueOf((short)trackStatusField.byteValue()));
				}
				
			}

			// -----------------------------------------------------------------
			// FRN 14:  Data Item I062/290 System Track Update Ages, 1+ bytes
			// Ages of the last plot/local track/target report update for each
			// sensor type.
			// Compound Data Item, comprising a primary subfield of up to two
			// octets, followed by the indicated subfields.
			if(record.getDataField(14) != null) {

				final DataItem dataItem 
					= record.getDataField(14).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(14);
				
				final DataItemContainer systemTrackAges 
					= ((SystemTrackUpdateAges)dataItem)
						.unpack((CompoundField)dataField);
	
				fixmAsterixRecord
					.setSystemTrackUpdateAges(new SystemTrackUpdateAgesType());
				
				List<Short> fixmSystemTrackAges 
					= fixmAsterixRecord.getSystemTrackUpdateAges().getValues();
			
				for (byte trackAge : systemTrackAges.getContainerData()) {
			
					fixmSystemTrackAges.add(Short.valueOf((short)trackAge));
				}
			}

			// -----------------------------------------------------------------
			// FRN 15:  I062/200 Mode of Movement, 1 byte
			// Calculated Mode of Movement of a target.
			if(record.getDataField(15) != null) {

				final DataItem dataItem 
					= record.getDataField(15).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(15);
				
				final ModeOfMovementContainer modeOfMovement 
					= ((ModeOfMovement)dataItem)
						.unpack((NonCompoundField)dataField);
				
				fixmAsterixRecord
					.setModeOfMovement((short)modeOfMovement.getBytes()[0]);
			}

			// -----------------------------------------------------------------
			// FRN 16:  Data Item I062/295 Track Data Ages, 1+ bytes
			// Ages of the data provided.
			// Compound Data Item, comprising a primary subfield of up to five
			//octets, followed by the indicated subfields.
			if(record.getDataField(16) != null) {

				final DataItem dataItem 
					= record.getDataField(16).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(16);
				
				final DataItemContainer trackDataAges 
					= ((TrackDataAges)dataItem)
						.unpack((CompoundField)dataField);
	
				fixmAsterixRecord.setTrackDataAges(new TrackDataAgesType());
				List<Short> fixmTrackAges 
				= fixmAsterixRecord.getTrackDataAges().getValues();
		
				for (byte trackAge : trackDataAges.getBytes()) {
					
					fixmTrackAges.add(Short.valueOf((short)trackAge));
				}
			}

			// -----------------------------------------------------------------
			// FRN 17:  Data Item I062/136 Measured Flight Level, 2 bytes
			// Last valid and credible flight level used to update the track, in
			// two’s complement form. Units 0.25 FL
			if(record.getDataField(17) != null) {

				final DataItem dataItem 
					= record.getDataField(17).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(17);
				
				final ByteBuffer measuredAltitude 
					= ((AbstractUniversalFixedDataItem)dataItem)
						.unpack((NonCompoundField)dataField);
	
				fixmAsterixRecord
				.setMeasuredFlightLevel((long)(measuredAltitude.getShort()));
			}

			// -----------------------------------------------------------------
			// FRN 18: Data Item I062/130:  Calculated Track Geometric Altitude,
			// 2 bytes
			// Vertical distance between the target and the projection of its
			//position on the earth’s ellipsoid, as defined by WGS84, in two’s
			// complement form. Units 6.25 feet
			if(record.getDataField(18) != null) {

				final DataItem dataItem 
					= record.getDataField(18).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(18);
				
				final ByteBuffer calTrackGeometricAlt 
					= ((AbstractUniversalFixedDataItem)dataItem)
						.unpack((NonCompoundField)dataField);

				fixmAsterixRecord
				.setCalculatedTrackGeometricAltitude(BigInteger
										.valueOf(calTrackGeometricAlt.getShort()));
			}

			// -----------------------------------------------------------------
			// FRN 19:  Data Item I062/135 Calculated Track Barometric Altitude,
			// 2 bytes
			// Calculated Barometric Altitude of the track, in two’s complement
			// form. Units 0.25 FL == 25 feet
			if(record.getDataField(19) != null) {

				final DataItem dataItem 
					= record.getDataField(19).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(19);

				final ByteBuffer calTrackBarometricAlt 
				= ((AbstractUniversalFixedDataItem)dataItem)
					.unpack((NonCompoundField)dataField);

				fixmAsterixRecord
				.setCalculatedTrackBarometricAltitude(BigInteger
									.valueOf(calTrackBarometricAlt.getShort()));
			}

			// -----------------------------------------------------------------
			// FRN 20:  Data Item I062/220 Calculated Rate Of Climb/Descent,
			// 2 bytes
			// Calculated rate of Climb/Descent of an aircraft in two’s
			// complement form. Units: 6.25 feet/minute
			if(record.getDataField(20) != null) {

				final DataItem dataItem 
					= record.getDataField(20).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(20);
				
				final ByteBuffer calRateClimbDescent 
				= ((AbstractUniversalFixedDataItem)dataItem)
					.unpack((NonCompoundField)dataField);

				fixmAsterixRecord
				.setCalculatedRateOfClimb(BigInteger
									.valueOf(calRateClimbDescent.getShort()));
			}

			// -----------------------------------------------------------------
			// FRN 21:  Data Item I062/390 Flight Plan Related Data, 1+ bytes
			// All flight plan related information, provided by ground-based
			// systems.
			// Compound Data Item, comprising a primary subfield of up to three
			// octets, followed by the indicated subfields.
			if(record.getDataField(21) != null) {

				// TODO: rjw, currently ignoring anything coming in the AST for 
				//		 flight plan related data.
//				final DataItem dataItem 
//					= record.getDataField(21).getAssociatedDataItem();
//			
//				final DataField dataField = record.getDataField(21);
//				
//				final List<Number> fltPlanRelData 
//					= ((AbstractUniversalExtendedDataItem)dataItem)
//						.unpack((NonCompoundField)dataField);
//
//				List<Short> fixmFltPlanRelData 
//				= fixmAsterixRecord.getFlightPlanRelatedData().getValues();
//			
//				for (Number fltPlanData : fltPlanRelData) {
//				
//					fixmFltPlanRelData
//					.add(Short.valueOf((short)fltPlanData.byteValue()));
//				}
			}

			// -----------------------------------------------------------------
			// FRN 22:  Data Item I062/270 Target Size & Orientation, 1+ bytes
			// Target size defined as length and width of the detected target,
			// and orientation. Units: Length Meters, 
			// Orientation 360° / 128 = approx. 2.81°, Width Meters
			if(record.getDataField(22) != null) {

				final DataItem dataItem 
					= record.getDataField(22).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(22);

				final List<Short> targetSizeAndOrientation 
					= ((TargetSizeAndOrientation)dataItem)
						.unpack((NonCompoundField)dataField);
	
				fixmAsterixRecord
					.setTargetSizeAndOrientation(new TargetSizeAndOrientationType());
				// Length, Orientation and Width.
				fixmAsterixRecord
					.getTargetSizeAndOrientation()
					.setLength(targetSizeAndOrientation.get(0));

				if(targetSizeAndOrientation.size()>1) {

					fixmAsterixRecord
						.getTargetSizeAndOrientation()
						.setOrientation(targetSizeAndOrientation.get(1));
				}
				
				if(targetSizeAndOrientation.size()>2) {

					fixmAsterixRecord
						.getTargetSizeAndOrientation()
						.setWidth(targetSizeAndOrientation.get(2));
				}
			}

			// -----------------------------------------------------------------
			// FRN 23:  Data Item I062/300 Vehicle Fleet Identification, 1 byte
			// Vehicle fleet identification number.
			if(record.getDataField(23) != null) {

				final DataItem dataItem 
					= record.getDataField(23).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(23);
				
				final ByteBuffer vehicleFleetId 
					= ((AbstractUniversalFixedDataItem)dataItem)
						.unpack((NonCompoundField)dataField);
				
				fixmAsterixRecord
				.setVehicleFleetIdentification(VehicleFleetIdentificationType
												.values()[vehicleFleetId.get()]);
			}

			// -----------------------------------------------------------------
			// FRN 24:  Data Item I062/110 Mode 5 Data reports & Extended Mode 
			// 1 Code, 1+ bytes
			// Mode 5 Data reports & Extended Mode 1 Code
			// Compound Data Item, comprising a primary subfield of one octet,
			// followed by the indicated subfields.
			if(record.getDataField(24) != null) {

				final DataItem dataItem 
					= record.getDataField(24).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(24);

				final List<Number> mode5AndExtendedMode1Codes 
					= ((AbstractUniversalExtendedDataItem)dataItem)
						.unpack((NonCompoundField)dataField);
				
				List<Short> fixmMode5AndExtendedMode1Codes 
					= fixmAsterixRecord.getMode5And1Code().getValues();
				
				for (Number mode5AndExtendedMode1Code 
						: mode5AndExtendedMode1Codes) {
					
					fixmMode5AndExtendedMode1Codes
						.add(mode5AndExtendedMode1Code.shortValue());
				}
			}

			// -----------------------------------------------------------------
			// FRN 25:  Data Item I062/120 Track Mode 2 Code, 2 bytes
			// Mode 2 code associated to the track
			if(record.getDataField(25) != null) {

				final DataItem dataItem 
					= record.getDataField(25).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(25);
				
				final ByteBuffer mode2Code 
					= ((AbstractUniversalFixedDataItem)dataItem)
						.unpack((NonCompoundField)dataField);
	
				fixmAsterixRecord
				.setTrackMode2Code(BigInteger.valueOf(mode2Code.getShort()));
			}

			// -----------------------------------------------------------------
			// FRN 26:  Data Item I062/510 Composed Track Number, 3+ bytes
			// Identification of a system track
			// Extendible data item, comprising a first part of three octets (Master
			// Track Number), followed by three-octet extents (Slave Tracks
			// Numbers).
			if(record.getDataField(26) != null) {

				final DataItem dataItem 
					= record.getDataField(26).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(26);
				
				final List<SystemTrack> systemTracks 
					= ((ComposedTrackNumber)dataItem)
						.unpack((NonCompoundField)dataField);
				
				ch.skyguide.fixm.extension.flight.enroute.ComposedTrackNumber fixmComposedTrackNumber 
				= new ch.skyguide.fixm.extension.flight.enroute.ComposedTrackNumber();
				
				fixmAsterixRecord.setComposedTrackNumber(fixmComposedTrackNumber);
				
				List<ch.skyguide.fixm.extension.flight.enroute.SystemTrack> fixmSystemTracks =
						fixmComposedTrackNumber.getSystemTracks();
				
				for(SystemTrack track : systemTracks) {
					
					ch.skyguide.fixm.extension.flight.enroute.SystemTrack fixmTrack
						= new ch.skyguide.fixm.extension.flight.enroute.SystemTrack();
			
					fixmTrack.setSystemUnitIdentification((short)track.getArtasUnitIdentification());
					fixmTrack.setSystemTrackNumber(track.getSystemTrack());
					
					fixmSystemTracks.add(fixmTrack);
				}
			}

			// -----------------------------------------------------------------
			// FRN 27:  Data Item I062/500 Estimated Accuracies, 1+ bytes
			// Overview of all important accuracies
			// Compound Data Item, comprising a primary subfield of up to two
			// octets, followed by the indicated subfields.
			if(record.getDataField(27) != null) {

				final DataItem dataItem 
					= record.getDataField(27).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(27);
				
				//TODO: Estimated accuracies.
				
			}
		
			// -----------------------------------------------------------------
			// FRN 28:  Data Item I062/340 Measured Information, 1+ bytes
			// All measured data related to the last report used to update the
			// track. These data are not used for ADS-B.
			// Compound Data Item, comprising a primary subfield of one octet,
			// followed by the indicated subfields.
			if(record.getDataField(28) != null) {

				final DataItem dataItem 
					= record.getDataField(28).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(28);
				
				final DefaultSpecificationDataItemContainer measuredInformation 
					= (DefaultSpecificationDataItemContainer)((MeasuredInformation)dataItem)
						.unpack((CompoundField)dataField);
				final FixedLengthBitSet bitSet 
					= measuredInformation.getPrimaryField();
				final ch.skyguide.fixm.extension.flight.enroute.MeasuredInformation fixmMeasuredInformation 
					= new ch.skyguide.fixm.extension.flight.enroute.MeasuredInformation();
				
				final ByteBuffer buffer 
					= ByteBuffer.wrap(measuredInformation.getContainerData());
				
				if(bitSet.get(0)) {
					
					final DataSourceIdentifier fixmDataSourceIdentifier 
						= new DataSourceIdentifier();
					fixmMeasuredInformation
						.setDataSourceIdentifier(fixmDataSourceIdentifier);
					fixmDataSourceIdentifier
						.setSystemAreaCode(buffer.get());
					fixmDataSourceIdentifier
						.setSystemIdentificationCode(buffer.get());
				}

				if(bitSet.get(1)) {
				
					final MeasuredPositionType fixmMeasuredPosition 
					= new MeasuredPositionType();
					fixmMeasuredInformation.setMeasuredPosition(fixmMeasuredPosition);
					fixmMeasuredPosition.setRho(buffer.getShort());
					fixmMeasuredPosition.setTheta(buffer.getShort());
				}
				
				// TODO: rjw, measured information, measured 3D height doesn't have required number of bytes.
//				if(bitSet.get(2)) {
//					
//					fixmMeasuredInformation.setMeasured3DHeight((int)buffer.getShort());
//				}
				
				if(bitSet.get(3)) {
					
					fixmMeasuredInformation
						.setLastMeasuredModeCCode((int)buffer.getShort());
				}
				
				if(bitSet.get(4)) {
					
					fixmMeasuredInformation
						.setLastMeasuredMode3ACode((int)buffer.getShort());
				}
				
				if(bitSet.get(5)) {
					
					fixmMeasuredInformation.setReportType((short)buffer.get());
				}
				
				fixmAsterixRecord
					.setMeasuredInformation(fixmMeasuredInformation);
			}

			// -----------------------------------------------------------------
			// FRN 29 - 33 Spare Fields

			// -----------------------------------------------------------------
			// FRN 34:  RE Reserved Expansion Field 
			if(record.getDataField(34) != null) {
				
//				final DataItem dataItem 
//					= record.getDataField(34).getAssociatedDataItem();
//		
//				final DataField dataField = record.getDataField(34);
//				
//				final REForCat062Container reserveExpansionField062 
//					= (REForCat062Container)((REForCat062)dataItem)
//						.unpack((CompoundField)dataField);
				
				// TODO: Translate into Fixm.
			}

			// -----------------------------------------------------------------
			// FRN 35:  RP Reserved For Special Purpose Indicator 
			if(record.getDataField(35) != null) {
				
				// TODO: not implemented FRN 35:  RP Reserved For Special Purpose Indicator
//				Assert.
//				assertTrue("Unexpected FRN 35: RP Reserved for Special Purpose"
//						+ " Indicator", false);
			}
		
			// -----------------------------------------------------------------
			// End of record conversion, process next record or finished. 
			fixmAsterixRecords.add(fixmAsterixRecord);
		}
	}
	
	
	/**
	 * Private helper method to convert an Asterix CAT065 
	 * {@code GenericDataBlock}s to a FIXM {@code AsterixDataBlock}.
	 * 
	 * @pre		dataBlock may not be {@code null}.
	 * 
	 * @param	dataBlock
	 * 			The {@code GenericDataBlock} to be converted to FIXM format.
	 * 			The dataBlock may not be {@code null}.
	 * 
	 * @return	A FIXM {@code AsterixDataBlock} containing the Asterix
	 * 			data block in FIXM format.
	 */
	private final FixmAsterixDataBlock 
	transform65BlockToFixm(GenericDataBlock dataBlock) {
		
		// ---------------------------------------------------------------------
		// Validate preconditions.
		ObjectHelper.notNull(dataBlock, "dataBlock");
		
		// ---------------------------------------------------------------------
		// Setup and fill the FIXM Data Block.
		final FixmAsterixDataBlock fixmAsterixDataBlock 
			= new FixmAsterixDataBlock();
		
		fixmAsterixDataBlock.setDataCategory(dataBlock.getDataCategory());
		fixmAsterixDataBlock
			.setLengthIndicator(dataBlock.getFieldLengthIndicator());
		
		final UserApplicationProfile uap = 
				AsterixUserApplicationProfileManager.getInstance()
					.getUserApplicationProfile(DataCategoryType
											.CAT065.getByteRepresentation());

		// ---------------------------------------------------------------------
		// Convert the Asterix Records to FIXM Records.
		transform65RecordsToFixm(fixmAsterixDataBlock.getFixmAsterixRecords(), 
						 dataBlock.getRecords(), 
						 uap);
		
		// ---------------------------------------------------------------------
		// Resulting FIXM Data block.
		return fixmAsterixDataBlock;
	}
	
	/**
	 * Private helper method to transform the CAT065 records to 
	 * {@code Collection} of FIXM {@code AsterixRecord}s. 
	 * 
	 * @pre		uap and records are not {@code null}.
	 * 
	 * @param 	fixmAsterixRecords
	 * 		  	The {@code List} of FIXM {@code AsterixRecords}s to be 
	 * 			filled with the converted from Asterix Records.  The records may
	 * 			not be {@code null}.
	 * 
	 * @param 	records
	 * 		  	The {@code List} of Asterix {@code GenericRecord}s to be 
	 * 			converted from Asterix to FIXM format.  The records may not be 
	 * 			{@code null}.
	 * 
	 * @param 	uap
	 * 		  	The {@code UserApplicationProfile} containing meta information
	 * 		  	concerning each Asterix Data Field.  The uap may not be 
	 * 		  	{@code null}.
	 * 
	 * @return 	A {@code Collection} containing the FIXM 
	 * 			{@code AsterixRecord}.
	 */
	private final void 
	transform65RecordsToFixm(final List<AsterixRecordType> fixmAsterixRecords,
					 final List<GenericRecord> records,
					 final UserApplicationProfile uap) {
		
		// ---------------------------------------------------------------------
		// Validate preconditions
		ObjectHelper.notNull(fixmAsterixRecords, "fixmAsterixRecords");
		ObjectHelper.notNull(records, "records");
		ObjectHelper.notNull(uap, "uap");
		
		// ---------------------------------------------------------------------
		// Iterate through asterix records and convert to FIXM format
		for(GenericRecord record : records) {
			
			final FieldSpecificationType fieldSpecification 
				= new FieldSpecificationType();
			
			final List<FieldReferenceType> fieldReferences 
				= fieldSpecification.getFieldReferences();
			
			final FixmSdpsService fixmAsterixRecord = new FixmSdpsService();
			fixmAsterixRecord.setFieldSpecification(fieldSpecification);
			
			//------------------------------------------------------------------
			// Setup Field Reference Numbers
			for(int i = 1; i < 8; i++) {
				
				FieldReferenceType fieldReference = new FieldReferenceType();
				fieldReference.setPresent(record.getDataField(i)!= null);
				fieldReference.setFrn(i);
				
				if(fieldReference.isPresent()) {
				
					fieldReference.setDataItem(record.getDataField(i)
										   .getAssociatedDataItem()
										   .getDataItemReferenceNumber());
				}
				
				fieldReferences.add(fieldReference);
			}
			

			// -----------------------------------------------------------------
			// FRN 1:  Data Source Identifier
			// I065/010, Data Source Identifier
			// Identification of the SDPS sending the data
			// No units.
			if(record.getDataField(1) != null) {

				final DataItem dataItem 
					= record.getDataField(1).getAssociatedDataItem();
				
				final DataField dataField = record.getDataField(1);
				
				
				final AsterixSystemIdentifier identifier 
					= ((IdentificationTag)dataItem).unpack((NonCompoundField)dataField);
				
				DataSourceIdentifier dataSourceId 
					= new DataSourceIdentifier();
				dataSourceId.setSystemAreaCode(identifier.getSystemAreaCode());
				dataSourceId.setSystemIdentificationCode(identifier.getSystemIdentificationCode());
				
				fixmAsterixRecord.setDataSourceIdentifier(dataSourceId);
			}
			
			
			// -----------------------------------------------------------------
			// FRN 2:  Message
			// Data Item I065/000, Message Type
			// This Data Item allows for a more convenient handling of the 
			// messages at the receiver side by further defining the type of transaction.
			// One-octet fixed length Data Item with no units.
			if(record.getDataField(2) != null) {

				final DataItem dataItem 
				= record.getDataField(2).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(2);
				final TypeOfMessageContainer OfMessage
					= ((TypeOfMessage)dataItem)
						.unpack((NonCompoundField)dataField);
				
				fixmAsterixRecord.setMessageType(OfMessage.getNatureOfMessage());
			}
			
			// -----------------------------------------------------------------
			// FRN 3:  Service Identification
			// Data Item I065/010, Data Source Identifier
			// Identification of the SDPS sending the data
			// Two-octet fixed length Data Item with no units.
			if(record.getDataField(3) != null) {

				final DataItem dataItem 
					= record.getDataField(3).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(3);

				final byte serviceIdentification 
					= ((ServiceIdentification)dataItem)
						.unpack((NonCompoundField)dataField);

				fixmAsterixRecord.setServiceIdentifier((short)serviceIdentification);
			}
			
			// -----------------------------------------------------------------
			// FRN 4:  Data Item I065/030, Time of Message
			// Absolute time stamping of the message, in the form of elapsed 
			// time since last midnight, expressed as UTC.
			// Three-Octet fixed length data item with unit of 1/128 s
			if(record.getDataField(4) != null) {

				final DataItem dataItem 
					= record.getDataField(4).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(4);
				
				final int timeOfMessage = 
					((TimeOfMessage)dataItem)
						.unpack((NonCompoundField)dataField);

				fixmAsterixRecord.setTimeOfMessage(timeOfMessage);
			}
		
			// -----------------------------------------------------------------
			// FRN 5:  Data Item I065/020, Batch Number
			// A number indicating the completion of a service for that batch of
			// track data, from 0 to N-1, N being the number of batches used to 
			// make one complete processing cycle.
			// One-Octet fixed length data item with no units.
			if(record.getDataField(5) != null) {

				final DataItem dataItem 
				= record.getDataField(5).getAssociatedDataItem();
			
				final DataField dataField = record.getDataField(5);
				
				final int batchNumber = 
					((BatchNumber)dataItem)
						.unpack((NonCompoundField)dataField);

				fixmAsterixRecord.setBatchNumber((short)batchNumber);
			}

			// -----------------------------------------------------------------
			// FRN 6:  Data Item I065/040, SDPS Configuration and Status
			// Status of an SDPS.
			// One-Octet fixed length data item with status flags containing 
			// enumerations.
			if(record.getDataField(6) != null) {

				final DataItem dataItem 
				= record.getDataField(6).getAssociatedDataItem();
			
				final int sdpsConfigAndStatus
					= ((BatchNumber)record
						.getDataField(dataItem.getDataItemIdentifier())
						.getAssociatedDataItem())
						.unpack((NonCompoundField)record
								.getDataField(dataItem.getDataItemIdentifier()));
				
				fixmAsterixRecord
					.setConfigurationAndStatus((short)sdpsConfigAndStatus);
			}
			

			// -----------------------------------------------------------------
			// FRN 7:  Data Item I065/050, Service Status Report
			// Report sent by the SDPS related to a service
			// One-Octet fixed length data item, with a status enumeration.
			if(record.getDataField(7) != null) {

				final DataItem dataItem 
					= record.getDataField(7).getAssociatedDataItem();
			
				final int serviceStatusReport 
					= ((ServiceStatusReport)record
						.getDataField(dataItem.getDataItemIdentifier())
						.getAssociatedDataItem())
						.unpack((NonCompoundField)record
								.getDataField(dataItem.getDataItemIdentifier()));
	
				fixmAsterixRecord.setServiceStatusReport((short)serviceStatusReport);
			}
			
			// -----------------------------------------------------------------
			// End of record conversion, process next record or finished. 
			fixmAsterixRecords.add(fixmAsterixRecord);
		}
	}
}
