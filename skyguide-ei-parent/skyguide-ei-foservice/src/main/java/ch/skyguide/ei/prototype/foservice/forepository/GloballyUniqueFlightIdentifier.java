package ch.skyguide.ei.prototype.foservice.forepository;

import ch.skyguide.ei.prototype.foservice.flightobject.IGloballyUniqueFlightIdentifier;

public class GloballyUniqueFlightIdentifier implements
		IGloballyUniqueFlightIdentifier {
	
	public GloballyUniqueFlightIdentifier(final String gufi) {
		
		if(gufi == null) {
			
			throw new IllegalArgumentException("Non-initialized Globally "
					+ "Unique Identifier.");
		}
		
		m_globallyUniqueIdentifier = gufi;
	}

	@Override
	public final String getIdentifier() {

		return m_globallyUniqueIdentifier;
	}
	
	private final String m_globallyUniqueIdentifier;

}
