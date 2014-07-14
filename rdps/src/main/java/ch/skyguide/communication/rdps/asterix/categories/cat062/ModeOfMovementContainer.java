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
import java.nio.ByteOrder;

import ch.skyguide.util.data.FixedLengthBitSet;
import ch.skyguide.util.data.FixedLengthBitSetFactory;

//------------------------------------------------------------------------------

/**
 * @author Guillaume Fouillet
 */
public class ModeOfMovementContainer 
{
    
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    /***************************************************************************
     * Class invariant:
     *
     * @author fouillge
     */
    public enum TransversalAcceleration {
        // Constant course
        CONSTANT(0),
        // Right turn
        RIGHT_TURN(1),
        // Left turn
        LEFT_TURN(2),
        // Undetermined
        UNDETERMINED(3);
        

        /***************************************************************************
        * Return the code of the num
        *
        * @return the code of the num
        */
        FixedLengthBitSet 
        getExtendedBitSet() {
            return FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_code, 2);
        }
        
        /***************************************************************************
         *
         * @param code
         * @return
         */
        static TransversalAcceleration 
        codeOf(int code) 
        {   
            if (code > 4) {
                throw new IllegalArgumentException("Unknown code " + code + 
                        "for TransversalAcceleration");
            }            
            for (TransversalAcceleration acceleration : 
                TransversalAcceleration.values()) {
                if (acceleration.m_code == code) {
                    return acceleration;
                }
            }
            throw new IllegalArgumentException("Unknown code " + code + 
            "for TransversalAcceleration");
        }
        
        
        private TransversalAcceleration(int code) 
        {
            m_code = code;
        }
        
        private final int m_code;        
    }
    
    /***************************************************************************
     * Class invariant:
     *
     * @author fouillge
     */
    public enum LongitudinalAcceleration {
        // Constant ground speed
        CONSTANT(0),
        // Increasing ground speed
        INCREASING(1),
        // Decreasing ground speed
        DECREASING(2),
        // Undetermined
        UNDETERMINED(3);
        
        
        /***************************************************************************
        * Return the code of the num
        *
        * @return the code of the num
        */
        FixedLengthBitSet 
        getExtendedBitSet() {
            return FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_code, 2);
        }
        
        /***************************************************************************
         *
         * @param code
         * @return
         */
        static LongitudinalAcceleration 
        codeOf(int code) 
        {   
            if (code > 4) {
                throw new IllegalArgumentException("Unknown code " + code + 
                        "for TransversalAcceleration");
            }            
            for (LongitudinalAcceleration acceleration : 
                LongitudinalAcceleration.values()) {
                if (acceleration.m_code == code) {
                    return acceleration;
                }
            }
            throw new IllegalArgumentException("Unknown code " + code + 
            "for TransversalAcceleration");
        }
        
        
        private LongitudinalAcceleration(int code) 
        {
            m_code = code;
        }
        
        private final int m_code;             
    }

    /***************************************************************************
     * Class invariant:
     *
     * @author fouillge
     */
    public enum VerticalRate {
        // Constant course
        LEVEL(0),
        // Right turn
        CLIMB(1),
        // Left turn
        DESCENT(2),
        // Straight
        UNDETERMINED(3);
        
        
        /***************************************************************************
        * Return the code of the num
        *
        * @return the code of the num
        */
        FixedLengthBitSet 
        getExtendedBitSet() {
            return FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_code, 2);
        }
        
        /***************************************************************************
         *
         * @param code
         * @return
         */
        static VerticalRate 
        codeOf(int code) 
        {   
            if (code > 4) {
                throw new IllegalArgumentException("Unknown code " + code + 
                        "for VerticalRate");
            }            
            for (VerticalRate rate : 
                VerticalRate.values()) {
                if (rate.m_code == code) {
                    return rate;
                }
            }
            throw new IllegalArgumentException("Unknown code " + code + 
            "for VerticalRate");
        }
        
        
        private VerticalRate(int code) 
        {
            m_code = code;
        }
        
        private final int m_code;             
    }

    //--------------------------------------------------------------------------
    // public constructors
    
    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
     * Returns the byte array for unpacking this container into a data field
     * 
     * @return the byte array for unpacking this container into a data field
     */
    public byte[] 
    getBytes() 
    {
        FixedLengthBitSet bitSet = FixedLengthBitSetFactory.instance().toFixedLengthBitSet(8);        
        // Vertical rate
        bitSet.fillUp(m_verticalRate.getExtendedBitSet(), 2);
        // Longitudinal acceleration
        bitSet.fillUp(m_longitudinalAcceleration.getExtendedBitSet(), 4);
        // transversal acceleration
        bitSet.fillUp(m_transversalAcceleration.getExtendedBitSet(), 6);
        return bitSet.toByteArray();
    }
    
    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_transversalAcceleration.
     * @return  Returns the m_transversalAcceleration.
     */
    public TransversalAcceleration getTransversalAcceleration()
    {
        return m_transversalAcceleration;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_longitudinalAcceleration.
     * @return  Returns the m_longitudinalAcceleration.
     */
    public LongitudinalAcceleration getLongitudinalAcceleration()
    {
        return m_longitudinalAcceleration;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_verticalRate.
     * @return  Returns the m_verticalRate.
     */
    public VerticalRate getVerticalRate()
    {
        return m_verticalRate;
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see java.lang.Object#toString()
     */
    @Override
    public String
    toString() {
        return "[TransversalAcceleration=" + m_transversalAcceleration.name() + "," +
               "LongitudinalAcceleration=" + m_longitudinalAcceleration.name() + "," +
               "VerticalRate=" + m_verticalRate.name() + "]";
    }
    
    //--------------------------------------------------------------------------
    // public static member functions

    //--------------------------------------------------------------------------
    // protected constructors

    //--------------------------------------------------------------------------
    // protected member functions
    /***************************************************************************
     * Public defined constructor 
     *  
     * @param transversalAccelerationCode
     * @param longitudinalAccelerationCode
     * @param verticalAccelerationCode
     */
     protected ModeOfMovementContainer(ByteBuffer buffer)
     {
         buffer.order(ByteOrder.LITTLE_ENDIAN);
         byte[] readBytes = new byte[buffer.limit()];
         buffer.get(readBytes);
         FixedLengthBitSet bitSet = FixedLengthBitSetFactory.instance().toFixedLengthBitSet(readBytes);
         m_verticalRate = VerticalRate.codeOf(bitSet.get(3,4).toInt());
         m_longitudinalAcceleration = LongitudinalAcceleration.codeOf(bitSet.get(5,6).toInt());
         m_transversalAcceleration = TransversalAcceleration.codeOf(bitSet.get(7,8).toInt());
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
    /**
     * Transversal acceleration
     */
    private final TransversalAcceleration m_transversalAcceleration;

    /**
     * Longitudinal acceleration
     */
    private final LongitudinalAcceleration m_longitudinalAcceleration;
    
    /**
     * Vertical rate
     */
    private final VerticalRate m_verticalRate;

    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block

}
