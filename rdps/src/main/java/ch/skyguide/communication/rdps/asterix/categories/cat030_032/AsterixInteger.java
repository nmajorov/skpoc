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

package ch.skyguide.communication.rdps.asterix.categories.cat030_032;

//------------------------------------------------------------------------------

import java.math.BigInteger;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/*******************************************************************************
* Represents an integer embedded in an asterix message.
* 
* @author R. Wilson
*/
public class AsterixInteger
extends AbstractLoggingNonCompoundDataItem 
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * The public defined constructor.
    *  
    * @param    dataCategory
    *           The data category for this data item
    *            
    * @param    dataItemIdentifier
    *           The data item identifier.
    *           
    * @param    description
    *           Description of the data item and contents.
    *           
    * @param    units
    *           Units of the data item.
    *           
    * @param    integerLength
    *           The space requirements in bytes for the integer field. 
    */
    public AsterixInteger(short dataCategory, 
                          int dataItemIdentifier, 
                          String description, 
                          String units, 
                          int integerLength) 
    {
        super(dataCategory, dataItemIdentifier, integerLength, description, units);    
    }
    
    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Creates the associated data fields.
    * 
    * @param    repetitionFactor
    *           The repetition factor for the data item.
    *           
    * @return   The data field associated with the data items.
    * 
    */
    @Override
    public NonCompoundField createAssociatedDataField(int repetitionFactor) 
    {
        return new DefaultFixedDataField(m_sizeFactor, this, false);     
    }
    
    /***************************************************************************
    * Create the associated data field based on the information contained in the
    * byte buffer.
    * 
    * @param    inBuffer
    *           The input buffer containing the information.
    *           
    * @return   The <code>NonCompoundField</code> created from the data item and the 
    *           input buffer information.
    *           
    */
    @Override
    public NonCompoundField createAssociatedDataField(ByteBuffer inBuffer) 
    {
        return createAssociatedDataField(1);
    }
    
    /***************************************************************************
    *  Packs the data into the data field.
    *  
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    theByte
    *           The byte to pack into the data field.
    *           
    * @return   The packed <code>NonCompoundField</code>.
    * 
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(byte theByte) 
    {
       NonCompoundField dataField =  createAssociatedDataField(1);
       
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate( dataField.getFieldLengthIndicator() );
       
       try {
           
           tempBuffer.put(theByte);
           tempBuffer.flip();
           dataField.decode(tempBuffer);
       }
       catch(ReadOnlyBufferException exception)
       {   // The buffer should not be filled after reaching its limit.
           logError(BUFFER_OVERFLOW_MESSAGE);
           throw exception;
       }

       return dataField;     
    }
    
    /***************************************************************************
    * Packs the data into the data field.
    *  
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    theShort
    *           The short to pack into the data field.
    *           
    * @return   The packed <code>NonCompoundField</code>.
    * 
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(short theShort) 
    {
       NonCompoundField dataField = createAssociatedDataField(1);
       
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate(dataField.getFieldLengthIndicator());
       
       try {
           
           tempBuffer.putShort(theShort);
           tempBuffer.flip();
           dataField.decode(tempBuffer);
       }
       catch(BufferOverflowException exception)
       {   // The buffer should not be filled after reaching its limit.
           logError(BUFFER_OVERFLOW_MESSAGE);
           throw exception;
       }

       return dataField;     
    }
    
    /***************************************************************************
    * Packs the data into the data field.
    *  
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    theInteger
    *           The integer to pack into the data field.
    *           
    * @return   The packed <code>NonCompoundField</code>.
    * 
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(int theInteger) 
    {
       NonCompoundField dataField = createAssociatedDataField(1);
       
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate(dataField.getFieldLengthIndicator());
       
       try {
           tempBuffer.putInt(theInteger);
           tempBuffer.flip();
           dataField.decode(tempBuffer);
       }
       catch(BufferOverflowException exception)
       {   // The buffer should not be filled after reaching its limit.
           logError(BUFFER_OVERFLOW_MESSAGE);
           throw exception;
       }

       return dataField;     
    }
    
    /***************************************************************************
    * Packs the data into the data field.
    *  
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    theBigInteger
    *           The BigInteger to pack into the data field.
    *           
    * @return   The packed <code>NonCompoundField</code>.
    * 
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(BigInteger theBigInteger) 
    {
       NonCompoundField dataField = createAssociatedDataField(1);
       
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate(dataField.getFieldLengthIndicator());
       
       try {
           tempBuffer.put(theBigInteger.toByteArray());
           tempBuffer.flip();
           dataField.decode(tempBuffer);
           
       }
       catch(BufferOverflowException exception)
       {   // The buffer should not be filled after reaching its limit.
           logError(BUFFER_OVERFLOW_MESSAGE);
           throw exception;
       }

       return dataField;     
    }
    
    /***************************************************************************
    * Packs the data into the data field.
    *  
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    theLong
    *           The long to pack into the data field.
    *           
    * @return   The packed <code>NonCompoundField</code>.
    * 
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(long theLong) 
    {
       NonCompoundField dataField = createAssociatedDataField(1);
       
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate(dataField.getFieldLengthIndicator());
       
       try {
           
           tempBuffer.putLong( theLong );
           tempBuffer.flip();
           dataField.decode(tempBuffer);
       }
       catch(BufferOverflowException exception)
       {   // The buffer should not be filled after reaching its limit.
           logError(BUFFER_OVERFLOW_MESSAGE);
           throw exception;
       }

       return dataField;     
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.dataitem.genericstructure.dataitem.DataItem#toUnpackedString(ch.skyguide.message.structure.datafield.genericstructure.datafield.DataField)
     */
    @Override
    public String toUnpackedString(DataField field) {
        return getDescription()+"={" + unpack((NonCompoundField)field).toString() + "}";
    }       
    
    /***************************************************************************
    * Unpacks the integer data into a big integer.
    *  
    * @param    dataField
    *           The data field to be unpacked.
    *           
    * @return   The <code>BigInteger</code> containing the integer data.
    * 
    * @throws   BufferUnderflowException
    *           if more elements shall be read after reaching the buffer's
    *           limit.
    */
    public BigInteger unpack(NonCompoundField dataField) 
    {
       ByteBuffer tempBuffer = dataField.encode();
       
       byte[] array = new byte[tempBuffer.limit() - tempBuffer.position()];
       tempBuffer.get(array);

       BigInteger integerData = null;

       try {

           integerData = new BigInteger(array);
       }
       catch(BufferUnderflowException exception)
       {   // Trying to read after reaching the limit of the buffer.
           logError(BUFFER_UNDERFLOW_MESSAGE);
           throw exception;
       }    
       
       return integerData;     
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
