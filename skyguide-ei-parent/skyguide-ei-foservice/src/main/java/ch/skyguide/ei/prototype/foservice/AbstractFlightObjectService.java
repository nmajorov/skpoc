package ch.skyguide.ei.prototype.foservice;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import aero.fixm.model.base.CarrierIdentifierType;
import aero.fixm.model.base.CountType;
import aero.fixm.model.base.FlightIdentifierType;
import aero.fixm.model.base.FlightRulesType;
import aero.fixm.model.base.FreeTextType;
import aero.fixm.model.base.IcaoAerodromeReferenceType;
import aero.fixm.model.base.IcaoAircraftIdentifierType;
import aero.fixm.model.base.MultiTimeType;
import aero.fixm.model.base.ReportedTime;
import aero.fixm.model.flight.AircraftAddressType;
import aero.fixm.model.flight.AircraftRegistrationType;
import aero.fixm.model.flight.AircraftType;
import aero.fixm.model.flight.AircraftTypeElement;
import aero.fixm.model.flight.FlightArrivalType;
import aero.fixm.model.flight.FlightDepartureType;
import aero.fixm.model.flight.FlightIdentificationElement;
import aero.fixm.model.flight.FlightType;
import aero.fixm.model.flight.RouteType;
import aero.fixm.model.flight.TypeOfFlightType;
import aero.fixm.model.flight.WakeTurbulenceCategoryType;
import aero.fixm.model.foundation.AerodromeIcaoCodeType;
import aero.fixm.model.foundation.AltitudeReferenceType;
import aero.fixm.model.foundation.AltitudeType;
import aero.fixm.model.foundation.TimeType;
import ch.skyguide.ei.prototype.foservice.flightobject.FlightObject;
import ch.skyguide.ei.prototype.foservice.flightobject.IAircraft;
import ch.skyguide.ei.prototype.foservice.flightobject.IFlightIdentification;
import ch.skyguide.ei.prototype.foservice.flightobject.IFlightObject;
import ch.skyguide.ei.prototype.foservice.flightobject.IGloballyUniqueFlightIdentifier;
import ch.skyguide.ei.prototype.foservice.flightobject.IUniversalTimeCoordinates;
import ch.skyguide.ei.prototype.foservice.flightobject.UniversalTimeCoordinates;
import ch.skyguide.ei.prototype.foservice.forepository.GloballyUniqueFlightIdentifier;
import ch.skyguide.ei.prototype.foservice.forepository.GloballyUniqueIdentifierFactory;
import ch.skyguide.ei.prototype.foservice.forepository.IFlightObjectRepository;
import ch.skyguide.environment.aeronautical.aerodrome.SimpleAerodrome;
import ch.skyguide.environment.aeronautical.navigation.FlightRuleType;
import ch.skyguide.environment.aeronautical.navigation.WakeTurbulenceType;
import ch.skyguide.flightplan.structure.route.IArrivalInformation;
import ch.skyguide.flightplan.structure.route.IDepartureInformation;
import ch.skyguide.quantity.QuantityFactory;

public abstract class AbstractFlightObjectService {
	
    protected final Set<FlightType> 
    transformToFixm(final Set<IFlightObject> repositoryFlights) 
    throws IllegalArgumentException {
    	
    	if (repositoryFlights == null) {
    		
    		throw new IllegalArgumentException("Non-initialized "
    				+ "repository flights.");
    	}
    	
    	final Set<FlightType> transformedFixmFlights 
    		= new HashSet<FlightType>();
    	
    	for(IFlightObject repositoryFlight : repositoryFlights) {
    		
    		final FlightType fixmFlight = transformToFixm(repositoryFlight);
    		transformedFixmFlights.add(fixmFlight);
    	}
    	
    	return transformedFixmFlights;
    }

