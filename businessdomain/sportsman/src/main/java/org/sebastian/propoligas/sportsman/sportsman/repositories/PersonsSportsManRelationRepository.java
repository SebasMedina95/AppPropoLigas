package org.sebastian.propoligas.sportsman.sportsman.repositories;

import org.sebastian.propoligas.sportsman.sportsman.models.entities.PersonsSportsManRelation;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonsSportsManRelationRepository extends JpaRepository<SportsManEntity, Long> {

    @Query("SELECT psr FROM PersonsSportsManRelation psr WHERE psr.personId = :personId")
    Optional<PersonsSportsManRelation> getRelationalPerson(@Param("personId") Long personId);

}
