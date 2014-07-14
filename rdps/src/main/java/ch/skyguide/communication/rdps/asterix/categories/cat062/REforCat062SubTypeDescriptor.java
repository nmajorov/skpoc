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
 * @author fouillge, Didier Bérard
 */
public class REforCat062SubTypeDescriptor extends
        AbstractSpecificationSubTypeDescriptor
{

	//--------------------------------------------------------------------------
	// public static final data members (constants)
	//--------------------------------------------------------------------------
	// public constructors
	
	REforCat062SubTypeDescriptor()
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
        return ReservedExpansionFieldTypes.fieldOf(index).getDescription();
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getByteLength(int)
     */
    @Override
    public int getByteLength(int index)
    {
        return ReservedExpansionFieldTypes.fieldOf(index).getByteLength();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getFlagBit(int)
     */
    @Override
    public int getFlagBit(int index)
    {
        // Order is little endian
        return getSpecification().getFixedLength() - ReservedExpansionFieldTypes.fieldOf(index).getBigEndianFlagBit();
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
        return ReservedExpansionFieldTypes.values().length;
    }
    
    
    /***************************************************************************
    * See description of superclass/interface.
    * @see ch.skyguide.message.structure.descriptor.genericstructure.descriptor.AbstractSpecificationSubTypeDescriptor#getLastFieldIndex()
    */
    @Override
    public int getLastFieldIndex() {
        return ReservedExpansionFieldTypes.getLastFieldIndex();
    }
    
    
	//--------------------------------------------------------------------------
	// public static member functions
	/***************************************************************************
	* Returns the maximum size of the descriptor
	* 
	* @return the maximum size of the descriptor
	*/
	public static int
	getMaxSize() {
	    int size = MAX_LENGTH;
	    for (ReservedExpansionFieldTypes type : ReservedExpansionFieldTypes.values()) {
	        size += type.getByteLength();
	    }
	    return size;
	}
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
    public enum ReservedExpansionFieldTypes implements SpecificationSubTypes
    {
        //--------------------------------------------------------------------------
        // enum types
        CST("Contributing Sensors With Local Tracknumbers", 1, 0, 6),
        CSN("Contributing Sensors No Local Tracknumbers", 2, 1, 4),
        TVS("Calculated Track Velocity Relative to System Reference Point", 3, 2, 4),
        STS("Supplementary Track Status", 4, 3, 1);

        /***************************************************************************
         * Precondition:
         *
         * Postcondition:
         *
         * @return
         */
        @Override
        public int getBigEndianFlagBit()
        {
            return m_bigEndianflagBit;
        }
        
        /***************************************************************************
         * Precondition:
         *
         * Postcondition:
         *
         * @return
         */
        @Override
        public int getByteLength() 
        {
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
        public String getDescription() 
        {
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
        public int getFieldIndex() 
        {
            return m_fieldIndex;
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
        * Returns the {@code ReservedExpansionFieldTypes} which 
        * corresponds to the entered {@code index} 
        * @param index
        *           The index to check
        * @throws IllegalArgumentException
        *       If the type is not a valid type, this exception is raised
        * @return The {@code ReservedExpansionFieldTypes} which correspond to 
        *       the entered {@code index} 
        */
        public static 
        ReservedExpansionFieldTypes fieldOf(int index)
        throws IllegalArgumentException
        {
            if (index > getLastFieldIndex()) {
                throw new IllegalArgumentException("Illegal index " + index + 
                       "for SubType descriptor");
            }            
            for (ReservedExpansionFieldTypes descriptor : values()) {
                if (descriptor.m_fieldIndex == index) {
                    return descriptor;
                }
            }
            throw new IllegalArgumentException("Unknown field index for " +
                    "ReservedExpansionFieldTypes : " + index);
        }
        
        
        /***************************************************************************
         * Creates a new <code>LightingMessageSubType</code> instance 
         * with the String <code>type</code> and the char origin
         *
         * @param    description
         *           the description of the field
         * @param    fieldIndex
         *           the index of the field
         * @param    bigEndianflagBit
         *           the index 
         * @param    the length of the field
         */
        private ReservedExpansionFieldTypes(String description, 
                                            int fieldIndex,
                                            int bigEndianflagBit,
                                            int byteLength) 
        {
            m_description = description;
            m_fieldIndex = fieldIndex;
            m_bigEndianflagBit = bigEndianflagBit;            
            m_byteLength = byteLength;
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