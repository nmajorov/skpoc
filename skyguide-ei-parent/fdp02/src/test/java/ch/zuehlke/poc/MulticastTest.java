package ch.zuehlke.poc;

import junit.framework.TestCase;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/camel-context.xml")
public class MulticastTest extends TestCase {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint result;

    @Produce
    protected ProducerTemplate template;

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
        String out = template.requestBody("start",
                "<root><poc>skyguide</poc></root>", String.class);

        assertEquals("<root><poc>OK</poc></root>", out);
    }
}
