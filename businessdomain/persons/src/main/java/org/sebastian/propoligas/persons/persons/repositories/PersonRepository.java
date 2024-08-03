package org.sebastian.propoligas.persons.persons.repositories;

import org.sebastian.propoligas.persons.persons.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, Long>, JpaSpecificationExecutor<PersonEntity> {

    @Query("SELECT p FROM PersonEntity p WHERE UPPER(p.email) = UPPER(:email) OR p.documentNumber = :documentNumber")
    Optional<PersonEntity> getPersonByDocumentAndByEmail(@Param("email") String email, @Param("documentNumber") String documentNumber);

    @Query("SELECT p FROM PersonEntity p WHERE (UPPER(p.email) = UPPER(:personEmail) OR p.documentNumber = :personDocument) AND p.id <> :id")
    Optional<PersonEntity> getPersonByDocumentAndEmailForEdit(
            @Param("personEmail") String personEmail,
            @Param("personDocument") String personDocument,
            @Param("id") Long id);

    @Query("SELECT p.id FROM PersonEntity p " +
            "WHERE " +
            "(UPPER(p.documentType) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.documentNumber) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.firstName) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.secondName) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.firstSurname) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.secondSurname) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.email) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.gender) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.phone1) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.phone2) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.address) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.contactPerson) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.phoneContactPerson) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.description) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.civilStatus) LIKE UPPER(CONCAT('%', :search, '%')))")
    List<Long> findIdsByCriteria(@Param("search") String search);

}
