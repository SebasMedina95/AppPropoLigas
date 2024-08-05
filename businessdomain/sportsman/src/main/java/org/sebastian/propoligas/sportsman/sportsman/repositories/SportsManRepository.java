package org.sebastian.propoligas.sportsman.sportsman.repositories;

import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SportsManRepository extends JpaRepository<SportsManEntity, Long>, JpaSpecificationExecutor<SportsManEntity> {

    @Query("SELECT sm FROM SportsManEntity sm WHERE sm.personId IN (:personIds)")
    List<SportsManEntity> findByPersonIds(@Param("personIds") List<Long> personIds);

    @Query("SELECT sm FROM SportsManEntity sm WHERE sm.personId = :personId")
    Optional<SportsManEntity> getSportManByPersonId(@Param("personId") Long personId);

    @Query("SELECT s FROM SportsManEntity s " +
            "WHERE (:search IS NULL OR :search = '' OR " +
            "LOWER(s.carnet) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.numberShirt) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.nameShirt) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.startingPlayingPosition) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "OR (:personIds IS NULL OR s.personId IN :personIds)")
    Page<SportsManEntity> findFilteredSportsMen(@Param("search") String search,
                                                @Param("personIds") List<Long> personIds,
                                                Pageable pageable);

    @Query("SELECT s FROM SportsManEntity s WHERE s.status = true")
    Page<SportsManEntity> findNoFilteredSportsMen(Pageable pageable);

}
