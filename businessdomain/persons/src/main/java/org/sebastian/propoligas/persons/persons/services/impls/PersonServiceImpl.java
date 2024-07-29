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
import org.springframework.data.jpa.domain.Specification;
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

        logger.info("Iniciando Acción - Creación de una persona");

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
            return new ResponseWrapper<>(personRepository.save(newPerson), "Persona guardada correctamente");


        }catch (Exception ex){

            logger.error("Ocurrió un error al intentar crear la persona, detalles ...", ex);
            return new ResponseWrapper<>(null, "La persona no pudo ser creada");

        }

    }

    @Override
    public Page<PersonEntity> findAll(String search, Pageable pageable) {

        logger.info("Iniciando Acción - Obtener todas las personas paginadas y con filtro");
        Specification<PersonEntity> spec = this.searchByFilter(search);

        logger.info("Listado de personas obtenida");
        return personRepository.findAll(spec, pageable);

    }

    @Override
    public ResponseWrapper<PersonEntity> findById(Long id) {

        logger.info("Iniciando Acción - Obtener una persona dado su ID");

        try{

            Optional<PersonEntity> personOptional = personRepository.findById(id);

            if( personOptional.isPresent() ){
                PersonEntity person = personOptional.orElseThrow();
                logger.info("Comodidad obtenida por su ID");
                return new ResponseWrapper<>(person, "Persona encontrada por ID correctamente");
            }

            logger.info("La comodidad no pudo ser encontrada cone el ID {}", id);
            return new ResponseWrapper<>(null, "La persona no pudo ser encontrado por el ID " + id);

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar obtener persona por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "La persona no pudo ser encontrado por el ID");

        }

    }

    @Override
    @Transactional
    public ResponseWrapper<PersonEntity> update(Long id, UpdatePersonDto person) {

        logger.info("Iniciando Acción - Actualizar una persona dado su ID");

        try{

            Optional<PersonEntity> personOptional = personRepository.findById(id);
            if( personOptional.isPresent() ){

                PersonEntity personDb = personOptional.orElseThrow();

                //? Validemos que no se repita la comodidad
                String personDocument = person.getDocumentNumber().trim();
                String personEmail = person.getDocumentNumber().trim().toUpperCase();
                Optional<PersonEntity> getPersonOptionalByDocumentAndEmail = personRepository.getPersonByDocumentAndEmailForEdit(personEmail, personDocument, id);

                if( getPersonOptionalByDocumentAndEmail.isPresent() ){
                    logger.info("La persona no se puede actualizar porque el email o documento ya está registrado");
                    return new ResponseWrapper<>(null, "El email o documento de la persona ya está registrado");
                }

                //? Vamos a actualizar si llegamos hasta acá
                personDb.setDocumentType(person.getDocumentType());
                personDb.setDocumentNumber(person.getDocumentNumber());
                personDb.setFirstName(person.getFirstName());
                personDb.setSecondName(person.getSecondName());
                personDb.setFirstSurname(person.getFirstSurname());
                personDb.setSecondSurname(person.getSecondSurname());
                personDb.setEmail(person.getEmail());
                personDb.setGender(person.getGender());
                personDb.setPhone1(person.getPhone1());
                personDb.setPhone2(person.getPhone2());
                personDb.setAddress(person.getAddress());
                personDb.setContactPerson(person.getContactPerson());
                personDb.setPhoneContactPerson(person.getPhoneContactPerson());
                personDb.setDescription(person.getDescription());
                personDb.setCivilStatus(person.getCivilStatus());
                personDb.setStatus(true); //* Por defecto entra en true
                personDb.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
                personDb.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

                logger.info("La persona fue actualizada correctamente");
                return new ResponseWrapper<>(personRepository.save(personDb), "Persona Actualizada Correctamente");

            }else{

                return new ResponseWrapper<>(null, "La persona no fue encontrada");

            }

        }catch (Exception err){

            logger.error("Ocurrió un error al intentar actualizar persona por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "La persona no pudo ser actualizada");

        }

    }

    @Override
    @Transactional
    public ResponseWrapper<PersonEntity> delete(Long id) {

        try{

            Optional<PersonEntity> personOptional = personRepository.findById(id);

            if( personOptional.isPresent() ){

                PersonEntity personDb = personOptional.orElseThrow();

                //? Vamos a actualizar si llegamos hasta acá
                //? ESTO SERÁ UN ELIMINADO LÓGICO!
                personDb.setStatus(false);
                personDb.setUserUpdated("usuario123");
                personDb.setDateUpdated(new Date());

                return new ResponseWrapper<>(personRepository.save(personDb), "Persona Eliminada Correctamente");

            }else{

                return new ResponseWrapper<>(null, "La persona no fue encontrado");

            }

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar eliminar lógicamente persona por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "La persona no pudo ser eliminada");

        }

    }

    //* Para el buscador de personas.
    //? Buscarémos tanto por name como por description.
    //? NOTA: No olvidar el status.
    public Specification<PersonEntity> searchByFilter(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.isTrue(root.get("status"));
            }

            String searchPatternWithUpper = "%" + search.toUpperCase() + "%"; //También podría ser toLowerCase o simplemente "%" + search + "%"

            return criteriaBuilder.and(
                    criteriaBuilder.isTrue(root.get("status")),
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("documentType")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("firstName")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("secondName")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("firstSurname")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("secondSurname")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("email")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("gender")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("address")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("contactPerson")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), searchPatternWithUpper),
                            criteriaBuilder.like(criteriaBuilder.upper(root.get("civilStatus")), searchPatternWithUpper),
                            // Campos numéricos como String
                            criteriaBuilder.like(root.get("documentNumber"), searchPatternWithUpper),
                            criteriaBuilder.like(root.get("phone1"), searchPatternWithUpper),
                            criteriaBuilder.like(root.get("phone2"), searchPatternWithUpper),
                            criteriaBuilder.like(root.get("phoneContactPerson"), searchPatternWithUpper)
                    )
            );
        };
    }
}
