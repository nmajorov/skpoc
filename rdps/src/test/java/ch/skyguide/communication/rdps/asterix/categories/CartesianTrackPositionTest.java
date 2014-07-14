//------------------------------------------------------------------------------
// PROJECT:
//      skyguide BEAM
// LAYER:
//      Test Framework
// COPYRIGHT:
//      Copyright (c) 1998-2007 skyguide AG, Wangen bei Dübendorf, Switzerland
//      All rights reserved.
// REVISION:
//      
//------------------------------------------------------------------------------


//------------------------------------------------------------------------------
package ch.skyguide.communication.rdps.asterix.categories;


//------------------------------------------------------------------------------

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.skyguide.communication.rdps.asterix.categories.CartesianTrackPosition.CartesianPosition;
import ch.skyguide.message.structure.datafield.NonCompoundField;

/**
 * Test cases for the class {@code CartesianTrackPosition}.
 * 
 * @author Ross Wilson
 */
public class CartesianTrackPositionTest
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors
    
    //--------------------------------------------------------------------------
    // public member functions
	
	@Before
	public void setup() {
		
		m_cartesianTrackPosition 
				= new CartesianTrackPosition((short)62, 100, 6, 
	   				     "Calculated Track Position (Cartesian)", 
					     "1/32 or 1/64 nm");
	}
	
	
	@Test
	public void testConstructor()
	{
		CartesianTrackPosition trackPosition 
		= new CartesianTrackPosition((short)62, 100, 8, 
				   				     "Calculated Track Position (Cartesian)", 
								     "1/32 or 1/64 nm");
		Assert.assertNotNull(trackPosition);
	}
	
//	@Ignore
	@Test
	public void testPacking() {
		
		
		NonCompoundField dataField = m_cartesianTrackPosition.pack(10, 200);
		
		CartesianPosition cartesianPosition 
			= m_cartesianTrackPosition.unpack(dataField);
		
		Assert.assertEquals(10, cartesianPosition.getXComponent());
		Assert.assertEquals(200, cartesianPosition.getYComponent());
	}
    
	@Test
	public void testPacking2() {
		
		m_cartesianTrackPosition 
		= new CartesianTrackPosition((short)62, 100, 8, 
				     "Calculated Track Position (Cartesian)", 
			     "1/32 or 1/64 nm");

		NonCompoundField dataField = m_cartesianTrackPosition.pack(10, 200);
		
		CartesianPosition cartesianPosition 
			= m_cartesianTrackPosition.unpack(dataField);
		
		Assert.assertEquals(10, cartesianPosition.getXComponent());
		Assert.assertEquals(200, cartesianPosition.getYComponent());
	}
	
	private CartesianTrackPosition m_cartesianTrackPosition;

    
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
    // package member functions

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
