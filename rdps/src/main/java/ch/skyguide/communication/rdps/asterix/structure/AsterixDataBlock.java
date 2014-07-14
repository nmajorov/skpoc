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
import java.util.ArrayList;
import java.util.List;

import ch.skyguide.message.structure.AbstractGenericDataBlock;
import ch.skyguide.message.structure.GenericRecord;
import ch.skyguide.message.structure.uap.UserApplicationProfile;


//------------------------------------------------------------------------------

/*******************************************************************************
* Models a block of data within the binary format protocol.  The application 
* data to be transmitted over the communication medium shall consist of either 
* one or a concatenation of consecutive Data Blocks.  A Data Block shall 
* consist of:
*   - a one-octet field Data Category (CAT) indicating to which category the
*     data transmitted belongs;
*   - a two-octet field ILength Indicator (LEN) indicating the total length (in
*     octets) of the Data Block, including the CAT and LEN fields;
*   - one or more Record(s) containing data of the same category.
* 
* @author R. Wilson
*/
public class AsterixDataBlock
extends AbstractGenericDataBlock
{

    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * Public defined constructor taking the data category, field length
    * indicator and the list of reconds contained within the block.
    * 
    * @param    dataCategory
    *           The data categories define which type of data exchanged shall 
    *           be standardised and be the same for all users of ASTERIX.
    * 
    * @param    fieldLengthIndicator
    *           Indicates the total length (in octets) of the Data Block, 
    *           including the CAT and LEN fields.
    *            
    * @param    records
    *           one or more Record(s) containing data of the same category.
    */
    public AsterixDataBlock(short dataCategory, 
                            int fieldLengthIndicator, 
                            List<GenericRecord> records) 
    {
        super(dataCategory, fieldLengthIndicator, records);
    }
    
    //--------------------------------------------------------------------------
    // public member functions
//
//    /***************************************************************************
//    * Creates and returns a copy of this object.
//    * The method performs a "deep copy" of this object.
//    * By convention, the object returned by this method should be independent
//    * of this object (which is being cloned). Typically, this means copying any
//    * mutable objects that comprise the internal "deep structure" of the object
//    * being cloned and replacing the references to these objects with
//    * references to the copies.
//    * 
//    * See also description of class <code>Object</code>.
//    * @see java.lang.Object
//    * @see java.lang.Cloneable
//    * 
//    * @return   A "deep copy" of this object.
//    */
//    @Override
//    public AsterixDataBlock
//    clone()
//    {
//        //----------------------------------------------------------------------
//        // Obtain an object by invoking the base class' clone method.
//        AsterixDataBlock clonedDataBlock;
//        
//        try {
//            clonedDataBlock = (AsterixDataBlock)super.clone();
//        }
//        catch (CloneNotSupportedException e) {
//            
//            throw new UnsupportedOperationException(e.getMessage());
//        }
//
//        //----------------------------------------------------------------------
//        // Copy attributes.
//        clonedDataBlock.m_dataCategory = m_dataCategory;
//        clonedDataBlock.m_fieldLengthIndicator = m_fieldLengthIndicator;
//        
//        clonedDataBlock.m_records = new ArrayList<AsterixRecord>();
//        
//        for(AsterixRecord record : m_records) {
//            
//            clonedDataBlock.m_records.add(record.clone());
//        }
//            
//        return clonedDataBlock;
//    }

    //--------------------------------------------------------------------------
    // public static member functions

    /***************************************************************************
    * Decodes a Byte Buffer into an Asterix Data Block.
    * 
    * @param    inBuffer
    *           The byte buffer which shall be decoded into an Asterix Data 
    *           Block. 
    *           
    * @return   The decoded Asterix data block
    */
    public static AsterixDataBlock decode(ByteBuffer inBuffer) 
    {
        // Extract the first data category from the message.
        short dataCategory = (short)(((inBuffer.get())<<24)>>>24);

        // Extract two bytes and deposit in an integer.
        int fieldLengthIndicator = (((inBuffer.getShort())<<16)>>>16);
        
        AsterixDataBlock asterixDataBlock = 
        new AsterixDataBlock(dataCategory, fieldLengthIndicator, inBuffer);
       
        return asterixDataBlock;     
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.AbstractGenericDataBlock#encode()
     */
    @Override
    public ByteBuffer encode() 
    {
       ByteBuffer dataBlockBuffer = 
       ByteBuffer.allocate(getFieldLengthIndicator());
       
       dataBlockBuffer.put((byte)getDataCategory());
       dataBlockBuffer.putShort((short) getFieldLengthIndicator());
       
       for (GenericRecord currentRecord : m_records)
       {
           dataBlockBuffer.put(currentRecord.encode());
       }
       
       dataBlockBuffer.flip();
       
       return dataBlockBuffer.asReadOnlyBuffer();     
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
    * Private defined constructor taking the data category, field length
    * indicator and the byte buffer directly to the message.
    *  
    * @param    dataCategory
    *           The data categories define which type of data exchanged shall 
    *           be standardised and be the same for all users of ASTERIX.
    *           
    * @param    fieldLengthIndicator
    *           Indicates the total length (in octets) of the Data Block, 
    *           including the CAT and LEN fields.
    *            
    * @param    inBuffer
    *           The direct byte buffer to the message.
    */
    private AsterixDataBlock(short dataCategory, 
                             int fieldLengthIndicator, 
                             ByteBuffer inBuffer) 
    {
        this(dataCategory, fieldLengthIndicator, new ArrayList<GenericRecord>());
        
        UserApplicationProfile profile = 
        AsterixUserApplicationProfileManager.
        getInstance().getUserApplicationProfile(dataCategory);
        
        // Decode inBuffer
        while(inBuffer.remaining() != 0) {
            getRecords().add(AsterixRecord.decode(inBuffer, profile));
        }     
    }
    
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
