package ch.skyguide.ei.prototype.foservice.flightobject;

public final class FlightIdentification implements IFlightIdentification {
	

	public FlightIdentification() {
		
		m_aircraftIdentification = null;
		m_majorCarrierIdentifier = null;
	}
	
	@Override
	public final String getAircraftIdentification() {
		
		return m_aircraftIdentification;
	}

	@Override
	public final String getMajorCarrierIdentifier() {
		
		return m_majorCarrierIdentifier;
	}

	@Override
	public final void setAircraftIdentification(final String aircraftIdentification) {

		m_aircraftIdentification = aircraftIdentification;
	}

	@Override
	public final void setMajorCarrierIdentifier(final String majorCarrierIdentifier) {
		
		m_majorCarrierIdentifier = majorCarrierIdentifier;
	}

	private String m_aircraftIdentification;
	
	private String m_majorCarrierIdentifier;
}
