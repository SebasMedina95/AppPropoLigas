package org.sebastian.propoligas.persons.persons.services;

import org.sebastian.propoligas.persons.persons.common.utils.ResponseWrapper;
import org.sebastian.propoligas.persons.persons.entities.PersonEntity;
import org.sebastian.propoligas.persons.persons.entities.dtos.create.CreatePersonDto;
import org.sebastian.propoligas.persons.persons.entities.dtos.update.UpdatePersonDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {

    ResponseWrapper<PersonEntity> create(CreatePersonDto person);
    Page<PersonEntity> findAll(String search, Pageable pageable);
    ResponseWrapper<PersonEntity> findById(Long id);
    ResponseWrapper<PersonEntity> update(Long id, UpdatePersonDto person);
    ResponseWrapper<PersonEntity> delete(Long id);
    List<Long> findPersonIdsByCriteria(String search);

}
