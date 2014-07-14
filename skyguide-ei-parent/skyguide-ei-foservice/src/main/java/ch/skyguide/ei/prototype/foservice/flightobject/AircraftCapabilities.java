package ch.skyguide.ei.prototype.foservice.flightobject;

public final class AircraftCapabilities implements IAircraftCapabilities {
	
	public AircraftCapabilities() {
		
		m_navigationCapabilities = null;
	}

	@Override
	public final INavigationCapabilities getNavigationCapabilities() {

		return m_navigationCapabilities;
	}

	@Override
	public final void setNavigationCapabilities(
			INavigationCapabilities navigationCapabilities) {

		m_navigationCapabilities = navigationCapabilities;
	}
	
	private INavigationCapabilities m_navigationCapabilities;
}
