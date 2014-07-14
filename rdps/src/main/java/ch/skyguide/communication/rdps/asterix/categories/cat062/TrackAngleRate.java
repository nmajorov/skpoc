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
import ch.skyguide.util.data.FixedLengthBitSet;
import ch.skyguide.util.data.FixedLengthBitSetFactory;

//------------------------------------------------------------------------------

/*******************************************************************************
* Represents a container for the Com ACAS capability
* 
* @author Guillaume Fouillet
*/
public class TrackAngleRate
implements Cloneable
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * Public defined constructor for the track angle rate
    * 
    * @param   rateOfTurn
    *          The rate of turn
    *          
    * @param   turnIndicator
    *          The turn indicator
    */
    public TrackAngleRate(int rateOfTurn, int turnIndicator) 
    {
        m_rateOfTurn = rateOfTurn;
        m_turnIndicator = TurnIndicator.codeOf(turnIndicator);
    }
    
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
    public TrackAngleRate clone()
    {
        //----------------------------------------------------------------------
        // Obtain an object by invoking the base class' clone method.
        TrackAngleRate trackAngleRate;
        
        try {
            trackAngleRate = (TrackAngleRate)super.clone();
        }
        catch (CloneNotSupportedException e) {
            
            throw new UnsupportedOperationException(e.getMessage());
        }

        //----------------------------------------------------------------------
        // Copy attributes.
        // Primative data types already covered.
        return trackAngleRate;
    }

    /***************************************************************************
    * Returns the byte representation of the class
    *
    * @return the byte representation of the class
    */
    public byte[] 
    getBytes() 
    {
        FixedLengthBitSet bitset = FixedLengthBitSetFactory.instance().toFixedLengthBitSet(2*8);
        bitset.fillUp(FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_rateOfTurn, 7), 1);
        bitset.fillUp(m_turnIndicator.getExtendedBitSet(), 14);
        return bitset.toByteArray();
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_turnIndicator.
     * @return  Returns the m_turnIndicator.
     */
    public TurnIndicator getTurnIndicator()
    {
        return m_turnIndicator;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_rateOfTurn expressed in 1/4 °/S
     * @return  Returns the m_rateOfTurn.
     */
    public int getRateOfTurn()
    {
        return m_rateOfTurn;
    }


    /***************************************************************************
     * See description of superclass/interface.
     * @see java.lang.Object#toString()
     */
    @Override
    public String
    toString() {
        return "[TurnIndicator=" +m_turnIndicator.name() + "," +
        "RateOfTurn=" + Double.valueOf(m_rateOfTurn*0.25).intValue() + " °/s]";        
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
    * The Turn indicator
    */
    private final TurnIndicator m_turnIndicator;

    /**
    * The Rate of turn
    */
    private final int m_rateOfTurn;
    
    //--------------------------------------------------------------------------
    // private static data members

    /***************************************************************************
     * Class invariant:
     *
     * @author fouillge
     */
    public enum TurnIndicator {
        // Not available
        NA(0),
        // Left
        LEFT(1),
        // Right
        RIGHT(2),
        // Straight
        STRAIGHT(3);
        
        
        /***************************************************************************
        * Return the code of the num
        *
        * @return the code of the num
        */
        public FixedLengthBitSet 
        getExtendedBitSet() {
            return FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_code, 2);
        }
        
        /***************************************************************************
         *
         * @param code
         * @return
         */
        public static TurnIndicator 
        codeOf(int code) 
        {   
            if (code > 4) {
                throw new IllegalArgumentException("Unknown code " + code + 
                        "for TurnIndicator");
            }            
            for (TurnIndicator status : 
                TurnIndicator.values()) {
                if (status.m_code == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown code " + code + 
            "for TurnIndicator");
        }
        
        
        private TurnIndicator(int code) 
        {
            m_code = code;
        }
        
        private final int m_code;
    }
    
    //--------------------------------------------------------------------------
    // Static initialization block
    
}
