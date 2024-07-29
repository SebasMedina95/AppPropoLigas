package org.sebastian.propoligas.persons.persons.services.impls;


import org.sebastian.propoligas.persons.persons.common.utils.ResponseWrapper;
import org.sebastian.propoligas.persons.persons.entities.PersonEntity;
import org.sebastian.propoligas.persons.persons.entities.dtos.create.CreatePersonDto;
import org.sebastian.propoligas.persons.persons.entities.dtos.update.UpdatePersonDto;
import org.sebastian.propoligas.persons.persons.repositories.PersonRepository;
import org.sebastian.propoligas.persons.persons.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    static String dummiesUser = "usuario123";
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(
            PersonRepository personRepository
    ){
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public ResponseWrapper<PersonEntity> create(CreatePersonDto person) {

        logger.info("Iniciando Acción - Creación de una comodidad");

        try {

            //? Validemos que no se repita la persona
            String personDocument = person.getDocumentNumber().trim().toUpperCase();
            String personEmail = person.getEmail().trim().toUpperCase();
            Optional<PersonEntity> getPersonOptional = personRepository.getPersonByDocumentAndByEmail(personEmail, personDocument);
            if( getPersonOptional.isPresent() )
                return new ResponseWrapper<>(null, "El documento de la persona o su email ya está registrado");

            //? Pasamos hasta acá, registramos persona
            PersonEntity newPerson = new PersonEntity();
            newPerson.setDocumentType(person.getDocumentType());
            newPerson.setDocumentNumber(person.getDocumentNumber());
            newPerson.setFirstName(person.getFirstName());
            newPerson.setSecondName(person.getSecondName());
            newPerson.setFirstSurname(person.getFirstSurname());
            newPerson.setSecondSurname(person.getSecondSurname());
            newPerson.setEmail(person.getEmail());
            newPerson.setGender(person.getGender());
            newPerson.setPhone1(person.getPhone1());
            newPerson.setPhone2(person.getPhone2());
            newPerson.setAddress(person.getAddress());
            newPerson.setContactPerson(person.getContactPerson());
            newPerson.setPhoneContactPerson(person.getPhoneContactPerson());
            newPerson.setDescription(person.getDescription());
            newPerson.setCivilStatus(person.getCivilStatus());
            newPerson.setStatus(true); //* Por defecto entra en true
            newPerson.setUserCreated(dummiesUser); //! Ajustar cuando se implemente Security
            newPerson.setDateCreated(new Date()); //! Ajustar cuando se implemente Security
            newPerson.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
            newPerson.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

            logger.info("Persona creada correctamente");
            return new ResponseWrapper<>(personRepository.save(newPerson), "Comfort guardado correctamente");


        }catch (Exception ex){

            logger.error("Ocurrió un error al intentar crear la persona, detalles ...", ex);
            return new ResponseWrapper<>(null, "La persona no pudo ser creada");

        }

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
    @Transactional
    public ResponseWrapper<PersonEntity> update(Long id, UpdatePersonDto person) {
        return null;
    }

    @Override
    @Transactional
    public ResponseWrapper<PersonEntity> delete(Long id) {
        return null;
    }
}
