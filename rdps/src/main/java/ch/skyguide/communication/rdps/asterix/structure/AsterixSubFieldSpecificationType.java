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

package ch.skyguide.communication.rdps.asterix.structure;

//------------------------------------------------------------------------------

import java.nio.ByteBuffer;

import static ch.skyguide.util.contract.ConditionUtils.preconditionNotNull;
import ch.skyguide.util.data.FixedLengthBitSet;
import ch.skyguide.util.data.FixedLengthBitSetFactory;

//------------------------------------------------------------------------------

/*******************************************************************************
* The class represents a subtype specification type
* 
* @author Guillaume Fouillet
*/
public class AsterixSubFieldSpecificationType
implements Cloneable
{
    
    //--------------------------------------------------------------------------
    // public static final data members (constants)
    /***************************************************************************
    * Public static factory method to create a {@code AsterixSubFieldSpecificationType}
    * from a given field Specification
    * 
    * @param fieldSpecication
    *        the field specification to set
    *        
    * @return the created {@code AsterixSubFieldSpecificationType}
    */
    public static final AsterixSubFieldSpecificationType
    typeOf(FixedLengthBitSet fieldSpecification) {
        return new AsterixSubFieldSpecificationType(fieldSpecification);
    }
    
    /***************************************************************************
    * Public static factory method to create a {@code AsterixSubFieldSpecificationType}
    * from a given Buffer
    * 
    * @param    inBuffer
    *           Creates the Field Specification from a byte buffer.
    *        
    * @return the created {@code AsterixSubFieldSpecificationType}
    */
    public static final AsterixSubFieldSpecificationType
    typeOf(ByteBuffer inBuffer,int maxReadBufferLength) 
    {
        if(inBuffer == null) {
        
            throw new NullPointerException("field specification not initialized");
        }
        //----------------------------------------------------------------------
        byte[] readBytes = new byte[maxReadBufferLength];
        int maxSize = maxReadBufferLength;
        short field;
        for (int i=0;i<maxReadBufferLength;i++) {
            readBytes[i] = inBuffer.get();
            field = (short)(readBytes[i]& 0xFF);
            if ((field & 0x01) != 0x01) {
                maxSize = i+1;
                break;
            }            
         }
        byte[] specification = new byte[maxSize];
        System.arraycopy(readBytes, 0, specification, 0, maxSize);
        
        return new AsterixSubFieldSpecificationType(FixedLengthBitSetFactory.instance().toFixedLengthBitSet(specification));   
    }
    
    //--------------------------------------------------------------------------
    // public constructors
    
    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Retrieves the number of bits set in the field specification.
    * 
    * @return The number of bits set in the field specification.
    */
    public int cardinality() 
    {
        int cardinality = 0;
        
        for(int bit = 1; bit <= end(); bit++) {
            
            if(m_fieldSpecification.get(bit-1) && 
               bit%8 != 0) {
               
                cardinality++;
            }
        }
        
        return cardinality;     
    }
    
    /***************************************************************************
    * Creates and returns a copy of this object.
    * The method performs a "deep copy" of this object.
    * By convention, the object returned by this method should be independent
    * of this object (which is being cloned). Typically, this means copying any
    * mutable objects that comprise the internal "deep structure" of the object
    * being cloned and replacing the references to these objects with
    * references to the copies.
    * 
    * See also description of class <code>Object</code>.
    * @see java.lang.Object
    * @see java.lang.Cloneable
    * 
    * @return   A "deep copy" of this object.
    */
    @Override
    public AsterixSubFieldSpecificationType
    clone()
    {
        //----------------------------------------------------------------------
        // Obtain an object by invoking the base class' clone method.
        AsterixSubFieldSpecificationType clonedSpecification;
        
        try {
            clonedSpecification = (AsterixSubFieldSpecificationType)super.clone();
        }
        catch (CloneNotSupportedException e) {
            
            throw new UnsupportedOperationException(e.getMessage());
        }

        //----------------------------------------------------------------------
        // Copy attributes.
        clonedSpecification.m_fieldSpecification = m_fieldSpecification.clone(); 
        
        return clonedSpecification;
    }
    
    /***************************************************************************
    * Retrieves the last Field Reference Number (FRN) in the Specification.
    * 
    * @return The last field reference number set in the specification.
    */
    public int end() 
    {
        return m_fieldSpecification.getFixedLength();     
    }
    
    /***************************************************************************
    * Retrieves the number of bytes the field specification would occupy when
    * converted to an array of bytes.
    * 
    * @return   The number of bytes occupied by the byte array of the field
    *           specification
    */
    public int getBytes() 
    {
        // Determine size of the array in bytes.
        return 1+m_fieldSpecification.getFixedLength()/9;     
    }
//    
//    /***************************************************************************
//    * Retrieves the current position.
//    * 
//    * @return The current position within the specification.
//    */
//    public int getPosition() 
//    {
//        return m_position;     
//    }
    
    /***************************************************************************
    * Returns the unmodifiable Set of specification bit
    * @return the unmodifiable Set of specification bit
    */
    public FixedLengthBitSet
    getFieldSpecification() {
        return m_fieldSpecification;
    }
    
    /***************************************************************************
    * The logical space that the field specification occupies in bits.
    * 
    * @return   Logical space in bits. 
    */
    public int size() 
    {
       return m_fieldSpecification.getFixedLength();     
    }
    
    /***************************************************************************
    * Returns the FieldSpecification as a byte array.
    * 
    * @return The byte array containing the Field Specification.
    */
    public byte[] toByteArray() 
    {
        return m_fieldSpecification.toByteArray();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see java.lang.Object#toString()
     */
    @Override
    public String 
    toString() {
        StringBuilder str = new StringBuilder();
        str.append("Specification : " + m_fieldSpecification.toString());
        return str.toString();
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
    /***************************************************************************
    * Private constructor
    * 
    * 
    * Precondition: fieldSpecification != null
    *
    * @param fieldSpecication
    *        the field specification to set
    * @return the created {@code AsterixSubFieldSpecificationType}
    */
    private AsterixSubFieldSpecificationType(FixedLengthBitSet fieldSpecification) 
    {
        // Precondition
        preconditionNotNull(fieldSpecification, "fieldSpecification");
        m_fieldSpecification = fieldSpecification;
    }
    //--------------------------------------------------------------------------
    // private member functions

    //--------------------------------------------------------------------------
    // private static member functions

    //--------------------------------------------------------------------------
    // private data members

    /**
    * The field specification containing the set fields of the message.
    */
    private FixedLengthBitSet m_fieldSpecification;
    
    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block
}
