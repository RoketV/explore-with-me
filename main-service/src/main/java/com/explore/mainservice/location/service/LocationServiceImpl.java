package com.explore.mainservice.location.service;

import com.explore.mainservice.exceptions.NotFoundException;
import com.explore.mainservice.location.dto.LocationDto;
import com.explore.mainservice.location.jpa.LocationPersistService;
import com.explore.mainservice.location.mapper.LocationMapper;
import com.explore.mainservice.location.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationMapper locationMapper;

    private final LocationPersistService locationPersistService;

    public LocationDto getLocationById(Long id) {

        Optional<Location> locationOpt = locationPersistService.findLocationById(id);
        if (locationOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Location with id = %s was not found", id));
        }

        return locationMapper.toLocationDto(locationOpt.get());
    }

    @Override
    public LocationDto save(LocationDto locationDto) {
        return locationMapper.toLocationDto(locationPersistService
                .save(locationMapper.toLocation(locationDto)));
    }
}

