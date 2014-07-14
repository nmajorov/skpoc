// ------------------------------------------------------------------------------
// PROJECT: Skyguide ei-prototype
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------

package ch.skyguide.ei.prototype.netty.handler.codec.ifpl;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aero.fixm.model.base.CountType;
import aero.fixm.model.base.FlightIdentifierType;
import aero.fixm.model.base.FlightRulesType;
import aero.fixm.model.base.FreeTextType;
import aero.fixm.model.base.GloballyUniqueFlightIdentifierType;
import aero.fixm.model.base.IcaoAerodromeReferenceType;
import aero.fixm.model.base.IcaoAircraftIdentifierType;
import aero.fixm.model.base.MultiTimeType;
import aero.fixm.model.base.ReportedTime;
import aero.fixm.model.flight.AircraftAddressType;
import aero.fixm.model.flight.AircraftCapabilitiesElement;
import aero.fixm.model.flight.AircraftRegistrationType;
import aero.fixm.model.flight.AircraftType;
import aero.fixm.model.flight.AircraftTypeElement;
import aero.fixm.model.flight.ClearedFlightInformationElement;
import aero.fixm.model.flight.CommunicationCapabilitiesElement;
import aero.fixm.model.flight.FlightArrivalType;
import aero.fixm.model.flight.FlightDepartureType;
import aero.fixm.model.flight.FlightIdentificationElement;
import aero.fixm.model.flight.FlightType;
import aero.fixm.model.flight.NavigationCapabilitiesElement;
import aero.fixm.model.flight.NavigationCodeType;
import aero.fixm.model.flight.RouteType;
import aero.fixm.model.flight.SurveillanceCapabilitiesElement;
import aero.fixm.model.flight.SurveillanceCodeType;
import aero.fixm.model.flight.TypeOfFlightType;
import aero.fixm.model.flight.WakeTurbulenceCategoryType;
import aero.fixm.model.foundation.AerodromeIcaoCodeType;
import aero.fixm.model.foundation.AltitudeMeasureType;
import aero.fixm.model.foundation.AltitudeReferenceType;
import aero.fixm.model.foundation.AltitudeType;
import aero.fixm.model.foundation.TimeType;
import aero.fixm.model.foundation.UnitOfMeasureType;
import ch.skyguide.environment.aeronautical.navigation.FlightRuleType;
import ch.skyguide.environment.aeronautical.navigation.WakeTurbulenceType;
import ch.skyguide.exception.CodingException;
import ch.skyguide.exception.MessageException;
import ch.skyguide.exception.NonFatalMessageException;
import ch.skyguide.flightplan.aftn.formats.FlightPlanMessage;
import ch.skyguide.flightplan.aftn.formats.IAeronauticalMessage;
import ch.skyguide.flightplan.aftn.formats.icao.IIcaoMessageCodec;
import ch.skyguide.flightplan.aftn.formats.icao.IcaoMessageCodecFactory;
import ch.skyguide.flightplan.aftn.formats.icao.field.FieldType18Designator;
import ch.skyguide.flightplan.aftn.formats.icao.field.IcaoOtherInformationItem;
import ch.skyguide.flightplan.aftn.formats.icao.field.IcaoOtherInformationTextItem;
import ch.skyguide.flightplan.structure.equipment.NavigationCommunicationEquipmentType;
import ch.skyguide.util.contract.ConditionUtils;

/**
 * Encodes the requested of IFP service (a text-based line protocol in a TCP/IP
 * socket) into a {@link ChannelBuffer}. It's a variant of
 * {@link org.jboss.netty.handler.codec.string.StringEncoder}
 * 
 * @see org.jboss.netty.handler.codec.string.StringEncoder
 * 
 */
@Sharable
public class IcaoAtsDecoder extends OneToOneDecoder {

    private static final Logger LOG = LoggerFactory
            .getLogger(IcaoAtsDecoder.class);
    
    
    private Charset charset = Charset.defaultCharset();

	
	@Override
	protected Object 
	decode(ChannelHandlerContext ctx, Channel channel, Object msg) 
	throws Exception {
		
		// --------------------------------------------------------------------
		// Check pre-requisites
	    if (!(msg instanceof ChannelBuffer)) {
            return msg;
        }
	    
		//----------------------------------------------------------------------
		// Extract the message.
		final ChannelBuffer channelBuffer = (ChannelBuffer) msg;
		
		LOG.info("Raw icao ats message: " + channel.toString());
		
		final CharBuffer messageBuffer 
			= extractMessageFromChannel(channelBuffer);

		//----------------------------------------------------------------------
		// Return converted message.
		return convertMessage(messageBuffer);
	}
	
