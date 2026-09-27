package com.prajwal.locationservice.controllers;

import com.prajwal.locationservice.dtos.DriverLocationDto;
import com.prajwal.locationservice.dtos.NearbyDriversRequestDto;
import com.prajwal.locationservice.dtos.SaveDriverLocationRequestDto;
import com.prajwal.locationservice.services.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/drivers")
    public ResponseEntity<Boolean> saveDriverLocation(@RequestBody SaveDriverLocationRequestDto dto) {
        boolean saved = locationService.saveDriverLocation(dto);

        if (saved) {
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(saved, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/nearby/drivers")
    public ResponseEntity<List<DriverLocationDto>> getNearByDrivers(
            @RequestParam double longitude,
            @RequestParam double latitude) {

        NearbyDriversRequestDto dto = new NearbyDriversRequestDto(longitude, latitude);
        System.out.println("Received search point: lon=" + dto.getLongitude() + ", lat=" + dto.getLatitude());
        List<DriverLocationDto> result = locationService.getNearByDrivers(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
