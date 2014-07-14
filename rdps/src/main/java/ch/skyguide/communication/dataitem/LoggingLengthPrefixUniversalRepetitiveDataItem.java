package ch.skyguide.communication.dataitem;

import java.util.ArrayList;
import java.util.List;

import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.DefaultRepetitiveDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;
import ch.skyguide.message.structure.dataitem.AbstractLengthPrefixUniversalRepetitiveDataItem;
import ch.skyguide.message.structure.dataitem.AbstractUniversalRepetitiveDataItem;

/**
 * A {@link AbstractUniversalRepetitiveDataItem} whose length field (the first byte) specifies the length
 * of the message <strong>PLUS</strong> the length of the length field itself (i.e one more). 
 * 
 * @author Stefan Wismer
 */
public final class LoggingLengthPrefixUniversalRepetitiveDataItem 
extends AbstractLengthPrefixUniversalRepetitiveDataItem
{
    
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors
    /***************************************************************************
     * Precondition:
      *
      * Postcondition:
      *
      * @param dataCategory
      * @param dataItemIdentifier
      * @param description
      * @param units
      * @param sizeFactor
      */
     public LoggingLengthPrefixUniversalRepetitiveDataItem(short dataCategory, 
                                                                int dataItemIdentifier, 
                                                                String description, 
                                                                String units, 
                                                                int sizeFactor) {
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
