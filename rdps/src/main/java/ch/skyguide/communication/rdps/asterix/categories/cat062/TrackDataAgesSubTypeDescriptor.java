//------------------------------------------------------------------------------
// PROJECT:
//		skyguide BEAM
// LAYER:
//      Application Logic
// COPYRIGHT :
//      Copyright (c) 1998-2009 skyguide, swiss air navigation ltd, 
//						      Wangen bei Dübendorf, Switzerland
//      All rights reserved.
// REVISION:
//      $Id:$
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
package ch.skyguide.communication.rdps.asterix.categories.cat062;

import ch.skyguide.message.structure.descriptor.AbstractSpecificationSubTypeDescriptor;
import ch.skyguide.message.structure.descriptor.SpecificationSubTypes;




/***************************************************************************
 * Class invariant:
 *
 * @author fouillge
 */
public class TrackDataAgesSubTypeDescriptor extends
        AbstractSpecificationSubTypeDescriptor
{

	//--------------------------------------------------------------------------
	// public static final data members (constants)
		
	//--------------------------------------------------------------------------
	// public constructors
	
	TrackDataAgesSubTypeDescriptor()
    {
        // Do nothing
    }

    //--------------------------------------------------------------------------
	// public member functions
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SubTypeDescriptor#getDescription(int)
     */
    @Override
    public String getDescription(int index)
    {
        return TrackDataAgesSubTypes.fieldOf(index).getDescription();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SubTypeDescriptor#getFlagBit(int)
     */
    @Override
    public int getFlagBit(int index)
    {
        // Order is little endian
        return getSpecification().getFixedLength() - TrackDataAgesSubTypes.fieldOf(index).getBigEndianFlagBit();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getByteLength(int)
     */
    @Override
    public int getByteLength(int index)
    {
        return TrackDataAgesSubTypes.fieldOf(index).getByteLength();
    }
    

    /***************************************************************************
    * See description of superclass/interface.
    * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SubTypeDescriptor#getMaxPrimaryFieldLength()
    */
    @Override
    public int
    getMaxPrimaryFieldLength() {
        return MAX_LENGTH;
    }
    
    /***************************************************************************
    * See description of superclass/interface.
    * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SubTypeDescriptor#size()
    */
    @Override
    public int
    size() {
        return TrackDataAgesSubTypes.values().length;
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getLastFieldIndex()
     */
     @Override
     public int getLastFieldIndex() {
         return TrackDataAgesSubTypes.getLastFieldIndex();
     }
    
	//--------------------------------------------------------------------------
	// public static member functions
	
	//--------------------------------------------------------------------------
	// protected constructors
	
	//--------------------------------------------------------------------------
	// protected member functions
	
	//--------------------------------------------------------------------------
	// protected static member functions
	
	//--------------------------------------------------------------------------
	// protected data members
	
	//--------------------------------------------------------------------------
	// protected static data members
	
	//--------------------------------------------------------------------------
	// package member functions
     /**
      * ILength of the primary subfield
      */
     static final int MAX_LENGTH = 5;	
	//--------------------------------------------------------------------------
	// private constructors
	
	//--------------------------------------------------------------------------
	// private member functions
	
	//--------------------------------------------------------------------------
	// private static member functions
	
	//--------------------------------------------------------------------------
	// private data members
    /***************************************************************************
     * Subtypes defining the descriptor
     *
     * @author Guillaume Fouillet
     */
    public enum TrackDataAgesSubTypes implements SpecificationSubTypes
    {
        //--------------------------------------------------------------------------
        // enum types
        MFL("Measured Flight Ages", 1, 0, 1),
        MD1("Mode 1 Age", 2, 1, 1),
        MD2("Mode 2 Age", 3, 2, 1),
        MDA("Mode 3/A Age", 4, 3, 1),
        MD4("Mode 4 Age", 5, 4, 1),
        MD5("Mode 5 Age", 6, 5, 1),
        MHG("Magnetic Heading Age", 7, 6, 1),
        IAS("Indicated Airspeed/MacNo Age", 8, 8, 1),
        TAS("True Airspeed Age", 9, 9, 1),
        SAL("Selected Altitude Age", 10, 10, 1),
        FSS("Final State Selected Altitude Age", 11, 11, 1),
        TID("Trajectory Intent data Age", 12, 12, 1),
        COM("Communication /ACAS Capability and Flight Status Age", 13, 13, 1),
        SAB("Status reported by ADS-B Age", 14, 14, 1),
        ACS("ACAS Resolution Advisory Report Age", 15, 16, 1),
        BVR("Barometric Vertical Rate Age", 16, 17, 1),
        GVR("Geometric Vertical Rate Age", 17, 18, 1),
        RAN("Roll Angle Age", 18, 19, 1),
        TAR("Track Angle Rate Age", 19, 20, 1),
        TAN("Track Angle Age", 20, 21, 1),
        GSP("Ground speed Age", 21, 22, 1),
        VUN("Velocity Uncertainty Age", 22, 24, 1),
        MET("Meteorological Data Age", 23, 25, 1),
        EMC("Emitter Category Age", 24, 26, 1),
        POS("Position Age", 25, 27, 1),
        GAL("Geometric altitude Age", 26, 28, 1),
        PUN("Position Uncertainty Data Age", 27, 29, 1),
        MB("Mode S MB Data Age", 28, 30, 1),
        IAR("Indicated Airspeed Age", 29, 32, 1),
        MAC("Mac number Age", 30, 33, 1),
        BPS("Barometric Pressure Setting Age", 31, 34, 1);
        
        /***************************************************************************
        * Precondition:
        *
        * Postcondition:
        *
        * @return
        */
        @Override
        public int 
        getByteLength() {
            return m_byteLength;
        }
        
        /***************************************************************************
        * Precondition:
        *
        * Postcondition:
        *
        * @return
        */
        @Override
        public String 
        getDescription() {
            return m_description;
        }

        /***************************************************************************
         * Precondition:
         *
         * Postcondition:
         *
         * @return
         */
        @Override
        public int 
        getFieldIndex() {
            return m_fieldIndex;
        }
        
        /***************************************************************************
         * Precondition:
         *
         * Postcondition:
         *
         * @return
         */
        @Override
        public int 
        getBigEndianFlagBit() {
            return m_bigEndianflagBit;
        }

        /***************************************************************************
        * See description of superclass/interface.
        * @see java.lang.Enum#toString()
        */
        @Override
        public String toString() 
        {
            return m_description;
        }


        /***************************************************************************
        * Returns the last field index of the subtypes
        *
        * @return the last field index of the subtypes
        */
        public static int getLastFieldIndex() {
            return 
                values()[values().length-1].m_fieldIndex;
        }
        
        /***************************************************************************
        * Returns the {@code AircraftDerivedDataSubTypeDescriptor} which 
        * corresponds to the entered {@code index} 
        * @param index
        *           The index to check
        * @throws IllegalArgumentException
        *       If the type is not a valid type, this exception is raised
        * @return The {@code AircraftDerivedDataSubTypeDescriptor} which correspond to 
        *       the entered {@code index} 
        */
        public static 
        TrackDataAgesSubTypes fieldOf(int index)
        throws IllegalArgumentException
        {
            if (index > getLastFieldIndex()) {
                throw new IllegalArgumentException("Illegal index " + index + 
                       "for SubType descriptor");
            }            
            for (TrackDataAgesSubTypes descriptor : values()) {
                if (descriptor.m_fieldIndex == index) {
                    return descriptor;
                }
            }
            throw new IllegalArgumentException("Unknown field index for " +
                    "MeasuredInformationSubTypeDescriptor : " + index);
        }
        
        
        /***************************************************************************
         * Creates a new <code>LightingMessageSubType</code> instance 
         * with the String <code>type</code> and the char origin
         *
         * @param    type
         *           The literal value of the
         *           <code>type</code>.
         */
        private 
        TrackDataAgesSubTypes(String description, 
                                             int fieldIndex, 
                                             int bigEndianflagBit,
                                             int byteLength) 
        {
            m_description = description;
            m_fieldIndex = fieldIndex;
            m_byteLength = byteLength;
            m_bigEndianflagBit = bigEndianflagBit;
            
        }
        /**
        * The description of the enum
        */
        private final String m_description;
        /**
        * The index of the subfield in the 062/380 message
        */
        private final int m_fieldIndex;    
        /**
        * The byte length of he enum in the 062 message
        */
        private final int m_byteLength; 
        /**
        * The bit position of the flag of the enum in the 062 message
        */    
        private final int m_bigEndianflagBit;
    }   
	//--------------------------------------------------------------------------
	// private static data members
	
	//--------------------------------------------------------------------------
	// Static initialization block
    
}