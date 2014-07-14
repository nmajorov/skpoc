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

import static ch.skyguide.util.data.StringManipulationUtil.getHexRepresentationfromByte;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nullable;

import ch.skyguide.communication.rdps.asterix.categories.cat062.REforCat062SubTypeDescriptor.ReservedExpansionFieldTypes;
import ch.skyguide.message.structure.container.AbstractSpecificationDataItemContainer;
import ch.skyguide.message.structure.descriptor.SpecificationSubTypeDescriptor;
import ch.skyguide.message.structure.descriptor.SubTypeDescriptor;
import ch.skyguide.util.data.ByteManipulationUtil;
import ch.skyguide.util.data.FixedLengthBitSet;
import ch.skyguide.util.data.FixedLengthBitSetFactory;

/*******************************************************************************
* Container class for the asterix 062 specification data item of the message.
*  
* @author Guillaume Fouillet, Didier Bérard
*/
public class REForCat062Container extends AbstractSpecificationDataItemContainer
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * Public defined constructor taking the family and nature of the asterix
    * message.
    *  
    * @param    buffer
    *           The buffer to deserialize
    *           
    * @param    subTypeDescriptor
    *           The specification subtype of the message
    *           
    */
    public REForCat062Container(ByteBuffer buffer,
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
        byte[] specificationBytes = new byte[getSubTypeDescriptor().getByteLength()];
        
        // Primary field
        byte[] fieldBytes = getPrimaryField().toByteArray();
        int length = 1;
        System.arraycopy(fieldBytes, 0, specificationBytes, length-1, fieldBytes.length);
        length += fieldBytes.length;
        
        // Secondary field
        for (ReservedExpansionFieldTypes type : ReservedExpansionFieldTypes.values()) {
            if (isFieldActive(type.getFieldIndex())) {
                switch (type) {
                    case CST:
                        fieldBytes = m_sensorsWithTrackNumber;
                        break;
                    case CSN:
                        fieldBytes = m_sensorsWithoutTrackNumber;
                        break;
                    case TVS:
                        fieldBytes = m_refVelocity.getBytes();
                        break;
                    case STS:
                        fieldBytes = m_supplementaryTrackStatus.toByteArray();
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Unknown type : " + type.name());
                }
                System.arraycopy(fieldBytes, 0, specificationBytes, length-1, fieldBytes.length);
                length += fieldBytes.length;
            }
        } 
        byte[] returnBytes = new byte[length];
        System.arraycopy(ByteManipulationUtil.convertToByteArray(length, 1), 0, 
                returnBytes, 0, 1);
        System.arraycopy(specificationBytes, 0, returnBytes, 1, length-1);
        
        return returnBytes;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_sensorsWithTrackNumber.
     * @return  Returns the m_sensorsWithTrackNumber.
     */
    public byte[] getSensorsWithTrackNumber()
    {
        return m_sensorsWithTrackNumber;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_sensorsWithoutTrackNumber.
     * @return  Returns the m_sensorsWithoutTrackNumber.
     */
    public byte[] getSensorsWithoutTrackNumber()
    {
        return m_sensorsWithoutTrackNumber;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_refVelocity.
     * @return  Returns the m_refVelocity.
     */
    public TrackVelocity getReferenceVelocity()
    {
        return m_refVelocity;
    }
    
    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_supplementaryTrackStatus.
     * @return  Returns the m_supplementaryTrackStatus.
     */
    public FixedLengthBitSet getSupplementaryTrackStatus11()
    {
        return m_supplementaryTrackStatus;
    }
    
    /***************************************************************************
    * See description of superclass/interface.
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() 
    {
        StringBuilder str = new StringBuilder("ReservedExpansionField={");
        //str.append("Item indicator : " + getHexRepresentationfromByte(m_itemIndicator));
        if (null != m_sensorsWithTrackNumber) {
            str.append(";SensorsWithTrackNumber : " + getHexRepresentationfromByte(m_sensorsWithTrackNumber));
        }
        if (null != m_sensorsWithoutTrackNumber) {
            str.append(";SensorsWithoutTrackNumber : " + getHexRepresentationfromByte(m_sensorsWithoutTrackNumber));
        }
        if (null != m_refVelocity) {
            str.append(";ReferenceVelocity : " + m_refVelocity.toString() + "]");
        }
        if (null != m_supplementaryTrackStatus) {
            str.append(";SupplementaryTrackStatus : " + m_supplementaryTrackStatus.toString() + "]"); 
        }
        str.append("}");
        return str.toString();
    }
    
    //--------------------------------------------------------------------------
    // public static member functions

    
    /***************************************************************************
    * Returns a dummy static ByteBuffer in which only track velocity really 
    * matters
    * 
    * @param subTypeDescriptor
    *        the subtype descriptor
    * 
    * @param trackVelocity
    *        the track velocity
    *        
    * @return a dummy static ByteBuffer in which only track velocity really 
    * matters 
    */
    public static ByteBuffer 
    getDefaultBuffer(SubTypeDescriptor subTypeDescriptor, 
                     TrackVelocity trackVelocity) 
    {
        
        byte[] byteArray = 
            getByteArray(subTypeDescriptor, 
                         null,
                         null,
                         trackVelocity, 
                         null);
        
        return ByteBuffer.wrap(byteArray);
    }
    
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
         buffer.get();
         deserializeSpecification(buffer, 1);
         
         if (isFieldActive(
                 REforCat062SubTypeDescriptor.ReservedExpansionFieldTypes.CST.getFieldIndex())) {
             m_sensorsWithTrackNumber = deserializeBytesField(buffer, 
                     ReservedExpansionFieldTypes.CST.getFieldIndex(), m_sensorsWithTrackNumber);
         }
         if (isFieldActive(
                 REforCat062SubTypeDescriptor.ReservedExpansionFieldTypes.CSN.getFieldIndex())) {
             m_sensorsWithoutTrackNumber = deserializeBytesField(buffer, 
                     ReservedExpansionFieldTypes.CSN.getFieldIndex(), m_sensorsWithoutTrackNumber);
         }
         if (isFieldActive(
                 REforCat062SubTypeDescriptor.ReservedExpansionFieldTypes.TVS.getFieldIndex())) {
             // unit of the track velocity for cat062 is 0.25 m/s
             m_refVelocity = new TrackVelocity(buffer, 0.25);
         }
         if (isFieldActive(
                 REforCat062SubTypeDescriptor.ReservedExpansionFieldTypes.STS.getFieldIndex())) {
             m_supplementaryTrackStatus = deserializeBitSetField(buffer, 
                     ReservedExpansionFieldTypes.STS.getFieldIndex(), m_supplementaryTrackStatus);
         }
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
    
    //--------------------------------------------------------------------------
    // private static member functions
    /***************************************************************************
    * Returns a static byte representation of the arguments
    * 
    *
    * @param subTypeDescriptor
    *        The subtype descriptor
    * 
    * @param itemIndicator
    *        The item indicator
    * 
    * @param sensorsWithTrackNumber
    *       the sensors with track number
    * 
    * @param sensorsWithoutTrackNumber
    *       the sensors without track number
    * 
    * @param refVelocity
    *       the reference velocity
    * 
    * @param supplementaryTrackStatus
    *       the supplementary track status
    * 
    * @return a static byte representation of the arguments
    */
    protected final static byte[]
    getByteArray(SubTypeDescriptor subTypeDescriptor,
                 @Nullable byte[] sensorsWithTrackNumber, 
                 @Nullable byte[] sensorsWithoutTrackNumber, 
                 @Nullable TrackVelocity refVelocity,
                 @Nullable FixedLengthBitSet supplementaryTrackStatus) {
        int length = 2;
        FixedLengthBitSet fieldSpec = 
                FixedLengthBitSetFactory.instance().toFixedLengthBitSet(8);        
        if (sensorsWithTrackNumber != null) {
            fieldSpec.set(fieldSpec.getFixedLength()-
                    ReservedExpansionFieldTypes.CST.getBigEndianFlagBit()-1);
            length += sensorsWithTrackNumber.length; 
        }
        if (sensorsWithoutTrackNumber != null) {
            fieldSpec.set(fieldSpec.getFixedLength()-
                    ReservedExpansionFieldTypes.CSN.getBigEndianFlagBit()-1);
            length += sensorsWithoutTrackNumber.length; 
        }
        if (refVelocity != null) {
            fieldSpec.set(fieldSpec.getFixedLength()-
                    ReservedExpansionFieldTypes.TVS.getBigEndianFlagBit()-1);
            length += REforCat062SubTypeDescriptor.ReservedExpansionFieldTypes.TVS.getByteLength(); 
        }
        if (supplementaryTrackStatus != null) {
            fieldSpec.set(fieldSpec.getFixedLength()-
                    ReservedExpansionFieldTypes.STS.getBigEndianFlagBit()-1);
            length += supplementaryTrackStatus.getFixedLength(); 
        }
        byte[] returnBytes = new byte[length];
        int pos = 0;
        // ILength field
        byte[] fieldBytes = ByteManipulationUtil.convertToByteArray(length, 1);
        System.arraycopy(fieldBytes, 0, returnBytes, pos, fieldBytes.length);
        pos += fieldBytes.length;
        // Item indicator

        fieldBytes = fieldSpec.toByteArray();
        System.arraycopy(fieldBytes, 0, returnBytes, pos, fieldBytes.length);
        pos += fieldBytes.length;
        
        // Sensor with track number
        if (sensorsWithTrackNumber != null) {            
            fieldBytes = sensorsWithTrackNumber;
            System.arraycopy(fieldBytes, 0, returnBytes, pos, fieldBytes.length);
            pos += fieldBytes.length;       
        }
        
        // Sensor without track number
        if (sensorsWithoutTrackNumber != null) {
            fieldBytes = sensorsWithoutTrackNumber;
            System.arraycopy(fieldBytes, 0, returnBytes, pos, fieldBytes.length);
            pos += fieldBytes.length;  
        }
        
        // Reference velocity
        if (refVelocity != null) {
            fieldBytes = refVelocity.getBytes();
            System.arraycopy(fieldBytes, 0, returnBytes, pos, fieldBytes.length);
            pos += fieldBytes.length;
        }
        
        // Supplementary track status
        if (supplementaryTrackStatus != null) {
            fieldBytes = supplementaryTrackStatus.toByteArray();
            System.arraycopy(fieldBytes, 0, returnBytes, pos, fieldBytes.length);
            pos += fieldBytes.length;
        }

        return returnBytes;        
    }
        
    //--------------------------------------------------------------------------
    // private data members

    /**
    * Contributing Sensors With Local Tracknumbers
    */
    private byte[] m_sensorsWithTrackNumber;

    /**
    * Contributing Sensors Without Local Tracknumbers
    */
    private byte[] m_sensorsWithoutTrackNumber;

    /**
    * The reference velocity vector
    */
    private TrackVelocity m_refVelocity;
    
    /**
    * The supplementary track status
    */
    private FixedLengthBitSet m_supplementaryTrackStatus;    
    
    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block
    
}
