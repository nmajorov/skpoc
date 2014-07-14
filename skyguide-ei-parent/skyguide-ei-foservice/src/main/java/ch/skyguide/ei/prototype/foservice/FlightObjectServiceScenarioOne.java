package ch.skyguide.ei.prototype.foservice;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aero.fixm.model.flight.FlightType;
import ch.skyguide.ei.prototype.foservice.flightobject.IFlightObject;
import ch.skyguide.ei.prototype.foservice.flightobject.IGloballyUniqueFlightIdentifier;
import ch.skyguide.ei.prototype.foservice.forepository.GloballyUniqueFlightIdentifier;

/**
 * Flight object CRUD service emulation for scenario one.
 * 
 * @author Nikolaj Majorov
 * 
 */
@Path("/crudone")
public class FlightObjectServiceScenarioOne 
extends AbstractFlightObjectService {
    private static final Logger LOG = LoggerFactory
            .getLogger(FlightObjectServiceScenarioOne.class);

    @Path("/status")
    @Produces("text/plain")
    @GET
    public String getStatus() {
        String version = "";

        try {
            Enumeration<URL> resources = getClass().getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {

                Manifest manifest = new Manifest(resources.nextElement()
                        .openStream());
                Attributes attributes = manifest.getMainAttributes();
                version = attributes.getValue("Implementation-Version");
            }

        } catch (IOException e) {
            LOG.warn("Error", e);
        }
        return "running version: " + version;
    }

    @GET
    @POST
    @Produces("application/xml")
    @Path("/flights/{arcid}")
    public Response getFlightObject(@PathParam("arcid") final String arcid) {
        LOG.info("getFlightObject for arcid: {}", arcid);
        Response response = null;
        // find in the storage by id
        try {

        	
        	final Set<IFlightObject> repositoryFlights 
        		= getRepository().retrieveFlightObject(arcid);
        	
        	final Set<FlightType> fixmFlights 
        		= transformToFixm(repositoryFlights);

            if (fixmFlights == null) {
                response =  Response.status(Response.Status.NOT_FOUND).build();
            } else {
                response = Response.ok().entity(fixmFlights).build();
            }
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getMessage()).build();
        }

        return response;
    }

    @POST
    @Consumes("application/xml")
    @Path("/flights/new")
    public Response createFlightObject(FlightType fixmFlight) {
        try {
        	
        	IFlightObject flightObject = transformToFlightObject(fixmFlight);
        	
        	getRepository().createFlightObject(flightObject);
        	
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getMessage()).build();
        }
        return Response.ok().build();
    }
    
    @POST
    @Consumes("application/xml")
    @Path("/flights/update")
    public Response updateFlightObject(FlightType fixmFlight) {
        LOG.info("updateFlightObject for aircraftIdentification: {}", fixmFlight.getFlightIdentification()
                .getAircraftIdentification().getValue());
        try {
        	
        	final IGloballyUniqueFlightIdentifier gufi = 
        		new GloballyUniqueFlightIdentifier(fixmFlight.getGufi().getValue());
        	
        	final IFlightObject repositoryFlight 
        		= getRepository().retrieveFlightObject(gufi);
        	
        	final  IFlightObject updatedFlightObject 
        			= mergeFlightObject(fixmFlight, repositoryFlight);
        	
        	getRepository().updateFlightObject(updatedFlightObject);
        	
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getMessage()).build();
        }
        return Response.ok().build();
    }
    
}
