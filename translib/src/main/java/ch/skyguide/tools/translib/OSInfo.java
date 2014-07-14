package ch.skyguide.tools.translib;

/**
 * Get information about current OS.
 * This class is useful to determine which native library to load depending on OS. 
 * 
 * @author Nikolaj Majorov
 *
 */
public class OSInfo {

    public static String getOSName(String property){
        if (property.contains("Windows")) {
            return "Windows";
        }
        else if (property.contains("Mac")) {
            return "Mac";
        }
        else if (property.contains("Linux")) {
            return "Linux";
        }
        else {
            return property.replaceAll("\\W", "");
        }
    
    }

    public static int getArchName(String property) {
       if (property.indexOf("64") != -1){
           return 64;
       }else{
           return 32;
       }
       
        
    }
    


}
