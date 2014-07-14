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

package ch.skyguide.communication.rdps.asterix.categories.cat062;

//------------------------------------------------------------------------------

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/**
 * @author Guillaume Fouillet
 */
public class CalculatedTrackVelocity 
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
    public CalculatedTrackVelocity(short dataCategory, 
                       int dataItemIdentifier, 
                       String description, 
                       String units) 
    {
        super(dataCategory, dataItemIdentifier, 4, description, units);     
    }
    
    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Creates the associated data fields.
    * 
    * @param    p_inBuffer
    *           The input buffer containing the information.
    *           
    * @return   The data field associated with the data items.
    * 
    */
    @Override
    public NonCompoundField createAssociatedDataField(ByteBuffer p_inBuffer) 
    {
        return createAssociatedDataField(1);     
    }
    
    /***************************************************************************
    * Create the associated data field based on the information contained in the
    * byte buffer.
    * 
    * @param    p_iRepetitionFactor
    *           The repetition factor for the data item.
    *           
    * @return   The <code>NonCompoundField</code> created from the data item and the 
    *           input buffer information.
    *           
    */
    @Override
    public NonCompoundField createAssociatedDataField(int p_iRepetitionFactor) 
    {
        return new DefaultFixedDataField(m_sizeFactor, this, false);     
    }
    
    /***************************************************************************
    * Packs the data into the data field.
    *  
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    mode3ACode
    *           The mode 3A code for the flight.
    *           
    * @return   The <code>NonCompoundField</code> containing the packed data.
    * 
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(TrackVelocity trackVelocity) 
    {
       NonCompoundField dataField = createAssociatedDataField(1);
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate(dataField.getFieldLengthIndicator());
       
       try {
           
           tempBuffer.put(trackVelocity.getBytes());
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
        return getDescription()+"={" + unpack((NonCompoundField)field) + "}";
    }       
    
    /***************************************************************************
    * Unpack the data from the data field.
    * 
    * @param    dataField
    *           The data field containing the data to be unpacked.
    *           
    * @return   The mode 3A code for the flight.
    * 
    * @throws   BufferUnderflowException
    *           if more elements shall be read after reaching the buffer's
    *           limit.
    */
    public TrackVelocity unpack(NonCompoundField dataField) 
    {
       ByteBuffer tempBuffer = dataField.encode();
       TrackVelocity trackVelocity;
       
       try {
           // unit of the track velocity for cat062 is 0.25 m/s
           trackVelocity = new TrackVelocity(tempBuffer, 0.25);
           
       }
       catch(BufferUnderflowException exception)
       {   // Trying to read after reaching the limit of the buffer.
           logError(BUFFER_UNDERFLOW_MESSAGE);
           throw exception;
       }    
       
       return trackVelocity;     
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
