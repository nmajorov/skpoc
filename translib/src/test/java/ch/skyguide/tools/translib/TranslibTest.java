package ch.skyguide.tools.translib;

import org.junit.Test;
import static org.junit.Assert.*;


 public class TranslibTest {
 
        @Test
		public void test() {
            // line 533 ConvArtasMv_GE
            double x = 43.03256322;
            double y = -169.2155317;
            double mode_c = 200;
            double x_mv_ref = 132.8105299;
            double y_mv_ref = -131.8076698;
            double speed = 330.908;
            double heading = 222.55;
            double vx = speed
                    * Math.sin(heading * 3.14159265358979323846 / 180.0);
            double vy = speed
                    * Math.cos(heading * 3.14159265358979323846 / 180.0);
            double speed_mv_ref = 330.983130615;
            double heading_mv_ref = 221.084919187;
            double vx_mv_ref = -217.514460358;
            double vy_mv_ref = -249.474031288;

            double epsilon = 1e-4;

            double[] resArray = ConvTranslibImpl.ARTAStoMv3(x, y, mode_c,
                    speed, heading, vx, vy, 0);

            double x_mv = resArray[0];
            double y_mv = resArray[1];
            double speed_mv = resArray[2];
            double heading_mv = resArray[3];
            double vx_mv = resArray[4];
            double vy_mv = resArray[5];

            double res1 = Math.sqrt((x_mv_ref - x_mv) * (x_mv_ref - x_mv)
                    + (y_mv_ref - y_mv) * (y_mv_ref - y_mv));

            double res2 = Math.abs(speed_mv_ref - speed_mv);

            double res3 = Math.sqrt((vx_mv_ref - vx_mv) * (vx_mv_ref - vx_mv)
                    + (vy_mv_ref - vy_mv) * (vy_mv_ref - vy_mv));

            double delta_heading1 = Math.abs(heading_mv_ref - heading_mv);
            double delta_heading2 = Math.abs(heading_mv_ref - heading_mv - 360);
            double delta_heading3 = Math.abs(heading_mv_ref - heading_mv + 360);
            double res4;
            if (delta_heading1 <= delta_heading2
                    && delta_heading1 <= delta_heading3)
                res4 = delta_heading1;
            else if (delta_heading2 <= delta_heading1
                    && delta_heading2 <= delta_heading3)
                res4 = delta_heading2;
            else
                res4 = delta_heading3;

            assertFalse("failed 1", res1 + res2 + res3 + res4 > epsilon);

            double[] resArray1 = ConvTranslibImpl.ARTAStoMv2(x, y, mode_c, vx,
                    vy, 0);

            x_mv = resArray1[0];
            y_mv = resArray1[1];
            vx_mv = resArray1[2];
            vy_mv = resArray1[3];

            res1 = Math.sqrt((x_mv_ref - x_mv) * (x_mv_ref - x_mv)
                    + (y_mv_ref - y_mv) * (y_mv_ref - y_mv));

            res3 = Math.sqrt((vx_mv_ref - vx_mv) * (vx_mv_ref - vx_mv)
                    + (vy_mv_ref - vy_mv) * (vy_mv_ref - vy_mv));

            assertFalse("failed2", res1 + res3 > epsilon);
            double[] resArray2 = ConvTranslibImpl.ARTAStoMv1(x, y, mode_c, 0);

            x_mv = resArray2[0];
            y_mv = resArray2[1];

            res1 = Math.sqrt((x_mv_ref - x_mv) * (x_mv_ref - x_mv)
                    + (y_mv_ref - y_mv) * (y_mv_ref - y_mv));

            assertFalse("failed 3", res1 > epsilon);
            // line 612 ConvArtasMv_ZR
            x = 13.89910141;
            y = -109.4266214;
            mode_c = 200;
            x_mv_ref = -3.148115418;
            y_mv_ref = -147.0803675;
            speed = 137.329;
            heading = 73.0701;
            vx = speed * Math.sin(heading * 3.14159265358979323846 / 180.0);
            vy = speed * Math.cos(heading * 3.14159265358979323846 / 180.0);
            speed_mv_ref = 137.375951226;
            heading_mv_ref = 73.356160153;
            vx_mv_ref = 131.620407105;
            vy_mv_ref = 39.347432046;

            double[] resArray3 = ConvTranslibImpl.ARTAStoMv3(x, y, mode_c,
                    speed, heading, vx, vy, 1);

            x_mv = resArray3[0];
            y_mv = resArray3[1];
            speed_mv = resArray3[2];
            heading_mv = resArray3[3];
            vx_mv = resArray3[4];
            vy_mv = resArray3[5];

            res1 = Math.sqrt((x_mv_ref - x_mv) * (x_mv_ref - x_mv)
                    + (y_mv_ref - y_mv) * (y_mv_ref - y_mv));

            res2 = Math.abs(speed_mv_ref - speed_mv);

            res3 = Math.sqrt((vx_mv_ref - vx_mv) * (vx_mv_ref - vx_mv)
                    + (vy_mv_ref - vy_mv) * (vy_mv_ref - vy_mv));

            delta_heading1 = Math.abs(heading_mv_ref - heading_mv);
            delta_heading2 = Math.abs(heading_mv_ref - heading_mv - 360);
            delta_heading3 = Math.abs(heading_mv_ref - heading_mv + 360);
            if (delta_heading1 <= delta_heading2
                    && delta_heading1 <= delta_heading3)
                res4 = delta_heading1;
            else if (delta_heading2 <= delta_heading1
                    && delta_heading2 <= delta_heading3)
                res4 = delta_heading2;
            else
                res4 = delta_heading3;

            assertFalse("failed 4", res1 + res2 + res3 + res4 > epsilon);
            double[] resArray4 = ConvTranslibImpl.ARTAStoMv2(x, y, mode_c, vx,
                    vy, 1);

            x_mv = resArray4[0];
            y_mv = resArray4[1];
            vx_mv = resArray4[2];
            vy_mv = resArray4[3];

            res1 = Math.sqrt((x_mv_ref - x_mv) * (x_mv_ref - x_mv)
                    + (y_mv_ref - y_mv) * (y_mv_ref - y_mv));

            res3 = Math.sqrt((vx_mv_ref - vx_mv) * (vx_mv_ref - vx_mv)
                    + (vy_mv_ref - vy_mv) * (vy_mv_ref - vy_mv));

            assertFalse("failed 5", res1 + res3 > epsilon);

            double[] resArray5 = ConvTranslibImpl.ARTAStoMv1(x, y, mode_c, 1);

            x_mv = resArray5[0];
            y_mv = resArray5[1];

            res1 = Math.sqrt((x_mv_ref - x_mv) * (x_mv_ref - x_mv)
                    + (y_mv_ref - y_mv) * (y_mv_ref - y_mv));

            assertFalse("failed 6", res1 > epsilon);

        }
    }