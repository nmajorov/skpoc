package ch.skyguide.ei.prototype.inflow;

import org.apache.camel.builder.RouteBuilder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

import ch.skyguide.ei.prototype.test.DataFlowTestSupport;

public class UDPInFlowCamelContextTest extends DataFlowTestSupport {
	
    @Test
    public void testUDPDataInFlow() throws Exception {
        getInterceptingEndpoint().expectedMessageCount(1);

        final byte[] asterixMessage = convert(byte[].class, getClass().getResourceAsStream("/62_65_message.bin"));
        ChannelBuffer messageChanneled = ChannelBuffers.buffer(asterixMessage.length);
        
        messageChanneled.writeBytes(asterixMessage);

        template.sendBody("netty:udp://localhost:25001?decoder=#nettyDecoder&broadcast=true&sync=false&disconnectOnNoReply=false", messageChanneled);

        assertMockEndpointsSatisfied();

        final Object payload = getInterceptingEndpoint().getReceivedExchanges().get(0).getIn().getBody();
        assertNotNull("payload", payload);
        // TODO: rjw, add in correct test for the fixm message
//        final String fixmMessage = assertIsInstanceOf(String.class, payload);
//
//        assertNotNull("fixmMessage", fixmMessage);
//        assertTrue(fixmMessage.contains("http://www.fixm.aero/flight/2.0"));
//        assertTrue(fixmMessage.contains("http://www.fixm.aero/flight/2.0/skyguide"));
//        assertTrue(fixmMessage.contains("http://www.fixm.aero/foundation/2.0"));
    }

    @Override
    protected RouteBuilder createAdvisingRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("activemq:topic:skyguide.surveillance.data").to(getInterceptingEndpoint());
            }
        };
    }

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/inflow-camel-context.xml";
    }

}
