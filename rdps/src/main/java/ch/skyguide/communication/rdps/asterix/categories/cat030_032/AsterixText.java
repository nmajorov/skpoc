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

import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/*******************************************************************************
* Represents text packed into an asterix message.
* 
* @author R. Wilson
*/
public class AsterixText 
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
    *           
    * @param    textLength
    *           Limiting length of the text.
    */
    public AsterixText(short dataCategory, 
                       int dataItemIdentifier, 
                       String description, 
                       String units, 
                       int textLength) 
    {
        super(dataCategory, dataItemIdentifier, textLength, description, units);    
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
    * Packs the text into the data field.
    *  
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    text
    *           The text to be packed into the data field.
    *           
    * @return   The <code>NonCompoundField</code> containing the packed text.
    * 
    * @throws   UnsupportedEncodingException
    *           if the needed encoding is not supported. 
    *           
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(String text)
    throws UnsupportedEncodingException 
    {
        String rawText = text;

        NonCompoundField dataField = createAssociatedDataField(1);

        ByteBuffer tempBuffer = 
            ByteBuffer.allocate(dataField.getFieldLengthIndicator());

        try {

            while(rawText.length() < dataField.getFieldLengthIndicator()) {

                // Pad with spaces.
                rawText = rawText.concat(" ");
            }

            // Cannot be more than the allocated length, therefore truncate.
            rawText = rawText.substring( 0, dataField.getFieldLengthIndicator());

            tempBuffer.put(rawText.getBytes(USED_ENCODING));

            tempBuffer.flip();
            dataField.decode(tempBuffer);
        }
        catch(BufferOverflowException exception)
        {   // The buffer should not be filled after reaching its limit.
            logError(BUFFER_OVERFLOW_MESSAGE);
            throw exception;
        }
        catch(UnsupportedEncodingException exception)
        {   // The requested encoding is not supported.
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
        String text;
        try {
            text = unpack((NonCompoundField)field);
        }
        catch (UnsupportedEncodingException e) {
            text = e.getMessage();
        }
        
        return getDescription()+"={" + text + "}";
    }       
    
    
    /***************************************************************************
    * Unpacks the text from the data field.
    * 
    * @param    dataField
    *           The data field to be unpacked.
    *           
    * @return   The text from the message
    * 
    * @throws   UnsupportedEncodingException
    *           is the requested encoding is not supported.
    *            
    * @throws   BufferUnderflowException
    *           if more elements shall be read after reaching the buffer's
    *           limit.
    */
    public String unpack(NonCompoundField dataField)
    throws UnsupportedEncodingException 
    {
       ByteBuffer tempBuffer = dataField.encode();
       
       String text = null;
       
       try
       {
           text = 
           new String(tempBuffer.get(new byte[dataField.getFieldLengthIndicator()], 
                                     0, 
                                     dataField.getFieldLengthIndicator()).array(), 
                                     USED_ENCODING);
       }
       catch(BufferUnderflowException exception)
       {   // Trying to read after reaching the limit of the buffer.
           logError(BUFFER_UNDERFLOW_MESSAGE);
           throw exception;
       }    
       catch(UnsupportedEncodingException exception)
       {   // The requested encoding is not supported.
           logError(UNSUPPORTED_ENCODING_MESSAGE);
           throw exception;
       }
       
       return text;     
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
     * The encoding which is used for the data.
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
