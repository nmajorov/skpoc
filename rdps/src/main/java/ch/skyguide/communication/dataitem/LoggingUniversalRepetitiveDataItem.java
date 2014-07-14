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

package ch.skyguide.communication.dataitem;

//------------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.DefaultRepetitiveDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;
import ch.skyguide.message.structure.dataitem.AbstractUniversalRepetitiveDataItem;


//------------------------------------------------------------------------------

/*******************************************************************************
* Represents for universal fixed length data item which doesn't require packing
* and unpacking. 
* 
* @author R. Wilson / M.A Galilée
*/
public final class LoggingUniversalRepetitiveDataItem 
extends AbstractUniversalRepetitiveDataItem
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
    * @param    description
    *           Description of the data item and contents.
    *           
    * @param    units
    *           Units of the data item.
    *           
    * @param    sizeFactor
    *           The size factor for the universal fixed data item. 
    */
    public LoggingUniversalRepetitiveDataItem(short dataCategory, 
                                       int dataItemIdentifier, 
                                       String description, 
                                       String units, 
                                       int sizeFactor) 
    {
        super(dataCategory, dataItemIdentifier, description, units, sizeFactor);    
    }
    
    //--------------------------------------------------------------------------
    // public member functions
    /***************************************************************************
     * See description of superclass/interface.
     * @see ch.skyguide.message.structure.dataitem.AbstractRepetitiveDataItem#createAssociatedDataField(int)
     */
    @Override
    public NonCompoundField createAssociatedDataField(int repetitionFactor) 
    {
       List<NonCompoundField> associatedFields = new ArrayList<>();
       
       for(int index = 0; index < repetitionFactor; index++) {
           
           associatedFields.add(new DefaultFixedDataField(m_sizeFactor,
                   new LoggingUniversalFixedDataItem(m_dataCategoryType,
                           index,
                           "Item " + index,
                           "n/a",
                           m_sizeFactor),
                   false));
       }
              
       return new DefaultRepetitiveDataField(this, associatedFields);     
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
