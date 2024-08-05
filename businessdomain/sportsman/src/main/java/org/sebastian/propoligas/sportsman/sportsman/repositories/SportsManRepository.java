package org.sebastian.propoligas.sportsman.sportsman.repositories;

import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SportsManRepository extends JpaRepository<SportsManEntity, Long>, JpaSpecificationExecutor<SportsManEntity> {

//    @Query("SELECT sm FROM SportsManEntity sm JOIN sm.personsSportsManRelation psr WHERE psr.personId IN (:personIds)")
//    List<SportsManEntity> findByPersonIds(@Param("personIds") List<Long> personIds);

    @Query("SELECT sm FROM SportsManEntity sm WHERE sm.personId = :personId")
    Optional<SportsManEntity> getSportManByPersonId(@Param("personId") Long personId);

}
