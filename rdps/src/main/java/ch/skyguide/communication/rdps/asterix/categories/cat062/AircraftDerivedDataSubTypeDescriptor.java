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
public class AircraftDerivedDataSubTypeDescriptor extends
        AbstractSpecificationSubTypeDescriptor
{

	//--------------------------------------------------------------------------
	// public static final data members (constants)
	//--------------------------------------------------------------------------
	// public constructors
	
	AircraftDerivedDataSubTypeDescriptor()
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
        return AircraftDerivedDataSubTypes.fieldOf(index).getDescription();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SubTypeDescriptor#getFlagBit(int)
     */
    @Override
    public int getFlagBit(int index)
    {
        // Order is little endian
        return getSpecification().getFixedLength() - AircraftDerivedDataSubTypes.fieldOf(index).getBigEndianFlagBit();

    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getByteLength(int)
     */
    @Override
    public int getByteLength(int index)
    {
        return AircraftDerivedDataSubTypes.fieldOf(index).getByteLength();
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
        return AircraftDerivedDataSubTypes.values().length;
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getLastFieldIndex()
     */
    @Override
    public int getLastFieldIndex() {
        return AircraftDerivedDataSubTypes.getLastFieldIndex();
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
     static final int MAX_LENGTH = 4;     
	
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
    public enum AircraftDerivedDataSubTypes implements SpecificationSubTypes
    {
        //--------------------------------------------------------------------------
        // enum types
        ADR("Target Address", 1, 0, 3),
        ID("Target Identification", 2, 1, 6),
        MHG("Magnetic Heading", 3, 2, 2),
        IAS("Indicated airspeed/Mach number", 4, 3, 2),
        TAS("True airspeed", 5, 4, 2),
        SAL("Selected Altitude", 6, 5, 2),
        FSS("Final State Selected Altitude", 7, 6, 2),
        TIS("Trajectory Intent Status", 8, 8, 1),
        TID("Trajectory Intent Data", 9, 9, 15),
        COM("Communication /ACAS Capability anf Flight Status", 10, 10, 2),
        SAB("Status reported by ADS-B", 11, 11, 2),
        ACS("ACAS Resolution Advisory Report", 12, 12, 7),
        BVR("Barometric Vertical Rate", 13, 13, 2),
        GVR("Geometric Vertical Rate", 14, 14, 2),
        RAN("Roll Angle", 15, 16, 2),
        TAR("Track Angle Rate", 16, 17, 2),
        TAN("Track Angle", 17, 18, 2),
        GSP("Ground speed", 18, 19, 2),
        VUN("Velocity Uncertainty", 19, 20, 1),
        MET("Meteorological Data", 20, 21, 8),
        EMC("Emitter Category", 21, 22, 1),
        POS("Position", 22, 24, 6),
        GAL("Geometric altitude", 23, 25, 2),
        PUN("Position Uncertainty Data", 24, 26, 1),
        MB("Mode S MB Data", 25, 27, 8),
        IAR("Indicated Airspeed", 26, 28, 2),
        MAC("Mach Number", 27, 29, 2),
        BPS("Barometric Pressure Setting", 28, 30, 2);
        
        
        /***************************************************************************
         * See description of superclass/interface.
         * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SpecificationSubTypes#getByteLength()
         */
        @Override
        public int 
        getByteLength() {
            return m_byteLength;
        }
        
        /***************************************************************************
         * See description of superclass/interface.
         * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SpecificationSubTypes#getDescription()
         */
        @Override
        public String 
        getDescription() {
            return m_description;
        }
//        
        /***************************************************************************
         * See description of superclass/interface.
         * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SpecificationSubTypes#getFieldIndex()
         */
        @Override
        public int 
        getFieldIndex() {
            return m_fieldIndex;
        }
        
        /***************************************************************************
         * See description of superclass/interface.
         * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SpecificationSubTypes#getBigEndianFlagBit()
         */
        @Override
        public int 
        getBigEndianFlagBit() {
            return m_bigEndianFlagBit;
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
            return values()[values().length-1].m_fieldIndex;
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
        AircraftDerivedDataSubTypes fieldOf(int index)
        throws IllegalArgumentException
        {
            if (index > getLastFieldIndex()) {
                throw new IllegalArgumentException("Illegal index " + index + 
                       "for SubType descriptor");
            }            
            for (AircraftDerivedDataSubTypes descriptor : values()) {
                if (descriptor.m_fieldIndex == index) {
                    return descriptor;
                }
            }
            throw new IllegalArgumentException("Unknown field index for " +
                    "AircraftDerivedDataSubTypeDescriptor : " + index);
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
        AircraftDerivedDataSubTypes(String description, 
                                             int fieldIndex, 
                                             int bigEndianFlagBit,
                                             int byteLength) 
        {
            m_description = description;
            m_fieldIndex = fieldIndex;
            m_byteLength = byteLength;
            m_bigEndianFlagBit = bigEndianFlagBit;
            
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
        private final int m_bigEndianFlagBit;
    }	
	//--------------------------------------------------------------------------
	// private static data members
	
	//--------------------------------------------------------------------------
	// Static initialization block
    
}