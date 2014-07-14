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

import ch.skyguide.util.data.FixedLengthBitSet;
import ch.skyguide.util.data.FixedLengthBitSetFactory;

//------------------------------------------------------------------------------

/*******************************************************************************
* Represents a container for the Com ACAS capability
* 
* @author Guillaume Fouillet
*/
public class ComAcasCapability
implements Cloneable
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * Public defined constructor for the ComAcasCapability.
    */
    public ComAcasCapability(int COMcapability, int flightStatus, 
                             boolean sSC, boolean aRC, boolean aIC) 
    {
        m_transponderCOMCapability = TransponderCOMCapability.codeOf(COMcapability);
        m_flightStatus = FlightStatus.codeOf(flightStatus);
        m_hasSpecificServiceCapability = sSC;
        m_hasAltitudeReportingCapability = aRC;
        m_hasAircraftIdentificationCapability = aIC;
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
    public ComAcasCapability clone()
    {
        //----------------------------------------------------------------------
        // Obtain an object by invoking the base class' clone method.
        ComAcasCapability clonedCapability;
        
        try {
            clonedCapability = (ComAcasCapability)super.clone();
        }
        catch (CloneNotSupportedException e) {
            
            throw new UnsupportedOperationException(e.getMessage());
        }

        //----------------------------------------------------------------------
        // Copy attributes.
        // Primative data types already covered.
        return clonedCapability;
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
        bitset.set(5, m_hasAircraftIdentificationCapability);
        bitset.set(6, m_hasAltitudeReportingCapability);
        bitset.set(7, m_hasSpecificServiceCapability);
        bitset.fillUp(m_flightStatus.getExtendedBitSet(), 10);
        bitset.fillUp(m_transponderCOMCapability.getExtendedBitSet(), 13);
        return bitset.toByteArray();
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_transponderCOMCapability.
     * @return  Returns the m_transponderCOMCapability.
     */
    public TransponderCOMCapability getTransponderCOMCapability()
    {
        return m_transponderCOMCapability;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_flightStatus.
     * @return  Returns the m_flightStatus.
     */
    public FlightStatus getFlightStatusCode()
    {
        return m_flightStatus;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_hasAircraftIdentificationCapability.
     * @return  Returns the m_hasAircraftIdentificationCapability.
     */
    public boolean hasAircraftIdentificationCapability()
    {
        return m_hasAircraftIdentificationCapability;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_hasAltitudeReportingCapability.
     * @return  Returns the m_hasAltitudeReportingCapability.
     */
    public boolean hasAltitudeReportingCapability()
    {
        return m_hasAltitudeReportingCapability;
    }

    /***************************************************************************
     * Postcondition:
     *
     * Returns the m_hasSpecificServiceCapability.
     * @return  Returns the m_hasSpecificServiceCapability.
     */
    public boolean hasSpecificServiceCapability()
    {
        return m_hasSpecificServiceCapability;
    }

    /***************************************************************************
     * See description of superclass/interface.
     * @see java.lang.Object#toString()
     */
    @Override
    public String
    toString() {
        return "[TransponderCOMCapability=" +m_transponderCOMCapability.name() + "," +
        "FlightStatus=" + m_flightStatus.name() + "," +
        "HasSpecificServiceCapability=" + String.valueOf(m_hasSpecificServiceCapability) + "," +
        "HasAltitudeReportingCapability=" + String.valueOf(m_hasAltitudeReportingCapability) + "," +
        "HasAircraftIdentificationCapability=" + String.valueOf(m_hasAircraftIdentificationCapability) + "]";        
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
    * The transponder capability
    */
    private final TransponderCOMCapability m_transponderCOMCapability;

    /**
    * The flight status
    */
    private final FlightStatus m_flightStatus;
    
    /**
    * True if it has specific service capability
    */
    private final boolean m_hasSpecificServiceCapability;
    
    /**
    * True if it has specific altitude reporting capability
    */
    private final boolean m_hasAltitudeReportingCapability;
    
    /**
    * True if it has specific aircraft identification capability
    */
    private final boolean m_hasAircraftIdentificationCapability;
    
    //--------------------------------------------------------------------------
    // private static data members

    /***************************************************************************
     * Class invariant:
     *
     * @author fouillge
     */
    public enum TransponderCOMCapability {
        // No communication capability
        NONE(0),
        // Com A and B
        A_B(1),
        // Com A + B + UpLink ELM
        A_B_ELMUP(2),
        // Com A + B + UpLink ELM + DownLink ELM
        A_B_ELMUP_ELMDOWN(3),
        // Level5 capability
        LEVEL5(4),
        // Not assigned
        NA(5);
        
        /***************************************************************************
        * Return the code of the num
        *
        * @return the code of the num
        */
        public FixedLengthBitSet 
        getExtendedBitSet() {
            return FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_code, 3);
        }
        
        /***************************************************************************
         *
         * @param code
         * @return
         */
        public static TransponderCOMCapability 
        codeOf(int code) 
        {   
            if (code > 7) {
                throw new IllegalArgumentException("Unknown code " + code + 
                        "for TransponderCOMCapability");
            }            
            for (TransponderCOMCapability capability : 
                 TransponderCOMCapability.values()) {
                if (capability.m_code == code) {
                    return capability;
                }
            }
            return NA;
        }
        
        private TransponderCOMCapability(int code) 
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
    public enum FlightStatus {
        // No alert, no SPI, aircraft airborn
        AIR(0),
        // No alert, no SPI, aircraft ground
        GROUND(1),
        // Alert, no SPI, aircraft airborn
        AIR_ALERT(2),
        // Alert, no SPI, aircraft ground
        GROUND_ALERT(3),
        // Alert and spi for aircraft airborn or ground
        ALERT_SPI(4),
        // No Alert, spi for aircraft airborn or ground
        SPI(5);
        
        
        /***************************************************************************
        * Return the code of the num
        *
        * @return the code of the num
        */
        public FixedLengthBitSet 
        getExtendedBitSet() {
            return FixedLengthBitSetFactory.instance().toFixedLengthBitSet(m_code, 3);
        }
        
        /***************************************************************************
         *
         * @param code
         * @return
         */
        public static FlightStatus 
        codeOf(int code) 
        {   
            if (code > 5) {
                throw new IllegalArgumentException("Unknown code " + code + 
                        "for TransponderCOMCapability");
            }            
            for (FlightStatus status : 
                FlightStatus.values()) {
                if (status.m_code == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown code " + code + 
            "for FlightStatus");
        }
        
        
        private FlightStatus(int code) 
        {
            m_code = code;
        }
        
        private final int m_code;
    }
    
    //--------------------------------------------------------------------------
    // Static initialization block
    
}
