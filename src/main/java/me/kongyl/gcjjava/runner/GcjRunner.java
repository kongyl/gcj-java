package me.kongyl.gcjjava.runner;

import org.gdal.gdal.gdal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import me.kongyl.gcjjava.util.GcjUtils;
import me.kongyl.gcjjava.util.TransUtils;

@Component
public class GcjRunner implements CommandLineRunner {
    
    private final static Logger logger = LoggerFactory.getLogger(GcjRunner.class);

    public void run(String... args) throws Exception {
        double lng = 116.30284478001347;
        double lat = 39.9035179065456;
        logger.info(String.format("origin: lng: %.10f, lat: %.10f", lng, lat));

        // wgs2gcj
        double[] gcj = GcjUtils.wgs2gcj(lng, lat);
        logger.info(String.format("wgs2gcj: lng: %.10f, lat: %.10f", gcj[0], gcj[1]));

        // gcj2wgs
        double[] wgs = GcjUtils.gcj2wgs(lng, lat);
        logger.info(String.format("gcj2wgs: lng: %.10f, lat: %.10f", wgs[0], wgs[1]));

        double x = 12946773.458720237;
        double y = 4851931.6540772095;
        logger.info(String.format("origin: x: %.10f, y: %.10f", x, y));
        gdal.AllRegister();
        double[] transOri = TransUtils.geoTrans(3857, 4326, x, y);
        // gdal version 3+ use north east axis
        double[] lngLat = new double[] { transOri[1], transOri[0] };        
        
        // wgs2gcj
        double[] gcjLngLat = GcjUtils.wgs2gcj(lngLat[0], lngLat[1]);
        double[] gcjXY = TransUtils.geoTrans(4326, 3857, gcjLngLat[1], gcjLngLat[0]);
        logger.info(String.format("wgs2gcj: x: %.10f, y: %.10f", gcjXY[0], gcjXY[1]));

        // gcj2wgs
        double[] wgsLngLat = GcjUtils.gcj2wgs(lngLat[0], lngLat[1]);
        double[] wgsjXY = TransUtils.geoTrans(4326, 3857, wgsLngLat[1], wgsLngLat[0]);
        logger.info(String.format("gcj2wgs: x: %.10f, y: %.10f", wgsjXY[0], wgsjXY[1]));

        logger.info("complete");
    }

}
