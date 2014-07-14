package ch.skyguide.ei.prototype.foservice;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.Set;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.skyguide.ei.prototype.foservice.flightobject.FlightObject;
import ch.skyguide.ei.prototype.foservice.flightobject.IFlightObject;
import ch.skyguide.ei.prototype.foservice.flightobject.IGloballyUniqueFlightIdentifier;
import ch.skyguide.ei.prototype.foservice.flightobject.IUniversalTimeCoordinates;
import ch.skyguide.ei.prototype.foservice.flightobject.UniversalTimeCoordinates;
import ch.skyguide.ei.prototype.foservice.forepository.FlightObjectRepository;
import ch.skyguide.ei.prototype.foservice.forepository.FlightObjectRepositoryException;
import ch.skyguide.ei.prototype.foservice.forepository.GloballyUniqueIdentifierFactory;
import ch.skyguide.ei.prototype.foservice.forepository.IFlightObjectRepository;
import ch.skyguide.environment.aeronautical.aerodrome.SimpleAerodrome;
import ch.skyguide.environment.aeronautical.navigation.FlightRuleType;
import ch.skyguide.environment.aeronautical.navigation.WakeTurbulenceType;
import ch.skyguide.flightplan.structure.FlightType;
import ch.skyguide.quantity.QuantityFactory;

public class FlightObjectRepositoryTest extends CamelTestSupport{
	
	@BeforeClass
	public static void deleteDatabase() {
		
		final File dbDirectory = new File("./");
		
		FilenameFilter filenameFilter 
			= new FilenameFilter() {
				public boolean accept(File dir, String name) {
					
					if (name.contains(".d4o")) {
						
						return true;
					}
					
					return false;
				};
			};
		
		if(dbDirectory.isDirectory()) {
			
			for(String filename: dbDirectory.list(filenameFilter)) {
			
				File file = new File(filename);
				file.delete();
				
			}
		}
		
	}
	
	public static void packDb() throws FlightObjectRepositoryException {
		
		final IUniversalTimeCoordinates eobt = new UniversalTimeCoordinates();
		eobt.setDateTime(Calendar.getInstance());
		
		IFlightObject flightObject = createFlightObject("SWR100", "LSZH", "LSGG", eobt, "Swiss", "HBCIO", 140);
		m_repository.createFlightObject(flightObject);
		
		flightObject = createFlightObject("SWR200", "LSGG", "LSZH", eobt, "Swiss", "HBDAO", 130);
		m_repository.createFlightObject(flightObject);

		flightObject = createFlightObject("DLH333", "EDMM", "LSZH", eobt, "Lufthansa", "DAGIA", 280);
		m_repository.createFlightObject(flightObject);

		flightObject = createFlightObject("DLH340", "LSZH", "EDMM", eobt, "Luftansa", "DIMMA", 240);
		m_repository.createFlightObject(flightObject);
		
		m_flightObject = flightObject;
	}
	
	private static final IFlightObject createFlightObject(
			final String arcid,
			final String adep,
			final String ades,
			final IUniversalTimeCoordinates eobt,
			final String carrier,
			final String registration,
			final int flightLevel) {
		
		final IGloballyUniqueFlightIdentifier gufi 
		= GloballyUniqueIdentifierFactory
		.getInstance().getUniqueIdentifier(arcid, adep, ades, eobt);
		
		final IFlightObject flightObject 
			= new FlightObject(gufi, FlightType.SCHEDULED);
		
		flightObject.getArrivalInformation().setAerodromeOfDestination(new SimpleAerodrome(ades));
		flightObject.getDepartureInformation().setAerodromeOfDeparture(new SimpleAerodrome(adep));
		flightObject.getDepartureInformation().setEstimatedOffBlockTime(eobt.getDateTime());
		flightObject.getClearedFlightInformation().setFlightLevel(QuantityFactory.getFlightLevel(230));
		flightObject.getFlightIdentification().setAircraftIdentification(arcid);
		flightObject.getFlightIdentification().setMajorCarrierIdentifier("Swiss");
		flightObject.getRoute().setInitialFlightRule(FlightRuleType.INSTRUMENT_FLIGHT_RULES);
		flightObject.getAircraft().setRegistration("HBCIO");
//		flightObject.getAircraft().setTypeOfAircraft(typeOfAircraft);
		flightObject.getAircraft().setWakeTurbulenceCategory(WakeTurbulenceType.HEAVY);
		
		return flightObject;
	}
	
	@BeforeClass
	public static void startupDb() throws FlightObjectRepositoryException {
		
		m_repository = new FlightObjectRepository("./testdb.d4o");
		
		Assert.assertNotNull(m_repository);
		
		packDb();
	}

	@AfterClass
	public static void shutdownDb() throws FlightObjectRepositoryException {
		
		m_repository.shutdown();
	}

	@Test
	public void storeFlightObject() throws FlightObjectRepositoryException {
		
		Assert.assertNotNull(m_repository);
		
		m_repository.createFlightObject(m_flightObject);
		m_repository.deleteFlightObject(m_flightObject);
	}
	
	@Test
	public void retrieveByArcid() throws FlightObjectRepositoryException {
		
		final Set<IFlightObject> results 
			= m_repository.retrieveFlightObject("SWR100");
		
		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertNotNull(results.iterator().next());
	}
	
	private static IFlightObject m_flightObject;
	
	private static IFlightObjectRepository m_repository;
}
