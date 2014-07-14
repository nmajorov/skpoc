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

import ch.skyguide.message.structure.dataitem.AbstractNonCompoundDataItem;



//------------------------------------------------------------------------------



//------------------------------------------------------------------------------

/*******************************************************************************
* Represents an abstract non-compound data item that contain a non-compound data
* item. 
* 
* @author Guillaume Fouillet
*/
public abstract class AbstractLoggingNonCompoundDataItem 
extends AbstractNonCompoundDataItem 
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Creates and returns a copy of this object.
    * The method performs a "deep copy" of this object.
    * By convention, the object returned by this method should be independent
    * of this object (which is being cloned). Typically, this means copying any
    * mutable objects that comprise the internal "deep structure" of the object
    * being cloned and replacing the references to these objects with
    * references to the copies.
    * 
    * See also description of class <code>Object</code>.
    * @see java.lang.Object
    * @see java.lang.Cloneable
    * 
    * @return   A "deep copy" of this object.
    */
    @Override
    public AbstractLoggingNonCompoundDataItem clone()
    {
        //----------------------------------------------------------------------
        // Obtain an object by invoking the base class' clone method.
        AbstractLoggingNonCompoundDataItem clonedDataItem = (AbstractLoggingNonCompoundDataItem)super.clone();
            
        //----------------------------------------------------------------------
        // Copy attributes.
        // Primative data types already covered.
        return clonedDataItem;
    }

    //--------------------------------------------------------------------------
    // public static member functions

    //--------------------------------------------------------------------------
    // protected constructors

    /***************************************************************************
    * The public defined constructor.
    *  
    * @param    dataCategory
    *           The data category for this data item
    *            
    * @param    dataItemIdentifier
    *           The data item identifier.
    *           
    * @param    sizeFactor
    *           The size factor in bytes required for the data item. 
    *           
    * @param    description
    *           Description of the data item and contents.
    *           
    * @param    units
    *           Units of the data item.
    *           
    */
    protected AbstractLoggingNonCompoundDataItem(short dataCategory, 
                                  int dataItemIdentifier, 
                                  int sizeFactor,
                                  String description, 
                                  String units) 
    {
        super(dataCategory, dataItemIdentifier, sizeFactor, description, units);
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

    //--------------------------------------------------------------------------
    // Static initialization block
    
}
