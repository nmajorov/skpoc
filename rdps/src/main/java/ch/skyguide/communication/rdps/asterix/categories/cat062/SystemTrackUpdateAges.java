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

import ch.skyguide.communication.dataitem.AbstractLoggingSpecificationCompoundDataItem;
import ch.skyguide.communication.dataitem.LoggingUniversalFixedDataItem;
import ch.skyguide.communication.rdps.asterix.categories.cat062.SystemTrackUpdateAgesSubTypeDescriptor.SystemTrackUpdateAgesSubTypes;
import ch.skyguide.message.structure.container.DataItemContainer;
import ch.skyguide.message.structure.container.DefaultSpecificationDataItemContainer;
import ch.skyguide.message.structure.datafield.CompoundField;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.dataitem.DataItem;

//------------------------------------------------------------------------------

/**
* Track data ages 062/290
*  
* @author Guillaume Fouillet
*/
public class SystemTrackUpdateAges
extends AbstractLoggingSpecificationCompoundDataItem 
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
    public SystemTrackUpdateAges(short dataCategory, 
                         int dataItemIdentifier, 
                         String description,
                         List<DataItem> secondaryDataItems) 
    {
        super(dataCategory, dataItemIdentifier, 
              description, "N/a", 
              SystemTrackUpdateAgesSubTypeDescriptor.MAX_LENGTH, 
              secondaryDataItems, new SystemTrackUpdateAgesSubTypeDescriptor());     
    }
    
    //--------------------------------------------------------------------------
    // public member functions
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.dataitem.genericstructure.dataitem.SpecificationCompoundDataItem#createAssociatedDataField(java.nio.ByteBuffer)
     */
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
        StringBuilder str = new StringBuilder("SystemTrackUpdateAges={");
        str.append(super.toUnpackedString(field));
        str.append("}");
        return str.toString();
    }
    
    //--------------------------------------------------------------------------
    // public static member functions
    
    /***************************************************************************
    * Precondition:
    *
    * Postcondition:
    *
    * @param byteRepresentation
    * @return
    * TODO : Turn it to package when test are finished 
    */
    public static List<DataItem> 
    initializeCat062_290SecondaryFields(short byteRepresentation) {
        List<DataItem> secondaryItems = new ArrayList<>();
        
        for (SystemTrackUpdateAgesSubTypes subtypes : 
            SystemTrackUpdateAgesSubTypes.values()) {
            secondaryItems.add(
                    getCat062_290SecondaryFixedField(byteRepresentation,
                        subtypes));
        }
        return secondaryItems;
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.dataitem.genericstructure.dataitem.SpecificationCompoundDataItem#unpack(ch.skyguide.message.structure.datafield.genericstructure.datafield.CompoundField)
     */
    @Override
    public DataItemContainer unpack(CompoundField dataField) {
        return new DefaultSpecificationDataItemContainer(dataField.encode(), getSubTypeDescriptor());    
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
    /***************************************************************************
     * Precondition:
     *
     * Postcondition:
     *
     * @param byteRepresentation
     * @param subtype
     * @return
     */
    private static DataItem
    getCat062_290SecondaryFixedField(short byteRepresentation,
            SystemTrackUpdateAgesSubTypes subtype) {
        return new LoggingUniversalFixedDataItem(byteRepresentation,
                subtype.getFieldIndex(),
                subtype.getDescription(), "N/a", 
                subtype.getByteLength());
    }

    //--------------------------------------------------------------------------
    // private data members

    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block

}
