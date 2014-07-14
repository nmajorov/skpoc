package ch.skyguide.ei.prototype.foservice.flightobject;

import ch.skyguide.quantity.IQuantity;

public interface IClearedFlightInformation {
	
	public IQuantity getFlightLevel();
	
	public void setFlightLevel(final IQuantity level);

}
