package me.kongyl.gcjjava.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import me.kongyl.gcjjava.util.GcjUtils;

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

        logger.info("complete");
    }

}
