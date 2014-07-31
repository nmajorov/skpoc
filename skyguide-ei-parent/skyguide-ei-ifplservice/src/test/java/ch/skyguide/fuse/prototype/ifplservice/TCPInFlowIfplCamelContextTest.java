package ch.skyguide.fuse.prototype.ifplservice;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Assert;
import org.junit.Test;

import ch.skyguide.ei.prototype.test.DataFlowTestSupport;

public class TCPInFlowIfplCamelContextTest extends DataFlowTestSupport {

    @Test
    public void testTCPDataInFlow() throws Exception {
        getInterceptingEndpoint().expectedMessageCount(29);
        
        final InputStream inputStream 
        	= getClass().getResourceAsStream("/icao_ats_flow.txt");
        Assert.assertNotNull(inputStream);
        
        final byte[] ifplMessages = convert(byte[].class, inputStream);
       

        ChannelBuffer messageChanneled = ChannelBuffers.buffer(ifplMessages.length);
        
        messageChanneled.writeBytes(ifplMessages);
       
  
        template.sendBody("netty:tcp://localhost:12345?decoder=#IcaoAtsDecoder&sync=false&decoderMaxLineLength=8192&serverPipelineFactory=#IcaoAtsServerPipelineFactory", messageChanneled);
        
        assertMockEndpointsSatisfied(5, TimeUnit.SECONDS);

//        final Object payload = getInterceptingEndpoint().getReceivedExchanges().get(0).getIn().getBody();
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
                interceptSendToEndpoint("log:ch.skyguide.fuse.prototype.ifplservice?level=DEBUG").to(getInterceptingEndpoint());
            }
        };
    }

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/ifpl-camel-context.xml";
    }
    
}
