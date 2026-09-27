package com.prajwal.locationservice.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NearbyDriversRequestDto {
    double longitude;
    double  latitude;
}
