package ch.skyguide.tools.translib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Load native library from the classpath
 * 
 * 
 * @see http://frommyplayground.com/how-to-load-native-jni-library-from-jar/
 * 
 * 
 * @author Nikolaj Majorov
 *
 */
public class NativeLibLoader {

    private static final Logger LOG = LoggerFactory.getLogger(NativeLibLoader.class);

    private NativeLibLoader(){}
    
    /**
     * Loads library from current JAR archive
     * 
     * The file from JAR is copied into system temporary directory and then loaded. The temporary file is deleted after exiting.
     * Method uses String as filename because the pathname is "abstract", not system-dependent.
     * 
     * @param filename The filename inside JAR as absolute path (beginning with '/'), e.g. /package/File.ext
     * @throws IOException If temporary file creation or read/write operation fails
     * @throws IllegalArgumentException If source file (param path) does not exist
     * @throws IllegalArgumentException If the path is not absolute or if the filename is shorter than three characters (restriction of {@see File#createTempFile(java.lang.String, java.lang.String)}).
     */
    public static void loadLibraryFromJar() throws IOException, UnsupportedOSException {
        String pefix = "translib";
        String suffix ="";
        String path="/";
        
        LOG.debug(System.getProperty("os.name"));
        LOG.debug(System.getProperty("os.arch"));
        
        String osname = OSInfo.getOSName(System.getProperty("os.name"));
        int archname = OSInfo.getArchName(System.getProperty("os.arch"));
        
        if (osname.equals("Linux")){
            if (archname == 64){
                suffix = ".so";
                path +=  "linux/x86_64/";    
            } else{
                throw new UnsupportedOSException("Linux 32 bit is not supported");
            }
            
        }
       
        
        if (osname.equals("Windows") && archname == 32){
            if (archname == 32){
                suffix = ".dll";
                path +=  "windows/i386/";
            }else{
                throw new UnsupportedOSException("Windows 64 bit is not supported");
            }
        }    
        
        
        
        path +=  pefix +suffix; 
        
        
        
        // Prepare temporary file
        File temp = File.createTempFile(pefix, suffix);
        //temp.deleteOnExit();
 
        if (!temp.exists()) {
            throw new FileNotFoundException("File " + temp.getAbsolutePath() + " does not exist.");
        }
 
        // Prepare buffer for data copying
        byte[] buffer = new byte[1024];
        int readBytes;
 
        // Open and check input stream
        InputStream is = NativeLibLoader.class.getResourceAsStream(path);
        if (is == null) {
		    throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }
 
//        // Open output stream and copy data between source file in JAR and the temporary file
        OutputStream os = new FileOutputStream(temp);
        try {
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } finally {
            // If read/write fails, close streams safely before throwing an exception
            os.close();
            is.close();
        }
        
        if (!System.getProperty("os.name").contains("Windows")) {
            try {
                Runtime.getRuntime().exec(new String[] { "chmod", "755", temp.getAbsolutePath() })
                .waitFor();
            }
            catch (Throwable e) {}
        };
 
        // Finally, load the library
        System.load(temp.getAbsolutePath());
    }

}
