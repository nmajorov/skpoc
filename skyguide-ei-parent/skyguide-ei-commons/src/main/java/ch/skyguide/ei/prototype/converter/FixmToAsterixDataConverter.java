// -----------------------------------------------------------------------------
// PROJECT:
// Skyguide VC2 Proto
//
// REVISION:
// $Id:$
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------

package ch.skyguide.ei.prototype.converter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.util.ObjectHelper;

import ch.skyguide.communication.dataitem.LoggingLengthPrefixUniversalRepetitiveDataItem;
import ch.skyguide.communication.dataitem.LoggingUniversalExtendedDataItem;
import ch.skyguide.communication.dataitem.LoggingUniversalFixedDataItem;
import ch.skyguide.communication.dataitem.LoggingUniversalRepetitiveDataItem;
import ch.skyguide.communication.rdps.asterix.categories.CartesianTrackPosition;
import ch.skyguide.communication.rdps.asterix.categories.DataCategoryType;
import ch.skyguide.communication.rdps.asterix.categories.IdentificationTag;
import ch.skyguide.communication.rdps.asterix.categories.TimeOfMessage;
import ch.skyguide.communication.rdps.asterix.categories.cat030_032.AsterixInteger;
import ch.skyguide.communication.rdps.asterix.categories.cat062.CalculatedTrackVelocity;
import ch.skyguide.communication.rdps.asterix.categories.cat062.TrackVelocity;
import ch.skyguide.communication.rdps.asterix.categories.cat242.LocalTrackNumber;
import ch.skyguide.communication.rdps.asterix.categories.cat242.MrtSource;
import ch.skyguide.communication.rdps.asterix.categories.cat243.FlowStatus;
import ch.skyguide.communication.rdps.asterix.structure.AsterixDataBlock;
import ch.skyguide.communication.rdps.asterix.structure.AsterixFieldSpecificationType;
import ch.skyguide.communication.rdps.asterix.structure.AsterixMessage;
import ch.skyguide.communication.rdps.asterix.structure.AsterixRecord;
import ch.skyguide.communication.rdps.asterix.structure.AsterixUserApplicationProfileManager;
import ch.skyguide.fixm.extension.flight.enroute.AsterixRecordType;
import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixDataBlock;
import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixMessage;
import ch.skyguide.fixm.extension.flight.enroute.FixmSdpsService;
import ch.skyguide.fixm.extension.flight.enroute.FixmSystemTrackData;
import ch.skyguide.message.structure.GenericDataBlock;
import ch.skyguide.message.structure.GenericRecord;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.dataitem.AbstractUniversalExtendedDataItem;
import ch.skyguide.message.structure.dataitem.AbstractUniversalFixedDataItem;
import ch.skyguide.message.structure.uap.UserApplicationProfile;


//------------------------------------------------------------------------------

//------------------------------------------------------------------------------

/**
 * Convert from FIXM canonical model to Eurocontrol ASTERIX data format.
 * 
 * @author Ross Wilson
 *
 */
public class FixmToAsterixDataConverter {
	
	/**
	 * A public helper method to transform from FIXM Messages to the Asterix
	 * Message Format.
	 *  
	 * @param	inputFixmMessage
	 * 			The FIXM {@code FixmAsterixMessage} to be transformed to the 
	 * 			Asterix format.  The input message may not be {@code null}.
	 * 
	 * @return 	The transformed {@code ByteBuffer} containing the encoded byte
	 * 			stream of the {@code AsterixMessage}.
	 */
	public final ByteBuffer
	transformToAsterix(final FixmAsterixMessage inputFixmMessage) {
	
		// ---------------------------------------------------------------------
		// Validate the precondition
		if(inputFixmMessage == null) {

			throw new IllegalArgumentException("Precondition failure: Input "
					+ "FIXM message is not initialized.");
		}
			
		// ---------------------------------------------------------------------
		// Transform the fixm asterix message to Asterix Message.
        final List<GenericDataBlock> outputAsterixDataBlocks 
        	= new ArrayList<GenericDataBlock>(); 
        
		// TODO: rjw, need to discuss how to get access to the dead letter
        //       queue.
		final List<FixmAsterixDataBlock> candidateDeadLetterDataBlocks 
		= new ArrayList<FixmAsterixDataBlock>();
      
		// ---------------------------------------------------------------------
		// Iterate through the data block from the various Asterix categories.
		for(FixmAsterixDataBlock dataBlock : 
				inputFixmMessage.getFixmAsterixDataBlocks()) {
			
			final short dataCategory = dataBlock.getDataCategory();
			
			switch(dataCategory) {
			
				// -------------------------------------------------------------
				// Transform Asterix CAT062 -> 242
				case 62:
					outputAsterixDataBlocks
						.add(transformFixm62BlockToAsterix242(dataBlock));
				break;

				// -------------------------------------------------------------
				// Transform Asterix CAT065 -> 243
				case 65:
					outputAsterixDataBlocks
						.add(transformFixm65BlockToAsterix243Block(dataBlock));
				break;
				
				default:
					candidateDeadLetterDataBlocks.add(dataBlock);
				break;
			}
		}
		
        final AsterixMessage outputAsterixMessage 
    		= new AsterixMessage(outputAsterixDataBlocks);

        // ---------------------------------------------------------------------
		// Resultant transformed FIXM Canonical Asterix format to Asterix Format.
		return outputAsterixMessage.encode();
	}

