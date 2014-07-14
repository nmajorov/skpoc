package ch.skyguide.ei.prototype.foservice.flightobject;

import java.util.Calendar;

public interface IUniversalTimeCoordinates {
	
	public Calendar getDateTime();
	
	public void setDateTime(final Calendar dateTime);
}
