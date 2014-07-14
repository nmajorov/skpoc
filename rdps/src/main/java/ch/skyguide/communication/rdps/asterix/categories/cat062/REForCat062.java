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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ch.skyguide.communication.dataitem.AbstractLoggingLengthSpecificationCompoundDataItem;
import ch.skyguide.communication.dataitem.LoggingUniversalFixedDataItem;
import ch.skyguide.communication.rdps.asterix.categories.cat062.REforCat062SubTypeDescriptor.ReservedExpansionFieldTypes;
import ch.skyguide.message.structure.container.DataItemContainer;
import ch.skyguide.message.structure.datafield.CompoundField;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.dataitem.DataItem;

//------------------------------------------------------------------------------

/**
* Reserved expansion field for cat 062
*  
* @author Guillaume Fouillet
*/
public class REForCat062
extends AbstractLoggingLengthSpecificationCompoundDataItem 
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
    public REForCat062(short dataCategory, 
                       int dataItemIdentifier, 
                       String description,
                       List<DataItem> secondaryDataItems) 
    {
        super(dataCategory, dataItemIdentifier, 
                description, "N/a", 
                REforCat062SubTypeDescriptor.MAX_LENGTH,
                secondaryDataItems, new REforCat062SubTypeDescriptor());
    }
    
    //--------------------------------------------------------------------------
    // public members functions
    
    @Override
    public DataField createAssociatedDataField(ByteBuffer inBuffer)
    {
        return createAssociatedDataField(1);
    }
    
     /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.dataitem.genericstructure.dataitem.DataItem#toUnpackedString(ch.skyguide.message.structure.datafield.genericstructure.datafield.DataField)
     */
     @Override
     public String toUnpackedString(DataField field) {
         return unpack((CompoundField)field).toString();
     }   
     
    /***************************************************************************
    * Unpacks the raw data into a <code>byte array</code>.
    *  
    * @param    dataField
    *           The data field to be unpacked.
    *           
    * @return   The <code>byte</code> array containing the data.
    * 
    */
    @Override 
    public DataItemContainer unpack(CompoundField dataField) 
    {
        return new REForCat062Container(dataField.encode(), getSubTypeDescriptor());
    }
    //--------------------------------------------------------------------------
    // public static member functions
    
    /***************************************************************************
     * Precondition:
     *
     * Postcondition:
     *
     * @param byteRepresentation
     */
    public static List<DataItem> 
    initializeCat062_RESecondaryFields(short byteRepresentation) {
        List<DataItem> secondaryItems = new ArrayList<>();
        
        for (ReservedExpansionFieldTypes subtype : 
            ReservedExpansionFieldTypes.values()) {
               secondaryItems.add(
                       new LoggingUniversalFixedDataItem(byteRepresentation,
                       subtype.getFieldIndex(),
                       subtype.getDescription(), "N/a", 
                       subtype.getByteLength()));
        }
        return secondaryItems;
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
    // private static member functions

    //--------------------------------------------------------------------------
    // private data members

    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block

}
