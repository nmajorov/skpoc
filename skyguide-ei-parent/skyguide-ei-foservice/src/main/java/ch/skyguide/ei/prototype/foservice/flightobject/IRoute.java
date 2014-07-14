package ch.skyguide.ei.prototype.foservice.flightobject;

import ch.skyguide.environment.aeronautical.navigation.FlightRuleType;

public interface IRoute {
	
	public FlightRuleType getInitialFlightRule();
	
	public void setInitialFlightRule(final FlightRuleType flightRule);

}
