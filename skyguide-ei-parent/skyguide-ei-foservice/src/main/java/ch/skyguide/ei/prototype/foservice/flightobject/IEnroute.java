package ch.skyguide.ei.prototype.foservice.flightobject;

import java.util.List;

import aero.fixm.model.flight.AircraftPositionType;

public interface IEnroute {
	
	public AircraftPositionType getPosition();
	
	public List<AircraftPositionType> getPreviousPositions();
	
	public void setPosition(final AircraftPositionType position);

}
