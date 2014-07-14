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

import java.util.List;

import ch.skyguide.message.structure.dataitem.AbstractLengthSpecificationCompoundDataItem;
import ch.skyguide.message.structure.dataitem.DataItem;
import ch.skyguide.message.structure.descriptor.AbstractSpecificationSubTypeDescriptor;

//------------------------------------------------------------------------------

/*******************************************************************************
* Container for abstract Length Specification Compound Data Item.
*
* @author Didier Bérard
*/
public abstract class AbstractLoggingLengthSpecificationCompoundDataItem 
extends AbstractLengthSpecificationCompoundDataItem
{

    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    //--------------------------------------------------------------------------
    // public member functions
    
    //--------------------------------------------------------------------------
    // public static member functions

    //--------------------------------------------------------------------------
    // protected constructors

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
    * @param    secondaryDataItems
    *           The secondary data items consisting of 
    *           <code>NonCompoundDataItem</code>s.  
    *
    */
    protected AbstractLoggingLengthSpecificationCompoundDataItem(short dataCategory,
                                     int dataItemIdentifier,
                                     String description,
                                     String units,
                                     int primarySubFieldLength,
                                     List<DataItem> secondaryDataItems,
                                     AbstractSpecificationSubTypeDescriptor subtypeDescriptor)
    {
        super(dataCategory, 
              dataItemIdentifier, 
              description, 
              units, 
              primarySubFieldLength,
              secondaryDataItems,
              subtypeDescriptor);
    }

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

}
