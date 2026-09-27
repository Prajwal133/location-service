package com.prajwal.locationservice.services;

import com.prajwal.locationservice.dtos.DriverLocationDto;
import com.prajwal.locationservice.dtos.NearbyDriversRequestDto;
import com.prajwal.locationservice.dtos.SaveDriverLocationRequestDto;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    private final StringRedisTemplate stringRedisTemplate;


    private static final String DRIVER_GEO_OPS_KEY = "drivers";

    private static final Double SEARCH_RADIUS = 500.0;

    public LocationService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    public Boolean saveDriverLocation(SaveDriverLocationRequestDto saveDriverLocationRequestDto) {

        try {
            GeoOperations<String, String> geoOperations = stringRedisTemplate.opsForGeo();
            geoOperations.add(DRIVER_GEO_OPS_KEY,
                    new Point(
                            saveDriverLocationRequestDto.getLongitude(),
                            saveDriverLocationRequestDto.getLatitude()
                    ),
                    saveDriverLocationRequestDto.getDriverId()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<DriverLocationDto> getNearByDrivers(NearbyDriversRequestDto nearByDriversRequestDto) {
        try {
            GeoOperations<String, String> geoOperations = stringRedisTemplate.opsForGeo();
            Distance radius = new Distance(SEARCH_RADIUS, Metrics.KILOMETERS);
            Circle within = new Circle(
                    new Point(
                            // Y is actually latitude! ,  // X is actually longitude!
                            nearByDriversRequestDto.getLongitude(),
                            nearByDriversRequestDto.getLatitude()

                    ),
                    radius
            );

            GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOperations.
                    radius(
                            DRIVER_GEO_OPS_KEY,
                            within,
                            RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates().sortAscending()
                    );
            System.out.println("Raw geo results size: " + (results != null ? results.getContent().size() : "null"));
            List<DriverLocationDto> drivers = new ArrayList<>();
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {

                DriverLocationDto driverLocation = DriverLocationDto.builder()
                        .driverId(result.getContent().getName())
                        .latitude(result.getContent().getPoint().getY())
                        .longitude(result.getContent().getPoint().getX())
                        .build();
                drivers.add(driverLocation);
            }

            System.out.println("Drivers list size before return: " + drivers.size());
            return drivers;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
