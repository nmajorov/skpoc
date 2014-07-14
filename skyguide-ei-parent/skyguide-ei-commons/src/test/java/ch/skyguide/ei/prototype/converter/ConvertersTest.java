package ch.skyguide.ei.prototype.converter;

import ch.skyguide.fixm.extension.flight.enroute.FixmAsterixMessage;

import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ConvertersTest extends CamelTestSupport {

    @Test
    public void invokeByteArrayToAsterixMessageTypeConversion() throws Exception {

        final InputStream is = getClass().getResourceAsStream("/62_65_message.bin");
        assertNotNull(is);
        final byte[] byteArray = convert(byte[].class, is);

        final FixmAsterixMessage fixmAsterixMessage = convert(FixmAsterixMessage.class, byteArray);
        assertNotNull(fixmAsterixMessage);
    }

    @Test
    public void invokeAsterixMessageTypeToByteArrayConversion() throws Exception {
        final InputStream is = getClass().getResourceAsStream("/position.xml");
        assertNotNull(is);

        final Unmarshaller unmarshaller = JAXBContext.newInstance(FixmAsterixMessage.class).createUnmarshaller();
        final FixmAsterixMessage fixmAsterixMessage = (FixmAsterixMessage)unmarshaller.unmarshal(is);
        final byte[] asterixMessage = convert(byte[].class, fixmAsterixMessage);

        assertNotNull(asterixMessage);
    }

    @Test
    public void invokeAsterix62_65ToAsterix242_243Conversion() throws Exception {
        
    	// Load from resources
    	final InputStream is = getClass().getResourceAsStream("/62_65_message.bin");
        assertNotNull(is);
        
        // Input stream to the byte array conversion.
        final byte[] byteArray = convert(byte[].class, is);

        // AST CAT 062/065 to FIXM AST.
        final FixmAsterixMessage fixmAsterixMessage 
        	= convert(FixmAsterixMessage.class, byteArray);
        assertNotNull(fixmAsterixMessage);
        
        final byte[] asterixMessage = convert(byte[].class, fixmAsterixMessage);
        assertNotNull(asterixMessage);
        
        FileOutputStream outputStream = new FileOutputStream("./242_243_Output.bin");
        
        outputStream.write(asterixMessage);
        outputStream.close();
        
    }

    protected <T> T convert(final Class<T> type, final Object value) throws Exception {
        return context().getTypeConverter().mandatoryConvertTo(type, value);
    }

}
