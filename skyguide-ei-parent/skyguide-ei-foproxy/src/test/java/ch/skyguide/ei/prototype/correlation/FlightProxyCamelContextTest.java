package ch.skyguide.ei.prototype.correlation;

import aero.fixm.model.base.IcaoAerodromeReferenceType;
import aero.fixm.model.flight.EngineTypeType;
import aero.fixm.model.flight.FlightType;
import ch.skyguide.ei.prototype.test.DataFlowTestSupport;
import ch.skyguide.ei.prototype.test.RestServiceHelper;
import ch.skyguide.fixm.extension.flight.enroute.CreateType;
import ch.skyguide.fixm.extension.flight.enroute.DeleteType;
import ch.skyguide.fixm.extension.flight.enroute.RequestType;
import ch.skyguide.fixm.extension.flight.enroute.RetrieveType;
import ch.skyguide.fixm.extension.flight.enroute.UpdateType;

import java.io.StringWriter;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class FlightProxyCamelContextTest extends DataFlowTestSupport {

    private NotifyBuilder notifier;
    static RestServiceHelper restServiceHelper  = new RestServiceHelper("http://localhost:8181/cxf/foservice");
    
    @BeforeClass
    public static void startRestServer() throws Exception {
        restServiceHelper.startServer();
    }

    @AfterClass
    public static void stopRestServer() throws Exception {
        restServiceHelper.stopServer();
    }

    @Test
    public void proxyFlightObjectCreateRequest() throws Exception {
        final String request = prepareRequest(new CreateType(), false);

        // now trigger the routing
        final Object reply = template.requestBody("netty:tcp://0.0.0.0:25003?sync=true", request);

        // wait long enough til the routing is completed
        assertTrue(notifier.matchesMockWaitTime());

        assertOnReply(reply, false);

        assertOnFoService(false, false);
    }

    @Test
    public void proxyFlightObjectUpdateRequest() throws Exception {
        createFlightObject();

        final String request = prepareRequest(new UpdateType(), true);

        // now trigger the routing
        final Object reply = template.requestBody("netty:tcp://0.0.0.0:25003?sync=true", request);

        // wait long enough til the routing is completed
        assertTrue(notifier.matchesMockWaitTime());

        assertOnReply(reply, true);

        assertOnFoService(true, false);
    }

    @Test
    public void proxyFlightObjectReadRequest() throws Exception {
        createFlightObject();

        final String request = prepareRequest(new RetrieveType(), false);

        // now trigger the routing
        final Object reply = template.requestBody("netty:tcp://0.0.0.0:25003?sync=true", request);

        // wait long enough til the routing is completed
        assertTrue(notifier.matchesMockWaitTime());

        assertOnReply(reply, false);

        assertOnFoService(false, false);
    }

    @Test
    public void proxyFlightObjectDeleteRequest() throws Exception {
        createFlightObject();

        final String request = prepareRequest(new DeleteType(), false);

        // now trigger the routing
        final Object reply = template.requestBody("netty:tcp://0.0.0.0:25003?sync=true", request);

        // wait long enough til the routing is completed
        assertTrue(notifier.matchesMockWaitTime());

        assertOnReply(reply, false);

        assertOnFoService(false, true);
    }

    private void assertOnReply(final Object reply, final boolean updated) {
        assertNotNull(reply);
        final String replyAsString = assertIsInstanceOf(String.class, reply);
        if (updated) {
            assertTrue(replyAsString.contains("<aircraftDescription aircraftColours=\"blue\" engineType=\"TURBO_FAN\" wakeTurbulence=\"L\"/>"));
        } else {
            assertTrue(replyAsString.contains("<aircraftDescription aircraftColours=\"red\" engineType=\"TURBO_JET\" wakeTurbulence=\"L\"/>"));
        }

        assertTrue(replyAsString.contains("<flightIdentification aircraftIdentification=\"SWR100\"/>"));
        assertTrue(replyAsString.contains("<departureAerodrome xsi:type=\"ns3:IcaoAerodromeReferenceType\" code=\"LSZH\"/>"));
        assertTrue(replyAsString.contains("<arrivalAerodrome xsi:type=\"ns3:IcaoAerodromeReferenceType\" code=\"GVA\"/>"));
    }

    private void assertOnFoService(final boolean updated, final boolean deleted) throws Exception {
        final WebClient client = restServiceHelper.createWebClient("/crud/id/SWR100/departure/LSZH/arrival/GVA");
        final Response response = client.get();
        assertNotNull(response);

        if (deleted) {
            assertEquals("", context().getTypeConverter().mandatoryConvertTo(String.class, response.getEntity()));
            return;
        }

        final FlightType flight = context().getTypeConverter().mandatoryConvertTo(FlightType.class, response.getEntity());
        assertNotNull(flight);

        if (updated) {
            assertEquals("blue", flight.getAircraftDescription().getAircraftColours().getValue());
            assertEquals(EngineTypeType.TURBO_FAN, flight.getAircraftDescription().getEngineType());
        } else {
            assertEquals("red", flight.getAircraftDescription().getAircraftColours().getValue());
            assertEquals(EngineTypeType.TURBO_JET, flight.getAircraftDescription().getEngineType());
        }
        assertEquals("SWR100", flight.getFlightIdentification().getAircraftIdentification().getValue());
        assertEquals("LSZH", ((IcaoAerodromeReferenceType) flight.getDeparture().getDepartureAerodrome()).getCode().getValue());
        assertEquals("GVA", ((IcaoAerodromeReferenceType) flight.getArrival().getArrivalAerodrome()).getCode().getValue());
    }

    private void createFlightObject() throws Exception {
        // create & upload the position we want to test with
        final WebClient client = restServiceHelper.createWebClient("/crud/id/SWR100/departure/LSZH/arrival/GVA");
        final FlightType flightAsObject = convert(FlightType.class, getClass().getClassLoader().getResource("flight2.xml"));
        client.post(flightAsObject, FlightType.class);
    }

    private String prepareRequest(final RequestType request, final boolean update) throws Exception {
        final FlightType flightAsObject = convert(FlightType.class, getClass().getClassLoader().getResource("flight2.xml"));
        if (update) {
            flightAsObject.getAircraftDescription().getAircraftColours().setValue("blue");
            flightAsObject.getAircraftDescription().setEngineType(EngineTypeType.TURBO_FAN);
        }

        request.getFlights().add(flightAsObject);
        final StringWriter writer = new StringWriter();
        JAXBContext.newInstance("ch.skyguide.fixm.extension.flight.enroute").createMarshaller().marshal(request, writer);

        return writer.getBuffer().toString();
    }

    @Override
    public void doPostSetup() throws Exception {
        super.doPostSetup();

        notifier = new NotifyBuilder(context()).whenDone(1).create();
    }

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/foproxy-camel-context.xml";
    }

}
