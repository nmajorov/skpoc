package ch.skyguide.ei.prototype.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;

import ch.skyguide.fixm.extension.flight.enroute.AsterixRecordType;
import ch.skyguide.fixm.extension.flight.enroute.CalculatedTrackVelocityType;
import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixDataBlock;
import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixMessage;
import ch.skyguide.fixm.extension.flight.enroute.FixmSystemTrackData;
import ch.skyguide.tools.translib.ConvTranslibImpl;

public class TranslatorInvoker {

    static private final int TRANSLATE_CH_GVA = 0;
    static private final int TRANSLATE_CH_ZRH = 1;

    public void translateCoordinate(final Exchange exchange) throws Exception {
        // for now let's just provide a dummy implementation
        final FixmAsterixMessage asterixMessage = exchange.getIn()
                .getMandatoryBody(FixmAsterixMessage.class);

        for (final FixmAsterixDataBlock fixmAsterixDataBlock : asterixMessage
                .getFixmAsterixDataBlocks()) {
            for (final AsterixRecordType fixmAsterixRecord : fixmAsterixDataBlock
                    .getFixmAsterixRecords()) {
                // 1. Determine the Asterix Data Block containing AST CAT62
                // FixmSystemTrackData messages
                if (fixmAsterixRecord instanceof FixmSystemTrackData) {
                    // 2. Extract from the AST CAT062 SystemTrackData message
                    // the following:
                    FixmSystemTrackData fixmSystemTrackData = (FixmSystemTrackData) fixmAsterixRecord;
                    // 2.1 Extract from the AST CAT062 SystemTrackData message
                    // the following:
                    List<Double> pos = fixmSystemTrackData
                            .getCalculatedTrackPositionCartesian()
                            .getLocation().getPos();

                    // 2.2 I062/136, Measured Flight Level with the method
                    // getMeasuredFlightLevel() in units of 1/4 FL (25').
                    Long measuredFlightLevel = fixmSystemTrackData
                            .getMeasuredFlightLevel();

                    // 2.3. I062/185, Calculated Track Velocity (Cartesian)
                    // with method getCalculatedTrackVelocityCartesian() with
                    // units 0.25 ms-1.
                    CalculatedTrackVelocityType calculatedTrackVelocityType 
                    	= fixmSystemTrackData
                    		.getCalculatedTrackVelocityCartesian();

                    // 3. Convert from AST CAT062 units to this methods units as
                    // follows (order is same as in point 2:
                    // 3.1. 0.5m -> Nautical Miles (NM): x*0.5/1852
                    final double xArtas = pos.get(0) * 0.5 / 1852;
                    final double yArtas = pos.get(1) * 0.5 / 1852;

                    // 3.2. 1/4 FL -> FL: x/4
                    final long level = measuredFlightLevel / 4;

                    // 3.3. ms-1 -> NMh-1: x*0.25/1852/3600
                    final double vxArtas 
                    = calculatedTrackVelocityType.getVx() * 0.25 / 1852 / 3600;
                    final double vyArtas 
                    = calculatedTrackVelocityType.getVy() * 0.25 / 1852 / 3600;

                    // 4. Call the Translib translation method: ARTAStoMv2
                    final double[] resArray 
                    	= ConvTranslibImpl.ARTAStoMv2(xArtas,yArtas, 
                    			level, vxArtas, vyArtas, TRANSLATE_CH_ZRH);

                    double x_mv = resArray[0];
                    double y_mv = resArray[1];
                    double vx_mv = resArray[2];
                    double vy_mv = resArray[3];

                    // 5. Convert from the methods units back to AST CAT062
                    // units as follows:

                    //5.1   NM -> 0.5m : y*1852/0.5
                    Double convertedResultX = (x_mv * 1852 / 0.5);
                    Double convertedResultY = (y_mv * 1852 / 0.5);
                    ArrayList<Double> convertedPositions = new ArrayList<>(2);
                    convertedPositions.add(convertedResultX);
                    convertedPositions.add(convertedResultY);
                    
                    //5.2  NMh-1 -> ms-1 : y*1852*3600/0.25
                    Double convertedVX =  vx_mv *1852*3600/0.25;
                    Double convertedVY = vy_mv  *1852*3600/0.25;
                    
                    // 6. Set the translated information back into the
                    // FixmSystemTrackData
                    // Message from where you took them out.
                    pos.clear();
                    pos.add(convertedResultX);
                    pos.add(convertedResultY);
                    
                    calculatedTrackVelocityType.setVx(convertedVX.intValue());
                    calculatedTrackVelocityType.setVy(convertedVY.intValue());
                    
                }

            }
        }
    }

}
