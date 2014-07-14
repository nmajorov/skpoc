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

import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.ACS;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.ADR;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.BPS;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.BVR;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.COM;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.EMC;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.FSS;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.GAL;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.GSP;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.GVR;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.IAR;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.IAS;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.ID;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.MAC;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.MB;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.MET;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.MHG;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.POS;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.PUN;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.RAN;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.SAB;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.SAL;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.TAN;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.TAR;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.TAS;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.TID;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.TIS;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.AircraftDerivedDataSubTypeDescriptor.AircraftDerivedDataSubTypes.VUN;
import static ch.skyguide.util.data.ByteManipulationUtil.convertToByteArray;
import static ch.skyguide.util.data.IA5EncoderDecoderUtil.encodeIA5String;
import static ch.skyguide.util.data.StringManipulationUtil.getHexRepresentationfromByte;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ch.skyguide.exception.MessageException;
import ch.skyguide.message.structure.container.AbstractSpecificationDataItemContainer;
import ch.skyguide.message.structure.descriptor.SpecificationSubTypeDescriptor;
import ch.skyguide.util.data.FixedLengthBitSet;
import ch.skyguide.util.data.FixedLengthBitSetFactory;


//------------------------------------------------------------------------------

/*******************************************************************************
* Container class for the asterix 062/380 family and nature of the message.
*  
* @author Guillaume Fouillet
*/
public class AircraftDerivedDataContainer extends AbstractSpecificationDataItemContainer
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * Public defined constructor taking the family and nature of the asterix
    * message.
    *  
    * @param    familyOfMessage
    *           The family of the asterix message.
    *           
    * @param    natureOfMessage
    *           The nature of the asterix message.
    *           
    */
    public AircraftDerivedDataContainer(ByteBuffer buffer, 
                                        SpecificationSubTypeDescriptor subTypeDescriptor)
    {
        super(buffer, subTypeDescriptor);
    }
    
    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Returns the byte array for unpacking this container into a data field
    * 
    * @return the byte array for unpacking this container into a data field
    */
    @Override
    public byte[] 
    getBytes() 
    {
        byte[] returnBytes = new byte[getSubTypeDescriptor().getByteLength()];
        byte[] fieldBytes = getPrimaryField().toByteArray();
        int currPos = 0;
        // Primary field
        System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
        currPos += fieldBytes.length;
        if (isFieldActive(ADR.getFieldIndex())) {
            fieldBytes = m_targetAddress.toByteArray();
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(ID.getFieldIndex())) {
            fieldBytes = encodeIA5String(m_targetIdentification, ID.getByteLength());
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(MHG.getFieldIndex())) {
            fieldBytes = 
                convertToByteArray(m_magneticHeading, MHG.getByteLength());
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(IAS.getFieldIndex())) {
            FixedLengthBitSet bitSet = 
                FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_IASMNoSpeed, IAS.getByteLength()*8);
            bitSet.set(bitSet.getFixedLength()-1, m_IASMNoMachNumberFlag);
            fieldBytes = bitSet.toByteArray();
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(TAS.getFieldIndex())) {
            fieldBytes = 
                convertToByteArray(m_trueAirspeed, TAS.getByteLength());
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(SAL.getFieldIndex())) {
            FixedLengthBitSet bitset = 
                FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_SALselectedAltitude, SAL.getByteLength()*8);            
            for (int i=0;i<m_SALsource.getFixedLength();i++) {
                bitset.set(13+i, m_SALsource.get(i));
            }
            bitset.set(bitset.getFixedLength()-1, m_SALsAS);
            fieldBytes = bitset.toByteArray();
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(FSS.getFieldIndex())) {
            FixedLengthBitSet bitset =
                FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_FSSselectedAltitude, FSS.getByteLength()*8);
            bitset.set(bitset.getFixedLength()-1, m_FSSverticalMode);
            bitset.set(bitset.getFixedLength()-2, m_FSSaltitudeHold);
            bitset.set(bitset.getFixedLength()-3, m_FSSapproachMode);
            fieldBytes = bitset.toByteArray();
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(TIS.getFieldIndex())) {
            fieldBytes = m_trajectoryIntentStatus;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(TID.getFieldIndex())) {
            fieldBytes = m_trajectoryIntentData;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;
        }
        if (isFieldActive(COM.getFieldIndex())) {
            fieldBytes = m_comAcasCapability.getBytes();
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(SAB.getFieldIndex())) {
            fieldBytes = m_statusADSB;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;
        }
        if (isFieldActive(ACS.getFieldIndex())) {
            fieldBytes = m_acasResolutionAdvisoryReport;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;
        }
        if (isFieldActive(BVR.getFieldIndex())) {
            fieldBytes = 
                convertToByteArray(m_barometricVerticalRate, BVR.getByteLength());            
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;
        }
        if (isFieldActive(GVR.getFieldIndex())) {
            fieldBytes = 
                convertToByteArray(m_geometricVerticalRate, GVR.getByteLength());     
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(RAN.getFieldIndex())) {
            fieldBytes = 
                convertToByteArray(m_rollAngle, RAN.getByteLength());     
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(TAR.getFieldIndex())) {
            fieldBytes = m_trackAngleRate.getBytes();
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;
        }
        if (isFieldActive(TAN.getFieldIndex())) {
            fieldBytes = 
                convertToByteArray(m_trackAngle, TAN.getByteLength());     
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(GSP.getFieldIndex())) {
            fieldBytes = 
                convertToByteArray(m_groundSpeed, GSP.getByteLength());     
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(VUN.getFieldIndex())) {
            fieldBytes = m_velocityUncertainty;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(MET.getFieldIndex())) {
            fieldBytes = m_metData;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(EMC.getFieldIndex())) {
            fieldBytes = m_emitterCategory;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(POS.getFieldIndex())) {
            fieldBytes = m_positionData;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;
        }
        if (isFieldActive(GAL.getFieldIndex())) {
            fieldBytes = m_geometricAltitude;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(PUN.getFieldIndex())) {
            fieldBytes = m_positionUncertainty;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(MB.getFieldIndex())) {
            fieldBytes = m_modeSMBData;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(IAR.getFieldIndex())) {
            fieldBytes = 
                convertToByteArray(m_indicatedAirspeed, IAR.getByteLength());   
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(MAC.getFieldIndex())) {
            fieldBytes = 
                convertToByteArray(m_machNumber, MAC.getByteLength());  
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }
        if (isFieldActive(BPS.getFieldIndex())) {
            fieldBytes = m_barometricPressureSetting;
            System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
            currPos += fieldBytes.length;            
        }        
        return returnBytes;
    }
    
    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_targetAddress.
     * @return  Returns the m_targetAddress.
     */
    public FixedLengthBitSet getTargetAddress()
    {
        return m_targetAddress;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_targetIdentification.
     * @return  Returns the m_targetIdentification.
     */
    public String getTargetIdentification()
    {
        return m_targetIdentification;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_magneticHeading.
     * @return  Returns the m_magneticHeading.
     */
    public int getMagneticHeading()
    {
        return m_magneticHeading;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_IASMNoSpeed.
     * @return  Returns the m_IASMNoSpeed.
     */
    public int getIASMNoSpeed()
    {
        return m_IASMNoSpeed;
    }


    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_IASMNoMachNumberFlag.
     * @return  Returns the m_IASMNoMachNumberFlag.
     */
    public boolean getIASMNoMachNumberFlag()
    {
        return m_IASMNoMachNumberFlag;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_trueAirspeed.
     * @return  Returns the m_trueAirspeed.
     */
    public short getTrueAirspeed()
    {
        return m_trueAirspeed;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_SALsAS.
     * @return  Returns the m_SALsAS.
     */
    public boolean getSALsAS()
    {
        return m_SALsAS;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_SALsource.
     * @return  Returns the m_SALsource.
     */
    public FixedLengthBitSet getSALsource()
    {
        return m_SALsource;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_selectedAltitude.
     * @return  Returns the m_selectedAltitude.
     */
    public int getSALSelectedAltitude()
    {
        return m_SALselectedAltitude;
    }


    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_FSSverticalMode.
     * @return  Returns the m_FSSverticalMode.
     */
    public boolean getFSSverticalMode()
    {
        return m_FSSverticalMode;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_FSSaltitudeHold.
     * @return  Returns the m_FSSaltitudeHold.
     */
    public boolean getFSSaltitudeHold()
    {
        return m_FSSaltitudeHold;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_FSSapproachMode.
     * @return  Returns the m_FSSapproachMode.
     */
    public boolean getFSSapproachMode()
    {
        return m_FSSapproachMode;
    }    
    
    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_finalStateSelectedAltitude.
     * @return  Returns the m_finalStateSelectedAltitude.
     */
    public int getFinalStateSelectedAltitude()
    {
        return m_FSSselectedAltitude;
    }
    
    
    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_trajectoryIntentStatus.
     * @return  Returns the m_trajectoryIntentStatus.
     */
    public byte[] getTrajectoryIntentStatus()
    {
        return m_trajectoryIntentStatus;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_trajectoryIntentData.
     * @return  Returns the m_trajectoryIntentData.
     */
    public byte[] getTrajectoryIntentData()
    {
        return m_trajectoryIntentData;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_comAcasCapability.
     * @return  Returns the m_comAcasCapability.
     */
    public ComAcasCapability getComAcasCapability()
    {
        return m_comAcasCapability;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_statusADSB.
     * @return  Returns the m_statusADSB.
     */
    public byte[] getStatusADSB()
    {
        return m_statusADSB;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_acasResolutionAdvisoryReport.
     * @return  Returns the m_acasResolutionAdvisoryReport.
     */
    public byte[] getAcasResolutionAdvisoryReport()
    {
        return m_acasResolutionAdvisoryReport;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_barometricVerticalRate.
     * @return  Returns the m_barometricVerticalRate.
     */
    public short getBarometricVerticalRate()
    {
        return m_barometricVerticalRate;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_geometricVerticalRate.
     * @return  Returns the m_geometricVerticalRate.
     */
    public short getGeometricVerticalRate()
    {
        return m_geometricVerticalRate;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_rollAngle.
     * @return  Returns the m_rollAngle.
     */
    public short getRollAngle()
    {
        return m_rollAngle;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_trackAngleRate.
     * @return  Returns the m_trackAngleRate.
     */
    public TrackAngleRate getTrackAngleRate()
    {
        return m_trackAngleRate;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_trackAngle.
     * @return  Returns the m_trackAngle.
     */
    public int getTrackAngle()
    {
        return m_trackAngle;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_groundSpeed.
     * @return  Returns the m_groundSpeed.
     */
    public int getGroundSpeed()
    {
        return m_groundSpeed;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_velocityUncertainty.
     * @return  Returns the m_velocityUncertainty.
     */
    public byte[] getVelocityUncertainty()
    {
        return m_velocityUncertainty;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_metData.
     * @return  Returns the m_metData.
     */
    public byte[] getMetData()
    {
        return m_metData;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_emitterCategory.
     * @return  Returns the m_emitterCategory.
     */
    public byte[] getEmitterCategory()
    {
        return m_emitterCategory;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_positionData.
     * @return  Returns the m_positionData.
     */
    public byte[] getPositionData()
    {
        return m_positionData;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_geometricAltitude.
     * @return  Returns the m_geometricAltitude.
     */
    public byte[] getGeometricAltitude()
    {
        return m_geometricAltitude;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_positionUncertainty.
     * @return  Returns the m_positionUncertainty.
     */
    public byte[] getPositionUncertainty()
    {
        return m_positionUncertainty;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_modeSMBData.
     * @return  Returns the m_modeSMBData.
     */
    public byte[] getModeSMBData()
    {
        return m_modeSMBData;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_indicatedAirspeed.
     * @return  Returns the m_indicatedAirspeed.
     */
    public short getIndicatedAirspeed()
    {
        return m_indicatedAirspeed;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_machNumber.
     * @return  Returns the m_machNumber.
     */
    public short getMachNumber()
    {
        return m_machNumber;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_barometricPressureSetting.
     * @return  Returns the m_barometricPressureSetting.
     */
    public byte[] getBarometricPressureSetting()
    {
        return m_barometricPressureSetting;
    }

    /***************************************************************************
     * Set the final state selected altitude from an altitude in 25 ft
     * Return true if the field had been added
     * 
     * @pre  altitude is between -1300 ft and 100000 ft
     *
     * @param altitude
     *        
     * @return
      * @throws MessageException 
     */
     public boolean 
     setFinalStateSelectedAltitude(int altitude) throws MessageException
     {
         // Preconditions
         AircraftDerivedDataSubTypeDescriptor descriptor =
                 (AircraftDerivedDataSubTypeDescriptor)getSubTypeDescriptor();
         
         if (null == descriptor || altitude > 4000 || altitude < -52) {
             //
             // Throwing MessageException fixes TDSCADET-2591.
             //
//             final String errorMessage = "Altitude " + altitude + 
//                     " cannot be set in the aircraft derived data, " +
//                     "target identification=" + getTargetIdentification();
             // TODO: rjw, talk with GFO logging concept insufficient.
//             final LogObject logMessage = new LogObject("", "", errorMessage);
//             Logger.instance().error(ILogger.EVENT, logMessage);
//             throw new MessageException(errorMessage);
         }
         
         // TODO: rjw, correction to avoid null pointer access warning, not very elegant.
         if(descriptor != null) {
             if (descriptor.addField(FSS.getFieldIndex())) {
                 m_FSSselectedAltitude = altitude;
                 m_FSSverticalMode = true;
                 m_FSSaltitudeHold = false;
                 m_FSSapproachMode = false;
                 return true;
             }
         }
         return false;
         
     }

    
    /***************************************************************************
    * Set the ground speed information from a speed in m/s 
    * Returns true if the field had been added
    * 
    * Precondition: speed is between -2 NM/s and 2 NM/s
    *
    * @param trackAngle
    *        the track angle to set
    * 
    * @return true if the field had been added
    */
    public boolean 
    setGroundSpeed(int speed) throws MessageException 
    {
        // Preconditions
        AircraftDerivedDataSubTypeDescriptor descriptor =
            (AircraftDerivedDataSubTypeDescriptor)getSubTypeDescriptor();
        
        if (null == descriptor || speed < -3700 || speed > 3700) {
            final String errorMessage = "GroundSpeed " + speed + 
                    " cannot be set in the aircraft derived data, " +
                    "target identification=" + getTargetIdentification();
            // TODO: rjw, talk with GFO logging concept insufficient.
            //            final LogObject logMessage = new LogObject("", "", errorMessage);
            //            Logger.instance().error(ILogger.EVENT, logMessage);
            throw new MessageException(errorMessage);
        }
        
        if (descriptor.addField(GSP.getFieldIndex())) {
            m_groundSpeed = Double.valueOf(speed/0.113).intValue();
            return true;
        }
        return false;
    }
    
    
    /***************************************************************************
    * Set the track angle information from a calculated track angle in degree 
    * Returns true if the field had been added
    * 
    * Precondition: track angle is between 0 and 360 °
    *
    * @param trackAngle
    *        the track angle to set
    *        
    * @return true if the field had been added
    */
    public boolean 
    setTrackAngle(double trackAngle) 
    {
        // Preconditions
        AircraftDerivedDataSubTypeDescriptor descriptor =
            (AircraftDerivedDataSubTypeDescriptor)getSubTypeDescriptor();
        
        if (null == descriptor || trackAngle<0D || trackAngle > 360D) {
            throw new IllegalArgumentException("TrackAngle " + trackAngle + 
                    " cannot be set in the aircraft derived data");
        }
        
        if (descriptor.addField(TAN.getFieldIndex())) {
            m_trackAngle = Double.valueOf(trackAngle/0.0055).intValue();
            return true;
        }
        return false;
    }
    
    /***************************************************************************
    * See description of superclass/interface.
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() 
    {
        StringBuilder str = new StringBuilder("AircraftDerivedData={\n");
        if (null != m_targetAddress) {
            str.append("\tTargetAddress : " + m_targetAddress.toString());
        }
        if (null != m_targetIdentification) {
            str.append(";\n\tTargetIdentification : " + m_targetIdentification);
        }
        str.append(";\n\tMagneticHeading : " + Double.valueOf(m_magneticHeading*0.0055).intValue()+ " °");
        str.append(";\n\tIASMNoMachNumberFlag : " + m_IASMNoMachNumberFlag);
        if (m_IASMNoMachNumberFlag) {
            str.append("- MacNo : " + Double.valueOf(m_IASMNoSpeed*0.001).doubleValue());
        } else {
            str.append("- IASpeed : " + Double.valueOf(m_IASMNoSpeed*0.113).intValue() + " m/s");
        }
        str.append(";\n\ttrueAirspeed : " + Double.valueOf(m_trueAirspeed*0.113).intValue() + " m/s");
        str.append(";\n\tSALsAS : " + m_SALsAS);
        if (null != m_targetAddress) {
            str.append(" - SALsource : " + m_SALsource.toString());
        }
        str.append(" - SALselectedAltitude : " + Integer.valueOf(m_SALselectedAltitude*25).intValue() + " ft");
        str.append(";\n\tFSSverticalMode: " + m_FSSverticalMode);
        str.append(" - FSSaltitudeHold : " + m_FSSaltitudeHold);
        str.append("- FSSapproachMode : " + m_FSSapproachMode);
        str.append(" - FSSselectedAltitude : " + Integer.valueOf(m_FSSselectedAltitude*25).intValue() + " ft");
        if (null != m_trajectoryIntentStatus) {
            str.append(";\n\ttrajectoryIntentStatus : " + getHexRepresentationfromByte(m_trajectoryIntentStatus));
        }
        if (null != m_trajectoryIntentData) {
            str.append(";\n\ttrajectoryIntentData : " + getHexRepresentationfromByte(m_trajectoryIntentData));
        }
        if (null != m_comAcasCapability) {
            str.append(";\n\tcomAcasCapability : " + m_comAcasCapability.toString());
        }
        if (null != m_statusADSB) {
            str.append(";\n\tstatusADSB : " + getHexRepresentationfromByte(m_statusADSB));
        }
        if (null != m_acasResolutionAdvisoryReport) {
            str.append(";\n\tacasResolutionAdvisoryReport : " + getHexRepresentationfromByte(m_acasResolutionAdvisoryReport));
        }
        str.append(";\n\tbarometricVerticalRate : " + Double.valueOf(m_barometricVerticalRate*6.25).intValue() + " ft/mn");
        str.append(";\n\tgeometricVerticalRate : " + Double.valueOf(m_geometricVerticalRate*6.25).intValue() + " ft/mn");
        str.append(";\n\trollAngle : " + Double.valueOf(m_rollAngle*0.01).intValue() + " °");
        if (null != m_trackAngleRate) {
            str.append(";\n\ttrackAngleRate : " + m_trackAngleRate.toString());
        }
        str.append(";\n\ttrackAngle : " + Double.valueOf(m_trackAngle*0.0055).intValue() + " °");
        str.append(";\n\tgroundSpeed : " + Double.valueOf(m_groundSpeed*0.113).intValue() + " m/s");
        if (null != m_velocityUncertainty) {
            str.append(";\n\tvelocityUncertainty : " + getHexRepresentationfromByte(m_velocityUncertainty));
        }
        if (null != m_metData) {
            str.append(";\n\tmetData: " + getHexRepresentationfromByte(m_metData));
        }
        if (null != m_emitterCategory) {
            str.append(";\n\temitterCategory : " + getHexRepresentationfromByte(m_emitterCategory));
        }
        if (null != m_positionData) {
            str.append(";\n\tpositionData : " + getHexRepresentationfromByte(m_positionData));
        }
        if (null != m_geometricAltitude) {
            str.append(";\n\tgeometricAltitude : " + getHexRepresentationfromByte(m_geometricAltitude));
        }
        if (null != m_positionUncertainty) {
            str.append(";\n\tpositionUncertainty : " + getHexRepresentationfromByte(m_positionUncertainty));
        }
        if (null != m_modeSMBData) {
            str.append(";\n\tmodeSMBData : " + getHexRepresentationfromByte(m_modeSMBData));
        }
        str.append(";\n\tindicatedAirspeed : " + Double.valueOf(m_indicatedAirspeed*0.51325).intValue() + " m/s");
        str.append(";\n\tmachNumber : " + Double.valueOf(m_machNumber*0.008).doubleValue());
        if (null != m_barometricPressureSetting) {
            str.append(";\n\tbarometricPressureSetting : " + getHexRepresentationfromByte(m_barometricPressureSetting));
        }
        str.append("}");
        return str.toString();
    }
    
    //--------------------------------------------------------------------------
    // public static member functions

    //--------------------------------------------------------------------------
    // protected constructors

    //--------------------------------------------------------------------------
    // protected member functions
    /***************************************************************************
     * Deserialize the buffer into the class structure
     *
     * Precondition : buffer != null
     *
     * @param buffer
     *        The buffer to de-serialize
     */
     @Override
     protected void 
     deserialize(ByteBuffer buffer) 
     {
         // Gather the first 4 bytes as the primary field
         buffer.order(ByteOrder.LITTLE_ENDIAN);
         deserializeSpecification(buffer, getSubTypeDescriptor().getMaxPrimaryFieldLength());
         m_targetAddress = deserializeBitSetField(buffer, ADR.getFieldIndex(), m_targetAddress);
         m_targetIdentification = deserializeStringField(buffer, ID.getFieldIndex(), m_targetIdentification);
         m_magneticHeading = deserializeIntField(buffer, MHG.getFieldIndex(), m_magneticHeading);
         deserializeIAS(buffer);
         m_trueAirspeed = deserializeShortField(buffer, TAS.getFieldIndex(), m_trueAirspeed);
         deserializeSAL(buffer);
         deserializeFSS(buffer);
         m_trajectoryIntentStatus = deserializeBytesField(buffer, TIS.getFieldIndex(), m_trajectoryIntentStatus);
         m_trajectoryIntentData = deserializeBytesField(buffer, TID.getFieldIndex(), m_trajectoryIntentData);
         deserializeCOM(buffer);
         m_statusADSB = deserializeBytesField(buffer, SAB.getFieldIndex(), m_statusADSB);
         m_acasResolutionAdvisoryReport = deserializeBytesField(buffer, ACS.getFieldIndex(), m_acasResolutionAdvisoryReport);
         m_barometricVerticalRate = deserializeShortField(buffer, BVR.getFieldIndex(), m_barometricVerticalRate);
         m_geometricVerticalRate = deserializeShortField(buffer, GVR.getFieldIndex(), m_geometricVerticalRate);
         m_rollAngle = deserializeShortField(buffer, RAN.getFieldIndex(), m_rollAngle);
         deserializeTAR(buffer);
         m_trackAngle = deserializeIntField(buffer, TAN.getFieldIndex(), m_trackAngle);
         m_groundSpeed = deserializeIntField(buffer, GSP.getFieldIndex(), m_groundSpeed);
         m_velocityUncertainty = deserializeBytesField(buffer, VUN.getFieldIndex(), m_velocityUncertainty);
         m_metData = deserializeBytesField(buffer, MET.getFieldIndex(), m_metData);
         m_emitterCategory = deserializeBytesField(buffer, EMC.getFieldIndex(), m_emitterCategory);
         m_positionData = deserializeBytesField(buffer, POS.getFieldIndex(), m_positionData);
         m_geometricAltitude = deserializeBytesField(buffer, GAL.getFieldIndex(), m_geometricAltitude);
         m_positionUncertainty = deserializeBytesField(buffer, PUN.getFieldIndex(), m_positionUncertainty);
         m_modeSMBData = deserializeBytesField(buffer, MB.getFieldIndex(), m_modeSMBData);
         m_indicatedAirspeed = deserializeShortField(buffer, IAR.getFieldIndex(), m_indicatedAirspeed);
         m_machNumber = deserializeShortField(buffer, MAC.getFieldIndex(), m_machNumber);
         m_barometricPressureSetting = deserializeBytesField(buffer, BPS.getFieldIndex(), m_barometricPressureSetting);
     }
     
    //--------------------------------------------------------------------------
    // protected static member functions

    //--------------------------------------------------------------------------
    // protected data members

    //--------------------------------------------------------------------------
    // protected static data members

    //--------------------------------------------------------------------------
    // package constructors

    //--------------------------------------------------------------------------
    // package member functions

    //--------------------------------------------------------------------------
    // package static member functions

    //--------------------------------------------------------------------------
    // package data members

    //--------------------------------------------------------------------------
    // package static data members

    //--------------------------------------------------------------------------
    // private constructors

    //--------------------------------------------------------------------------
    // private member functions
    /***************************************************************************
     * Precondition:
     *
     * Postcondition:
     *
     * @param buffer
     */
    private void deserializeIAS(ByteBuffer buffer) {
        byte[] secondaryField = extractByteFromBuffer(buffer, IAS.getFieldIndex());
        if (null != secondaryField) {
            FixedLengthBitSet bitset = FixedLengthBitSetFactory.instance().toFixedLengthBitSet(secondaryField);
            m_IASMNoMachNumberFlag = bitset.get(bitset.getFixedLength()-1);
            bitset.set(bitset.getFixedLength()-1, false);
            m_IASMNoSpeed = bitset.toInt();
        }
    }    

    /***************************************************************************
     * Precondition:
     *
     * Postcondition:
     *
     * @param buffer
     */
    private void deserializeFSS(ByteBuffer buffer) {
        byte[] secondaryField = extractByteFromBuffer(buffer, FSS.getFieldIndex());
        if (null != secondaryField) {
            FixedLengthBitSet bitset = FixedLengthBitSetFactory.instance().toFixedLengthBitSet(secondaryField);
            m_FSSverticalMode = bitset.get(bitset.getFixedLength()-1);
            m_FSSaltitudeHold = bitset.get(bitset.getFixedLength()-2);
            m_FSSapproachMode = bitset.get(bitset.getFixedLength()-3);
            for (int i=0;i<3;i++) {
                bitset.set(bitset.getFixedLength()-1-i, false);
            }
            m_FSSselectedAltitude = bitset.toInt();
        }
    }
    
    
    /***************************************************************************
     * Precondition:
     *
     * Postcondition:
     *
     * @param buffer
     */
    private void deserializeSAL(ByteBuffer buffer) {
        byte[] secondaryField = extractByteFromBuffer(buffer, SAL.getFieldIndex());
        if (null != secondaryField) {
            FixedLengthBitSet bitset = FixedLengthBitSetFactory.instance().toFixedLengthBitSet(secondaryField);
            m_SALsAS = bitset.get(bitset.getFixedLength()-1);
            m_SALsource =  FixedLengthBitSetFactory.instance().toFixedLengthBitSet(2);
            for (int i=0;i<2;i++) {
                m_SALsource.set(i, bitset.get(bitset.getFixedLength()-3+i));
            }
            for (int i=0;i<3;i++) {
                bitset.set(bitset.getFixedLength()-1-i, false);
            }
            m_SALselectedAltitude = bitset.toInt();
        }
    }    
    
    /***************************************************************************
     * Precondition:
     *
     * Postcondition:
     *
     * @param buffer
     */
    private void deserializeCOM(ByteBuffer buffer) {
        byte[] secondaryField = extractByteFromBuffer(buffer, COM.getFieldIndex());
        if (null != secondaryField) {
            FixedLengthBitSet bitset = FixedLengthBitSetFactory.instance().toFixedLengthBitSet(secondaryField);
            m_comAcasCapability = 
                new ComAcasCapability(bitset.get(13, 15).toInt(),
                        bitset.get(10, 12).toInt(),
                        bitset.get(7), bitset.get(6), bitset.get(5));
        }
    }    
    
    
    /***************************************************************************
     * Precondition:
     *
     * Postcondition:
     *
     * @param buffer
     */
    private void deserializeTAR(ByteBuffer buffer) {
        byte[] secondaryField = extractByteFromBuffer(buffer, TAR.getFieldIndex());
        if (null != secondaryField) {
            FixedLengthBitSet bitset = FixedLengthBitSetFactory.instance().toFixedLengthBitSet(secondaryField);
            m_trackAngleRate = 
                new TrackAngleRate(bitset.get(1, 8).toInt(), 
                                   bitset.get(14, 16).toInt());
        }
    }    
    
    //--------------------------------------------------------------------------
    // private static member functions

    //--------------------------------------------------------------------------
    // private data members

    /**
    * Target address
    */
    private FixedLengthBitSet m_targetAddress;

    /**
    * Target identification 
    */
    private String m_targetIdentification;

    /**
    * Magnetic Heading defined in the 360°/2^16 unit 
    */
    private int m_magneticHeading;

    /**
    * Indicated airspeed/MacNo defined in the 2^-14 NM/s or 0.001 unit depending on the MachNumber Flag 
    */
    private int m_IASMNoSpeed;

    /**
    * Indicated airspeed/MacNo Flag 
    */
    private boolean m_IASMNoMachNumberFlag;
    
    /**
    * True airspeed
    */
    private short m_trueAirspeed;

    /**
    * Selected altitude SAS
    */
    private boolean m_SALsAS;    
    
    /**
    * Selected altitude Source
    */
    private FixedLengthBitSet m_SALsource;    
    
    /**
    * Selected altitude in the 25ft unit
    */
    private int m_SALselectedAltitude;

    /**
     * The final state altitude vertical mode
     */
    private boolean m_FSSverticalMode;
    
    /**
     * The final state altitude hold
     */
    private boolean m_FSSaltitudeHold;
    
    /**
     * The final state altitude approach mode
     */
    private boolean m_FSSapproachMode;
    
    /**
    * Final State Selected Altitude 
    */
    private int m_FSSselectedAltitude;

    /**
    * Trajectory Intent Status
    */
    private byte[] m_trajectoryIntentStatus;

    /**
    * Trajectory Intent data
    */
    private byte[] m_trajectoryIntentData;

    /**
    * Com/ACAS Capability
    */
    private ComAcasCapability m_comAcasCapability;

    /**
    * Status reported by ADS-B
    */
    private byte[] m_statusADSB;

    /**
    * ACAS resolution advisory report  
    */
    private byte[] m_acasResolutionAdvisoryReport;

    /**
    * Barometric vertical rate in the 6.25 feet/mn unit
    */
    private short m_barometricVerticalRate;

    /**
    * Geometric vertical rate in the 6.25 feet/mn unit
    */
    private short m_geometricVerticalRate;

    /**
    * Roll angle in the 0.01 degree unit
    */
    private short m_rollAngle;

    /**
    * Track angle rate
    */
    private TrackAngleRate m_trackAngleRate;

    /**
    * Track angle in the 360°/2^16 unit 
    */
    private int m_trackAngle;

    /**
    * Ground speed in the 2^-14 NM/s unit
    */
    private int m_groundSpeed ;

    /**
    * Velocity uncertainty 
    */
    private byte[] m_velocityUncertainty;

    /**
    * Meteorological data
    */
    private byte[] m_metData;
    
    /**
    * Emitter category 
    */
    private byte[] m_emitterCategory;

    /**
    * Position data
    */
    private byte[] m_positionData;

    /**
    * Geometric altitude
    */
    private byte[] m_geometricAltitude;

    /**
    * Position uncertainty
    */
    private byte[] m_positionUncertainty;

    /**
    * Mode S MB Data
    */
    private byte[] m_modeSMBData;

    /**
    * Indicated airspeed 
    */
    private short m_indicatedAirspeed;

    /**
    * Mach number in the 1/0.008 unit
    */
    private short m_machNumber;

    /**
    * Barometric pressure setting
    */
    private byte[] m_barometricPressureSetting;
    
    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block
    
}
