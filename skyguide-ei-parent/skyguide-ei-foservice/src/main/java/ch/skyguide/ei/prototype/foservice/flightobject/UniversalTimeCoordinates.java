package ch.skyguide.ei.prototype.foservice.flightobject;

import java.util.Calendar;

public class UniversalTimeCoordinates implements IUniversalTimeCoordinates {
	
	public UniversalTimeCoordinates() {
		
		m_dateTime = null;
	}

	@Override
	public Calendar getDateTime() {

		return (Calendar)m_dateTime.clone();
	}

	@Override
	public void setDateTime(final Calendar dateTime) {

		if(dateTime == null) {
			
			throw new IllegalArgumentException("Non-initialized date/time.");
		}
		
		m_dateTime = (Calendar)dateTime.clone();
	}
	
	private Calendar m_dateTime;

}
