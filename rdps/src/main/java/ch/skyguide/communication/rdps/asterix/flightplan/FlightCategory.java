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

package ch.skyguide.communication.rdps.asterix.flightplan;

//------------------------------------------------------------------------------

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import ch.skyguide.communication.dataitem.AbstractLoggingNonCompoundDataItem;
import ch.skyguide.environment.aeronautical.navigation.FlightRuleType;
import ch.skyguide.flightplan.structure.AirTrafficType;
import ch.skyguide.message.structure.datafield.DataField;
import ch.skyguide.message.structure.datafield.DefaultFixedDataField;
import ch.skyguide.message.structure.datafield.NonCompoundField;

//------------------------------------------------------------------------------

/*******************************************************************************
 * Represents the flight category based upon the flight rule and air-traffic
 * type.
 * 
 * @author R. Wilson
 */
public class FlightCategory
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
    */
    public FlightCategory(short dataCategory, 
                          int dataItemIdentifier, 
                          String description, 
                          String units) 
    {
        super(dataCategory, dataItemIdentifier, 1, description, units);     
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
    * Pack the flight rule and air-traffic type into the data field.
    *  
    * Precondition: allocatedCodes != null
    * 
    * @param    flightRule
    *           The flight rule under which the flight is operating.
    *           
    * @param    typeOfAirTraffic
    *           The type of air-traffic under which the flight is operating.
    *           
    * @return   The <code>NonCompoundField</code> containing the packed data.
    * 
    * @throws   BufferOverflowException
    *           if elements shall be added after the buffer reached its limit.
    */
    public NonCompoundField pack(FlightRuleType flightRule, 
                          AirTrafficType typeOfAirTraffic) 
    {
       NonCompoundField dataField = createAssociatedDataField( 1 );
       ByteBuffer tempBuffer = 
       ByteBuffer.allocate(dataField.getFieldLengthIndicator());
       
       try {
           
           byte temp = (byte)(typeOfAirTraffic.getBitRepresentation()<<6);
           temp |= (byte)(flightRule.getBitRepresentation()<<4);
           tempBuffer.put(temp);
           tempBuffer.flip();
           dataField.decode(tempBuffer);
           
       }
       catch(BufferOverflowException exception)
       {   // The buffer should not be filled after reaching its limit.
           logError(BUFFER_OVERFLOW_MESSAGE);
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
        return getDescription()+"={" + unpack((NonCompoundField)field).toString() + "}";
    }
    
    
    /***************************************************************************
    * Unpack the flight rule and air-traffic type from the data field into a 
    * container.
    *  
    * @param    dataField
    *           The data field containing the flight rule and air-traffic type.
    *           
    * @return   The flight rule and air-traffic type from the data field.
    * 
    * @throws   BufferUnderflowException
    *           if more elements shall be read after reaching the buffer's
    *           limit.
    */
    public FlightCategoryContainer unpack(NonCompoundField dataField) 
    {
       ByteBuffer tempBuffer = dataField.encode();
       FlightCategoryContainer container = null;
       
       try {
           
           byte temp = tempBuffer.get();
          
           container = 
           new FlightCategoryContainer(FlightRuleType.determineRule((temp<<1)>>>3), 
                                       AirTrafficType.determineType((temp<<6)>>>6 ));
           
       }
       catch(BufferUnderflowException exception)
       {   // Trying to read after reaching the limit of the buffer.
           logError(BUFFER_UNDERFLOW_MESSAGE);
           throw exception;
       }    
       
       return container;     
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
