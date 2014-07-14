package ch.skyguide.tools.translib;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvTranslibImpl {

 private static final Logger LOG = LoggerFactory.getLogger(ConvTranslibImpl.class);

 private static boolean isLoaded = false;
 
 
  /**
   * Class:     ConvTranslibImpl
   * Method:    ARTAStoMv1
   * @param x_artas double: X ARTAS coordinates in NM
   * @param y_artas double: Y ARTAS coordinates in NM
   * @param alt double: aircraft altitude in FL
   * @param center int: = 0 for Geneva, = 1 for Zurich
   * @return double[]
       x_mv: X projected coordinates in NM
       y_mv: Y projected coordinates in NM
   */
  public static double[] ARTAStoMv1(final double x_artas, final double y_artas, final double alt,
               final int center) {
    return nativeAtoM1(x_artas, y_artas, alt, center);
  }

  /**
   * Class:     ConvTranslibImpl
   * Method:    ARTAStoMv2
   * @param x_artas double: X ARTAS coordinates in NM
   * @param y_artas double: Y ARTAS coordinates in NM
   * @param alt double: aircraft altitude in FL
   * @param vx_artas double: X velocity in knots
   * @param vy_artas double: Y velocity in knots
   * @param center int: = 0 for Geneva, = 1 for Zurich
   * @return double[]
       x_mv: X projected coordinates in NM
       y_mv: Y projected coordinates in NM
       vx_mv: X velocity in knots
       vy_mv: Y velocity in knots
   */
  public static double[]
    ARTAStoMv2(final double x_artas, final double y_artas, final double alt,
               final double vx_artas, final double vy_artas, final int center){
    return nativeAtoM2(x_artas, y_artas, alt, vx_artas, vy_artas, center);
  }

  /**
   * Class:     ConvTranslibImpl
   * Method:    ARTAStoMv3
   * @param x_artas double: X ARTAS coordinates in NM
   * @param y_artas double: Y ARTAS coordinates in NM
   * @param alt double: aircraft altitude in FL
   * @param speed_artas double: ARTAS speed in knots
   * @param heading_artas double: ARTAS heading in degrees (in [0, 360[)
   * @param vx_artas double: X velocity in knots
   * @param vy_artas double: Y velocity in knots
   * @param center int: = 0 for Geneva, = 1 for Zurich
   * @return double[]
       x_mv: X projected coordinates in NM
       y_mv: Y projected coordinates in NM
       speed_mv: speed in knots
       heading_mv: heading in degrees (in [0, 360[)
       vx_mv: X velocity in knots
       vy_mv: Y velocity in knots
   */
  public static double[]
    ARTAStoMv3(final double x_artas, final double y_artas, final double alt,
               final double speed_artas, final double heading_artas,
               final double vx_artas, final double vy_artas, final int center){
    return nativeAtoM3(x_artas, y_artas, alt, speed_artas, heading_artas,
                       vx_artas, vy_artas, center);
  }

  // --------
  // JNI part
  // --------
  static {
   try {
       if (!isLoaded){
         NativeLibLoader.loadLibraryFromJar();
         isLoaded = true;
         LOG.info("translib native library succesfully loaded");
       }
        
    } catch (UnsupportedOSException ex) {
        LOG.error("Current version is not supported",ex);
    } catch (IOException ex) {
        LOG.error("error at loading native library",ex);
    }
  }

  private static native double[]
    nativeAtoM1(double x_artas, double y_artas, double alt, int center);

  private static native double[]
    nativeAtoM2(double x_artas, double y_artas, double alt,
                double vx_artas, double vy_artas, int center);

  private static native double[]
    nativeAtoM3(double x_artas, double y_artas, double alt,
                double speed_artas, double heading_artas,
                double vx_artas, double vy_artas, int center);
}