	private final CharBuffer 
	extractMessageFromChannel(final ChannelBuffer inChannel) 
	throws MessageException{

		final ByteBuffer byteBuffer = ByteBuffer.allocate(1);
		final CharBuffer messageBuffer 
			= CharBuffer.allocate(MAX_MESSAGE_LENGTH);
		
		boolean startFound = false;
		int maxGarbageLengthBetweenMessages 
			= MAX_GARBAGE_LENGTH_BETWEEN_MESSAGES;
		
		while(inChannel.readable()) {
		    
			if(maxGarbageLengthBetweenMessages < 0) {
				
				throw new MessageException("Exceed maximum garbage length "
						+ "between messages: " 
						+ MAX_GARBAGE_LENGTH_BETWEEN_MESSAGES);
			}

			inChannel.readBytes(byteBuffer);
			byteBuffer.flip();
			
			final CharBuffer charBuffer = charset.decode(byteBuffer);
			byteBuffer.flip();
			final char character = charBuffer.get();
			charBuffer.flip();
			
			if(!startFound && START_CHARACTER == character) {
				
				// Discard start character.
				startFound = true;
			}
			else if(startFound && END_CHARACTER != character){
				
				// message character stored in message buffer
				messageBuffer.put(character);	
			}
			else if(startFound && END_CHARACTER == character) {
				
				// Start looking for a new message
				startFound = false;
				break;
			}
			else {
			
				// Discard all other characters (garbage).
				maxGarbageLengthBetweenMessages--;
			}
		}
		
		// Assume a message is found.
		messageBuffer.flip();
		return messageBuffer;
	}
    
	
	/**
	 * Parses the {@code ByteBuffer} and decodes the 
	 * {@code IAeronauticalMessage}.
	 * 
	 * @param messageBuffer
	 * 		The {@code ByteBuffer} containing the message to be decoded.
	 * 
	 * @return	A decoded {@code IAeronauticalMessage}.
	 * 
	 * @throws MessageException
	 * 		Thrown when the message can not be parsed.
	 */
    private final IAeronauticalMessage 
    parseAeronauticalMessage(final ByteBuffer messageBuffer) 
    throws MessageException {
    	
        try{
            //
            // Decode message and throw MessageException if it fails,
            // enriched with CodingException.
            //
            IAeronauticalMessage decodedMessage = null;
            decodedMessage
            = m_icaoMessageCodec.decode(messageBuffer);

            //------------------------------------------------------------------
            // Check postcondition
            ConditionUtils.preconditionNotNull(decodedMessage, "decodedMessage");

            return decodedMessage;
            
        }catch(CodingException e){
            
        	//
            // Throw non-fatal message exception because
            // fatal message exception would close exception
            // which is not required
            //
            throw new NonFatalMessageException("Failed to decode message='"
                    + new String(messageBuffer.array()) + "'.",e);
        }
    }
    
    /**
     * Converts to a Fixm Flight Object
     * 
     * @param messageBuffer
     * 		The {@code CharBuffer} to be converted to a FIXM Flight Object.
     * 
     * @return	The converted {@code FlightType} as Flight Object.
     * 
     * @throws MessageException
     * 		Thrown when the message can not be parsed.
     */
    private final FlightType convertMessage(final CharBuffer messageBuffer) 
    throws MessageException {
    	
        final ByteBuffer byteBuffer = charset.encode(messageBuffer);
    	
    	
		//----------------------------------------------------------------------
		// Parse the message
		final IAeronauticalMessage aeronauticalMessage 
			= parseAeronauticalMessage(byteBuffer);
		
		if(aeronauticalMessage instanceof FlightPlanMessage) {

			// -----------------------------------------------------------------
			// Create a fixm flight object out of the flight plan message.
			final FlightType fixmFlightObject 
				= createFixmFlightObject((FlightPlanMessage)aeronauticalMessage);
			
			return fixmFlightObject;
		}
		
		//---------------------------------------------------------------------
		// Return null when message is to be discarded, contract from decode.
		return null;
    }
    
