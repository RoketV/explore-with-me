package com.explore.mainservice.location.jpa;

import com.explore.mainservice.location.model.Location;

import java.util.Optional;

public interface LocationPersistService {
    Optional<Location> findLocationById(Long id);

    Location save(Location location);
}
