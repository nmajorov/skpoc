package ch.zuehlke.poc;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.log.LogEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;

import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class RouteTest extends CamelSpringTestSupport {
    @EndpointInject(uri = "log:customers")
    protected LogEndpoint resultEndpoint;

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/spring-context.xml");
    }

    @Test
    public void testCustomerFuse() throws Exception {
         template.sendBody("direct:in", "");

         assertTrue(resultEndpoint.isStarted());

    }
}

