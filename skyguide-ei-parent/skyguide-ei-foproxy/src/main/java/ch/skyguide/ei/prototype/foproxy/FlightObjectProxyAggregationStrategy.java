package ch.skyguide.ei.prototype.foproxy;

import static org.apache.camel.util.ObjectHelper.notEmpty;

import aero.fixm.model.base.IcaoAerodromeReferenceType;

import aero.fixm.model.flight.FlightType;

import ch.skyguide.fixm.extension.flight.enroute.AcknowledgementType;
import ch.skyguide.fixm.extension.flight.enroute.CreateType;
import ch.skyguide.fixm.extension.flight.enroute.DeleteType;
import ch.skyguide.fixm.extension.flight.enroute.ErrorType;
import ch.skyguide.fixm.extension.flight.enroute.RequestType;
import ch.skyguide.fixm.extension.flight.enroute.ResponseType;
import ch.skyguide.fixm.extension.flight.enroute.RetrieveType;
import ch.skyguide.fixm.extension.flight.enroute.UpdateType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Headers;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.util.ObjectHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightObjectProxyAggregationStrategy implements AggregationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(FlightObjectProxyAggregationStrategy.class);
    private static final Map<Class<? extends RequestType>, String> TYPE_MAPPING = loadMappings();
    private static final String CLIENT_REQUEST_KEY = "FlightRequest";
    private static final String FLIGHT_ID_KEY = "FlightId";
    private static final String FLIGHT_DEPARTURE_AERODROME_KEY = "FlightDepartureAerodrome";
    private static final String FLIGHT_ARRIVAL_AERODROME_KEY = "FlightArrivalAerodrome";

    private static Map<Class<? extends RequestType>, String> loadMappings() {
        final Map<Class<? extends RequestType>, String> result = new HashMap<Class<? extends RequestType>, String>();
        result.put(CreateType.class, "POST");
        result.put(RetrieveType.class, "GET");
        result.put(UpdateType.class, "PUT");
        result.put(DeleteType.class, "DELETE");

        return result;
    }

    public void mapRequestToFoService(final Exchange exchange, @Headers final Map<String, Object> headers) throws Exception {
        final RequestType request = exchange.getIn().getBody(RequestType.class);
        final List<FlightType> flights = request.getFlights();
        final int size = flights.size();
        if (size != 1) {
            throw new IllegalStateException("The only currently supported number of flight objects is 1, is: " + size);
        }

        final FlightType flight = flights.get(0);
        final String id = flight.getFlightIdentification().getAircraftIdentification().getValue();
        final String departure = ((IcaoAerodromeReferenceType) flight.getDeparture().getDepartureAerodrome()).getCode().getValue();
        final String arrival = ((IcaoAerodromeReferenceType) flight.getArrival().getArrivalAerodrome()).getCode().getValue();

        headers.put(CLIENT_REQUEST_KEY, request);
        headers.put(FLIGHT_ID_KEY, notEmpty(id, FLIGHT_ID_KEY));
        headers.put(FLIGHT_DEPARTURE_AERODROME_KEY, notEmpty(departure, FLIGHT_DEPARTURE_AERODROME_KEY));
        headers.put(FLIGHT_ARRIVAL_AERODROME_KEY, notEmpty(arrival, FLIGHT_ARRIVAL_AERODROME_KEY));
        headers.put(Exchange.HTTP_METHOD, notEmpty(TYPE_MAPPING.get(request.getClass()), request.getClass().getName()));

        if (!isHTTPDeleteRequest(exchange)) {
            exchange.getIn().setBody(exchange.getContext().getTypeConverter().mandatoryConvertTo(String.class, flight));
        } else {
            // need to set the body to null because of http://bugs.java.com/view_bug.do?bug_id=7157360
            exchange.getIn().setBody(null);
        }
    }

    @Override
    public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
        Response response;
        try {
            response = newExchange.getIn().getMandatoryBody(Response.class);
        } catch (final Exception exception) {
            return prepareClientReply(newExchange, exception);
        }

        boolean failed = false;
        final Object entity = response.getEntity();
        LOG.info("The returned message entity from the foservice is {}", entity);
        if (entity != null) {
            // it could be an empty string if the flight doesn't exist
            final String string = newExchange.getContext().getTypeConverter().convertTo(String.class, entity);
            if (ObjectHelper.isEmpty(string) && !isHTTPDeleteRequest(newExchange)) {
                failed = true;
            }
        } else {
            failed = true;
        }

        return prepareClientReply(newExchange, failed ? new Exception("Unexpected failure") : null);
    }

    private static Exchange prepareClientReply(final Exchange exchange, final Exception exception) {
        final RequestType originalRequest = exchange.getIn().getHeader(CLIENT_REQUEST_KEY, RequestType.class);
        final ResponseType clientReply = createResponse(exception);
        clientReply.getOriginalRequests().add(originalRequest);

        final String replyAsString = exchange.getContext().getTypeConverter().convertTo(String.class, clientReply);
        exchange.getIn().setBody(replyAsString);

        return exchange;

    }

    private static ResponseType createResponse(final Exception exception) {
        if (exception != null) {
            final ErrorType error = new ErrorType();
            error.setErrorText(exception.getMessage());
            return error;
        }

        return new AcknowledgementType();
    }

    private static boolean isHTTPDeleteRequest(final Exchange exchange) {
        return "DELETE".equals(exchange.getIn().getHeader(Exchange.HTTP_METHOD));
    }

}
