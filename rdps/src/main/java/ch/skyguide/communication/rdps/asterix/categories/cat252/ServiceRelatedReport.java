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

package ch.skyguide.communication.rdps.asterix.categories.cat252;

//------------------------------------------------------------------------------

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import ch.skyguide.message.structure.datafield.NonCompoundField;
import ch.skyguide.message.structure.dataitem.AbstractRepetitiveDataItem;

//------------------------------------------------------------------------------

/*******************************************************************************
* Represents a report message sent by the server to the user in relation to a 
* connection or a service.
* 
* @author R. Wilson
*/
public class ServiceRelatedReport 
extends AbstractRepetitiveDataItem 
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
    * @param    sizeFactor
    *           Size factor of the item in bytes.
    *           
    * @param    description
    *           Description of the data item and contents.
    *           
    * @param    units
    *           Units of the data item.
    */
    public ServiceRelatedReport(short dataCategory, 
                                int dataItemIdentifier, 
                                String description, 
                                String units) 
    {
        super(dataCategory, dataItemIdentifier, description, units, 2);    
    }
    
    //--------------------------------------------------------------------------
    // public member functions
    /***************************************************************************
    * Packs the data into the data field.
    *  
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    serviceRelatedReports
    *           The nature and code packed into a short (24 bits).
    *  
    * @return   The packed <code>NonCompoundField</code>.
    * 
    * @throws   IllegalArgumentException
    *           if the parameter allocatedCodes is null.
    *           
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(short[] serviceRelatedReports) 
    {
        // Validate the precondition.
        if(serviceRelatedReports == null)
        {
            throw new IllegalArgumentException
            ("The parameter serviceRelatedReports must not be null");
        }
              
       NonCompoundField repetitiveDataField = 
       createAssociatedDataField(serviceRelatedReports.length);
                     
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate(repetitiveDataField.getFieldLengthIndicator());

        try {
            
           tempBuffer.put((byte)serviceRelatedReports.length);

           for(int index = 0; index < serviceRelatedReports.length; index++) {
               
               tempBuffer.putShort(serviceRelatedReports[index]);
           }
           
           tempBuffer.flip();
           repetitiveDataField.decode(tempBuffer);

        }
        catch(BufferOverflowException exception)
        {   // The buffer should not be filled after reaching its limit.
            logError(BUFFER_OVERFLOW_MESSAGE);
            throw exception;
        }

        return repetitiveDataField;     
    }
    
    /***************************************************************************
    * Unpacks the service identification into a bit set containing the
    * services.
    *  
    * @param    dataField
    *           The data field to be unpacked.
    *           
    * @return   The <code>BitSet</code> containing the integer data.
    * 
    * @throws   BufferUnderflowException
    *           if more elements shall be read after reaching the buffer's
    *           limit.
    */
    public short[] unpack(NonCompoundField dataField) 
    {
       ByteBuffer tempBuffer = dataField.encode();
       
       short[] serviceRelatedReports = null;
       
       try {

           int repetitionFactor = (tempBuffer.get()<<24)>>>24;

           serviceRelatedReports = new short[repetitionFactor];

           for(int index = 0; index < repetitionFactor; index++) {
               
               serviceRelatedReports[index] = tempBuffer.getShort();
           }
       }
       catch(BufferUnderflowException exception)
       {   // Trying to read after reaching the limit of the buffer.
           logError(BUFFER_UNDERFLOW_MESSAGE);
           throw exception;
       }    
       
       return serviceRelatedReports;     
    }

    //--------------------------------------------------------------------------
    // public static member functions

    //--------------------------------------------------------------------------
    // protected constructors

    //--------------------------------------------------------------------------
    // protected member functions
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.dataitem.AbstractDataItem#logError(java.lang.String)
     */
    @Override
    protected void logError(String logMessage)
    {
        // TODO: rjw, talk with GFO logging concept insufficient.
//        LogObject logObject = new LogObject("", "", logMessage);
//        Logger.instance().error(ILogger.EVENT, logObject);
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
