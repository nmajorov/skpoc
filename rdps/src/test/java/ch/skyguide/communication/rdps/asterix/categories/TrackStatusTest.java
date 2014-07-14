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

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.skyguide.communication.rdps.asterix.categories.TrackStatus.SourceType;
import ch.skyguide.message.structure.datafield.NonCompoundField;

/**
 * Test cases for the class {@code TrackStatus}.
 * 
 * @author Ross Wilson
 */
public class TrackStatusTest
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors
    
    //--------------------------------------------------------------------------
    // public member functions
	
	@Before
	public void setup() {
		
		m_trackStatus 
				= new TrackStatus((short)62, 80, "Track Status", "n/a");
	}
	
	
	@Test
	public void testConstructor()
	{
		TrackStatus trackStatus 
		= new TrackStatus((short)62, 80,  "Track Status", "n/a");
		Assert.assertNotNull(trackStatus);
	}
	
	@Test
	public void testPacking() {
		
		
		NonCompoundField dataField 
			= m_trackStatus.pack(true, false, true, SourceType.DEFAULT_HEIGHT, 
						false, false, false, false, false, false, false, false);
		
		List<Byte> fieldBytes  
			= m_trackStatus.unpack(dataField);
		
	}
	
	private TrackStatus m_trackStatus;

    
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
