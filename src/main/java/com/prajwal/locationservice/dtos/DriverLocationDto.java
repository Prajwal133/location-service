package com.prajwal.locationservice.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  DriverLocationDto {
    String driverId;
    double longitude;
    double latitude;
}
