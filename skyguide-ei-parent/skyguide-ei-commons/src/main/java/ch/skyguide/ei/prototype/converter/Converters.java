package ch.skyguide.ei.prototype.converter;

import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixMessage;

import java.nio.ByteBuffer;

import org.apache.camel.Converter;

@Converter
public class Converters {

    @Converter
    public FixmAsterixMessage convertByteArrayToAsterixMessage(final byte[] array) throws Exception {
        final ByteBuffer workingBuffer = ByteBuffer.allocate(array.length);
        workingBuffer.put(array).rewind();

        return new AsterixToFixmDataConverter().transformToFixm(workingBuffer);
    }

    @Converter
    public byte[] convertAsterixMessageToByteArray(final FixmAsterixMessage asterixMessage) throws Exception {
        final ByteBuffer resultantBuffer = new FixmToAsterixDataConverter().transformToAsterix(asterixMessage);

        return resultantBuffer.array();
    }

}
