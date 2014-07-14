package ch.skyguide.ei.prototype.foservice.flightobject;

import ch.skyguide.environment.aeronautical.navigation.WakeTurbulenceType;

public interface IAircraft {
	
	public String getAddress();
	
	public int getAircraftQuantity();
	
	public String getRegistration();
	
	public String getTypeOfAircraftDesignator();
	
	public WakeTurbulenceType getWakeTurbulenceCategory();
		
	public void setAddress(final String address);
	
	public void setAircraftQuantity(final int aircraftQuantity);
	
	public void setRegistration(final String registration);

	public void setTypeOfAircraftDesignator(final String typeOfAircraftDesignator);
	
	public void setWakeTurbulenceCategory(final WakeTurbulenceType wtc);
	

}
