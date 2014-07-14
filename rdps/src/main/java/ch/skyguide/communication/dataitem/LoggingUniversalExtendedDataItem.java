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

import ch.skyguide.message.structure.dataitem.AbstractUniversalExtendedDataItem;


//------------------------------------------------------------------------------

/*******************************************************************************
* Container for the Universal Extended Data Item.
*
* @author R. Wilson
*/
public final class LoggingUniversalExtendedDataItem 
extends AbstractUniversalExtendedDataItem 
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
    * @param    sizeFactor
    *           The size factor of the extended data item.  
    *
    */
    public LoggingUniversalExtendedDataItem(short dataCategory,
                                     int dataItemIdentifier,
                                     String description,
                                     String units,
                                     int sizeFactor)
    {
        super(dataCategory, dataItemIdentifier, description, units, sizeFactor);
    }
    //--------------------------------------------------------------------------
    // public member functions

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
