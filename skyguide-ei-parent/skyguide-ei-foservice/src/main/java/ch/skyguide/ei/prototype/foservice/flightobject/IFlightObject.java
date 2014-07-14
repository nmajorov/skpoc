package ch.skyguide.ei.prototype.foservice.flightobject;

import ch.skyguide.flightplan.structure.FlightType;
import ch.skyguide.flightplan.structure.route.IArrivalInformation;
import ch.skyguide.flightplan.structure.route.IDepartureInformation;

public interface IFlightObject {
	
	public IGloballyUniqueFlightIdentifier getGloballyUniqueFlightIdentifier();
	
	public IArrivalInformation getArrivalInformation();
	
	public IDepartureInformation getDepartureInformation();
	
	public IClearedFlightInformation getClearedFlightInformation();
	
	public IFlightIdentification getFlightIdentification();
	
	public IAircraft getAircraft();
	
	public IRoute getRoute();
	
	public FlightType getTypeOfFlight();

	public void setTypeOfFlight(FlightType typeOfFlight);
	
}
