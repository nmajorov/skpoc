package ch.skyguide.ei.prototype.foservice.flightobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aero.fixm.model.flight.AircraftPositionType;

public final class Enroute implements IEnroute {

	public Enroute() {
		
		m_position = null;
		
	}
	
	@Override
	public final AircraftPositionType getPosition() {

		return m_position;
	}

	@Override
	public final List<AircraftPositionType> getPreviousPositions() {

		return Collections.<AircraftPositionType>unmodifiableList(m_previousPositions);
	}

	@Override
	public final void setPosition(final AircraftPositionType position) 
	throws IllegalArgumentException {
		
		if(position == null) {
			
			throw new IllegalArgumentException("Non-initialized position.");
		}

		if(m_position != null) {
		
			m_previousPositions.add(getPosition());
		}
		
		m_position = position;

	}
	
	private AircraftPositionType m_position;
	
	private final List<AircraftPositionType> m_previousPositions 
		= new ArrayList<AircraftPositionType>();

}
