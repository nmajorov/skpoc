package ch.skyguide.ei.prototype.test;

import org.apache.activemq.broker.BrokerService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class DataFlowTestSupport extends CamelBlueprintTestSupport {

    private static BrokerService broker;

    @BeforeClass
    public static void startBroker() throws Exception {
        broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.setPersistent(false);
        broker.start();
    }

    @AfterClass
    public static void stopBroker() throws Exception {
        broker.stop();
    }

    protected MockEndpoint getInterceptingEndpoint() {
        return getMockEndpoint("mock:intercepting");
    }

    protected RouteBuilder createAdvisingRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // no advising logic per default
            }
        };
    }

    @Override
    public void doPostSetup() throws Exception {
        // we expect to have one single route per context
        assertEquals(1, context.getRouteDefinitions().size());

        final RouteBuilder routeBuilder = createAdvisingRouteBuilder();
        assertNotNull("routeBuilder", routeBuilder);
        context.getRouteDefinitions().get(0).adviceWith(context, routeBuilder);

        super.doPostSetup();
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    protected <T> T convert(final Class<T> type, final Object value) throws Exception {
        return context().getTypeConverter().mandatoryConvertTo(type, value);
    }

}