    protected final FlightType 
    transformToFixm(final IFlightObject repositoryFlight) 
    throws IllegalArgumentException {
    	
    	if (repositoryFlight == null) {
    		
    		throw new IllegalArgumentException("Non-initialized repository "
    				+ "Flight.");
    	}
    	
    	FlightType fixmFlight = new FlightType();
    	
    	// ---------------------------------------------------------------------
    	// Aircraft description
    	IAircraft aircraft = repositoryFlight.getAircraft();
    	
    	if(aircraft != null) {
    		
    		fixmFlight.setAircraftDescription(new AircraftType());
    		
    		if (aircraft.getAddress() != null) {
    		
    			fixmFlight.getAircraftDescription()
    				.setAircraftAddress(new AircraftAddressType());
    			fixmFlight.getAircraftDescription()
    				.getAircraftAddress().setValue(new FreeTextType());
    			fixmFlight.getAircraftDescription()
    				.getAircraftAddress().getValue()
    					.setValue(aircraft.getAddress());;
    		}
    		
			fixmFlight.getAircraftDescription()
				.setAircraftQuantity(new CountType());
			fixmFlight.getAircraftDescription()
				.getAircraftQuantity().setValue(aircraft.getAircraftQuantity());
    		
    		if (aircraft.getRegistration() != null) {
        		
    			fixmFlight.getAircraftDescription()
    				.setRegistration(new AircraftRegistrationType());
    			fixmFlight.getAircraftDescription()
    				.getRegistration().setValue(new FreeTextType());
    			fixmFlight.getAircraftDescription()
    				.getRegistration().getValue()
    					.setValue(aircraft.getRegistration());;
    		}
    		
    		if (aircraft.getTypeOfAircraftDesignator() != null) {
        		
    			fixmFlight.getAircraftDescription()
    				.setAircraftType(new AircraftTypeElement());
    			fixmFlight.getAircraftDescription()
    				.getAircraftType()
    					.setIcaoModelIdentifier(new IcaoAircraftIdentifierType());
    			fixmFlight.getAircraftDescription()
    				.getAircraftType().getIcaoModelIdentifier()
    					.setValue(aircraft.getTypeOfAircraftDesignator());
    		}
    		
    		if (aircraft.getWakeTurbulenceCategory() != null) {
        		
    			fixmFlight.getAircraftDescription()
    				.setWakeTurbulence(WakeTurbulenceCategoryType
    					.valueOf(String.valueOf(aircraft
    						.getWakeTurbulenceCategory()
    							.getCharacterRepresentation())));
    		}
    	}
    	
    	if (repositoryFlight.getArrivalInformation() != null) {
    		
    		final IArrivalInformation arrival 
    			= repositoryFlight.getArrivalInformation();
    		
			fixmFlight.setArrival(new FlightArrivalType());

			if (arrival.getAerodromeOfDestination() != null) {
    			
    			final IcaoAerodromeReferenceType ades 
    				= new IcaoAerodromeReferenceType();
    			ades.setCode(new AerodromeIcaoCodeType());
    			ades.getCode().setValue(arrival
    				.getAerodromeOfDestination().getIdentifier());
    			
    			fixmFlight.getArrival().setArrivalAerodrome(ades);
    		}
    	}

    	if (repositoryFlight.getDepartureInformation() != null) {
    		
    		final IDepartureInformation departure 
    			= repositoryFlight.getDepartureInformation();

    		fixmFlight.setDeparture(new FlightDepartureType());
    		
    		if (departure.getAerodromeOfDeparture() != null) {
    			
    			final IcaoAerodromeReferenceType adep 
    				= new IcaoAerodromeReferenceType();
    			adep.setCode(new AerodromeIcaoCodeType());
    			adep.getCode().setValue(departure
    				.getAerodromeOfDeparture().getIdentifier());
    			
    			fixmFlight.getDeparture().setDepartureAerodrome(adep);
    		}
    		
    		if (departure.getEstimatedOffBlockTime() != null) {
    			
    			fixmFlight.getDeparture()
    				.setOffBlockReadyTime(new MultiTimeType());
    			
    			fixmFlight.getDeparture().getOffBlockReadyTime()
    				.setEstimated(new ReportedTime());
    			fixmFlight.getDeparture().getOffBlockReadyTime().getEstimated()
    				.setTime(new TimeType());
    			
    			
				try {
					XMLGregorianCalendar calender 
						= DatatypeFactory.newInstance()
    					.newXMLGregorianCalendar((GregorianCalendar)departure
    						.getEstimatedOffBlockTime());
	    			fixmFlight.getDeparture().getOffBlockReadyTime().getEstimated()
    				.getTime().setValue(calender);

				} catch (DatatypeConfigurationException e) {
					e.printStackTrace();
				}
    		}
    	}

    	if (repositoryFlight.getFlightIdentification() != null) {
    		
    		IFlightIdentification flightId 
    			= repositoryFlight.getFlightIdentification();

    		fixmFlight
    			.setFlightIdentification(new FlightIdentificationElement());
    		
    		if (flightId.getAircraftIdentification() != null) {

        		fixmFlight.getFlightIdentification()
    				.setAircraftIdentification(new FlightIdentifierType());
	    		fixmFlight.getFlightIdentification().getAircraftIdentification()
	    			.setValue(flightId.getAircraftIdentification());
    		}
    		
    		if (flightId.getMajorCarrierIdentifier() != null) {
    			
        		fixmFlight.getFlightIdentification()
    				.setMajorCarrierIdentifier(new CarrierIdentifierType());
	    		fixmFlight.getFlightIdentification().getMajorCarrierIdentifier()
	    			.setValue(flightId.getMajorCarrierIdentifier());
    		}
    	}
    	
    	if (repositoryFlight.getRoute() != null) {
    		
    		if (repositoryFlight.getRoute().getInitialFlightRule() != null) {
    			
    			fixmFlight.setRoute(new RouteType());
    			
    			FlightRuleType flightRule 
    				= repositoryFlight.getRoute().getInitialFlightRule();

    			if (FlightRuleType.CONTROLLED_VISUAL_FLIGHT_RULES
    					.equals(flightRule) 
					|| FlightRuleType.JOINING.equals(flightRule)
					|| FlightRuleType.VISUAL_FLIGHT_RULES.equals(flightRule)) {
    				
        			fixmFlight
        				.getRoute().setInitialFlightRules(FlightRulesType.VFR);
    			}
    			else if (FlightRuleType.INSTRUMENT_FLIGHT_RULES.equals(flightRule) 
    					|| FlightRuleType.LEAVING.equals(flightRule)) {
    				
        			fixmFlight
        				.getRoute().setInitialFlightRules(FlightRulesType.IFR);
    			}
    			else {
    				
        			fixmFlight
    					.getRoute().setInitialFlightRules(FlightRulesType.IFR);
    			}
    		}
    	}
    	
    	if (repositoryFlight.getTypeOfFlight() != null) {
    		
    		final ch.skyguide.flightplan.structure.FlightType 
    			typeOfFlight = repositoryFlight.getTypeOfFlight();
    		
    		if (ch.skyguide.flightplan.structure.FlightType.NONSCHEDULED.equals(typeOfFlight)) {
    			
    			fixmFlight.setFlightType(TypeOfFlightType.NON_SCHEDULED);
    		}
    		else if (ch.skyguide.flightplan.structure.FlightType.UNKNOWN.equals(typeOfFlight)) {
    			
    			fixmFlight.setFlightType(TypeOfFlightType.OTHER);
    		}
    		else {
    			
    			fixmFlight.setFlightType(TypeOfFlightType.valueOf(String
    				.valueOf(typeOfFlight.name())));
    		}
    	}
    	
    	return fixmFlight;
    }

