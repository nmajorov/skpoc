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

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.communication.rdps.asterix.categories.cat062.SystemTrack;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultExtendedDataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/*******************************************************************************
* Container for the ARTAS Tracker number.
*
* @author R. Wilson
*/
public class ArtasTrackNumber 
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
    *           The assigned data category for the item.
    *
    * @param    dataItemIdentifier
    *           The identifier for the data item.
    *
    * @param    description
    *           The description of the data item.
    *
    * @param    units
    *           The units of the data item.
    *
    */
    public ArtasTrackNumber(short dataCategory,
                            int dataItemIdentifier,
                            String description,
                            String units)
    {
        super(dataCategory, dataItemIdentifier, 3, description, units);
    }

    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Creates the association with a data field.
    *
    * @param    repetitionFactor
    *           A repetition factor for how many times it is to repeat.
    *
    * @return   The <code>NonCompoundField</code> associated with the data item.
    *
    */
    @Override
    public NonCompoundField createAssociatedDataField(int repetitionFactor)
    {
        return new DefaultFixedDataField(getSizeFactor(), this, true);
    }

    /***************************************************************************
    * Creates the associated data field with the information in the byte buffer.
    *
    * @param    inBuffer
    *           The input buffer containing the information to be encoded into
    *           the data field.
    *
    * @return   The <code>NonCompoundField</code> associated with the data item.
    *
    */
    @Override
    public NonCompoundField createAssociatedDataField(ByteBuffer inBuffer)
    {
       List<NonCompoundField> dataFields = new ArrayList<>();
       NonCompoundField dataField = null;

       do {

           dataField = createAssociatedDataField(1);
           dataField.setFieldExtensionBit();
           dataFields.add(dataField);
           dataField.decode(inBuffer);
       }
       while((inBuffer.get(m_sizeFactor-1) & 0x1) == 0x1);

       dataField.clearFieldExtensionBit();    // reset the last one.

       return new DefaultExtendedDataField(this, dataFields);
    }

    /***************************************************************************
    * Packs the track number into the data field.
    * 
    * Precondition: systemTracks != null
    *
    * Remark:   The <code>ReadOnlyBufferException</code> exception is not
    *           handled since the used <code>ByteBuffer</code> is not read only.
    *           
    * @param    systemTracks
    *           The system tracks to extract the track numbers.
    *
    * @return   The <code>NonCompoundField</code> packed with the system track numbers.
    *
    * @throws   IllegalArgumentException
    *           if the parameter systemTracks is null.
    *           
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(List<SystemTrack> systemTracks)
    {
        // Validate the precondition.
        if(systemTracks == null)
        {
            throw new IllegalArgumentException
            ("The list with the system tracks must not be null.");
        }
        
        
       NonCompoundField extendedDataField = null;

       try
       {

           List<NonCompoundField> dataFields = new ArrayList<>();

           NonCompoundField dataField = null;
           for(SystemTrack systemTrack : systemTracks) {

                dataField = createAssociatedDataField(1);
                dataField.setFieldExtensionBit();

                ByteBuffer tempBuffer =
                ByteBuffer.allocate(dataField.getFieldLengthIndicator());

                tempBuffer.
                put((byte)systemTrack.getArtasUnitIdentification());
                tempBuffer.
                putShort((short)((systemTrack.getSystemTrack()<<13)>>>12));

                tempBuffer.flip();
                dataField.decode(tempBuffer);
           }
           
           if(dataField != null) {
               
               dataField.clearFieldExtensionBit();
           }
           
           extendedDataField =
           new DefaultExtendedDataField(this, dataFields);

       }
       catch(BufferOverflowException exception)
       {   // The buffer should not be filled after reaching its limit.
           logError(BUFFER_OVERFLOW_MESSAGE);
           throw exception;
       }

       return extendedDataField;
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.dataitem.genericstructure.dataitem.DataItem#toUnpackedString(ch.skyguide.message.structure.datafield.genericstructure.datafield.DataField)
     */
    @Override
    public String toUnpackedString(DataField field) {
        StringBuilder str = new StringBuilder(getDescription()+"={");
        for (SystemTrack track : unpack((NonCompoundField)field)) {
            str.append(track.toString() + ";");
        }
        str.append("}");
        return str.toString();
    }    
    
    /***************************************************************************
    * Unpack the data field into system track data.
    *
    * @param    dataField
    *           The data field to be unpacked.
    *
    * @return   A <code>List</code> of <code>SystemTrack</code>s from the data 
    *           field.
    *
    * @throws   BufferUnderflowException
    *           if more elements shall be read after reaching the buffer's
    *           limit.
    */
    public List<SystemTrack> unpack(NonCompoundField dataField)
    {
       ByteBuffer tempBuffer = dataField.encode();
       List<SystemTrack> systemTracks = new ArrayList<>();

       try {

           boolean fieldExtensionBit = true;

            while(fieldExtensionBit) {

                short Id = tempBuffer.get();
                int track = tempBuffer.getShort();

                SystemTrack systemTrack = new SystemTrack(Id, (track>>>1));
                systemTracks.add(systemTrack);

                // Check if extended beyond this field.
                fieldExtensionBit = ((track & 0x1) == 0x1);
             }
       }
       catch(BufferUnderflowException exception)
       {   // Trying to read after reaching the limit of the buffer.
           logError(BUFFER_UNDERFLOW_MESSAGE);
           throw exception;
       }    

       return systemTracks;
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
