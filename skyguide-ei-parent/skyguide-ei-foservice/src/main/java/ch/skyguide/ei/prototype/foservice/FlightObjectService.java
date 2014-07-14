package ch.skyguide.ei.prototype.foservice;

import aero.fixm.model.flight.FlightType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

@Path( "/crud/arcid/{arcid}/departure/{departure}/arrival/{arrival}")
@Consumes("application/xml")
@Produces("application/xml")
public class FlightObjectService 
extends AbstractFlightObjectService {

    private static final Logger LOG = LoggerFactory.getLogger(FlightObjectService.class);
    private final Map<CompositeFlightKey, FlightType> positions = new ConcurrentHashMap<CompositeFlightKey, FlightType>();

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
    public Response readFlightObject(@PathParam("arcid") final String arcid,
                                       @PathParam("departure") final String departure,
                                       @PathParam("arrival") final String arrival) {
        final CompositeFlightKey key = new CompositeFlightKey(arcid, departure, arrival);
        final FlightType flight = positions.get(key);
        LOG.debug("readFlightObject() using the key {} returning the flight object {}", key, flight);
        return Response.ok().entity(flight).build();
    }

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
