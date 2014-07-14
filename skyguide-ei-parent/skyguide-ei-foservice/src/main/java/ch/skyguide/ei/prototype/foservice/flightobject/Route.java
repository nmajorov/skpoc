package ch.skyguide.ei.prototype.foservice.flightobject;

import ch.skyguide.environment.aeronautical.navigation.FlightRuleType;

public final class Route implements IRoute {
	
	public Route() {
		
		m_initialFlightRule = FlightRuleType.UNKNOWN;
	}

	@Override
	public final FlightRuleType getInitialFlightRule() {

		return m_initialFlightRule;
	}

	@Override
	public final void setInitialFlightRule(FlightRuleType flightRule) {

		m_initialFlightRule = flightRule;
	}
	
	private FlightRuleType m_initialFlightRule;

}
