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
public class SystemTrackUpdateAgesSubTypeDescriptor extends
        AbstractSpecificationSubTypeDescriptor
{

	//--------------------------------------------------------------------------
	// public static final data members (constants)
	//--------------------------------------------------------------------------
	// public constructors
	
	SystemTrackUpdateAgesSubTypeDescriptor()
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
        return SystemTrackUpdateAgesSubTypes.fieldOf(index).getDescription();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SubTypeDescriptor#getFlagBit(int)
     */
    @Override
    public int getFlagBit(int index)
    {
        // Order is little endian
        return getSpecification().getFixedLength() - SystemTrackUpdateAgesSubTypes.fieldOf(index).getBigEndianFlagBit();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getByteLength(int)
     */
    @Override
    public int getByteLength(int index)
    {
        return SystemTrackUpdateAgesSubTypes.fieldOf(index).getByteLength();
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
        return SystemTrackUpdateAgesSubTypes.values().length;
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getLastFieldIndex()
     */
     @Override
     public int getLastFieldIndex() {
         return SystemTrackUpdateAgesSubTypes.getLastFieldIndex();
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
    static final int MAX_LENGTH = 2;        
	
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
    public enum SystemTrackUpdateAgesSubTypes implements SpecificationSubTypes
    {
        //--------------------------------------------------------------------------
        // enum types
        TRK("Track Age", 1, 0, 1),
        PSR("Primary surveillance radar Age", 2, 1, 1),
        SSR("Secondary surveillance radar Age", 3, 2, 1),
        MDS("Mode S Age", 4, 3, 1),
        ADS("ADS-C Age", 5, 4, 2),
        ES("ADS-B Extended Squitter Age", 6, 5, 1),
        VDL("ADS-B VDL Mode 4 Age", 7, 6, 1),
        UAT("ADS-B UAT Age", 8, 8, 1),
        LOP("Loop Age", 9, 9, 1),
        MLT("Multilateration Age", 10, 10, 1);
        
        
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
        SystemTrackUpdateAgesSubTypes fieldOf(int index)
        throws IllegalArgumentException
        {
            if (index > getLastFieldIndex()) {
                throw new IllegalArgumentException("Illegal index " + index + 
                       "for SubType descriptor");
            }            
            for (SystemTrackUpdateAgesSubTypes descriptor : values()) {
                if (descriptor.m_fieldIndex == index) {
                    return descriptor;
                }
            }
            throw new IllegalArgumentException("Unknown field index for " +
                    "MeasuredInformationSubTypeDescriptor : " + index);
        }
        
        
        /***************************************************************************
         * Creates a new <code>SystemTrackUpdateAgesSubTypes</code> instance 
         * with the String <code>type</code> and the char origin
         *
         * @param    type
         *           The literal value of the
         *           <code>type</code>.
         */
        private 
        SystemTrackUpdateAgesSubTypes(String description, 
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