    protected final IFlightObject 
    transformToFlightObject(final FlightType fixmFlight) {

    	String aircraftIdentification = "Unknown";
		if (fixmFlight.isSetFlightIdentification()) {
			
			if (fixmFlight.getFlightIdentification()
					.isSetAircraftIdentification()) {
				
				aircraftIdentification =
					fixmFlight.getFlightIdentification()
						.getAircraftIdentification().getValue();
			}
		}
		
    	String aerodromeOfDepartureDesignator = "Unknown Departure";
    	IUniversalTimeCoordinates estimatedOffBlockTime 
    		= new UniversalTimeCoordinates();
    	estimatedOffBlockTime.setDateTime(Calendar.getInstance());
    	
    	if (fixmFlight.isSetDeparture()) {
    		
    		if (fixmFlight.getDeparture().isSetDepartureAerodrome()) {
    			
    			aerodromeOfDepartureDesignator 
				= ((IcaoAerodromeReferenceType)fixmFlight.getDeparture()
						.getDepartureAerodrome()).getCode().getValue();
			
    		}
    		
    		if (fixmFlight.getDeparture().isSetOffBlockReadyTime()) {
    			
    			XMLGregorianCalendar gregorianCalendar 
    				= fixmFlight.getDeparture().getOffBlockReadyTime()
    					.getEstimated().getTime().getValue();
    			
    			estimatedOffBlockTime
    				.setDateTime(gregorianCalendar.toGregorianCalendar());
    		}
    	}
 
    	String aerodromeOfDestinationDesignator = "Unknown Destination";
    	
    	if (fixmFlight.isSetArrival()) {
    		
    		if (fixmFlight.getArrival().isSetArrivalAerodrome()) {
    			
    			aerodromeOfDestinationDesignator 
    				= ((IcaoAerodromeReferenceType)fixmFlight.getArrival()
    						.getArrivalAerodrome()).getCode().getValue();
    			
    		}
    	}
    	
    	IGloballyUniqueFlightIdentifier gufi = null;

    	if(fixmFlight.isSetGufi()) {
    	
    		gufi 
    			= new GloballyUniqueFlightIdentifier(fixmFlight.getGufi().getValue());
    	}
    	else {
    		
    		gufi = GloballyUniqueIdentifierFactory.getInstance()
    				.getUniqueIdentifier(aircraftIdentification, 
    									 aerodromeOfDepartureDesignator, 
    									 aerodromeOfDestinationDesignator, 
    									 estimatedOffBlockTime);
    	}
    	
    	ch.skyguide.flightplan.structure.FlightType typeOfFlight = null;
    	if(fixmFlight.isSetFlightType()) {
    		
    		if(fixmFlight.getFlightType().equals(TypeOfFlightType.NON_SCHEDULED)) {
    			
    			typeOfFlight = ch.skyguide.flightplan.structure
    					.FlightType.NONSCHEDULED;
    		}
    		else if(fixmFlight.getFlightType().equals(TypeOfFlightType.OTHER)) {
    			
    			typeOfFlight = ch.skyguide.flightplan.structure
    					.FlightType.UNKNOWN;
    		}
    		else {
    			
    			typeOfFlight = ch.skyguide.flightplan.structure
    					.FlightType.valueOf(fixmFlight.getFlightType().name());
    		}
    	}
    	
    	final IFlightObject repositoryFlight 
    		= new FlightObject(gufi, typeOfFlight);
    	
    	// ---------------------------------------------------------------------
    	// Aircraft description
    	if(fixmFlight.isSetAircraftDescription()) {
    		
    		if(fixmFlight.getAircraftDescription().isSetAircraftAddress()) {
    			
    			repositoryFlight.getAircraft()
    				.setAddress(fixmFlight.getAircraftDescription()
    					.getAircraftAddress().getValue().getValue());
    		}

    		if(fixmFlight.getAircraftDescription().isSetAircraftQuantity()) {
    			
    			
    			repositoryFlight.getAircraft()
    				.setAircraftQuantity(fixmFlight.getAircraftDescription()
    						.getAircraftQuantity().getValue());
    		}

    		if (fixmFlight.getAircraftDescription().isSetAircraftType()) {
    		
    			repositoryFlight.getAircraft()
    				.setTypeOfAircraftDesignator(fixmFlight
    					.getAircraftDescription().getAircraftType()
    						.getIcaoModelIdentifier().getValue());
    		}
    		
    		if (fixmFlight.getAircraftDescription().isSetRegistration()) {
    			
    			repositoryFlight.getAircraft()
					.setRegistration(fixmFlight.getAircraftDescription()
						.getRegistration().getValue().getValue());
    		}

    		if (fixmFlight.getAircraftDescription().isSetWakeTurbulence()) {
    			
    			WakeTurbulenceCategoryType fixmWtc 
    				= fixmFlight.getAircraftDescription().getWakeTurbulence();
    			
    			WakeTurbulenceType repositoryWtc 
    				= WakeTurbulenceType
    					.determineCategory(fixmWtc.name().charAt(0));
    					
    			repositoryFlight.getAircraft()
    				.setWakeTurbulenceCategory(repositoryWtc);
    		}
    	}
    	
    	// ---------------------------------------------------------------------
    	// Arrival Information
    	if (fixmFlight.isSetArrival()) {
    		
    		if (fixmFlight.getArrival().isSetArrivalAerodrome()) {
    			
    			final String icaoAerodromeDesignator 
    				= ((IcaoAerodromeReferenceType)fixmFlight.getArrival()
    						.getArrivalAerodrome()).getCode().getValue();
    			
    			repositoryFlight.getArrivalInformation()
    				.setAerodromeOfDestination(new SimpleAerodrome(icaoAerodromeDesignator));
    		}
    	}
    	
    	// ---------------------------------------------------------------------
    	// Cleared Information
    	if (fixmFlight.isSetCleared()) {
    		
    		if (fixmFlight.getCleared().isSetFlightLevel()) {
    			
    			
    			AltitudeType altitude = fixmFlight.getCleared().getFlightLevel();
    			AltitudeReferenceType altRef = altitude.getRef();
    			// Currently ignore the UoM and assume only feet and not meters
    			if (AltitudeReferenceType.FLIGHT_LEVEL.equals(altRef)) {

        			QuantityFactory
        				.getFlightLevel(altitude.getValue().getValue()/100);
    			}
    			else {
    				
        			QuantityFactory
        				.getAltitudeLevel((int)(altitude.getValue().getValue()/100));
    			}
    		}
    	}
    	
    	// ---------------------------------------------------------------------
    	// Departure Information
    	if (fixmFlight.isSetDeparture()) {
    		
    		if (fixmFlight.getDeparture().isSetDepartureAerodrome()) {
    			
    			final String icaoAerodromeDesignator 
				= ((IcaoAerodromeReferenceType)fixmFlight.getDeparture()
						.getDepartureAerodrome()).getCode().getValue();
			
			repositoryFlight.getDepartureInformation()
				.setAerodromeOfDeparture(new SimpleAerodrome(icaoAerodromeDesignator));
    		}
    		
    		if (fixmFlight.getDeparture().isSetOffBlockReadyTime()) {
    			
    			XMLGregorianCalendar gregorianCalendar 
    				= fixmFlight.getDeparture().getOffBlockReadyTime()
    					.getEstimated().getTime().getValue();
    			
    			Calendar calendar = gregorianCalendar.toGregorianCalendar();
    			repositoryFlight.getDepartureInformation()
    				.setEstimatedOffBlockTime(calendar);
    		}
    	}
    		
		// ---------------------------------------------------------------------
		// Flight Identification
		if (fixmFlight.isSetFlightIdentification()) {
			
			if (fixmFlight.getFlightIdentification()
					.isSetAircraftIdentification()) {
				
				repositoryFlight.getFlightIdentification()
					.setAircraftIdentification(fixmFlight
							.getFlightIdentification()
								.getAircraftIdentification().getValue());
			}
			
			if (fixmFlight.getFlightIdentification()
					.isSetMajorCarrierIdentifier()) {
				
				repositoryFlight.getFlightIdentification()
					.setMajorCarrierIdentifier(fixmFlight
							.getFlightIdentification()
								.getMajorCarrierIdentifier().getValue());
			}
			
		}
    		
		// ---------------------------------------------------------------------
		// Route
		if (fixmFlight.isSetRoute()) {
			
			if (fixmFlight.getRoute().isSetInitialFlightRules()) {
				
				FlightRulesType fixmFlightRule 
					= fixmFlight.getRoute().getInitialFlightRules();
				
				if (FlightRulesType.IFR.equals(fixmFlightRule)) {
					
					repositoryFlight.getRoute()
						.setInitialFlightRule(FlightRuleType
								.INSTRUMENT_FLIGHT_RULES);
				}
				else if (FlightRulesType.VFR.equals(fixmFlightRule)) {
					
					repositoryFlight.getRoute()
						.setInitialFlightRule(FlightRuleType
							.VISUAL_FLIGHT_RULES);
				}
			}
		}
    		
    	// ---------------------------------------------------------------------
    	// The transformed flight object 
    	return repositoryFlight;
    }
    
