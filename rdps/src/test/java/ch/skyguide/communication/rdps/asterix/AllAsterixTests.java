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

package ch.skyguide.communication.rdps.asterix;

//------------------------------------------------------------------------------

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 
 * Runs all unit tests in this package and all subpackages.
 * 
 * @author Christoph Schmitz
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({AllPackageTests.class,
                     ch.skyguide.communication.rdps.asterix.categories.AllPackageTests.class,
                     ch.skyguide.communication.rdps.asterix.categories.cat242.AllPackageTests.class,
                     ch.skyguide.communication.rdps.asterix.generaldataitems.AllPackageTests.class,
                     ch.skyguide.communication.rdps.asterix.structure.AllPackageTests.class})
public class AllAsterixTests
{
    // For JUnit4 test suites the class can be empty. All the necessary
    // information is contained in the annotations.
}
