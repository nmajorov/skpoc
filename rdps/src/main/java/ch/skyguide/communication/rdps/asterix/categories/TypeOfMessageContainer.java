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

package ch.skyguide.communication.rdps.asterix.categories;

//------------------------------------------------------------------------------


//------------------------------------------------------------------------------

/*******************************************************************************
* Container class for the asterix family and nature of the message.
*  
* @author R. Wilson
*/
public class TypeOfMessageContainer 
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors

    /***************************************************************************
    * Public defined constructor taking the family and nature of the asterix
    * message.
    *  
    * @param    familyOfMessage
    *           The family of the asterix message.
    *           
    * @param    natureOfMessage
    *           The nature of the asterix message.
    *           
    */
    public TypeOfMessageContainer(byte familyOfMessage, byte natureOfMessage) 
    {
        m_familyOfMessage = familyOfMessage;
        m_natureOfMessage = natureOfMessage;     
    }
    
    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Retrieves the family of the asterix message.
    *  
    * @return   The <code>byte</code> indicating the asterix family of message.
    */
    public final byte getFamilyOfMessage() 
    {
        return m_familyOfMessage;     
    }
    
    /***************************************************************************
    * Retrieves the nature of the asterix message.
    *  
    * @return   The <code>byte</code> indicating the asterix nature of the 
    *           message.
    */
    public final byte getNatureOfMessage() 
    {
        return m_natureOfMessage;     
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
    * The family of the asterix message. 
    */
    private final byte m_familyOfMessage;
    
    /**
    * The nature of the asterix message. 
    */
    private final byte m_natureOfMessage;

    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialization block
    
}
