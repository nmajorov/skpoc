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
import java.util.Map;
import java.util.TreeMap;

import ch.skyguide.message.structure.AbstractGenericRecord;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.uap.UserApplicationProfile;

//------------------------------------------------------------------------------

/*******************************************************************************
* Contains the Asterix data fields.  Each field is associated with one and only 
* one data item as defined by the UAP.
* 
* @author R. Wilson
*/
public class AsterixRecord
extends AbstractGenericRecord
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * Public defined constructor for the record taking the field specification
    * and the data fields.
    *  
    * @param    fieldSpecification
    *           The field specification defining the data items within the data
    *           fields.
    *           
    * @param    dataFields
    *           A <code>Map</code> of <code>DataField</code>s containing the 
    *           information encoded based upon the data items.  They are stored
    *           using the field reference number as a key.
    */
    public AsterixRecord(AsterixFieldSpecificationType fieldSpecification, 
                         Map<Integer, DataField> dataFields) 
    {
        super(fieldSpecification, dataFields);
    }
    
    //--------------------------------------------------------------------------
    // public member functions

    //--------------------------------------------------------------------------
    // public static member functions
    /***************************************************************************
    * Decodes the byte buffer into a record based upon the user application
    * profile.
    *  
    * @param    inBuffer
    *           The byte buffer containing the encoded record.
    *           
    * @param    profile
    *           The user application profile describing how the bytes are 
    *           decoded based upon the data items and data fields.
    *           
    * @return   The Record decoded from the byte buffer.
    */
    public static AsterixRecord 
    decode(ByteBuffer inBuffer, UserApplicationProfile profile) 
    {
       // Determine the length of the Field Specification.
       AsterixFieldSpecificationType fieldSpecification = 
       new AsterixFieldSpecificationType(inBuffer);

       Map<Integer, DataField> dataFields = new TreeMap<>();
       
       for(int fieldReferenceNumber = fieldSpecification.rewind(); 
           fieldReferenceNumber != -1 && 
           fieldReferenceNumber <= fieldSpecification.end();) {
           
           DataField dataField = 
           profile.getDataItem(fieldReferenceNumber).
           createAssociatedDataField(inBuffer);
           
           dataField.decode(inBuffer);
            
           dataFields.put(Integer.valueOf(fieldReferenceNumber), dataField); 
            
           fieldReferenceNumber = fieldSpecification.nextFieldReferenceNumber();
       }
       
       return new AsterixRecord(fieldSpecification, dataFields);     
    }
    
    
    /***************************************************************************
    * Encodes the record into a byte buffer.
    *  
    * @return The <code>ByteBuffer</code> containing the encoded record.
    */
    @Override
    public ByteBuffer encode() 
    {
       ByteBuffer recordBuffer = 
       ByteBuffer.allocate(getFieldLengthIndicator());
       
       // Encode the field specification.
       recordBuffer.put(m_fieldSpecification.toByteArray());
              
       // Encode the Data fields.
       for(int fieldspecificationNumber = m_fieldSpecification.rewind();
    		   fieldspecificationNumber != -1; 
    		   fieldspecificationNumber = m_fieldSpecification.nextFieldReferenceNumber()) {
    	   
    	   DataField dataField = m_dataFields.get(Integer.valueOf(fieldspecificationNumber));
    	   
           recordBuffer.put(dataField.encode());
       }

       recordBuffer.flip();
       
       return recordBuffer.asReadOnlyBuffer();     
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
