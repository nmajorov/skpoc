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
import ch.skyguide.communication.rdps.asterix.categories.cat062.MeasuredInformationDataSubTypeDescriptor.MeasuredInformationSubTypes;
import ch.skyguide.message.structure.container.DataItemContainer;
import ch.skyguide.message.structure.container.DefaultSpecificationDataItemContainer;
import ch.skyguide.message.structure.datafield.CompoundField;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.dataitem.DataItem;

//------------------------------------------------------------------------------

/**
* Track data ages 062/340
*  
* @author Guillaume Fouillet
*/
public class MeasuredInformation
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
    public MeasuredInformation(short dataCategory, 
                         int dataItemIdentifier, 
                         String description,
                         List<DataItem> secondaryDataItems) 
    {
        super(dataCategory, dataItemIdentifier, 
              description, "N/a", 
              MeasuredInformationDataSubTypeDescriptor.MAX_LENGTH, 
              secondaryDataItems, new MeasuredInformationDataSubTypeDescriptor());     
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
        StringBuilder str = new StringBuilder("MeasuredInformation={");
        str.append(super.toUnpackedString(field));
        str.append("}");
        return str.toString();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.dataitem.AbstractSpecificationCompoundDataItem#unpack(ch.skyguide.message.structure.datafield.CompoundField)
     */
    @Override
    public DataItemContainer unpack(CompoundField dataField) {
        return new DefaultSpecificationDataItemContainer(dataField.encode(), getSubTypeDescriptor());    
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
    initializeCat062_340SecondaryFields(short byteRepresentation) {
        List<DataItem> secondaryItems = new ArrayList<>();
        
        for (MeasuredInformationSubTypes subtypes : 
                MeasuredInformationSubTypes.values()) {
            secondaryItems.add(
                    getCat062_340SecondaryFixedField(byteRepresentation,
                        subtypes));
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
    getCat062_340SecondaryFixedField(short byteRepresentation,
            MeasuredInformationSubTypes subtype) {
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
