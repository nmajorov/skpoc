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

import static ch.skyguide.util.data.ByteManipulationUtil.convertToByteArray;

import static ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAgesSubTypeDescriptor.TrackDataAgesSubTypes.GSP;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAgesSubTypeDescriptor.TrackDataAgesSubTypes.TAN;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAgesSubTypeDescriptor.TrackDataAgesSubTypes.TAR;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAgesSubTypeDescriptor.TrackDataAgesSubTypes.FSS;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAgesSubTypeDescriptor.TrackDataAgesSubTypes.SAL;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAgesSubTypeDescriptor.TrackDataAgesSubTypes.IAR;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAgesSubTypeDescriptor.TrackDataAgesSubTypes.IAS;
import static ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAgesSubTypeDescriptor.TrackDataAgesSubTypes.MAC;

import java.nio.ByteBuffer;

import ch.skyguide.communication.rdps.asterix.categories.cat062.TrackDataAgesSubTypeDescriptor.TrackDataAgesSubTypes;
import ch.skyguide.message.structure.container.AbstractSpecificationTypeDataItemContainer;
import ch.skyguide.message.structure.descriptor.SpecificationSubTypeDescriptor;


//------------------------------------------------------------------------------

/*******************************************************************************
* Container class for the asterix 062/295 Track data ages
*  
* @author Guillaume Fouillet
*/
public class TrackDataAgesContainer extends AbstractSpecificationTypeDataItemContainer<Integer>
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
    public TrackDataAgesContainer(ByteBuffer buffer, 
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
        int value;
        for (TrackDataAgesSubTypes type : TrackDataAgesSubTypes.values()) {
            if (isFieldActive(type.getFieldIndex())) {
                assert null != getAgeMap().get(type.name());
                value = getAgeMap().get(type.name()).intValue();
                fieldBytes = 
                    convertToByteArray(value, type.getByteLength());
                System.arraycopy(fieldBytes, 0, returnBytes, currPos, fieldBytes.length);
                currPos += fieldBytes.length;                   
            }
        }        
        return returnBytes;
    }
    
    /***************************************************************************
    * Set the final state selected altitude age information if it doesn't exist yet
    * Do it based on the SAL (selected altitude) or the first existing age
    * Returns true if the field had been added
    * 
    * @return true if the field had been added
    */       
    public boolean
    addFinalStateSelectedAltitudeAge() 
    {
        return addField(FSS, getAgeMap().get(SAL.name()));
    }
    
    /***************************************************************************
    * Set the ground speed age information if it doesn't exist yet
    * Do it based on the TAR (track angle rate) or the first existing age
    * Returns true if the field had been added
    * 
    * @return true if the field had been added
    */
    public boolean 
    addGroundSpeedAge() 
    {
        return addField(GSP, getAgeMap().get(TAR.name()));
    }    
    
    /***************************************************************************
    * Set the indicated airspeed age information if it doesn't exist yet
    * Do it based on the IAS (IndicatedAirSpeed/MacNo) or the first existing age
    * Returns true if the field had been added
    * 
    * @return true if the field had been added
    */       
    public boolean
    addIndicatedAirspeedAge() 
    {
        return setField(IAR, getAgeMap().get(IAS.name()));
    }
    
    
    /***************************************************************************
    * Set the Mach number age information if it doesn't exist yet
    * Do it based on the IAS (IndicatedAirSpeed/MacNo) or the first existing age
    * Returns true if the field had been added
    * 
    * @return true if the field had been added
    */       
    public boolean
    addMacNoAge() 
    {
        return setField(MAC,  getAgeMap().get(IAS.name()));
    }
    
    /***************************************************************************
    * Set the track angle age information if it doesn't exist yet
    * Do it based on the TAR (track angle rate) or the first existing age
    * Returns true if the field had been added
    * 
    * @return true if the field had been added
    */
    public boolean 
    addTrackAngleAge() 
    {
        return addField(TAN, getAgeMap().get(TAR.name()));
    }
    
    /***************************************************************************
    * See description of superclass/interface.
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() 
    {
        StringBuilder str = new StringBuilder("TrackDataAges={");
        for (String key : getAgeMap().keySet()) {
            str.append(key +" : " + getAgeMap().get(key).intValue()/4 + " s,\t");
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
    * See description of superclass/interface.
    * @see ch.skyguide.beam.communication.rdps.asterix.genericstructure.container.SpecificationTypeDataItemContainer#deserializeFields(java.nio.ByteBuffer)
    */
    @Override
    protected void 
    deserializeFields(ByteBuffer buffer) 
    {
        int initValue;
        for (TrackDataAgesSubTypes type : TrackDataAgesSubTypes.values()) {
            if (isFieldActive(type.getFieldIndex())) {
                initValue = 0;
                if (getAgeMap().get(type.name()) != null) {
                    initValue = getAgeMap().get(type.name()).intValue();
                }
                setAge(type.name(), Integer.valueOf(deserializeIntField(buffer, 
                       type.getFieldIndex(), initValue)));
            }
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

    //--------------------------------------------------------------------------
    // private data members
    
    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block
    
}
