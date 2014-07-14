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

import ch.skyguide.environment.aeronautical.navigation.FlightRuleType;
import ch.skyguide.flightplan.structure.AirTrafficType;

//------------------------------------------------------------------------------

/*******************************************************************************
 * A container class for the flight category determined from the flight rule
 * and the air-traffic type.
 * 
 * @author R. Wilson
 */
public class FlightCategoryContainer 
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
     * The flight category container holding the flight rule and air-traffic 
     * type.
     * 
     * @param   flightRule
     *          The flight rules under which the flight is operating.
     *          
     * @param   typeOfAirTraffic
     *          The air-traffic type under whihc the flight is operating.
     *          
     */
    public FlightCategoryContainer(FlightRuleType flightRule, 
                                   AirTrafficType typeOfAirTraffic) 
    {
        m_flightRule = flightRule;
        m_typeOfAirTraffic = typeOfAirTraffic;     
    }
    
    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Retrieves the flight rule type under which the flight is operating.
    * 
    * @return  The <code>FlightRuleType</code> for the operating flight rule.
    */
    public FlightRuleType getFlightRule() 
    {
        return m_flightRule;     
    }
    
    /***************************************************************************
    * Retrieves the air-traffic type under which the flight is operating.
    * 
    * @return  The <code>AirTrafficType</code> for the operating type of 
    *          air-traffic.
    */
    public AirTrafficType getTypeOfAirTraffic() 
    {
        return m_typeOfAirTraffic;     
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see java.lang.Object#toString()
     */
    @Override
    public String
    toString() {
        return "[FlightRule=" + m_flightRule.getDescription()+", "+
                "TypeOfAirTraffic" + m_typeOfAirTraffic.getDescription()+"]";
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

    /**
    * The flight rule of the flight. 
    */
    private final FlightRuleType m_flightRule;

    /**
    * The air-traffic type of the flight. 
    */
    private final AirTrafficType m_typeOfAirTraffic;
    
    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block

}
