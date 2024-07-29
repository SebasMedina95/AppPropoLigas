package org.sebastian.propoligas.persons.persons.repositories;

import org.sebastian.propoligas.persons.persons.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, Long>, JpaSpecificationExecutor<PersonEntity> {

    @Query("SELECT p FROM PersonEntity p WHERE UPPER(p.email) = UPPER(:email) OR p.documentNumber = :documentNumber")
    Optional<PersonEntity> getPersonByDocumentAndByEmail(@Param("email") String email, @Param("documentNumber") String documentNumber);

    @Query("SELECT p FROM PersonEntity p WHERE (UPPER(p.email) = UPPER(:personEmail) OR p.documentNumber = :personDocument) AND p.id <> :id")
    Optional<PersonEntity> getPersonByDocumentAndEmailForEdit(
            @Param("personEmail") String personEmail,
            @Param("personDocument") String personDocument,
            @Param("id") Long id);

}
