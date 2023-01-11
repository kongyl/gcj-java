package me.kongyl.gcjjava.util;

public class GcjUtils {
    
    private GcjUtils() {}

    private static final double a = 6378245.0;
    private static final double f = 1 / 298.3;
    private static final double b = a * (1 - f);
    private static final double ee = 1 - (b * b) / (a * a);

    public static boolean outOfChina(double lng, double lat) {
        if (lng < 72.004 || lng > 137.8347 || lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret = ret + (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret = ret + (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret = ret + (160.0 * Math.sin(y / 12.0 * Math.PI) + 320.0 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLng(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x +  0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret = ret + (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret = ret + (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret = ret + (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double[] wgs2gcj(double wgsLng, double wgsLat) {
        if (outOfChina(wgsLng, wgsLat)) {
            return new double[] { wgsLng, wgsLat };
        }
      
        double dLat = transformLat(wgsLng - 105.0, wgsLat - 35.0);
        double dLng = transformLng(wgsLng - 105.0, wgsLat - 35.0);
        double radLat = wgsLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);
        double gcjLat = wgsLat + dLat;
        double gcjLng = wgsLng + dLng;
        return new double[] { gcjLng, gcjLat };
    }

    public static double[] gcj2wgs(double gcjLng, double gcjLat) {
        double[] g0 = new double[] { gcjLng, gcjLat };
        double[] w0 = g0;
        double[] g1 = wgs2gcj(w0[0], w0[1]);
      
        // w1 = w0 - (g1 - g0)
        double w1x = w0[0] - (g1[0] - g0[0]);
        double w1y = w0[1] - (g1[1] - g0[1]);
        double[] w1 = new double[] { w1x, w1y };
        // delta = w1 - w0
        double dx = w1[0] - w0[0];
        double dy = w1[1] - w0[1];
        double[] delta = new double[] { dx, dy };
      
        while (Math.abs(delta[0]) >= 1e-6 || Math.abs(delta[1]) >= 1e-6) {
            w0 = w1;
            g1 = wgs2gcj(w0[0], w0[1]);
      
            // w1 = w0 - (g1 - g0)
            w1x = w0[0] - (g1[0] - g0[0]);
            w1y = w0[1] - (g1[1] - g0[1]);
            w1 = new double[] { w1x, w1y };
            // delta = w1 - w0
            dx = w1[0] - w0[0];
            dy = w1[1] - w0[1];
            delta = new double[] { dx, dy };
        }

        return w1;
      }

}