    private final FlightType 
    createFixmFlightObject(final FlightPlanMessage flightPlanMessage) {
    	
    	final FlightType flightObject = new FlightType();
    	
    	
    	//----------------------------------------------------------------------
    	// Fill in from the Aircraft
    	flightObject.setAircraftDescription(new AircraftType());

    	flightObject.getAircraftDescription()
    		.setAircraftType(new AircraftTypeElement());
    	flightObject.getAircraftDescription().getAircraftType()
    		.setIcaoModelIdentifier(new IcaoAircraftIdentifierType());
    	flightObject.getAircraftDescription().getAircraftType()
    		.getIcaoModelIdentifier()
    			.setValue(flightPlanMessage.getTypeOfAircraft());

    	List<IcaoOtherInformationItem> icaoOtherItems = null;
    	if((icaoOtherItems = flightPlanMessage.getOtherInformation()
    			.get(FieldType18Designator.CODE)) != null) {
	    	
    		// TODO: rjw, what if multiple address codes.
    		flightObject.getAircraftDescription()
    			.setAircraftAddress(new AircraftAddressType());
    		flightObject.getAircraftDescription()
    			.getAircraftAddress().setValue(new FreeTextType());
	    	flightObject.getAircraftDescription()
	    		.getAircraftAddress().getValue()
	    			.setValue((((IcaoOtherInformationTextItem)icaoOtherItems
	    					.get(0)).getText()));
    	}
    	
    	flightObject.getAircraftDescription()
    		.setCapabilities(new AircraftCapabilitiesElement());
    	
    	flightObject.getAircraftDescription().getCapabilities()
    		.setCommunication(new CommunicationCapabilitiesElement());
    	// TODO: communication capabilities.
    	
    	flightObject.getAircraftDescription().getCapabilities()
    		.setNavigation(new NavigationCapabilitiesElement());
    	List<NavigationCodeType> fixmNavigationCodes
			= flightObject.getAircraftDescription().getCapabilities()
				.getNavigation().getNavigationCode();

    	// Only require RVSM, therefore not complete.
    	if (flightPlanMessage.getEquipment()
    			.getNavigationCommunicationEquipment()
    				.contains(NavigationCommunicationEquipmentType.RVSM)) {
    		
    		fixmNavigationCodes.add(NavigationCodeType.W);
    	}
    	
    	flightObject.getAircraftDescription().getCapabilities()
    		.setSurveillance(new SurveillanceCapabilitiesElement());
    	
    	// Only require the Transponder, therefore not complete
    	List<SurveillanceCodeType> surveillanceCodes 
    		= flightObject.getAircraftDescription().getCapabilities()
    			.getSurveillance().getSurveillanceCode();
    	
    	surveillanceCodes.add(SurveillanceCodeType
    		.valueOf(String.valueOf(flightPlanMessage.getEquipment()
    				.getMainSecondarySurveillanceEquipment()
    					.getCharacterRepresentation())));
    	
    	flightObject.getAircraftDescription()
    		.setAircraftQuantity(new CountType());
    	flightObject.getAircraftDescription().getAircraftQuantity()
    		.setValue(flightPlanMessage.getNumberOfAircraft());
    	
    	icaoOtherItems = null;
    	if((icaoOtherItems = flightPlanMessage.getOtherInformation()
    			.get(FieldType18Designator.REG)) != null) {
	    	
    		// TODO: rjw, what if multiple registrations.
    		flightObject.getAircraftDescription()
    			.setRegistration(new AircraftRegistrationType());
    		flightObject.getAircraftDescription().getRegistration()
    			.setValue(new FreeTextType());
	    	flightObject.getAircraftDescription().getRegistration()
	    		.getValue()
	    			.setValue((((IcaoOtherInformationTextItem)icaoOtherItems
	    				.get(0)).getText()));
    	}
    	
    	if(!WakeTurbulenceType.SMALL.equals(flightPlanMessage.getWakeTurbulenceCategory())) {
    		
        	flightObject.getAircraftDescription()
    		.setWakeTurbulence(WakeTurbulenceCategoryType
    				.valueOf(String.valueOf(flightPlanMessage
    						.getWakeTurbulenceCategory()
    							.getCharacterRepresentation())));
    	}
    	else {

    		// SMALL type is Swiss specific, long history.
    		flightObject.getAircraftDescription()
	    		.setWakeTurbulence(WakeTurbulenceCategoryType.L);
    	}
    	
    	// ---------------------------------------------------------------------
    	// Fill in Arrival Information
    	flightObject.setArrival(new FlightArrivalType());
    	IcaoAerodromeReferenceType icaoAerodrome 
    		= new IcaoAerodromeReferenceType();
    	icaoAerodrome.setCode(new AerodromeIcaoCodeType());
    	icaoAerodrome.getCode().setValue(flightPlanMessage
    			.getAerodromeOfDestination());
    	flightObject.getArrival()
    		.setArrivalAerodrome(icaoAerodrome);
    	
    	// ---------------------------------------------------------------------
    	// Fill-in cleared information
    	// Utilize the RFL as the initial Cleared Flight Level (CFL)
    	flightObject.setCleared(new ClearedFlightInformationElement());
    	flightObject.getCleared().setFlightLevel(new AltitudeType());
    	flightObject.getCleared().getFlightLevel()
    		.setRef(AltitudeReferenceType.FLIGHT_LEVEL);
    	flightObject.getCleared().getFlightLevel().setUom(AltitudeMeasureType.FEET);
    	flightObject.getCleared().getFlightLevel().setValue(new UnitOfMeasureType());
    	flightObject.getCleared().getFlightLevel().getValue()
    		.setValue(flightPlanMessage.getIcaoFlightRoute()
    				.getInitialRequestedLevel().getValue().doubleValue());
    	
       	// ---------------------------------------------------------------------
    	// Fill in Departure Information
    	flightObject.setDeparture(new FlightDepartureType());
    	icaoAerodrome = new IcaoAerodromeReferenceType();
    	icaoAerodrome.setCode(new AerodromeIcaoCodeType());
    	icaoAerodrome.getCode().setValue(flightPlanMessage.getAerodromeOfDeparture());
    	flightObject.getDeparture().setDepartureAerodrome(icaoAerodrome);
		try {
	    	
	    	if (flightPlanMessage.getEstimatedOffBlockTime() != null) {

		    	flightObject.getDeparture()
		    		.setOffBlockReadyTime(new MultiTimeType());
		    	flightObject.getDeparture().getOffBlockReadyTime()
		    		.setEstimated(new ReportedTime());
		    	flightObject.getDeparture().getOffBlockReadyTime().getEstimated()
		    		.setTime(new TimeType());

		    	XMLGregorianCalendar eobt
				 = DatatypeFactory.newInstance()
	    			.newXMLGregorianCalendar((GregorianCalendar)flightPlanMessage
	    					.getEstimatedOffBlockTime());
		    	flightObject.getDeparture().getOffBlockReadyTime().getEstimated()
		    		.getTime().setValue(eobt);
	    	}
		
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}

    	//----------------------------------------------------------------------
    	// Fill in from the Flight Identification
    	String aircraftIdentification 
    		= flightPlanMessage.getAircraftIdentification();
    	
    	FlightIdentifierType flightIdentifierType = new FlightIdentifierType();
    	flightIdentifierType.setValue(aircraftIdentification);
    	
    	FlightIdentificationElement flightIdentificationElement 
    		= new FlightIdentificationElement();
    	flightIdentificationElement
    		.setAircraftIdentification(flightIdentifierType);
    	flightObject.setFlightIdentification(flightIdentificationElement);
    	
    	// ---------------------------------------------------------------------
    	// Fill in the Globally Unique Identifier
    	final StringBuilder builder = new StringBuilder();
    	builder.append(flightPlanMessage.getAircraftIdentification()).append('-');
    	builder.append(flightPlanMessage.getAerodromeOfDeparture()).append('-');
    	builder.append(flightPlanMessage.getAerodromeOfDestination()).append('-');
    	
    	if(flightPlanMessage.getEstimatedOffBlockTime() != null) {
    	
    		builder.append(flightPlanMessage
    				.getEstimatedOffBlockTime().toString()).append('-');
    	}
    	else {
    		
    		builder.append("AIRBORNE").append('-');
    	}
    	
    	builder.append(Integer.valueOf((Double.valueOf(Math.random()*1000000)
    		.intValue())).toString());
    	
    	flightObject.setGufi(new GloballyUniqueFlightIdentifierType());
    	flightObject.getGufi().setValue(builder.toString());
    	
    	// ---------------------------------------------------------------------
    	// Fill in route
    	flightObject.setRoute(new RouteType());
    	// Should parse through the route and depending upon the flight rule
    	// of the segment through the Area of Responsibility.
    	if(FlightRuleType.INSTRUMENT_FLIGHT_RULES
    			.equals(flightPlanMessage.getFlightRule()) 
    			|| FlightRuleType.LEAVING
    				.equals(flightPlanMessage.getFlightRule())) {
    		
    		flightObject.getRoute().setInitialFlightRules(FlightRulesType.IFR);
    	}
    	else if(FlightRuleType.VISUAL_FLIGHT_RULES
    				.equals(flightPlanMessage.getFlightRule()) 
        			|| FlightRuleType.CONTROLLED_VISUAL_FLIGHT_RULES
        				.equals(flightPlanMessage.getFlightRule())
        			|| FlightRuleType.JOINING
        				.equals(flightPlanMessage.getFlightRule())) {
    	
    		flightObject.getRoute().setInitialFlightRules(FlightRulesType.VFR);
    	}
    	else {
  
    		// Take most common and highest category.
    		flightObject.getRoute().setInitialFlightRules(FlightRulesType.IFR);
    	}
    	
    	// ---------------------------------------------------------------------
    	// Fill in Type of Flight
    	if(ch.skyguide.flightplan.structure.FlightType.GENERAL
    			.equals(flightPlanMessage.getTypeOfFlight())) {

        	flightObject.setFlightType(TypeOfFlightType.GENERAL);
    	}
    	else if(ch.skyguide.flightplan.structure.FlightType.MILITARY
    			.equals(flightPlanMessage.getTypeOfFlight())) {

        	flightObject.setFlightType(TypeOfFlightType.MILITARY);
    	}
    	else if(ch.skyguide.flightplan.structure.FlightType.NONSCHEDULED
    			.equals(flightPlanMessage.getTypeOfFlight())) {
    		
        	flightObject.setFlightType(TypeOfFlightType.NON_SCHEDULED);
    	}
    	else if(ch.skyguide.flightplan.structure.FlightType.SCHEDULED
    			.equals(flightPlanMessage.getTypeOfFlight())) {
    		
        	flightObject.setFlightType(TypeOfFlightType.SCHEDULED);
    	}
    	else if(ch.skyguide.flightplan.structure.FlightType.UNKNOWN
    			.equals(flightPlanMessage.getTypeOfFlight())) {
    		
        	flightObject.setFlightType(TypeOfFlightType.OTHER);
    	}
    	else {
    		
        	flightObject.setFlightType(TypeOfFlightType.OTHER);
    	}
    	
    	// ---------------------------------------------------------------------
    	// Finished converting a minimum Flight Object.
    	return flightObject;
    }

    /**
     * ICAO message codec.
     */
    private final IIcaoMessageCodec<IAeronauticalMessage> m_icaoMessageCodec = 
            IcaoMessageCodecFactory.instance().createIcaoMessageCodec();

    /**
     * The open bracket indicating the start of an ICAO message.
     */
    private static final char START_CHARACTER = '(';

    /**
     * The closing bracket indicating the end of an ICAO message.
     */
    private static final char END_CHARACTER = ')';

    /**
     * The maximum number of bytes to read before the start pattern is detected
     * in the input stream (excluding the length of the start pattern),
     * which is identical to the length of garbage data for a "tolerant 
     * application" to accept between messages.<br>
     * Maximum number of bytes to be extracted from the MessageExtractor before
     * it can be assumed that there is a problem with the input stream.
     */
    private static final int MAX_GARBAGE_LENGTH_BETWEEN_MESSAGES = 2048;

    /**
     * The maximum number of bytes to read after the start pattern before the
     * end pattern is detected in the input stream (excluding the length of the
     * end pattern), which is identical to the maximum possible length of a
     * message without the patterns).<br>
     * The start and end patterns are part of the message.
     */
    private static final int MAX_MESSAGE_LENGTH = 8192;
    
}
