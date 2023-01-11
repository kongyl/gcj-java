package me.kongyl.gcjjava.util;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

public class TransUtils {

    private TransUtils() {}

    public static double[] geoTrans(SpatialReference inSr, SpatialReference outSr, double x, double y) {
        CoordinateTransformation ct = CoordinateTransformation.CreateCoordinateTransformation(inSr, outSr);
        return ct.TransformPoint(x, y);
    }
    
    public static double[] geoTrans(int inEpsg, int outEpsg, double x, double y) {
        SpatialReference inSr = new SpatialReference();
        inSr.ImportFromEPSG(inEpsg);
        SpatialReference outSr = new SpatialReference();
        outSr.ImportFromEPSG(outEpsg);
        return geoTrans(inSr, outSr, x, y);
    }
    
}
