package ch.zuehlke.poc;

import junit.framework.TestCase;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


public class RouteTest extends CamelSpringTestSupport {
	
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint result;

	@Produce
	protected ProducerTemplate template;

	@Override protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("/META-INF/spring/camel-context.xml");
	}

	@SuppressWarnings("restriction")
	static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "<root><poc>OK</poc></root>";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
	
	@SuppressWarnings("restriction")
	@Before
	@Override
	public void setUp() throws Exception {
		HttpServer server;

		server = HttpServer.create(new InetSocketAddress(8000), 0);

		server.createContext("/test", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();

		super.setUp();
	}

	@Test
	public void testCustomerFuse() throws Exception {

		RouteDefinition route = context.getRouteDefinition("from-netty-to-http");
		route.adviceWith(context, new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				interceptSendToEndpoint("netty-http:*").to("mock:foo");
			}
		});

		result.expectedMessageCount(1);
		
		String out = template.requestBody("start",
				"<root><poc>skyguide</poc></root>", String.class);

		assertEquals("<root><poc>OK</poc></root>", out);
	}
}
