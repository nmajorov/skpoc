//------------------------------------------------------------------------------
//PROJECT:
//    Skyguide BEAM
//LAYER:
//    Business Logic
//COPYRIGHT:
//    Copyright (c) 1998-2003 skyguide,
//    swiss air navigation services, Zürich, Switzerland
//    All rights reserved.
//REVISION:
//    $Id:$
//------------------------------------------------------------------------------

package ch.skyguide.communication.rdps.asterix.categories;

//------------------------------------------------------------------------------


//------------------------------------------------------------------------------

/*******************************************************************************
* Describes the Asterix Data Categories.
* 
* 
* @author R. Wilson
*/
public enum DataCategoryType
{

    //--------------------------------------------------------------------------
    // public static final data members (constants)

    CAT030("CAT030", (short) 30, ""),
    CAT032("CAT032", (short) 32, ""),
    CAT252("CAT252", (short)252, ""),
    CAT062("CAT062", (short) 62, ""),
    CAT065("CAT065", (short) 65, ""),
    CAT242("CAT242", (short)242, ""),
    CAT243("CAT243", (short)243, "");

    //--------------------------------------------------------------------------
    // public constructors

    //--------------------------------------------------------------------------
    // public member functions

    /***************************************************************************
    * Retrieves the abbreviation describing the Data Category Type.
    * 
    * @return  java.lang.String
    *          Abbreviation for the Flight Rule.
    * 
    */
    public String 
    getAbbreviation() 
    {
        return m_abbreviation;     
    }
       
    /***************************************************************************
    * Retrieves the byte representatiion of the Data Category Type.
    * 
    * @return The short containing the unsigned byte.
    * 
    */
    public short 
    getByteRepresentation() 
    {
        return m_byteRepresentation;     
    }
 
    /***************************************************************************
    * Retrieves the string text describing the Data Category Type.
    * 
    * @return java.lang.String
    * 
    */
    public String 
    getDescription() 
    {
        return m_description;     
    }

    /***************************************************************************
    * See description of class <code>Object</code>.
    * @see java.lang.Object#toString()
    */
    @Override
	public String 
    toString()
    {
        return m_abbreviation;
    }

    //--------------------------------------------------------------------------
    // public static member functions
    
    /***************************************************************************
    * Determine the Data Category based on the byte representation of the 
    * Data Category Type.
    * 
    * @param   determineBasedOnByteRepresentation
    *          Determine based on byte representation of the data category.
    * 
    * @return   The <code>DataCategory</code> based upon the byte representation
    *           
    * 
    * 
    */
    public static DataCategoryType 
    determineRule(short determineBasedOnByteRepresentation) 
    {
        for (DataCategoryType category : values()) {
        	if (category.getByteRepresentation() == determineBasedOnByteRepresentation) {
        		return category;
        	}
        }
        throw new IllegalArgumentException();
    }

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

    /***************************************************************************
    * Private constructor creating the Data Category Type with abbreviation, 
    * the byte representation and string description.
    *
    * @param   abbreviation
    *          String containing an abbreviation for the category.
    *  
    * @param   byteRepresentation
    *          Byte represenation of the data category.
    * 
    * @param   description
    *          String description of the data category.
    *
    */
    private 
    DataCategoryType(String abbreviation, 
                     short byteRepresentation, 
                     String description) 
    {
        m_abbreviation = abbreviation;
        m_byteRepresentation = byteRepresentation;
        m_description = description;
    }

    //--------------------------------------------------------------------------
    // private member functions

    //--------------------------------------------------------------------------
    // private static member functions

    //--------------------------------------------------------------------------
    // private data members

    /***************************************************************************
    * The abbreviation of the Data Category Type.
    */
    private final String m_abbreviation;

    /***************************************************************************
    * The byte representation of the Data Category Type.
    */
    private final short m_byteRepresentation;
    
    /***************************************************************************
    * The string description of the Data Category Type.
    */
    private final String m_description;
    
    //--------------------------------------------------------------------------
    // private static data members

    //--------------------------------------------------------------------------
    // Static initialisation block
}