	/**
	 * Private helper method to transform an FIXM Asterix CAT062 data format 
	 * to {@code AsterixDataBlock}s to a FIXM {@code AsterixDataBlock}.
	 * 
	 * @pre		dataBlock may not be {@code null}.
	 * 
	 * @param	dataBlock
	 * 			The FIXM {@code AsterixDataBlock} to be converted to Asterix
	 * 			CAT242 format. The dataBlock may not be {@code null}.
	 * 
	 * @return	An {@code AsterixDataBlock} containing the CAT242 data block.
	 */
	private final GenericDataBlock 
	transformFixm62BlockToAsterix242(FixmAsterixDataBlock fixmDataBlock) {
		
		// ---------------------------------------------------------------------
		// Validate preconditions.
		ObjectHelper.notNull(fixmDataBlock, "fixmDataBlock");

		// ---------------------------------------------------------------------
		// Setup and fill the Asterix Data Block.
		final UserApplicationProfile uap = 
				AsterixUserApplicationProfileManager.getInstance()
					.getUserApplicationProfile(DataCategoryType
											.CAT242.getByteRepresentation());
		
		final List<GenericRecord> asterixRecords 
			= new ArrayList<GenericRecord>();
		
		// ---------------------------------------------------------------------
		// Convert the FIXM 65 Records to Asterix 243 Records.
		transformFixm62RecordsToAsterix242(asterixRecords, 
				fixmDataBlock.getFixmAsterixRecords(), uap);
		
		// ---------------------------------------------------------------------
		// Resulting Asterix Data Block.
		int lengthIndicator = 3;  // Initial value of CAT + LEN.
		
		for (GenericRecord record : asterixRecords) {
			
			// Add the length of each encoded record.
			lengthIndicator += record.getFieldLengthIndicator();
		}
		
		return new AsterixDataBlock((short)242, lengthIndicator, asterixRecords);
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
	transformFixm62RecordsToAsterix242(
			final List<GenericRecord> asterix242records,
			   final List<AsterixRecordType> fixmAsterix62Records,
			   final UserApplicationProfile cat242Uap) {
		
		// ---------------------------------------------------------------------
		// Validate preconditions
		ObjectHelper.notNull(asterix242records, "asterix242records");
		ObjectHelper.notNull(fixmAsterix62Records, "fixmAsterix62Records");
		ObjectHelper.notNull(cat242Uap, "cat242Uap");
		
		// ---------------------------------------------------------------------
		// Iterate through asterix records and convert to FIXM format
		for(AsterixRecordType record : fixmAsterix62Records) {
			
			final AsterixFieldSpecificationType fieldSpecification 
				= new AsterixFieldSpecificationType();
			
			//------------------------------------------------------------------
			// Retrieve CAT062 message for transformation.
			final FixmSystemTrackData fixmSystemTrackData 
				= (FixmSystemTrackData)record;

			Map<Integer, DataField> dataFields 
				= new HashMap<Integer, DataField>();
			
			// -----------------------------------------------------------------
			// FRN 1:  I242/010 MRT source
			{
				final MrtSource mvSourcedataItem 
					= (MrtSource)cat242Uap.getDataItem(1);
				
				// default is set Site (1), MV(1) and ARTAS Unit (Flow) is 2.
				dataFields
					.put(1, mvSourcedataItem
						.pack((short)1, (short)1, (short)2));
				
				fieldSpecification.enableField(1);
			}

			// -----------------------------------------------------------------
			// FRN 2:  I242/020, Time of track information, 	1/128 s	F/3
			// Time: taken from I062/070, same Units are 1/128 s
			if (fixmSystemTrackData.isSetTimeOfTrackInformation()) {
				
				final TimeOfMessage timeOfTrackInfoDataItem 
					= (TimeOfMessage)cat242Uap.getDataItem(2);
				
				dataFields
					.put(2, timeOfTrackInfoDataItem.pack((int)fixmSystemTrackData
						.getTimeOfTrackInformation()));

				fieldSpecification.enableField(2);
			}
			
			// -----------------------------------------------------------------
			// FRN 3:  I242/030, Track number, N.A.	F/2
			// Track Number taken from I062/020, copy track number
			
			if (fixmSystemTrackData.isSetTrackNumber()) {
				
				final AsterixInteger trackNumber 
					= (AsterixInteger)cat242Uap.getDataItem(3);
				
				
				dataFields
					.put(3, trackNumber.pack((short)fixmSystemTrackData
						.getTrackNumber()));
				
				fieldSpecification.enableField(3);
			}
			
			// -----------------------------------------------------------------
			// FRN 4:  I242/040, Local track number, N.A.	F/2
			// Always set due to EMDIS to valid and local track number ONE.
			{
				final LocalTrackNumber localTrackNumber
					= (LocalTrackNumber)cat242Uap.getDataItem(4);
				
				// fixed always to track one and valid is true.
				dataFields.put(4, localTrackNumber.pack(1, true));

				fieldSpecification.enableField(4);
			}
			
			// -----------------------------------------------------------------
			// FRN 5:  I242/050, Track status, E/2 + 2
			// Complicated translation based on information in I062/080, refer
			// to notes below.
			// Necessary that I062/080 data is available.
			if(fixmSystemTrackData.isSetTrackStatus() && 
					fixmSystemTrackData.getTrackStatus().isSetValues()) {
			
				LoggingUniversalExtendedDataItem trackStatus 
					= (LoggingUniversalExtendedDataItem)cat242Uap.getDataItem(5);
				
				// List of bytes (under the hood) containing the main and
				// extended fields.
				List<Short> trackStatusStructure 
					= fixmSystemTrackData.getTrackStatus().getValues();

				final byte mainStructure 
					= trackStatusStructure.get(0).byteValue();
				
				// TODO: rjw, complete with correct translation.
				final BitSet bsTrackStatusMainStrut = new BitSet(16);

				bsTrackStatusMainStrut.set(0, true); // Extended 1st Extent.
				bsTrackStatusMainStrut.set(1, false); // U/S.
				bsTrackStatusMainStrut.set(2, false); // OOB.
				bsTrackStatusMainStrut.set(3, ((mainStructure & 0x40) == 0x40)); // SPI.
				bsTrackStatusMainStrut.set(4, false); // EM, decided no emergency
				bsTrackStatusMainStrut.set(5, false); // EM.
				
				if(trackStatusStructure.size()>2) {

					byte thirdExtent = trackStatusStructure.get(3).byteValue();
					if((thirdExtent & 0x80) == 0x80) {
							bsTrackStatusMainStrut.set(6, true); // CST.
					}
				}

				if(trackStatusStructure.size()>1) {

					byte firstExtent = trackStatusStructure.get(1).byteValue();
					if((firstExtent & 0x40) == 0x40) {
						
						bsTrackStatusMainStrut.set(7, true); // TRM.
					}

					if((firstExtent & 0x20) == 0x20) {
				
						bsTrackStatusMainStrut.set(8, true); // CRE.
					}

				}
				
				bsTrackStatusMainStrut.set(9, false); // MIL.
				bsTrackStatusMainStrut.set(10, false); // VFR.
				bsTrackStatusMainStrut.set(11, false); // SIM.
				bsTrackStatusMainStrut.set(12, false); // FPC.
				
				if(trackStatusStructure.size()>2) {

					byte thirdExtent = trackStatusStructure.get(3).byteValue();
					if((thirdExtent & 0x40) == 0x40 &&
						(thirdExtent & 0x20) != 0x20) {
						
						bsTrackStatusMainStrut.set(13, true); // TYP.
						bsTrackStatusMainStrut.set(14, false); // TYP.
					}
					else if((thirdExtent & 0x40) != 0x40 &&
							(thirdExtent & 0x20) == 0x20) {
						
						bsTrackStatusMainStrut.set(13, false); // TYP.
						bsTrackStatusMainStrut.set(14, true); // TYP.
					}
					else {
						
						bsTrackStatusMainStrut.set(13, true); // TYP.
						bsTrackStatusMainStrut.set(14, true); // TYP.
					}
 				}
					
				bsTrackStatusMainStrut
					.set(15, ((mainStructure & 0x80) == 0x80)); // MON.

				final BitSet bsTrackStatusFirstExt = new BitSet(16);
				bsTrackStatusFirstExt.set(0, false); // ACD
				bsTrackStatusFirstExt.set(1, false); // HLD
				bsTrackStatusFirstExt.set(2, false); // FOR
				bsTrackStatusFirstExt.set(3, false); // AAC
				
				if(trackStatusStructure.size()>2) {

					byte thirdExtent = trackStatusStructure.get(3).byteValue();
					bsTrackStatusFirstExt.set(4, ((thirdExtent & 0x10) == 0x10)); // MSF
				}
				
				bsTrackStatusFirstExt.set(5, false); // QNHM
				bsTrackStatusFirstExt.set(6, false);  // QNHM
				bsTrackStatusFirstExt.set(7, false);  // Reserved
				bsTrackStatusFirstExt.set(8, false);  // Reserved
				bsTrackStatusFirstExt.set(9, false);  // Reserved
				bsTrackStatusFirstExt.set(10, false);  // Reserved
				bsTrackStatusFirstExt.set(11, false);  // Reserved
				bsTrackStatusFirstExt.set(12, false);  // Reserved
				bsTrackStatusFirstExt.set(13, false);  // Reserved
				bsTrackStatusFirstExt.set(14, false);  // Reserved
				bsTrackStatusFirstExt.set(15, false);  // Reserved
				
				final List<Number> list = new ArrayList<Number>();
				final ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
				buffer.put(bsTrackStatusMainStrut.toByteArray());
				buffer.putShort((short)0);  // to ensure at least 2 bytes.
				
				buffer.flip();
				list.add(buffer.getShort());
				buffer.flip();
				buffer.put(bsTrackStatusFirstExt.toByteArray());
				buffer.putShort((short)0);  // to ensure at least 2 bytes.
				buffer.flip();
				list.add(buffer.getShort());
				
				dataFields.put(5, trackStatus.pack(list));

				fieldSpecification.enableField(5);
			}
			

			// -----------------------------------------------------------------
			// FRN 6:  I242/060, APW/DAIW track status, N.A.	E/2 + 2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(6);
			
			// -----------------------------------------------------------------
			// FRN 7:  I242/070, MSAW track status, N.A.	E/2 + 2
			// Based on information from J.P. Shepherd, clear the alert status.
			{
				final AbstractUniversalExtendedDataItem msawTrackStatus 
					= (AbstractUniversalExtendedDataItem)cat242Uap.getDataItem(7);

				final BitSet setBits = new BitSet(16);
				setBits.clear(15); // ALA
				setBits.clear(14); // ALA
				setBits.clear(13); // DISABLED
				final ByteBuffer bufMsawTrackStatus 
					= ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
				bufMsawTrackStatus
					.putShort((short)0).flip(); // Ensure at least 2 bytes
				
				List<Number> list = new ArrayList<Number>();
				list.add(bufMsawTrackStatus.getShort());
				dataFields.put(7, msawTrackStatus.pack(list));
				
				fieldSpecification.enableField(7);
			}
			
			// -----------------------------------------------------------------
			// FRN 8:  I242/080, STCA track status, N.A.	E/2 + 2
			// Based on information from J.P. Shepherd, clear the alert status.
			{
				final AbstractUniversalExtendedDataItem stcaTrackStatus 
					= (AbstractUniversalExtendedDataItem)cat242Uap.getDataItem(8);

				final BitSet setBits = new BitSet(16);
				setBits.clear(15); // ALA
				setBits.clear(14); // ALA
				setBits.clear(13); // DISABLED
				setBits.set(12); // SRV-> STCA Track RVSM State

				final ByteBuffer bufStcaTrackStatus 
					= ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
				bufStcaTrackStatus
					.put(setBits.toByteArray()).flip();
				
				final List<Number> list = new ArrayList<Number>();
				list.add(bufStcaTrackStatus.getShort());
				dataFields.put(8, stcaTrackStatus.pack(list));
				
				fieldSpecification.enableField(8);
			}
			
			// -----------------------------------------------------------------
			// FRN 9:  I242/090, Flight plan status, N.A.	E/2 + 2 
			// Advised initially RVSM (1), AOC (0)-Alloc, Match POSC (1), COR (1),
			// PRCO (0), PRCA (0), COF (0), ROF (0), AADMM (No Indicator)
			{
				final AbstractUniversalExtendedDataItem flightPlanStatus 
					= (AbstractUniversalExtendedDataItem)cat242Uap.getDataItem(9);

				final BitSet bsMainStructure = new BitSet(16);
				bsMainStructure.set(14); // RVSM
				bsMainStructure.clear(4); // AOC (0, 1, 2, 3), default 1
				bsMainStructure.clear(3); // AOC, setting alloc (0).
				bsMainStructure.set(2, false); // Advised setting (1)
				bsMainStructure.set(1); // MSM (0, 1, 2, 3), default 1 
				bsMainStructure.set(0); // Fx

				final BitSet bsFirstExtent = new BitSet(16);
				bsFirstExtent.set(15); // POSC
				bsFirstExtent.set(14); // COR
				bsFirstExtent.set(13, false); // PRCO
				bsFirstExtent.set(12, false); // PRCA
				bsFirstExtent.set(11, false); // COF
				bsFirstExtent.set(10, false); // ROF
				bsFirstExtent.set(9, false); // AADM
				bsFirstExtent.set(8, false); // ROFM
				bsFirstExtent.clear(0); // Fx

				final List<Number> list = new ArrayList<Number>();
				final ByteBuffer bufFlightPlanTrackStatus 
					= ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
				bufFlightPlanTrackStatus
					.put(bsMainStructure.toByteArray()).flip();
				list.add(bufFlightPlanTrackStatus.getShort());
				bufFlightPlanTrackStatus.flip();
				
				bufFlightPlanTrackStatus
					.put(bsFirstExtent.toByteArray()).flip();
				list.add(bufFlightPlanTrackStatus.getShort());
				
				dataFields.put(9, flightPlanStatus.pack(list));

				fieldSpecification.enableField(9);
			}
			
			// -----------------------------------------------------------------
			// FRN 10:  I242/100, Track position in Cartesian coordinates, 
			// 1/4096 NM	F/8
			// Taken from I62/100 with 0.5m units, therefore to m -> Nm.
			if (fixmSystemTrackData.isSetCalculatedTrackPositionCartesian()) {
				
				final CartesianTrackPosition trackPosition 
					= (CartesianTrackPosition)cat242Uap.getDataItem(10);
				
				List<Double> positions 
					= fixmSystemTrackData.getCalculatedTrackPositionCartesian()
						.getLocation().getPos();
				dataFields
					.put(10, trackPosition.pack((int)(positions.get(0).intValue()*0.5/1852*4096), 
											    (int)(positions.get(1).intValue()*0.5/1852*4096)));
			
				fieldSpecification.enableField(10);
			}
										
			// -----------------------------------------------------------------
			// FRN 11:  I242/110, Track area membership, N.A.	F/2
			// As advised: ACC, APP and UAC
			{
				final AbstractUniversalFixedDataItem trackArea 
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(11);

				final BitSet setBits = new BitSet(16);
				setBits.set(15); // ACC
				setBits.set(14); // APP
				setBits.clear(13); // MIL
				setBits.clear(12); // TDI
				setBits.set(11); // UAC
				setBits.clear(10); // TARMAC

				final ByteBuffer bufTrackArea = ByteBuffer.allocate(2);
				bufTrackArea
					.put(setBits.toByteArray()[1])
					.put(setBits.toByteArray()[0])
					.flip();

				dataFields.put(11, trackArea.pack(bufTrackArea));
				
				fieldSpecification.enableField(11);
			}
			
			// -----------------------------------------------------------------
			// FRN 12:  I242/120, Track velocity in Cartesian coordinates, 
			// 1/16384 NM/s	F/4
			// Track velocity Cartesian coordinates: copied from I062/RE/TVS 
			// if filled else from I062/185.
			// Initially taken from I062/185 with units 0.25 m/s 
			if (fixmSystemTrackData.isSetCalculatedTrackVelocityCartesian()) {
				
				final CalculatedTrackVelocity trackVelocityCartesian
					= (CalculatedTrackVelocity)cat242Uap.getDataItem(12);
				
				// TODO: rjw, check whether we can use the I62 RE TVS value
				fieldSpecification.enableField(12);
				final TrackVelocity trackVelocity 
					= new TrackVelocity((short)(fixmSystemTrackData
										.getCalculatedTrackVelocityCartesian()
										.getVx()*0.25/1852*16384),
										(short)(fixmSystemTrackData
										.getCalculatedTrackVelocityCartesian()
										.getVy()*0.25/1852*16384), 
										1);  // use one as factor
				
				dataFields.put(12, trackVelocityCartesian.pack(trackVelocity));

				fieldSpecification.enableField(12);
			}
			
			// -----------------------------------------------------------------
			// FRN 13:  I242/121, Track velocity in polar coordinates, 
			// 1/16384 NM/s 360/65536 °	F/4 
			// Track velocity in polar coordinates: computed from I062/RE/TVS 
			// if filled else from I062/185.
			// Initially taken from I062/185
			if (fixmSystemTrackData.isSetCalculatedTrackVelocityCartesian()) {
				
				final AbstractUniversalFixedDataItem velocityPolarCoordinates
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(13);
				
				// TODO: rjw, check whether we can use the I62 RE TVS value
				final TrackVelocity trackVelocity 
				= new TrackVelocity((short)(fixmSystemTrackData
									.getCalculatedTrackVelocityCartesian()
									.getVx()),
									(short)(fixmSystemTrackData
									.getCalculatedTrackVelocityCartesian()
									.getVy()), 
									0.25);  // use 0.25 ms-1 from I62.

				final ByteBuffer bufPolar = ByteBuffer.allocate(4);
				bufPolar.putShort((short)(trackVelocity.getMagnitude()/1852*16384));
				bufPolar.putShort((short)(trackVelocity.getAngle()*65536));
				bufPolar.flip();
				dataFields.put(13, velocityPolarCoordinates.pack(bufPolar));

				fieldSpecification.enableField(13);
			}
			
			// -----------------------------------------------------------------
			// FRN 14:  I242/130, Track altitude, 5 ft	F/4
			// Taken from  Data Item I062/136 Measured Flight Level, units 0.25 FL
			if (fixmSystemTrackData.isSetMeasuredFlightLevel()) {
				
				final AbstractUniversalFixedDataItem trackAltitude
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(14);
				
				final BitSet setBits = new BitSet(16);
				setBits.set(13);  // TL, default below.
				setBits.clear(14); // Garbling
				setBits.set(15); // Valid set.

				final ByteBuffer bufTrackAlt 
					= ByteBuffer.allocate(trackAltitude.getSizeFactor());
				ByteBuffer temp 
					= ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
				temp.put(setBits.toByteArray()).flip();
				bufTrackAlt.putShort(temp.getShort());
				bufTrackAlt
					.putShort((short)(fixmSystemTrackData
						.getMeasuredFlightLevel().shortValue()*0.25*100/5));
				bufTrackAlt.flip();
				dataFields.put(14, trackAltitude.pack(bufTrackAlt));

				fieldSpecification.enableField(14);
			}
			
			// -----------------------------------------------------------------
			// FRN 15:  I242/140, QNH area membership, 	N.A.	F/2
			{
				final AbstractUniversalFixedDataItem qnhAreaMembership
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(15);
				
				final BitSet setBits = new BitSet(16);
				setBits.clear(13);	// BRN
				setBits.set(14); 	// ZRH 
				setBits.clear(15); 	// GVA

				final ByteBuffer bufQnhAreaMembership 
					= ByteBuffer.allocate(qnhAreaMembership.getSizeFactor())
					.order(ByteOrder.LITTLE_ENDIAN);
				
				bufQnhAreaMembership.put(setBits.toByteArray());
				bufQnhAreaMembership.flip();
				short temp = bufQnhAreaMembership.getShort();
				bufQnhAreaMembership.flip();
				bufQnhAreaMembership.order(ByteOrder.BIG_ENDIAN);
				bufQnhAreaMembership.putShort(temp);
				bufQnhAreaMembership.flip();
				
				dataFields.put(15, qnhAreaMembership.pack(bufQnhAreaMembership));

				fieldSpecification.enableField(15);
			}
			
			// -----------------------------------------------------------------
			// FRN 16:  I242/150, Rate of climb/descent, 2-10 FL/s	F/2
			// Taken from Data Item I062/220 Calculated Rate Of Climb/Descent
			// with units 6.25 feet/minute
			if (fixmSystemTrackData.isSetCalculatedRateOfClimb()) {
				
				final AbstractUniversalFixedDataItem rateOfClimbDescent
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(16);
				
				final ByteBuffer bufRateOfClimbDescent 
				= ByteBuffer.allocate(rateOfClimbDescent.getSizeFactor());
				
				bufRateOfClimbDescent
				.putShort((short)(fixmSystemTrackData
						.getCalculatedRateOfClimb().shortValue()*6.25/6000*1024)).flip();
				
				dataFields
					.put(16, rateOfClimbDescent.pack(bufRateOfClimbDescent));

				fieldSpecification.enableField(16);
			}
			
			// -----------------------------------------------------------------
			// FRN 17:  I242/160, Rate of turn, ¼ °/s	F/2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(17);
			
			// -----------------------------------------------------------------
			// FRN 18:	I242/170, Mode of flight, N.A.	F/2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(18);

			// -----------------------------------------------------------------
			// FRN 19:	I242/180, Track mode 3/A, N.A.	F/2
			if (fixmSystemTrackData.isSetTrackMode3ACode()) {
				
				final AbstractUniversalFixedDataItem trackMode3A
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(19);
				
				final ByteBuffer bufTrackMode3A 
				= ByteBuffer.allocate(trackMode3A.getSizeFactor());
				
				final short fixmTrackMode3A 
					= fixmSystemTrackData.getTrackMode3ACode().shortValue(); 
				// TODO: rjw, complete with the bit shifting.
				bufTrackMode3A.putShort(fixmTrackMode3A).flip();
				
				dataFields.put(19, trackMode3A.pack(bufTrackMode3A));

				fieldSpecification.enableField(19);
			}
			
			// -----------------------------------------------------------------
			// FRN 20	I242/190, Emergency mode 3/A, N.A.	F/2
			// TODO: rjw, not handling emergency mode 3/A code.
			fieldSpecification.disableField(20);
			
			// -----------------------------------------------------------------
			// FRN 21	I242/200, Radar identification tag, N.A.	F/2
			if(fixmSystemTrackData.isSetMeasuredInformation() 
					&& fixmSystemTrackData.getMeasuredInformation()
						.isSetDataSourceIdentifier()) {
				
				final IdentificationTag identificationTag
					= (IdentificationTag)cat242Uap.getDataItem(21);
				
				final DataField idTagField 
					= identificationTag
						.pack((byte)fixmSystemTrackData
								.getMeasuredInformation()
								.getDataSourceIdentifier().getSystemAreaCode(), 
							  (byte)fixmSystemTrackData.getMeasuredInformation()
								.getDataSourceIdentifier()
								.getSystemIdentificationCode());
				
				dataFields.put(21, idTagField);

				fieldSpecification.enableField(21);
			}
			
			// -----------------------------------------------------------------
			// FRN 22:	I242/201, Radar identification tag mnemonic, ASCII	F/2
			fieldSpecification.disableField(22);

			// -----------------------------------------------------------------
			// FRN 23:	I242/210, ACD sectors, N.A.	R/1 + nx1
			{
				// TODO: rjw, check with J.P. Shepherd
				// TODO: rjw, debug repetitive data fields
//				final AbstractUniversalRepetitiveDataItem acdSectors 
//					= (AbstractUniversalRepetitiveDataItem)cat242Uap
//						.getDataItem(23);
//				
//				fieldSpecification.enableField(23);
//				final List<Number> sectors = new ArrayList<Number>();
//				sectors.add(Byte.valueOf((byte)9));
//				
//				dataFields.put(23, acdSectors.pack(sectors));
				fieldSpecification.disableField(23);
			}

			// -----------------------------------------------------------------
			// FRN 24:	I242/220, HLD sectors, N.A.	R/1 + nx1
			{
				// TODO: rjw, check with J.P. Shepherd
				// TODO: rjw, debug repetitive data fields
//				final AbstractUniversalRepetitiveDataItem acdSectors 
//					= (AbstractUniversalRepetitiveDataItem)cat242Uap
//						.getDataItem(24);
//				
//				fieldSpecification.enableField(24);
//				final List<Number> sectors = new ArrayList<Number>();
//				sectors.add(Byte.valueOf((byte)9));
//				
//				dataFields.put(24, acdSectors.pack(sectors));
				fieldSpecification.disableField(24);
			}
			
			// -----------------------------------------------------------------
			// FRN 25:	I242/230, APW/DAIW sectors, N.A.	R/1 + nx1
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(25);

			// -----------------------------------------------------------------
			// FRN 26:	I242/240, APW/DAIW vector, 1/16384 NM/s	F/4
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(26);

			// -----------------------------------------------------------------
			// FRN 27:	I242/250, MSAW sectors, N.A.	R/1 + nx1
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(27);

			// -----------------------------------------------------------------
			// FRN 28:	I242/260, MSAW vector, 1/16384 NM/s	F/4
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(28);

			// -----------------------------------------------------------------
			// FRN 29:	I242/270, STCA sectors, N.A.	R/1 + nx1
			fieldSpecification.disableField(29);
			
			// -----------------------------------------------------------------
			// FRN 30:	I242/280, STCA vector, 1/16384 NM/s	F/4
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(30);

			// -----------------------------------------------------------------
			// FRN 31:	I242/290, Ages (MV internal use only), N.A.	F/2
			// TODO: rjw, check with J.P. Shepherd concerning disabling ages.
			fieldSpecification.disableField(31);
			
			// -----------------------------------------------------------------
			// FRN 32:	I242/185, Track previous mode 3/A, N.A.	F/2
			// TODO: rjw, check with J.P. Shepherd concerning disabling previous mode 3/A.
			fieldSpecification.disableField(32);
			
			// -----------------------------------------------------------------
			// FRN 33:	I242/300, Track mode C, 5 ft	F/4
			// Taken from  Data Item I062/136 Measured Flight Level, units 0.25 FL
			if (fixmSystemTrackData.isSetMeasuredFlightLevel()) {
				
				final AbstractUniversalFixedDataItem trackModeC
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(33);
				
				final ByteBuffer bufTrackModeC 
					= ByteBuffer.allocate(trackModeC.getSizeFactor());
				bufTrackModeC.putShort((short)0x8000);
				bufTrackModeC
					.putShort((short)(fixmSystemTrackData
						.getMeasuredFlightLevel().shortValue()*0.25*100/5));
				bufTrackModeC.flip();
				dataFields.put(33, trackModeC.pack(bufTrackModeC));

				fieldSpecification.enableField(33);
			}

			// -----------------------------------------------------------------
			// FRN 34:	I242/9100, Reserved for future use	N.A.	F/4
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(34);

			// -----------------------------------------------------------------
			// FRN 35:	I242/9200, Reserved for future use	N.A.	E/2 + 2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(35);
			
			// -----------------------------------------------------------------
			// FRN 36:	I242/9200, Reserved for future use	N.A.	E/2 + 2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(36);
			
			// -----------------------------------------------------------------
			// FRN 37:	I242/9300, Reserved for future use	N.A.	R/1 + nx1
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(37);
			
			// -----------------------------------------------------------------
			// FRN 38:	I242/9300, Reserved for future use	N.A.	R/1 + nx1
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(38);
			
			// -----------------------------------------------------------------
			// FRN 39:	I242/9400, Reserved for future use	N.A.	R/1 + nx2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(39);
			
			// -----------------------------------------------------------------
			// FRN 40:	I242/9400, Reserved for future use	N.A.	R/1 + nx2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(40);
			
			// -----------------------------------------------------------------
			// FRN 41:	I242/9500, Reserved for future use	N.A.	Ex
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(41);
			
			// -----------------------------------------------------------------
			// FRN 42:	I242/9500, Reserved for future use	N.A.	Ex
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(42);
			
			// -----------------------------------------------------------------
			// FRN 43:	I242/9500, Reserved for future use	N.A.	Ex
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(43);
			
			// -----------------------------------------------------------------
			// FRN 44:	I242/9500, Reserved for future use	N.A.	Ex
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(44);
			
			// -----------------------------------------------------------------
			// FRN 45:	I242/9500, Reserved for future use	N.A.	Ex
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(45);
			
			// -----------------------------------------------------------------
			// FRN 46:	I242/500, Callsign, ASCII	Ex
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetTargetIdentification()) {
				
				final LoggingLengthPrefixUniversalRepetitiveDataItem callsign
					= (LoggingLengthPrefixUniversalRepetitiveDataItem)cat242Uap.getDataItem(46);
			
				final String identification 
					= fixmSystemTrackData.getAircraftDerivedData()
						.getTargetIdentification().getValue();
				
				final ByteBuffer bufTargetIdentification 
					= ByteBuffer.allocate(8);
				
				bufTargetIdentification
					.put(identification.getBytes(Charset.forName("US-ASCII")));
				bufTargetIdentification.flip();
				dataFields
					.put(46, callsign.pack(bufTargetIdentification.array()));

				fieldSpecification.enableField(46);
			}
			
			// -----------------------------------------------------------------
			// FRN 47:	I242/510, Allocated/Current physical sector 
			// 			(A/C-sector; A/C-PS), N.A.	F/1
			// Hard coded for M4 (9).
			{
				final AsterixInteger allocatedCurrentPhysicalSectors 
					= (AsterixInteger)cat242Uap.getDataItem(47);
				
				dataFields.put(47, allocatedCurrentPhysicalSectors.pack((byte)9));

				fieldSpecification.enableField(47);
			}
			
			// -----------------------------------------------------------------
			// FRN 48:	I242/520, Allocated/Current logical sector 
			//			(A/C-sector; A/C-LS), N.A.	F/1
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(48);
			
			// -----------------------------------------------------------------
			// FRN 49:	I242/530, A/C-sector's consoles, N.A.	R/1 + nx1
			// Hard coded consoles from J.P. Shepherd, A/C Sector Consoles.
			{
				LoggingUniversalRepetitiveDataItem repetitiveItem
					= (LoggingUniversalRepetitiveDataItem)cat242Uap.getDataItem(49);
				
				byte[] list = new byte[3];
				list[0] =((byte)20);
				list[1] =((byte)21);
				list[2] =((byte)29);
				dataFields.put(49, repetitiveItem.pack(list));
				fieldSpecification.enableField(49);
			}
			
			// -----------------------------------------------------------------
			// FRN 50	I242/540, Receiving physical sector (R-sector; R-PS), N.A.	F/1
			{
				// advised not to send, J.P. Shepherd, Receiving Physical Sectors.
				fieldSpecification.disableField(50);
			}
			
			// -----------------------------------------------------------------
			// FRN 51	I242/550, Receiving logical sector (R-sector; R-LS), N.A.	F/1
			{
				// advised not to send,  J.P. Shepherd, Receiving Logical Sectors
				fieldSpecification.disableField(51);
			}
			
			// -----------------------------------------------------------------
			// FRN 52	I242/560, R-sector's console, N.A.	R/1 + nx1
			{
				// advised not to send,  J.P. Shepherd, Receiving Logical Sectors
				fieldSpecification.disableField(52);
			}
			
			// -----------------------------------------------------------------
			// FRN 53	I242/570, SI field, N.A.	F/2
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(53);
			
			// -----------------------------------------------------------------
			// FRN 54	I242/580, Aircraft type, ASCII	F/4
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(54);
			
			// -----------------------------------------------------------------
			// FRN 55	I242/590, Wake turbulence category, ASCII	F/1
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(55);

			// -----------------------------------------------------------------
			// FRN 56	I242/600, Aircraft company, ASCII	Ex
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(56);

			// -----------------------------------------------------------------
			// FRN 57	I242/610, PTID1, ASCII	Ex
			// TODO: rjw, enrich with flight information when connected to repository.
			// TODO: rjw, check with J.P. Shepherd if ok not to send.
			fieldSpecification.disableField(57);

			// -----------------------------------------------------------------
			// FRN 58	I242/620, PTID2, ASCII	Ex
			// Not Implemented in I242 v2.0
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(58);

			// -----------------------------------------------------------------
			// FRN 59	I242/630, PTID3, ASCII	Ex
			// Not Implemented in I242 v2.0
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(59);

			// -----------------------------------------------------------------
			// FRN 60	I242/640, PTID4, ASCII	Ex
			// Not Implemented in I242 v2.0
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(60);

			// -----------------------------------------------------------------
			// FRN 61	I242/650, PTID5, ASCII	Ex
			// TODO: rjw, enrich with flight information when connected to repository.
			// TODO: rjw, check with J.P. Shepherd if ok not to send.
			fieldSpecification.disableField(61);

			// -----------------------------------------------------------------
			// FRN 62	I242/660, FL1, ¼ FL	F/2
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(62);
			
			// -----------------------------------------------------------------
			// FRN 63	I242/670, FL2, ¼ FL	F/2
			// Not Implemented in I242 v2.0
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(63);

			// -----------------------------------------------------------------
			// FRN 64	I242/680, FL3, ¼ FL	F/2
			// Not Implemented in I242 v2.0
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(64);

			// -----------------------------------------------------------------
			// FRN 65	I242/690, FL4, ¼ FL	F/2
			// Not Implemented in I242 v2.0
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(65);

			// -----------------------------------------------------------------
			// FRN 66	I242/700, FL5,¼ FL	F/2
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(66);
			
			// -----------------------------------------------------------------
			// FRN 67	I242/710, CFL, ¼ FL	F/2
			// TODO: rjw, enrich with flight information when connected to repository.
			// TODO: rjw, CFL needs to be transmitted for scenario II.
			fieldSpecification.disableField(67);
			
			// -----------------------------------------------------------------
			// FRN 68	I242/720, ADEP, ASCII	F/4
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(68);
			
			// -----------------------------------------------------------------
			// FRN 69	I242/730, ADES, ASCII	F/4
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(69);
			
			// -----------------------------------------------------------------
			// FRN 70	I242/740, Allocated SSR code, N.A.	R/1 + nx2
			// TODO: rjw, enrich with flight information when connected to repository.
			fieldSpecification.disableField(70);
			
			// -----------------------------------------------------------------
			// FRN 71	I242/750, Allocated heading, 360/65536 °	F/2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(71);

			// -----------------------------------------------------------------
			// FRN 72	I242/760, ADD Aircraft address, N.A.	F/3
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetTargetAddress()) {
				
				final AbstractUniversalFixedDataItem targetAddress
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(72);
			
				final ByteBuffer bufTargetAddress 
					= ByteBuffer.allocate(targetAddress.getSizeFactor());
				final int fixmTargetAddress 
					= fixmSystemTrackData
						.getAircraftDerivedData().getTargetAddress().intValue();
				
				byte[] byteArrayTgtAddress = new byte[3];
				byteArrayTgtAddress[2] = (byte)((fixmTargetAddress<<24)>>>24);
				byteArrayTgtAddress[1] = (byte)((fixmTargetAddress<<16)>>>24);
				byteArrayTgtAddress[0] = (byte)((fixmTargetAddress<<8)>>24);
				bufTargetAddress.put(byteArrayTgtAddress);
				
				dataFields.put(72, targetAddress.pack(bufTargetAddress));

				fieldSpecification.enableField(72);
			}

			// -----------------------------------------------------------------
			// FRN 73	I242/770, ADD Aircraft identification, ASCII	F/8
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetTargetIdentification()) {
				
				final AbstractUniversalFixedDataItem targetIdentification
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(73);
			
				final String identification 
					= fixmSystemTrackData.getAircraftDerivedData()
						.getTargetIdentification().getValue();
				
				final ByteBuffer bufTargetIdentification 
					= ByteBuffer.allocate(targetIdentification.getSizeFactor());
				
				bufTargetIdentification
					.put(identification.getBytes(Charset.forName("US-ASCII")));
				bufTargetIdentification.flip();
				dataFields
					.put(73, targetIdentification.pack(bufTargetIdentification));

				fieldSpecification.enableField(73);
			}

