package ch.skyguide.ei.prototype.test;

import java.io.File;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.skyguide.ei.prototype.foservice.AbstractFlightObjectService;
import ch.skyguide.ei.prototype.foservice.FlightObjectService;
import ch.skyguide.ei.prototype.foservice.forepository.FlightObjectRepository;

/**
 * helper class to test restful endpoints
 * 
 * scenario url is:
 * <code>
 *        http://localhost:8181/cxf/foservice
 * </code> 
 * @author majoronj
 *
 */
public class RestServiceHelper {
    private static final Logger LOG = LoggerFactory.getLogger(RestServiceHelper.class);
    private static Server server;
    private String url;
    private AbstractFlightObjectService flightObjectService;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RestServiceHelper(String url) {
        this.url = url;
    }

    public  void startServer() throws Exception {
        if (this.url == null){
            throw new RuntimeException("endpoit url for test is not set");
        }

        final JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
        LOG.info("Starting restful service crud on URL: " + this.url );
  
        final String dbPath = "data/testDb.d4o";
        removeTestDatabase(dbPath);
        flightObjectService = new FlightObjectService();
        flightObjectService.setRepository(new FlightObjectRepository(dbPath));
        jaxrsServerFactoryBean.setResourceClasses(FlightObjectService.class);
        jaxrsServerFactoryBean.setResourceProvider(FlightObjectService.class, new SingletonResourceProvider(flightObjectService));
        jaxrsServerFactoryBean.setAddress(url);
        server = jaxrsServerFactoryBean.create();
    }

    public  void stopServer() throws Exception {
    	
    	flightObjectService.getRepository().shutdown();
        server.stop();
        server.destroy();
    }

    public  WebClient createWebClient(final String path) {
        return WebClient.create(this.url + path);
    }
    
    private final void removeTestDatabase(String path) {
    	
    	File file = new File(path);
    	
    	if(file.exists()) {
    		
    		file.delete();
    	}
    }

}
