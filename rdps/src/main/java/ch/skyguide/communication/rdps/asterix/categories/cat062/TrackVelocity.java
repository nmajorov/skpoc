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

import java.nio.ByteBuffer;

import ch.skyguide.util.data.ByteManipulationUtil;

//------------------------------------------------------------------------------

/*******************************************************************************
* Represents a container for the Track velocity
* 
* @author Guillaume Fouillet
*/
public class TrackVelocity
implements Cloneable
{

     
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors
     
     /***************************************************************************
     * Public defined constructor
     *
     * @param buffer
     * @param unitFactor
     */
     public TrackVelocity(ByteBuffer buffer, double unitFactor) {
         byte[] xVelocityByte = new byte[2];
         byte[] yVelocityByte = new byte[2];
         buffer.get(xVelocityByte);
         buffer.get(yVelocityByte);
         m_xVelocity = ByteManipulationUtil.convertToUnsignedShort(xVelocityByte);
         m_yVelocity = ByteManipulationUtil.convertToUnsignedShort(yVelocityByte);
         m_unitFactor = unitFactor;
     }
     
     /***************************************************************************
     * Public defined constructor
     *
     * @param xVelocity
     * @param yVelocity
     * @param unitFactor
     * 
     */
     public TrackVelocity(short xVelocity, short yVelocity, double unitFactor) {
         m_xVelocity = xVelocity;
         m_yVelocity = yVelocity;
         m_unitFactor = unitFactor;
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
    public TrackVelocity clone()
    {
        //----------------------------------------------------------------------
        // Obtain an object by invoking the base class' clone method.
        TrackVelocity trackVelocity;
        
        try {
            trackVelocity = (TrackVelocity)super.clone();
        }
        catch (CloneNotSupportedException e) {
            
            throw new UnsupportedOperationException(e.getMessage());
        }

        //----------------------------------------------------------------------
        // Copy attributes.
        // Primative data types already covered.
        return trackVelocity;
    }
    
    
    /***************************************************************************
     * Returns byte array
     *
     * @return byte array
     */
     public byte[] getBytes() {
         byte[] xVelocityByte = 
             ByteManipulationUtil.convertToByteArrayFromUnsignedShort(m_xVelocity);
         byte[] yVelocityByte = 
             ByteManipulationUtil.convertToByteArrayFromUnsignedShort(m_yVelocity);
         byte[] refVelocityByte = new byte[4];
         System.arraycopy(xVelocityByte, 0, refVelocityByte, 0, xVelocityByte.length);
         System.arraycopy(yVelocityByte, 0, refVelocityByte, xVelocityByte.length, yVelocityByte.length);
         return refVelocityByte;
     }
    
    /***************************************************************************
    * Returns the horizontal velocity 
    *
    * @return the horizontal velocity
    */
    public short getXVelocity() {
        return m_xVelocity;
    }
    
    /***************************************************************************
    * Returns the vertical velocity 
    *
    * @return the vertical velocity
    */
    public short getYVelocity() {
        return m_yVelocity;
    }

    /***************************************************************************
    * Returns the track velocity angle in degrees
    * 
    * @return the track velocity angle in degrees
    */
    public double getAngle() {
        double radian = Math.atan2(m_xVelocity,m_yVelocity);
        if (radian < 0) {
            return Math.toDegrees(radian+2*Math.PI);
        }
        return Math.toDegrees(radian);
    }
    
    /***************************************************************************
    * Returns the track velocity magnitude in m/s
    * 
    * @return the track velocity magnitude in m/s
    */
    public int getMagnitude() {
        return Double.valueOf(Math.sqrt(
                Math.pow(m_xVelocity*m_unitFactor, 2)+
                Math.pow(m_yVelocity*m_unitFactor, 2))).intValue();
    }
    
    /***************************************************************************
     * See description of superclass/interface.
     * @see java.lang.Object#toString()
     */
    @Override
    public String
    toString() {
        return "[XVelocity=" +Double.valueOf(m_xVelocity*m_unitFactor) + " m/s," +
               "YVelocity=" +Double.valueOf(m_yVelocity*m_unitFactor) + " m/s]";        
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
    * The reference horizontal velocity in the unit 0.25 m.s-1
    */
    private final short m_xVelocity;
    
    /**
    * The reference vertical velocity in the unit 0.25 m.s-1 
    */
    private final short m_yVelocity;
    
    /**
    * The unit factor 
    */
    private final double m_unitFactor;
    //--------------------------------------------------------------------------
    // Static initialization block
    
}
