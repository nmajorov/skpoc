package ch.skyguide.ei.prototype.test;

import ch.skyguide.ei.prototype.foservice.FlightObjectService;
import ch.skyguide.ei.prototype.foservice.FlightObjectServiceScenarioOne;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * helper class to test restful endpoints
 * Scenario one url is:
 * <code>
 *          http://localhost:8181/cxf/foservice/crudone
 * </code>
 * Advanced scenario url is:
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
        if (url.contains("crudone")){
            LOG.info("Starting restful service crudone on URL: " + this.url );
            jaxrsServerFactoryBean.setResourceClasses(FlightObjectServiceScenarioOne.class);
            jaxrsServerFactoryBean.setResourceProvider(FlightObjectServiceScenarioOne.class, new SingletonResourceProvider(new FlightObjectServiceScenarioOne()));
            
        }else{
            LOG.info("Starting  advanced  crud restful service on URL: " + this.url );
            jaxrsServerFactoryBean.setResourceClasses(FlightObjectService.class);
            jaxrsServerFactoryBean.setResourceProvider(FlightObjectService.class, new SingletonResourceProvider(new FlightObjectService()));
        }
        jaxrsServerFactoryBean.setAddress(url);
        server = jaxrsServerFactoryBean.create();
    }

    public  void stopServer() throws Exception {
        server.stop();
        server.destroy();
    }

    public  WebClient createWebClient(final String path) {
        return WebClient.create(this.url + path);
    }

}
