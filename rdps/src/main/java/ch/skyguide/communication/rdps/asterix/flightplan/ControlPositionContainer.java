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

//------------------------------------------------------------------------------

/**
 * @author R. Wilson
 */
public class ControlPositionContainer 
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * Public defined constructor taking the centre and position handling the
    * flight.
    * 
    * @param    centre
    *           The centre handling the flight.
    *           
    * @param    position
    *           The control position in the centre handling the flight.
    *           
    */
    public ControlPositionContainer(int centre, int position) 
    {
       m_centre = centre;
       m_position = position;     
    }
    
    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Retrieves the centre number of the centre handling the flight.
    * 
    * @return The number indicating the centre controlling the flight.
    * 
    */
    public int getCentre() 
    {
        return m_centre;     
    }
    
    /***************************************************************************
    * Retrieves the number indicating the control position handling the flight.
    * 
    * @return The number indicating the control position handling the flight.
    * 
    */
    public int getPosition() 
    {
        return m_position;     
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see java.lang.Object#toString()
     */
    @Override
    public String
    toString() {
        return "[Centre=" + m_centre+", Position="+m_position+"]";
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
    * The centre handling the flight. 
    */
    private final int m_centre;

    /**
    * The control position in the centre handling the flight. 
    */
    private final int m_position;
    
    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block

    
}
