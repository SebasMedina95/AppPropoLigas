package org.sebastian.propoligas.sportsman.sportsman.services;

import org.sebastian.propoligas.sportsman.sportsman.common.utils.ResponseWrapper;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.create.CreateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SportsManService {

    ResponseWrapper<SportsManEntity> create(CreateSportsManDto sportsMan);
    Page<SportsManEntity> findAll(String search, Pageable pageable);
    ResponseWrapper<SportsManEntity> findById(Long id);

}
