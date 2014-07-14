package ch.skyguide.ei.prototype.foservice.flightobject;

public interface IFlightIdentification {

	public String getAircraftIdentification();
	
	public String getMajorCarrierIdentifier();
	
	public void setAircraftIdentification(final String aircraftIdentification);
	
	public void setMajorCarrierIdentifier(final String majorCarrierIdentifier);
	
}
