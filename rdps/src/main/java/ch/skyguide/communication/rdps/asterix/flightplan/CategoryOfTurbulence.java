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

import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.environment.aeronautical.navigation.WakeTurbulenceType;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/*******************************************************************************
* A class that represents the wake turbulence category of the flight.
* 
* @author R. Wilson
*/
public class CategoryOfTurbulence 
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
    public CategoryOfTurbulence(short dataCategory, 
                                int dataItemIdentifier, 
                                String description, 
                                String units) 
    {
        super(dataCategory, dataItemIdentifier, 1, description, units);     
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
    * Pack the wake turbulence category into the data field.
    *  
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    wakeTurbulenceCategory
    *           The wake turbulence category to be encoded in the data field.
    *           
    * @return   The <code>NonCompoundField</code> containing the packed data.
    * 
    * @throws   UnsupportedEncodingException
    *           if the required encoding is not supported. 
    * 
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(WakeTurbulenceType wakeTurbulenceCategory)
    throws UnsupportedEncodingException 
    {
       NonCompoundField dataField = createAssociatedDataField(1);
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate(dataField.getFieldLengthIndicator());
       
       try {
           
           tempBuffer.put(new String(new char[]{wakeTurbulenceCategory.
                                                getCharacterRepresentation()}).
                                                getBytes(USED_ENCODING));
           tempBuffer.flip();
           dataField.decode(tempBuffer);
       }
       catch(BufferOverflowException exception)
       {   // The buffer should not be filled after reaching its limit.
           logError(BUFFER_OVERFLOW_MESSAGE);
           throw exception;
       }
       catch(UnsupportedEncodingException exception)
       {
           logError(UNSUPPORTED_ENCODING_MESSAGE);
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
        return getDescription()+"={" + unpack((NonCompoundField)field).getDescription() + "}";
    }
    
    /***************************************************************************
    * Unpack the wake turbulence category from the data field.
    *  
    * @param    dataField
    *           The data field containing the wake turbulence category.
    *           
    * @return   The wake turbulence category from the data field.
    */
    public WakeTurbulenceType unpack(NonCompoundField dataField) 
    {
       ByteBuffer tempBuffer = dataField.encode();
       
       WakeTurbulenceType wakeTurbulenceCategory = null;
       
       try {
            
           wakeTurbulenceCategory = 
            WakeTurbulenceType.determineCategory(tempBuffer.getChar());           
       }
       catch(BufferUnderflowException exception)
       {   // Trying to read after reaching the limit of the buffer.
           logError(BUFFER_UNDERFLOW_MESSAGE);
           throw exception;
       }    
       
       return wakeTurbulenceCategory;     
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

    /***************************************************************************
     * The encoding for the data.
     */
    private static final String USED_ENCODING = "US-ASCII";
    

    /***************************************************************************
     * The required encoding is unsupported.
     */
    private static final String UNSUPPORTED_ENCODING_MESSAGE =
    "The encoding" + USED_ENCODING + "is not supported.";
    
    //--------------------------------------------------------------------------
    // Static initialisation block

}
