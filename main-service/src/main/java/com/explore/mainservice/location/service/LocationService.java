package com.explore.mainservice.location.service;

import com.explore.mainservice.location.dto.LocationDto;

public interface LocationService {

    LocationDto getLocationById(Long id);

    LocationDto save(LocationDto locationDto);
}
