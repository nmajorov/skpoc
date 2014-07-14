package ch.skyguide.ei.prototype.correlation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import ch.skyguide.ei.prototype.test.DataFlowTestSupport;
import ch.skyguide.ei.prototype.test.RestServiceHelper;
import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixMessage;
import ch.skyguide.fixm.extension.flight.enroute.SkyguideAircraftPosition;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class CorrelationCamelContextTest extends DataFlowTestSupport {

    static RestServiceHelper restServiceHelper;
    private static final String URL = "http://localhost:8181/cxf/foservice/crudone";
    
    
    @BeforeClass
    public static void startRestServer() throws Exception {
        restServiceHelper= new RestServiceHelper(URL);
        restServiceHelper.startServer();
    }

    @AfterClass
    public static void stopRestServer() throws Exception {
        restServiceHelper.stopServer();
    }

    
    @Test
    public void enrichNoFlightObjectsfounds() throws Exception{
        // validate input
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream("fo.xml")));

        StringBuilder xmlString = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            xmlString.append(inputLine);

        in.close();

        template.sendBody("activemq:topic:skyguide.surveillance.data",xmlString);
    }
    
    //@Test
    public void enrichAircraftPositionFoundOnRSService() throws Exception {
        // first create & upload the position we want to test with
        final WebClient client = restServiceHelper.createWebClient("/crud/position/123");
        final String aircraftPosition = convert(String.class, getClass().getClassLoader().getResource("position.xml"));
        client.post(aircraftPosition, SkyguideAircraftPosition.class);

        // now trigger the routing
        final String aircraftPosition2 = convert(String.class, getClass().getClassLoader().getResource("position2.xml"));
        template.sendBody("activemq:topic:skyguide.surveillance.data", aircraftPosition2);

        // wait a bit until the routing is completed
        Thread.sleep(3000);

        // read back the same position again which should now have the track number "3333.0  + 1111.0"
        final SkyguideAircraftPosition updatedAircraftPosition = client.get(SkyguideAircraftPosition.class);
        assertNotNull("updatedAircraftPosition", updatedAircraftPosition);
        assertEquals(new Double(4444.0), new Double(updatedAircraftPosition.getTrack().getValue().getValue().getValue()));
    }

    //@Test
    public void enrichAircraftPositionNotFoundOnRSService() throws Exception {
        // first make sure the position doesn't preexist at all
        WebClient client = restServiceHelper.createWebClient("/crud/position/123");
        client.delete();

        // now trigger the routing
        final String aircraftPosition2 = convert(String.class, getClass().getClassLoader().getResource("position2.xml"));
        template.sendBody("activemq:topic:skyguide.radar.data", aircraftPosition2);

        // wait a bit until the routing is completed
        Thread.sleep(3000);

        // read the position now which should have the track number "1111.0"
        client = restServiceHelper.createWebClient("/crud/position/123");
        final SkyguideAircraftPosition updatedAircraftPosition = client.get(SkyguideAircraftPosition.class);
        assertNotNull("updatedAircraftPosition", updatedAircraftPosition);
        assertEquals(new Double(1111.0), new Double(updatedAircraftPosition.getTrack().getValue().getValue().getValue()));
    }

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/correlation-camel-context.xml";
    }

}
