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

package ch.skyguide.communication.rdps.asterix.flightplan;

//------------------------------------------------------------------------------

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.DefaultRepetitiveDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/*******************************************************************************
* Represents all the SSR Codes allocated to the flight.
* 
* @author R. Wilson
*/
public class AllocatedSSRCodes 
extends AbstractLoggingNonCompoundDataItem 
{
    
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * Public defined constructor 
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
    */
    public AllocatedSSRCodes(short dataCategory, 
                             int dataItemIdentifier, 
                             String description, 
                             String units) 
    {
        super(dataCategory, dataItemIdentifier, 2, description, units);     
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
       List<NonCompoundField> associatedFields = new ArrayList<>();
       
       for(int index = 0; index < repetitionFactor; index++) {
           
           associatedFields.add(new DefaultFixedDataField(m_sizeFactor, this, false));
       }
              
       return new DefaultRepetitiveDataField(this, associatedFields);     
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
       int repititionFactor = ((inBuffer.get()<< 24)>>>24);
       
       return createAssociatedDataField(repititionFactor);     
    }
    
    /***************************************************************************
    * Pack the codes into the data field.
    * 
    * Precondition: allocatedCodes != null
    * 
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    allocatedCodes
    *           The allocated SSR codes to be encoded into the data field.
    *           
    * @return   The <code>NonCompoundField</code> containing the packed data.
    * 
    * @throws   IllegalArgumentException
    *           if the parameter allocatedCodes is null.
    *           
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(short[] allocatedCodes) 
    {
        // Validate the precondition.
        if(allocatedCodes == null)
        {
            
            throw new IllegalArgumentException
            ("The parameter allocatedCodes must not be null");
        }
              
       NonCompoundField repetitiveDataField = 
       createAssociatedDataField(allocatedCodes.length);
                     
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate(repetitiveDataField.getFieldLengthIndicator());

        try {
            
           tempBuffer.put((byte) allocatedCodes.length);

           for(int index = 0; index < allocatedCodes.length; index++) {
               
               tempBuffer.putShort(allocatedCodes[index]);
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
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.dataitem.genericstructure.dataitem.DataItem#toUnpackedString(ch.skyguide.message.structure.datafield.genericstructure.datafield.DataField)
     */
    @Override
    public String toUnpackedString(DataField field) {
        StringBuilder str = new StringBuilder(getDescription()+"={");
        for (short code : unpack((NonCompoundField)field)) {
            str.append(code + ",");
        }
        return str.toString();
    }       
    
    
    /***************************************************************************
    * Unpack the allocated SSR codes from the data field.
    *  
    * @param    dataField
    *           The data field containing the allocated SSR codes.
    *           
    * @return   The allocated SSR codes from the data field.
    * 
    * @throws   BufferUnderflowException
    *           if more elements shall be read after reaching the buffer's
    *           limit.
    */
    public short[] unpack(NonCompoundField dataField) 
    {
        ByteBuffer tempBuffer = dataField.encode();
        short[] allocatedCodes = null;

        try {
            
           int repetitionFactor = ((tempBuffer.get()<<24)>>>24);

           allocatedCodes = new short[repetitionFactor];

           for(int index = 0; index < repetitionFactor; index++) {
               
               allocatedCodes[index] = tempBuffer.getShort();
           }
        }
        catch(BufferUnderflowException exception)
        {   // Trying to read after reaching the limit of the buffer.
            logError(BUFFER_UNDERFLOW_MESSAGE);
            throw exception;
        }    
        
        return allocatedCodes;     
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
