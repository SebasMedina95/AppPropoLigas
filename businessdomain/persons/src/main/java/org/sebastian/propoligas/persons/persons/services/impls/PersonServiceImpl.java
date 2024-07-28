package org.sebastian.propoligas.persons.persons.services.impls;


import org.sebastian.propoligas.persons.persons.common.utils.ResponseWrapper;
import org.sebastian.propoligas.persons.persons.entities.PersonEntity;
import org.sebastian.propoligas.persons.persons.entities.dtos.create.CreatePersonDto;
import org.sebastian.propoligas.persons.persons.entities.dtos.update.UpdatePersonDto;
import org.sebastian.propoligas.persons.persons.services.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Override
    public ResponseWrapper<PersonEntity> create(CreatePersonDto person) {
        return null;
    }

    @Override
    public Page<PersonEntity> findAll(String search, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseWrapper<PersonEntity> findById(Long id) {
        return null;
    }

    @Override
    public ResponseWrapper<PersonEntity> update(Long id, UpdatePersonDto person) {
        return null;
    }

    @Override
    public ResponseWrapper<PersonEntity> delete(Long id) {
        return null;
    }
}
