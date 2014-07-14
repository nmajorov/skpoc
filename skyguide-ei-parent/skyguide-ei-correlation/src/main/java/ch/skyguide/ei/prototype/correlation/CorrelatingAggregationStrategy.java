package ch.skyguide.ei.prototype.correlation;

import ch.skyguide.ei.prototype.UnexpectedPayloadException;
import ch.skyguide.fixm.extension.flight.enroute.AsterixRecordType;
import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixDataBlock;
import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixMessage;
import ch.skyguide.fixm.extension.flight.enroute.FixmSystemTrackData;
import ch.skyguide.fixm.extension.flight.enroute.SkyguideAircraftPosition;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorrelatingAggregationStrategy implements AggregationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(CorrelatingAggregationStrategy.class);
    private static final String CONSUMED_POSITION_POJO = "SkyguideConsumedPosition";
    private static final String FLIGHT_POSITION_ID = "SkyguideFlightPositionId";

    public void enrichExchange(final Exchange exchange) {
        LOG.debug("start enrichExchange");
        final FixmAsterixMessage asterixMessage = exchange.getIn().getBody(FixmAsterixMessage.class);
        String targetIdentification = null;
        
        
        for (final FixmAsterixDataBlock fixmAsterixDataBlock : asterixMessage
                .getFixmAsterixDataBlocks()) {
            for (final AsterixRecordType fixmAsterixRecord : fixmAsterixDataBlock
                    .getFixmAsterixRecords()) {
            
                if (fixmAsterixRecord instanceof FixmSystemTrackData) {
                    FixmSystemTrackData fixmSystemTrackData = (FixmSystemTrackData) fixmAsterixRecord;
                   if (fixmSystemTrackData.getAircraftDerivedData() != null){
                       if (fixmSystemTrackData.getAircraftDerivedData().getTargetIdentification() != null){
                            targetIdentification  = fixmSystemTrackData.getAircraftDerivedData().getTargetIdentification().getValue().trim();
                            LOG.info("Processing asterixMessage gete targetIdentification {}", targetIdentification);
                            exchange.getIn().setHeader(FLIGHT_POSITION_ID, targetIdentification);
                       }
                       
                   }
                }
            }
        }

        LOG.debug("end enrichExchange");
       
    }

    @Override
    public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
        LOG.debug("start aggregate");
        // for now let's just provide a dummy implementation
//        final SkyguideAircraftPosition consumedAircraftPosition = newExchange.getIn().getHeader(CONSUMED_POSITION_POJO, SkyguideAircraftPosition.class);
//        final double oldValue = consumedAircraftPosition.getTrack().getValue().getValue().getValue();
//
//        SkyguideAircraftPosition currentAircraftPosition;
//        boolean currentAircraftPositionExisting = false;
//        try {
//            final Response response = newExchange.getIn().getMandatoryBody(Response.class);
//            final Object entity = response.getEntity();
//            LOG.info("The returned message entity from the foservice is {}", entity);
//            if (entity != null) {
//                // it could be an empty string if the postion doesn't exist
//                final String string = newExchange.getContext().getTypeConverter().convertTo(String.class, entity);
//                if (!ObjectHelper.isEmpty(string)) {
//                    // we've got a proper position as xml, so let's demarshall it and do our "business logic"...
//                    currentAircraftPosition = newExchange.getContext().getTypeConverter().convertTo(SkyguideAircraftPosition.class, string);
//                    final double newValue = currentAircraftPosition.getTrack().getValue().getValue().getValue();
//                    final double calculatedValue = oldValue + newValue;
//                    currentAircraftPosition.getTrack().getValue().getValue().setValue(calculatedValue);
//
//                    newExchange.getIn().setBody(currentAircraftPosition);
//                    currentAircraftPositionExisting = true;
//
//                    LOG.info("Changed the position's track number from {} to {}", oldValue, calculatedValue);
//                }
//            }
//        } catch (final Exception e) {
//            throw new UnexpectedPayloadException("The payload was " + newExchange.getIn().getBody(), e);
//        }
//
//        if (!currentAircraftPositionExisting) {
//            // a fictive user case: if the position doesn't exist through the flight object service then just skip the aggregation and continue routing
//            // with the position we've consumed from the topic (as is) representing the result
//            LOG.warn("The position with the id {} is non-existing by the flight object service", newExchange.getIn().getHeader(FLIGHT_POSITION_ID));
//            newExchange.getIn().setBody(consumedAircraftPosition);
//        }
        LOG.debug("end  aggregate");
        return newExchange;
    }

}
