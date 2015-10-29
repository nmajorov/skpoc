package ch.zuehlke.poc;

import junit.framework.TestCase;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/camel-context.xml")
public class RouteTest extends TestCase {
	
	 @EndpointInject(uri = "mock:result")
	 protected MockEndpoint result;

	 
	 @Produce(ref = "start")
	 protected ProducerTemplate template;
	 
	 
   

    @Test
    public void testCustomerFuse() throws Exception {
         
    	 template.sendBodyAndHeader( "<root><poc>skyguide</poc></root>","","");

    	 Exchange out = result.getExchanges().get(0);
    	 
       
    	 assertEquals("<root><poc>skyguide</poc></root>", out.getOut().getBody(String.class));

    }
}