			// -----------------------------------------------------------------
			// FRN 74	I242/780, ADD Communications/ACAS Capability and Flight Status, N.A.	F/2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(74);

			// -----------------------------------------------------------------
			// FRN 75	I242/790, ADD Magnetic Heading, 360/65536 °	F/2
			// Taken from Data Item I062/380 Aircraft Derived Data, 
			// Magnetic Heading with 360° / 2^16 (65536) @ 0.0055°
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetMagneticHeading()) {
				
				final AbstractUniversalFixedDataItem magneticHeading
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(75);
			
				final ByteBuffer bufAddMagneticHeading 
					= ByteBuffer.allocate(magneticHeading.getSizeFactor());
				
				bufAddMagneticHeading
					.putShort((short)(fixmSystemTrackData.getAircraftDerivedData()
						.getMagneticHeading().shortValue()));
				bufAddMagneticHeading.flip();
				dataFields
					.put(75, magneticHeading.pack(bufAddMagneticHeading));
				fieldSpecification.enableField(75);
			}
			
			// -----------------------------------------------------------------
			// FRN 76	I242/800, ADD Selected Altitude, 5 ft	F/2
			// Taken from Data Item I062/380 Aircraft Derived Data, 
			// Selected Altitude with 25ft units
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetSelectedAltitude()) {
				
				final AbstractUniversalFixedDataItem selectedAltitude
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(76);
			
				final ByteBuffer bufAddSelectedAltitude 
					= ByteBuffer.allocate(selectedAltitude.getSizeFactor());
				
				// TODO: check conversion factors.
				// Upper three bits are Source Info Indicator and Source.
				// MV doesn't require the information, therefore discard.
				bufAddSelectedAltitude
					.putShort((short)((fixmSystemTrackData.getAircraftDerivedData()
						.getSelectedAltitude().shortValue()<<3>>>3)*25/5));
				bufAddSelectedAltitude.flip();
				dataFields
					.put(76, selectedAltitude.pack(bufAddSelectedAltitude));

				fieldSpecification.enableField(76);
			}
			
			// -----------------------------------------------------------------
			// FRN 77	I242/810, ADD ACAS Resolution Advisory Report, N.A.	F/2
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetAcasResolutionAdvisoryReport()) {
				
				final AbstractUniversalFixedDataItem acasResolutionAdvisoryReport
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(77);
			
				final ByteBuffer bufAddAcasResolutionAdvisoryReport 
					= ByteBuffer.allocate(acasResolutionAdvisoryReport
							.getSizeFactor());
				
				final long fixmAcasResAdvReport 
					= fixmSystemTrackData
						.getAircraftDerivedData()
						.getAcasResolutionAdvisoryReport()
						.getValue().getValue().longValue();
				
				byte[] byteArrayAcasResAdvReprot = new byte[7];
				byteArrayAcasResAdvReprot[6] = (byte)((fixmAcasResAdvReport<<24)>>>24);
				byteArrayAcasResAdvReprot[5] = (byte)((fixmAcasResAdvReport<<24)>>>24);
				byteArrayAcasResAdvReprot[4] = (byte)((fixmAcasResAdvReport<<24)>>>24);
				byteArrayAcasResAdvReprot[3] = (byte)((fixmAcasResAdvReport<<24)>>>24);
				byteArrayAcasResAdvReprot[2] = (byte)((fixmAcasResAdvReport<<24)>>>24);
				byteArrayAcasResAdvReprot[1] = (byte)((fixmAcasResAdvReport<<16)>>>24);
				byteArrayAcasResAdvReprot[0] = (byte)((fixmAcasResAdvReport<<8)>>24);

				bufAddAcasResolutionAdvisoryReport.put(byteArrayAcasResAdvReprot);
				bufAddAcasResolutionAdvisoryReport.flip();
				dataFields
					.put(77, acasResolutionAdvisoryReport
							.pack(bufAddAcasResolutionAdvisoryReport));
				
				fieldSpecification.enableField(77);
			}
			
			// -----------------------------------------------------------------
			// FRN 78	I242/820, ADD Vertical Rate, 2-10 FL/s	F/2
			// Taken from Data Item I062/380 Aircraft Derived Data, 
			// Barometric Vertical Rate with 6.25 feet/minute
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetBarometricVerticalRate()) {
				
				final AbstractUniversalFixedDataItem verticalRate
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(78);
			
				final ByteBuffer bufVerticalRate 
					= ByteBuffer.allocate(verticalRate.getSizeFactor());
				
				bufVerticalRate
					.putShort((short)(fixmSystemTrackData
						.getAircraftDerivedData()
						.getBarometricVerticalRate().shortValue()*6.25/6000*1024));
				
				bufVerticalRate.flip();
				
				dataFields.put(78, verticalRate.pack(bufVerticalRate));

				fieldSpecification.enableField(78);
			}
			
			// -----------------------------------------------------------------
			// FRN 79	I242/830, ADD Roll Angle, 360/65536 °	F/2
			// Taken from Data Item I062/380 Aircraft Derived Data, 
			// Roll Angle with 0.01 degree and -180 < range < 180
			// One to one copy
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetRollAngle()) {
				
				final AbstractUniversalFixedDataItem rollAngle
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(79);
				
				final ByteBuffer bufRollAngle 
					= ByteBuffer.allocate(rollAngle.getSizeFactor());
				
				bufRollAngle
					.putShort(fixmSystemTrackData
						.getAircraftDerivedData()
						.getRollAngle().shortValue());
				
				bufRollAngle.flip();
				
				dataFields.put(79, rollAngle.pack(bufRollAngle));

				fieldSpecification.enableField(79);
			}
			
			// -----------------------------------------------------------------
			// FRN 80	I242/840, ADD Ground Speed, 1/16384 NM/s	F/2
			// Taken from I062/380, Aircraft Derived Data, Ground Speed
			// (LSB) = 2-14 NM/s @ 0.22 kt	-2 NM/s <= Ground Speed < 2 NM/s
			// Therefore one to one copy.
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetGroundSpeed()) {
				
				final AbstractUniversalFixedDataItem groundSpeed
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(80);
				
				final ByteBuffer bufGroundSpeed 
					= ByteBuffer.allocate(groundSpeed.getSizeFactor());
				
				bufGroundSpeed
					.putShort(fixmSystemTrackData
						.getAircraftDerivedData()
						.getGroundSpeed().shortValue());
				
				bufGroundSpeed.flip();
				
				dataFields.put(80, groundSpeed.pack(bufGroundSpeed));
			
				fieldSpecification.enableField(80);
			}
			
			// -----------------------------------------------------------------
			// FRN 81	I242/850, ADD Indicated Airspeed, 1/16384 NM/s	F/2
			// Taken from I062/380, Aircraft Derived Data, Indicated Airspeed,
			// 0 Kt <= Indicated Airspeed <= 1100 Kt	(LSB) = 1 Kt
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetIndicatedAirspeed()) {
				
				final AbstractUniversalFixedDataItem indicatedAirSpeed
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(81);
				
				final ByteBuffer bufIndicatedAirspeed 
					= ByteBuffer.allocate(indicatedAirSpeed.getSizeFactor());
				
				bufIndicatedAirspeed
					.putShort((short)(fixmSystemTrackData
						.getAircraftDerivedData()
						.getIndicatedAirspeed().shortValue()/3600*16384));
				
				bufIndicatedAirspeed.flip();
				
				dataFields.put(81, indicatedAirSpeed.pack(bufIndicatedAirspeed));
				
				fieldSpecification.enableField(81);
			}
			
			// -----------------------------------------------------------------
			// FRN 82	I242/860, ADD Mach Number, N.A.	F/2
			// Taken from I062/380, Aircraft Derived Data, MACH Number,
			// 0 <= Mach Number <= 4.096 	(LSB) = Mach 0.008
			// One to one copy.
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetMachNumber()) {
				
				final AbstractUniversalFixedDataItem machNumber
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(82);
			
				final ByteBuffer bufMachNumber 
					= ByteBuffer.allocate(machNumber.getSizeFactor());
				
				bufMachNumber
					.putShort(fixmSystemTrackData
						.getAircraftDerivedData()
						.getMachNumber().shortValue());
				
				bufMachNumber.flip();
				
				dataFields.put(82, machNumber.pack(bufMachNumber));

				fieldSpecification.enableField(82);
			}
			
			// -----------------------------------------------------------------
			// FRN 83	I242/870, Wind in polar coordinates, 1/16384 NM/s 
			//			360/65536 °	F/4
