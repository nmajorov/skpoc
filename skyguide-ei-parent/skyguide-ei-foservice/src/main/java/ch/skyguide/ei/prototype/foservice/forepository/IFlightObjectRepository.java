package ch.skyguide.ei.prototype.foservice.forepository;

import java.util.Set;

import ch.skyguide.ei.prototype.foservice.flightobject.IFlightObject;
import ch.skyguide.ei.prototype.foservice.flightobject.IGloballyUniqueFlightIdentifier;
import ch.skyguide.ei.prototype.foservice.flightobject.IUniversalTimeCoordinates;

public interface IFlightObjectRepository {
	
	public void createFlightObject(IFlightObject flightObject)
	throws FlightObjectRepositoryException;
	
	public IFlightObject retrieveFlightObject(final IGloballyUniqueFlightIdentifier gufi)
	throws FlightObjectRepositoryException;
	
	public IFlightObject retrieveFlightObject(final String aircraftIdentification,
											  final String aerodromeOfDepartureDesignator,
											  final String aerodromeOfDestinationDesignator,
											  final IUniversalTimeCoordinates estimatedOffBlockTime)
	throws FlightObjectRepositoryException;

	public Set<IFlightObject> retrieveFlightObject(final String aircraftIdentification)
	throws FlightObjectRepositoryException;
	
	public Set<IFlightObject> retrieveFlightObject(final String aircraftIdentification,
												   final String aerodromeOfDepartureDesignator,
												   final String aerodromeOfDestinationDesignator)
	throws FlightObjectRepositoryException;
	
	public void updateFlightObject(IFlightObject flightObject)
	throws FlightObjectRepositoryException;
	
	public void deleteFlightObject(IFlightObject flightObject)
	throws FlightObjectRepositoryException;
	
	public void shutdown() throws FlightObjectRepositoryException;
	
	public void deleteFlightObject(IGloballyUniqueFlightIdentifier gufi)
	throws FlightObjectRepositoryException;

}
