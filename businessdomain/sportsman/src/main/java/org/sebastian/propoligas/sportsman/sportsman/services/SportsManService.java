package org.sebastian.propoligas.sportsman.sportsman.services;

import org.sebastian.propoligas.sportsman.sportsman.common.utils.ResponseWrapper;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.create.CreateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.other.ListSportsManEntity;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface SportsManService {

    ResponseWrapper<SportsManEntity> create(CreateSportsManDto sportsMan);
    Map<String, Object> findAll(Integer page, Integer size, String search, String order, String sort);
    ResponseWrapper<SportsManEntity> findById(Long id);

}
