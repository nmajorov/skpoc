package ch.skyguide.ei.prototype.foservice.forepository;

import java.util.concurrent.atomic.AtomicInteger;

import ch.skyguide.ei.prototype.foservice.flightobject.IGloballyUniqueFlightIdentifier;
import ch.skyguide.ei.prototype.foservice.flightobject.IUniversalTimeCoordinates;

public class GloballyUniqueIdentifierFactory {
	
	
	public IGloballyUniqueFlightIdentifier 
	getUniqueIdentifier(final String aircraftIdentification,
						final String aerodromeOfDepartureDesignator,
						final String aerodromeOfDestinationDesignator,
						final IUniversalTimeCoordinates estimatedOffBlockTime) {
		
		final String gufi = aircraftIdentification + "-" 
				+ aerodromeOfDepartureDesignator + "-" 
				+ aerodromeOfDestinationDesignator + "-" 
				+ estimatedOffBlockTime.getDateTime().toString() 
				+ m_uniqueNumber.getAndIncrement();
		
		return new GloballyUniqueFlightIdentifier(gufi);
	}
	
	public static GloballyUniqueIdentifierFactory getInstance() {
		
		if(m_instance == null) {
			
			m_instance = new GloballyUniqueIdentifierFactory();
		}
		
		return m_instance;
	}
	
	private GloballyUniqueIdentifierFactory() {
		
	}
 	
	private AtomicInteger m_uniqueNumber = new AtomicInteger(0);
	
	private static GloballyUniqueIdentifierFactory m_instance = null;

}
