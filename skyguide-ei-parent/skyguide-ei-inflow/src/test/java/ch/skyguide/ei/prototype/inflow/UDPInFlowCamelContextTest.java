package ch.skyguide.ei.prototype.inflow;

import ch.skyguide.ei.prototype.test.DataFlowTestSupport;

import org.apache.camel.builder.RouteBuilder;

import org.junit.Test;

public class UDPInFlowCamelContextTest extends DataFlowTestSupport {

   // @Test
    public void testUDPDataInFlow() throws Exception {
        getInterceptingEndpoint().expectedMessageCount(1);

        final byte[] asterixMessage = convert(byte[].class, getClass().getResourceAsStream("/62_65_message.bin"));
        template.sendBody("netty:udp://localhost:25001?broadcast=true&sync=false", asterixMessage);

        assertMockEndpointsSatisfied();

        final Object payload = getInterceptingEndpoint().getReceivedExchanges().get(0).getIn().getBody();
        final String fixmMessage = assertIsInstanceOf(String.class, payload);

        assertNotNull("fixmMessage", fixmMessage);
        assertTrue(fixmMessage.contains("http://www.fixm.aero/flight/2.0"));
        assertTrue(fixmMessage.contains("http://www.fixm.aero/flight/2.0/skyguide"));
        assertTrue(fixmMessage.contains("http://www.fixm.aero/foundation/2.0"));
    }

    @Override
    protected RouteBuilder createAdvisingRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("activemq:topic:skyguide.radar.data").to(getInterceptingEndpoint());
            }
        };
    }

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/inflow-camel-context.xml";
    }

}
