package ch.skyguide.ei.prototype.foservice.flightobject;

import ch.skyguide.quantity.IQuantity;

public final class ClearedFlightInformation 
implements IClearedFlightInformation {

	public ClearedFlightInformation() {
		
		m_level = null;
	}
	
	@Override
	public final IQuantity getFlightLevel() {

		return m_level;
	}

	@Override
	public final void setFlightLevel(final IQuantity level) {

		m_level = level;
	}
	
	private IQuantity m_level;

}
