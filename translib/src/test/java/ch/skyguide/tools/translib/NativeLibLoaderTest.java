package ch.skyguide.tools.translib;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;


public class NativeLibLoaderTest {

    @Test
    public void testWin32BitLoadTest() {
        try {
            NativeLibLoader.loadLibraryFromJar();
        } catch (UnsupportedOSException ex) {
            fail("Current version is not supported");
        } catch (IOException ex) {
            ex.printStackTrace();
            fail("error at loading native library");
        }
     
    }

   
}
