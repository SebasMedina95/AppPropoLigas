package org.sebastian.propoligas.sportsman.sportsman.repositories;

import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SportsManServiceRepository extends JpaRepository<SportsManEntity, Long>, JpaSpecificationExecutor<SportsManEntity> {
}
