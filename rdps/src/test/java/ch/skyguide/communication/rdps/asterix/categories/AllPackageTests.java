//------------------------------------------------------------------------------
//PROJECT:
//    skyguide BEAM
//LAYER:
//    IUnit Test
//COPYRIGHT:
//    Copyright (c) 1998-2004 Zühlke Engineering AG, Schlieren, Switzerland
//    All rights reserved.
//REVISION:
//
//------------------------------------------------------------------------------

package ch.skyguide.communication.rdps.asterix.categories;

//------------------------------------------------------------------------------

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Test suite which executes all unit tests in this package.
 * 
 * @author Ross Wilson
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CartesianTrackPositionTest.class, 
					 TrackStatusTest.class})
public class AllPackageTests
{
    // For JUnit4 test suites the class can be empty. All the necessary
    // information is contained in the annotations.
}
