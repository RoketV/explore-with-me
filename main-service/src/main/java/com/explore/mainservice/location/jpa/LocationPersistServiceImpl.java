package com.explore.mainservice.location.jpa;

import com.explore.mainservice.location.model.Location;
import com.explore.mainservice.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationPersistServiceImpl implements LocationPersistService {

    private final LocationRepository repository;

    public Optional<Location> findLocationById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Location save(Location location) {
        return repository.save(location);
    }
}
