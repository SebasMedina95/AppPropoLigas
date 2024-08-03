package org.sebastian.propoligas.sportsman.sportsman.repositories;

import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SportsManServiceRepository extends JpaRepository<SportsManEntity, Long>, JpaSpecificationExecutor<SportsManEntity> {

    @Query("SELECT sm FROM SportsManEntity sm JOIN sm.personsSportsManRelation psr WHERE psr.personId IN (:personIds)")
    List<SportsManEntity> findByPersonIds(@Param("personIds") List<Long> personIds);

}
