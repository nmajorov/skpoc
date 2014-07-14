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
package ch.skyguide.communication.rdps.asterix.categories.cat242;


//------------------------------------------------------------------------------

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.skyguide.message.structure.datafield.NonCompoundField;

/**
 * Test cases for the class {@code TrackStatus}.
 * 
 * @author Ross Wilson
 */
public class MrtSourceTest
{
    //--------------------------------------------------------------------------
    // public static final data members (constants)

    //--------------------------------------------------------------------------
    // public constructors
    
    //--------------------------------------------------------------------------
    // public member functions
	
	@Before
	public void setup() {
		
		m_mrtSource 
				= new MrtSource((short)242, 10, "MRT Source", "N.A.");
	}
	
	
	@Test
	public void testConstructor()
	{
		MrtSource mrtSource 
			= new MrtSource((short)242, 10,  "MRT Source", "N/A");
		Assert.assertNotNull(mrtSource);
	}
	
	@Test
	public void testPacking() {
		
		
		NonCompoundField dataField 
			= m_mrtSource.pack((short)1, (short)1, (short)2);
		
		MrtSource.MrtSourceContainer container   
			= m_mrtSource.unpack(dataField);
		
		Assert.assertNotNull(container);
		Assert.assertEquals(container.getSiteIdentification(), 1);
		Assert.assertEquals(container.getMvIdentification(), 1);
		Assert.assertEquals(container.getArtasUnit(), 2);
	}
	
	@Test
	public void testLowerRangePacking() {
		
		
		NonCompoundField dataField 
			= m_mrtSource.pack((short)0, (short)0, (short)0);
		
		MrtSource.MrtSourceContainer container   
			= m_mrtSource.unpack(dataField);
		
		Assert.assertNotNull(container);
		Assert.assertEquals(container.getSiteIdentification(),0);
		Assert.assertEquals(container.getMvIdentification(), 0);
		Assert.assertEquals(container.getArtasUnit(), 0);
	}

	@Test
	public void testUpperRangePacking() {
		
		
		NonCompoundField dataField 
			= m_mrtSource.pack((short)15, (short)31, (short)31);
		
		MrtSource.MrtSourceContainer container   
			= m_mrtSource.unpack(dataField);
		
		Assert.assertNotNull(container);
		Assert.assertEquals(container.getSiteIdentification(), 15);
		Assert.assertEquals(container.getMvIdentification(), 31);
		Assert.assertEquals(container.getArtasUnit(), 31);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testOutofRangePacking() {
		
		
		NonCompoundField dataField 
			= m_mrtSource.pack((short)20, (short)20, (short)20);
		
		MrtSource.MrtSourceContainer container   
			= m_mrtSource.unpack(dataField);
		
		Assert.assertNotNull(container);
		Assert.assertEquals(20, container.getSiteIdentification());
		Assert.assertEquals(20, container.getMvIdentification());
		Assert.assertEquals(20, container.getArtasUnit());
	}

	private MrtSource m_mrtSource;

    
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
