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





/***************************************************************************
 * Class invariant:
 *
 * @author fouillge
 */
public class MeasuredInformationDataSubTypeDescriptor extends
        AbstractSpecificationSubTypeDescriptor
{

	//--------------------------------------------------------------------------
	// public static final data members (constants)
	//--------------------------------------------------------------------------
	// public constructors
	
	MeasuredInformationDataSubTypeDescriptor()
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
        return MeasuredInformationSubTypes.fieldOf(index).getDescription();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.SubTypeDescriptor#getFlagBit(int)
     */
    @Override
    public int getFlagBit(int index)
    {
        // Order is little endian
        return getSpecification().getFixedLength() - MeasuredInformationSubTypes.fieldOf(index).getBigEndianFlagBit();
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getByteLength(int)
     */
    @Override
    public int getByteLength(int index)
    {
        return MeasuredInformationSubTypes.fieldOf(index).getByteLength();
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
        return MeasuredInformationSubTypes.values().length;
    }
    
    
    /***************************************************************************
    * See description of superclass/interface.
    * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getLastFieldIndex()
    */
    @Override
    public int getLastFieldIndex() {
        return MeasuredInformationSubTypes.getLastFieldIndex();
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
    static final int MAX_LENGTH = 1;        
	
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
    public enum MeasuredInformationSubTypes
    {
        //--------------------------------------------------------------------------
        // enum types
        SID("Sensor Identification", 1, 0, 2),
        POS("Measured Position", 2, 1, 4),
        HEI("Measured 3-D Height", 3, 2, 2),
        MDC("Last measured Mode C code", 4, 3, 2),
        MDA("Last measured Mode 3/A code", 5, 4, 2),
        TYP("Report type", 6, 5, 1);
        
        /***************************************************************************
         * Precondition:
         *
         * Postcondition:
         *
         * @return
         */
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
        public String 
        getDescription() {
            return m_description;
        }
//        
        /***************************************************************************
         * Precondition:
         *
         * Postcondition:
         *
         * @return
         */
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
        MeasuredInformationSubTypes fieldOf(int index)
        throws IllegalArgumentException
        {
            if (index > getLastFieldIndex()) {
                throw new IllegalArgumentException("Illegal index " + index + 
                       "for SubType descriptor");
            }            
            for (MeasuredInformationSubTypes descriptor : values()) {
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
        MeasuredInformationSubTypes(String description, 
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