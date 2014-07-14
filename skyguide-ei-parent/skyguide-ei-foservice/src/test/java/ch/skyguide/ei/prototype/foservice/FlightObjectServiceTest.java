package ch.skyguide.ei.prototype.foservice;

import aero.fixm.model.flight.FlightType;

import ch.skyguide.ei.prototype.test.RestServiceHelper;

import javax.ws.rs.core.Response;

import org.apache.camel.test.junit4.CamelTestSupport;

import org.apache.cxf.jaxrs.client.WebClient;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FlightObjectServiceTest extends CamelTestSupport {

    static RestServiceHelper restServiceHelper  = new RestServiceHelper("http://localhost:8181/cxf/foservice");
    @BeforeClass
    public static void startRsServer() throws Exception {
        restServiceHelper.startServer();
    }

    @AfterClass
    public static void stopRsServer() throws Exception {
        restServiceHelper.stopServer();
    }

    //@Test TODO enable test and put test xml there
    public void testCRUD() throws Exception {
        // read a flight object with the id 123 from zurich to geneva which should resolve to null
        final WebClient client = restServiceHelper.createWebClient("/crud/arcid/SWR100/departure/LSZH/arrival/GVA");
        Response response = client.get();
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals("", context().getTypeConverter().mandatoryConvertTo(String.class, response.getEntity()));

        // load the flight object we want to test with
        final String jaxbXML = context().getTypeConverter().mandatoryConvertTo(String.class, getClass().getResourceAsStream("/flight.xml"));

        // create the flight object
        FlightType flight = client.post(jaxbXML, FlightType.class);
        assertNotNull("flight", flight);

        // read back the same flight object again which should not resolve to null anymore
        flight = client.get(FlightType.class);
        assertNotNull("flight", flight);
        assertEquals("red", flight.getAircraftDescription().getAircraftColours().getValue());

        // update the flight object
        flight.getAircraftDescription().getAircraftColours().setValue("blue");
        final String jaxbXMLUpdated = context().getTypeConverter().mandatoryConvertTo(String.class, flight);
        response = client.put(jaxbXMLUpdated);
        assertNotNull("response", response);
        assertEquals(200, response.getStatus());

        // read back the same flight object again which should now reflect the updated attribute (red => blue)
        flight = client.get(FlightType.class);
        assertNotNull("flight", flight);
        assertEquals("blue", flight.getAircraftDescription().getAircraftColours().getValue());

        // delete the flight object
        response = client.delete();
        assertNotNull("response", response);
        assertEquals(200, response.getStatus());

        // read back the same flight object again which should now resolve to null again as at the beginning of this test
        response = client.get();
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals("", context().getTypeConverter().mandatoryConvertTo(String.class, response.getEntity()));

        // delete the same flight object again which should now resolve to "Not Modified"
        response = client.delete();
        assertNotNull("response", response);
        assertEquals(304, response.getStatus());
    }

    @Test
    public void testWADL() throws Exception {
        final WebClient client = restServiceHelper.createWebClient("?_wadl");
        final Response response = client.get();
        assertNotNull("response", response);
        assertEquals(200, response.getStatus());

        // let's see how the wadl of our rest based service looks like
        final String wadl = context.getTypeConverter().mandatoryConvertTo(String.class, response.getEntity());
        log.info("The WADL contract is: {}{}", LS, wadl);

        assertTrue(wadl.contains("<resource path=\"/crud/arcid/{arcid}/departure/{departure}/arrival/{arrival}\">"));
        assertTrue(wadl.contains("<method name=\"GET\">"));
        assertTrue(wadl.contains("<method name=\"POST\">"));
        assertTrue(wadl.contains("<method name=\"PUT\">"));
        assertTrue(wadl.contains("<method name=\"DELETE\">"));
    }

}
