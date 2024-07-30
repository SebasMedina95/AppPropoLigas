package org.sebastian.propoligas.sportsman.sportsman.services;

import org.sebastian.propoligas.sportsman.sportsman.common.utils.ResponseWrapper;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.CreateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;

public interface SportsManService {

    ResponseWrapper<SportsManEntity> create(CreateSportsManDto sportsMan);

}