    protected final IFlightObject 
    mergeFlightObject(final FlightType fixmFlight, 
    				  final IFlightObject repositoryFlight) {
    	

    	// ---------------------------------------------------------------------
    	// Aircraft description
    	if(fixmFlight.isSetAircraftDescription()) {
    		
    		if(fixmFlight.getAircraftDescription().isSetAircraftAddress()) {
    			
    			repositoryFlight.getAircraft()
    				.setAddress(fixmFlight.getAircraftDescription()
    					.getAircraftAddress().getValue().getValue());
    		}

    		if(fixmFlight.getAircraftDescription().isSetAircraftQuantity()) {
    			
    			
    			repositoryFlight.getAircraft()
    				.setAircraftQuantity(fixmFlight.getAircraftDescription()
    						.getAircraftQuantity().getValue());
    		}

    		if (fixmFlight.getAircraftDescription().isSetAircraftType()) {
    		
    			repositoryFlight.getAircraft()
    				.setTypeOfAircraftDesignator(fixmFlight
    					.getAircraftDescription().getAircraftType()
    						.getIcaoModelIdentifier().getValue());
    		}
    		
    		if (fixmFlight.getAircraftDescription().isSetRegistration()) {
    			
    			repositoryFlight.getAircraft()
					.setRegistration(fixmFlight.getAircraftDescription()
						.getRegistration().getValue().getValue());
    		}

    		if (fixmFlight.getAircraftDescription().isSetWakeTurbulence()) {
    			
    			WakeTurbulenceCategoryType fixmWtc 
    				= fixmFlight.getAircraftDescription().getWakeTurbulence();
    			
    			WakeTurbulenceType repositoryWtc 
    				= WakeTurbulenceType
    					.determineCategory(fixmWtc.name().charAt(0));
    					
    			repositoryFlight.getAircraft()
    				.setWakeTurbulenceCategory(repositoryWtc);
    		}
    	}
    	
    	// ---------------------------------------------------------------------
    	// Arrival Information
    	if (fixmFlight.isSetArrival()) {
    		
    		if (fixmFlight.getArrival().isSetArrivalAerodrome()) {
    			
    			final String icaoAerodromeDesignator 
    				= ((IcaoAerodromeReferenceType)fixmFlight.getArrival()
    						.getArrivalAerodrome()).getCode().getValue();
    			
    			repositoryFlight.getArrivalInformation()
    				.setAerodromeOfDestination(new SimpleAerodrome(icaoAerodromeDesignator));
    		}
    	}
    	
    	// ---------------------------------------------------------------------
    	// Cleared Information
    	if (fixmFlight.isSetCleared()) {
    		
    		if (fixmFlight.getCleared().isSetFlightLevel()) {
    			
    			
    			AltitudeType altitude = fixmFlight.getCleared().getFlightLevel();
    			AltitudeReferenceType altRef = altitude.getRef();
    			// Currently ignore the UoM and assume only feet and not meters
    			if (AltitudeReferenceType.FLIGHT_LEVEL.equals(altRef)) {

        			QuantityFactory
        				.getFlightLevel(altitude.getValue().getValue()/100);
    			}
    			else {
    				
        			QuantityFactory
        				.getAltitudeLevel((int)(altitude.getValue().getValue()/100));
    			}
    		}
    	}
    	
    	// ---------------------------------------------------------------------
    	// Departure Information
    	if (fixmFlight.isSetDeparture()) {
    		
    		if (fixmFlight.getDeparture().isSetDepartureAerodrome()) {
    			
    			final String icaoAerodromeDesignator 
				= ((IcaoAerodromeReferenceType)fixmFlight.getDeparture()
						.getDepartureAerodrome()).getCode().getValue();
			
			repositoryFlight.getDepartureInformation()
				.setAerodromeOfDeparture(new SimpleAerodrome(icaoAerodromeDesignator));
    		}
    		
    		if (fixmFlight.getDeparture().isSetOffBlockReadyTime()) {
    			
    			XMLGregorianCalendar gregorianCalendar 
    				= fixmFlight.getDeparture().getOffBlockReadyTime()
    					.getEstimated().getTime().getValue();
    			
    			Calendar calendar = gregorianCalendar.toGregorianCalendar();
    			repositoryFlight.getDepartureInformation()
    				.setEstimatedOffBlockTime(calendar);
    		}
    	}
    		
		// ---------------------------------------------------------------------
		// Flight Identification
		if (fixmFlight.isSetFlightIdentification()) {
			
			if (fixmFlight.getFlightIdentification()
					.isSetAircraftIdentification()) {
				
				repositoryFlight.getFlightIdentification()
					.setAircraftIdentification(fixmFlight
							.getFlightIdentification()
								.getAircraftIdentification().getValue());
			}
			
			if (fixmFlight.getFlightIdentification()
					.isSetMajorCarrierIdentifier()) {
				
				repositoryFlight.getFlightIdentification()
					.setMajorCarrierIdentifier(fixmFlight
							.getFlightIdentification()
								.getMajorCarrierIdentifier().getValue());
			}
			
		}
    		
		// ---------------------------------------------------------------------
		// Route
		if (fixmFlight.isSetRoute()) {
			
			if (fixmFlight.getRoute().isSetInitialFlightRules()) {
				
				FlightRulesType fixmFlightRule 
					= fixmFlight.getRoute().getInitialFlightRules();
				
				if (FlightRulesType.IFR.equals(fixmFlightRule)) {
					
					repositoryFlight.getRoute()
						.setInitialFlightRule(FlightRuleType
								.INSTRUMENT_FLIGHT_RULES);
				}
				else if (FlightRulesType.VFR.equals(fixmFlightRule)) {
					
					repositoryFlight.getRoute()
						.setInitialFlightRule(FlightRuleType
							.VISUAL_FLIGHT_RULES);
				}
			}
		}

		// ---------------------------------------------------------------------
		// Type of Flight
    	ch.skyguide.flightplan.structure.FlightType typeOfFlight = null;
    	if(fixmFlight.isSetFlightType()) {
    		
    		if(fixmFlight.getFlightType().equals(TypeOfFlightType.NON_SCHEDULED)) {
    			
    			typeOfFlight = ch.skyguide.flightplan.structure
    					.FlightType.NONSCHEDULED;
    		}
    		else if(fixmFlight.getFlightType().equals(TypeOfFlightType.OTHER)) {
    			
    			typeOfFlight = ch.skyguide.flightplan.structure
    					.FlightType.UNKNOWN;
    		}
    		else {
    			
    			typeOfFlight = ch.skyguide.flightplan.structure
    					.FlightType.valueOf(fixmFlight.getFlightType().name());
    		}

    		repositoryFlight.setTypeOfFlight(typeOfFlight);
    	}
		
    	// ---------------------------------------------------------------------
    	// The merged flight object 
    	return repositoryFlight;
    }

    protected IFlightObjectRepository repository;

    public IFlightObjectRepository getRepository() {
        return repository;
    }

    public void setRepository(IFlightObjectRepository repository){
        this.repository = repository;
    }


    
}
