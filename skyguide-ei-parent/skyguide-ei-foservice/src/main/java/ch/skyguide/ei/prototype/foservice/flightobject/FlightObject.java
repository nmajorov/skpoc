package ch.skyguide.ei.prototype.foservice.flightobject;

import ch.skyguide.flightplan.structure.FlightType;
import ch.skyguide.flightplan.structure.route.ArrivalInformation;
import ch.skyguide.flightplan.structure.route.DepartureInformation;
import ch.skyguide.flightplan.structure.route.IArrivalInformation;
import ch.skyguide.flightplan.structure.route.IDepartureInformation;

public final class FlightObject implements IFlightObject {
	
	public FlightObject(final IGloballyUniqueFlightIdentifier gufi,
						final FlightType typeOfFlight) {
		
		if(gufi == null) {
			
			throw new IllegalArgumentException("Non-initialized GUFI");
		}

		if(typeOfFlight == null) {
			
			throw new IllegalArgumentException("Non-initialized Type of Flight");
		}

		m_globallyUniqueFlightIdentier = gufi;
		m_typeOfFlight = typeOfFlight;
	}

	@Override
	public final IGloballyUniqueFlightIdentifier getGloballyUniqueFlightIdentifier() {

		return m_globallyUniqueFlightIdentier;
	}

	@Override
	public final IArrivalInformation getArrivalInformation() {

		return m_arrivalInformation;
	}

	@Override
	public final IDepartureInformation getDepartureInformation() {

		return m_departureInformation;
	}

	@Override
	public final IClearedFlightInformation getClearedFlightInformation() {
		
		return m_clearedFlightInformation;
	}

	@Override
	public final IFlightIdentification getFlightIdentification() {
		
		return m_flightIdentification;
	}

	@Override
	public final IAircraft getAircraft() {

		return m_aircraft;
	}

	@Override
	public final IRoute getRoute() {
		
		return m_route;
	}

	@Override
	public final FlightType getTypeOfFlight() {

		return m_typeOfFlight;
	}
	
	@Override
	public final void setTypeOfFlight(final FlightType typeOfFlight) {

		m_typeOfFlight = typeOfFlight;
	}
	
	private final IAircraft m_aircraft = new Aircraft();
	
	private final IArrivalInformation m_arrivalInformation 
		= new ArrivalInformation();
	
	private final IClearedFlightInformation m_clearedFlightInformation 
		= new ClearedFlightInformation();
	
	private final IDepartureInformation m_departureInformation 
		= new DepartureInformation();
	
	private final IFlightIdentification m_flightIdentification
		= new FlightIdentification();
	
	private final IGloballyUniqueFlightIdentifier m_globallyUniqueFlightIdentier;
	
	private final IRoute m_route = new Route();
	
	private FlightType m_typeOfFlight;
	

}