//			if(fixmSystemTrackData.isSetAircraftDerivedData() 
//					&& fixmSystemTrackData.getAircraftDerivedData()
//						.isSetMetData()) {
//				
//				final AbstractUniversalFixedDataItem metData
//					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(83);
//			
//				
//				final ByteBuffer bufMetData 
//					= ByteBuffer.allocate(metData.getSizeFactor());
//
//				// TODO: rjw, complete meteo data transmission.
//				bufMetData
//					.putShort(fixmSystemTrackData
//						.getAircraftDerivedData()
//						.getMetData().getValue());
//				
//				bufMetData.flip();
//				
//				dataFields.put(83, metData.pack(bufMetData));
//			
//				fieldSpecification.enableField(83);
//			}
			fieldSpecification.disableField(83);

			// -----------------------------------------------------------------
			// FRN 84	I242/880, Previous Callsign, ASCII	Ex
			// Only utilize when I242/500 is changed on association with another
			// flight track.  Currently for prototype we will never send the
			// data.
			fieldSpecification.disableField(84);
			
			// -----------------------------------------------------------------
			// FRN 85	I242/890, C-ATSU, N.A.	F/4
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(85);

			// -----------------------------------------------------------------
			// FRN 86	I242/900, Transmitting physical sector (T-sector; T-PS), N.A.	F/1
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(86);

			// -----------------------------------------------------------------
			// FRN 87	I242/910, Transmitting logical sector (T-sector; T-LS), N.A.	F/1
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(87);

			// -----------------------------------------------------------------
			// FRN 88	I242/920, T-sector's consoles, N.A.	R/1 + nx1
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(88);

			// -----------------------------------------------------------------
			// FRN 89	I242/930, ADD Barometric Pressure Setting, 0.1 mb	F/2
			// Taken from I062/380, Aircraft Derived Data, Barometric Pressure
			// Setting, 0mb <= BPS <= 409.5 mb NOTE - BPS is the barometric 
			// pressure setting of the aircraft minus 800 mb.
			// One to one copy.
			if(fixmSystemTrackData.isSetAircraftDerivedData() 
					&& fixmSystemTrackData.getAircraftDerivedData()
						.isSetBarometricPressureSetting()) {
				
				final AbstractUniversalFixedDataItem barometricPressureSetting
					= (AbstractUniversalFixedDataItem)cat242Uap.getDataItem(89);
			
				final ByteBuffer bufBaroPressSetting 
					= ByteBuffer.allocate(barometricPressureSetting
							.getSizeFactor());
				
				bufBaroPressSetting
					.putShort(fixmSystemTrackData
						.getAircraftDerivedData()
						.getBarometricPressureSetting().shortValue());
				
				bufBaroPressSetting.flip();
				
				dataFields.put(89, barometricPressureSetting.pack(bufBaroPressSetting));

				fieldSpecification.enableField(89);
			}
			
			// -----------------------------------------------------------------
			// FRN 90	I242/9000, Reserved for future use	N.A.	F/2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(90);

			// -----------------------------------------------------------------
			// FRN 91	I242/9000, Reserved for future use	N.A.	F/2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(91);
			
			// -----------------------------------------------------------------
			// FRN 92	I242/9100, Reserved for future use	N.A.	F/4
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(92);
			
			// -----------------------------------------------------------------
			// FRN 93	I242/9100, Reserved for future use	N.A.	F/4
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(93);
			
			// -----------------------------------------------------------------
			// FRN 94	I242/9200, Reserved for future use	N.A.	E/2 + 2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(94);
			
			// -----------------------------------------------------------------
			// FRN 95	I242/9200, Reserved for future use	N.A.	E/2 + 2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(95);
			
			// -----------------------------------------------------------------
			// FRN 96	I242/9300, Reserved for future use	N.A.	R/1 + nx1
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(96);
			
			// -----------------------------------------------------------------
			// FRN 97	I242/9400, Reserved for future use	N.A.	R/1 + nx2
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(97);
			
			// -----------------------------------------------------------------
			// FRN 98	I242/9500, Reserved for future use	N.A.	Ex
			// Not Implemented in I242 v2.0
			fieldSpecification.disableField(98);
			
			//------------------------------------------------------------------
			// Create and add the corresponding CAT242 record.
			asterix242records
				.add(new AsterixRecord(fieldSpecification, dataFields));
		}
	}
	
	
	/**
	 * Private helper method to convert an CAT065 
	 * FIXM {@code AsterixDataBlock} to an Asterix {@code GenericDataBlock}s.
	 * 
	 * @pre		dataBlock may not be {@code null}.
	 * 
	 * @param	dataBlock
	 * 			The {@code FixmAsterixDataBlock} canonical model to be 
	 * 			converted to Asterix format. The dataBlock may not be 
	 * 			{@code null}.
	 * 
	 * @return	An Asterix {@code GenericDataBloc} containing the Asterix 65
	 * 			data block converted to Asterix 243.
	 */
	private final GenericDataBlock 
	transformFixm65BlockToAsterix243Block(FixmAsterixDataBlock fixmDataBlock) {
		
		// ---------------------------------------------------------------------
		// Validate preconditions.
		ObjectHelper.notNull(fixmDataBlock, "fixmDataBlock");

		// ---------------------------------------------------------------------
		// Setup and fill the Asterix Data Block.
		final UserApplicationProfile uap = 
				AsterixUserApplicationProfileManager.getInstance()
					.getUserApplicationProfile(DataCategoryType
											.CAT243.getByteRepresentation());
		
		final List<GenericRecord> asterixRecords 
			= new ArrayList<GenericRecord>();
		
		// ---------------------------------------------------------------------
		// Convert the FIXM 65 Records to Asterix 243 Records.
		transformFixm65RecordsToAsterix243(asterixRecords, 
										   fixmDataBlock
										   .getFixmAsterixRecords(), uap);
		
		// ---------------------------------------------------------------------
		// Resulting Asterix Data Block.
		int lengthIndicator = 3;  // Initial value of CAT + LEN.
		
		for (GenericRecord record : asterixRecords) {
			
			// Add the length of each encoded record.
			lengthIndicator += record.getFieldLengthIndicator();
		}
		
		return new AsterixDataBlock((short)243, lengthIndicator, asterixRecords);
	}
	
	/**
	 * Private helper method to transform the {@code List} of FIXM CAT065 
	 * {@code AsterixRecordType} records to CAT243 {@code AsterixRecord}s. 
	 * 
	 * @pre		cat243Uap, asterix243recordsrecords, fixmAsterix65Records are 
	 * 			not {@code null}.
	 * 
	 * @param 	asterix243records
	 * 			The {@code List} Asterix 243 {@code GenericRecord} to be filled.
	 * 			The records may not be {@code null}.
	 * 
	 * @param 	fixmAsterix65Records
	 * 		  	The {@code List} of 65 {@code AsterixRecordType}s to be 
	 * 			transformed from FIXM 65 Records to Asterix 243.  The records 
	 * 			may	not be {@code null}.
	 * 
	 * @param 	cat243Uap
	 * 		  	The {@code UserApplicationProfile} containing meta information
	 * 		  	concerning each Asterix Data Field.  The uap may not be 
	 * 		  	{@code null}.
	 */
	private final void 
	transformFixm65RecordsToAsterix243(final List<GenericRecord> asterix243records,
									   final List<AsterixRecordType> fixmAsterix65Records,
									   final UserApplicationProfile cat243Uap) {
		
		// ---------------------------------------------------------------------
		// Validate preconditions
		ObjectHelper.notNull(asterix243records, "asterix243records");
		ObjectHelper.notNull(fixmAsterix65Records, "fixmAsterix65Records");
		ObjectHelper.notNull(cat243Uap, "cat243Uap");
		
		// ---------------------------------------------------------------------
		// Iterate through asterix records and convert to FIXM format
		for(AsterixRecordType record : fixmAsterix65Records) {
			
			final AsterixFieldSpecificationType fieldSpecification 
				= new AsterixFieldSpecificationType();
			
			//------------------------------------------------------------------
			// Retrieve CAT065 message for transformation.
			final FixmSdpsService fixmSdpsService = (FixmSdpsService)record;

			final Map<Integer, DataField> dataFields 
				= new HashMap<Integer, DataField>();
			
			// -----------------------------------------------------------------
			// FRN 1:  I243/010 MRT source
			{
				final MrtSource mvSourcedataItem 
					= (MrtSource)cat243Uap.getDataItem(1);
				
				// default is set Site (1), MV(1) and ARTAS Unit (Flow) is 2.
				dataFields.put(Integer.valueOf(1), 
					mvSourcedataItem.pack((short)1, (short)1, (short)2));
				fieldSpecification.enableField(1);
			}

			// -----------------------------------------------------------------
			// FRN 2:  I243/020, Time of message
			if (fixmSdpsService.isSetTimeOfMessage()) {

				final TimeOfMessage toMdataItem 
					= (TimeOfMessage)cat243Uap.getDataItem(2);
				dataFields.put(Integer.valueOf(2), 
					toMdataItem.pack((int)fixmSdpsService.getTimeOfMessage()));
				fieldSpecification.enableField(2);
			}
			
			// -----------------------------------------------------------------
			// FRN 3:  I243/030, Flow status
			if (fixmSdpsService.isSetBatchNumber()) {

				final FlowStatus flowStatusdataItem 
					= (FlowStatus)cat243Uap.getDataItem(3);
				
				final short stripNumber 
					= fixmSdpsService.getBatchNumber().byteValue();
				
				final boolean resynchronizationFlag = (stripNumber == 0);
			
				dataFields.put(Integer.valueOf(3), 
					flowStatusdataItem
						.pack(resynchronizationFlag, false, stripNumber));
				
				fieldSpecification.enableField(3);
			}

			// -----------------------------------------------------------------
			// FRN 4: I243/040, Total number of strips
			// The total number of strips is 20 (confirmed).
			LoggingUniversalFixedDataItem totalNumberOfStripsDataItem 
				= (LoggingUniversalFixedDataItem)cat243Uap.getDataItem(4);
			dataFields
				.put(Integer.valueOf(4), 
					 totalNumberOfStripsDataItem.pack((byte)20));
			fieldSpecification.enableField(4);
			
			//------------------------------------------------------------------
			// Create and add the corresponding CAT243 record.
			asterix243records
				.add(new AsterixRecord(fieldSpecification, dataFields));
		}
	}
}
