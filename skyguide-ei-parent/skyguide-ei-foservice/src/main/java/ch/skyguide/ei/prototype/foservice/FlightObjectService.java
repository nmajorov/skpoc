package ch.skyguide.ei.prototype.foservice;

import aero.fixm.model.flight.FlightType;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.skyguide.ei.prototype.foservice.flightobject.IFlightObject;
import ch.skyguide.ei.prototype.foservice.flightobject.IGloballyUniqueFlightIdentifier;
import ch.skyguide.ei.prototype.foservice.forepository.GloballyUniqueFlightIdentifier;

@Path( "/crud")
@Consumes("application/xml")
@Produces("application/xml")
public class FlightObjectService 
extends AbstractFlightObjectService {

    private static final Logger LOG = LoggerFactory.getLogger(FlightObjectService.class);
    private final Map<CompositeFlightKey, FlightType> positions = new ConcurrentHashMap<CompositeFlightKey, FlightType>();

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
        return Response.ok().entity(fixmFlight).build();
    }
    
    @Path("/arcid/{arcid}/departure/{departure}/arrival/{arrival}")
    @POST
    public Response createFlightObject(@PathParam("arcid") final String arcid,
                                       @PathParam("departure") final String departure,
                                       @PathParam("arrival") final String arrival,
                                       final FlightType flight) {
    	
        final CompositeFlightKey key = new CompositeFlightKey(arcid, departure, arrival);
        positions.put(key, flight);
        
        
        LOG.debug("createFlightObject() using the key {} for the flight object {}", key, flight);
        return Response.ok().entity(flight).build();
    }

    @GET
    @POST
    @Produces("application/xml")
    @Path("/flights/arcid/{arcid}")
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
            	
            		// TODO: rjw, only handling one flight.
	            	FlightType theFixmFlight = fixmFlights.iterator().next();

            	 	response = Response.ok().entity(theFixmFlight).build();
            }
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getMessage()).build();
        }

        return response;
    }

    @GET
    @Path("flights/arcid/{arcid}/departure/{departure}/arrival/{arrival}")
    public Response getFlightObject(@PathParam("arcid") final String arcid,
                                    @PathParam("departure") final String departure,
                                    @PathParam("arrival") final String arrival) {
        final CompositeFlightKey key = new CompositeFlightKey(arcid, departure, arrival);
        final FlightType flight = positions.get(key);
        LOG.debug("readFlightObject() using the key {} returning the flight object {}", key, flight);
        return Response.ok().entity(flight).build();
    }

    @Path("flights/arcid/{arcid}/departure/{departure}/arrival/{arrival}")
    @PUT
    public Response updateFlightObject(@PathParam("arcid") final String arcid,
                                       @PathParam("departure") final String departure,
                                       @PathParam("arrival") final String arrival,
                                       final FlightType flight) {
        final CompositeFlightKey key = new CompositeFlightKey(arcid, departure, arrival);
        positions.put(key, flight);
        LOG.debug("updateFlightObject() using the key {} updating with the flight object {}", key, flight);
        return Response.ok().entity(flight).build();
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

    @Path("flights/arcid/{arcid}/departure/{departure}/arrival/{arrival}")
    @DELETE
    public Response deleteFlightObject(@PathParam("arcid") final String arcid,
                                       @PathParam("departure") final String departure,
                                       @PathParam("arrival") final String arrival) {
        final CompositeFlightKey key = new CompositeFlightKey(arcid, departure, arrival);
        final FlightType flight = positions.remove(key);
        if (flight != null) {
            LOG.debug("deleteFlightObject() using the key {} deleting the flight object {}", key, flight);
            return Response.ok(flight).build();
        } else {
            LOG.debug("deleteFlightObject() using the key {} not found", key);
            return Response.notModified().build();
        }
    }

}
