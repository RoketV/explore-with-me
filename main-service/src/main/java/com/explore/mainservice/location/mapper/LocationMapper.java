package com.explore.mainservice.location.mapper;

import com.explore.mainservice.location.dto.LocationDto;
import com.explore.mainservice.location.model.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto toLocationDto(Location entity);

    Location toLocation(LocationDto dto);
}
