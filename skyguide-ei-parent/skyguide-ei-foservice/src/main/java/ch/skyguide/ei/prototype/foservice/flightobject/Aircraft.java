package ch.skyguide.ei.prototype.foservice.flightobject;

import ch.skyguide.environment.aeronautical.navigation.WakeTurbulenceType;

public class Aircraft implements IAircraft {
	
	public Aircraft() {
		
		m_address = null;
		m_registration = null;
		m_typeOfAircraftDesignator = null;
		m_wakeTurbulenceCategory = null;
	}

	@Override
	public String getAddress() {
		
		return m_address;
	}

	@Override
	public int getAircraftQuantity() {
		
		return m_aircraftQuantity;
	}
	
	@Override
	public String getRegistration() {
		
		return m_registration;
	}

	@Override
	public String getTypeOfAircraftDesignator() {
		
		return m_typeOfAircraftDesignator;
	}

	@Override
	public WakeTurbulenceType getWakeTurbulenceCategory() {

		return m_wakeTurbulenceCategory;
	}

	@Override
	public void setAddress(final String address) {

		m_address = address;
	}

	@Override
	public void setAircraftQuantity(int aircraftQuantity) {
		
		m_aircraftQuantity = aircraftQuantity;
	}

	@Override
	public void setRegistration(final String registration) {

		m_registration = registration;
	}

	@Override
	public void setTypeOfAircraftDesignator(final String typeOfAircraftDesignator) {

		m_typeOfAircraftDesignator = typeOfAircraftDesignator;
	}

	@Override
	public void setWakeTurbulenceCategory(final WakeTurbulenceType wtc) {

		m_wakeTurbulenceCategory = wtc;
	}
	
	private String m_address;
	
	private int m_aircraftQuantity = 1; // Default value assumed
	
	private String m_registration;
	
	private String m_typeOfAircraftDesignator;
	
	private WakeTurbulenceType m_wakeTurbulenceCategory;

}
