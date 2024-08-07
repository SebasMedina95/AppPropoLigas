package org.sebastian.propoligas.sportsman.sportsman.services.impls;

import feign.FeignException;
import org.sebastian.propoligas.sportsman.sportsman.clients.PersonClientRest;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ApiResponseConsolidation;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ResponseWrapper;
import org.sebastian.propoligas.sportsman.sportsman.models.Persons;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.create.CreateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.update.UpdateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.sebastian.propoligas.sportsman.sportsman.repositories.SportsManRepository;
import org.sebastian.propoligas.sportsman.sportsman.services.SportsManService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
public class SportsManServiceImpl implements SportsManService {

    static String dummiesUser = "usuario123";
    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Logger logger = LoggerFactory.getLogger(SportsManServiceImpl.class);

    private final SportsManRepository sportsManRepository;
    private final PersonClientRest personClientRest;

    @Autowired
    public SportsManServiceImpl(
            SportsManRepository sportsManRepository,
            PersonClientRest personClientRest
    ){
        this.sportsManRepository = sportsManRepository;
        this.personClientRest = personClientRest;
    }

    @Override
    @Transactional
    public ResponseWrapper<SportsManEntity> create(CreateSportsManDto sportsMan) {

        logger.info("Iniciando guardado de deportista");
        Persons personData;
        Long personIdMs = sportsMan.getPersonId();
        try{

            personData = this.getPersonOfMsPersons(personIdMs);
            if( personData == null ){
                logger.warn("Ocurrió algo en el servicio de MS Persons, persona no hallada o MS caído");
                return new ResponseWrapper<>(
                        null,
                        "La persona para ser asociada al deportista no fue hallada en la búsqueda"
                );
            }

        }catch (FeignException fe){
            logger.error("Ocurrió un error al intentar obtener la persona del MS Persons, error: ", fe);
            return new ResponseWrapper<>(
                    null, "La persona para ser asociada al deportista no fue hallada"
            );
        }

        //? Debemos revisar que el deportista no se halle registrado a nivel de persona, no puede estar más de una vez.
        Optional<SportsManEntity> optionalSportManByPersonId =
                sportsManRepository.getSportManByPersonId(personIdMs);

        if(optionalSportManByPersonId.isPresent()){
            logger.warn("La persona ya se encuentra registrada a nivel de deportista");
            return new ResponseWrapper<>(
                    null, "La persona ya se encuentra asociada como deportista");
        }

        //? Generemos el carnet (Combinación de documento con 4 dígitos aleatorios)
        String generateCarnet = "CT-" + personData.getDocumentNumber() + "-" + this.generateRandomString();

        //? Guardamos
        SportsManEntity newSportMan = new SportsManEntity();
        newSportMan.setPersonId(sportsMan.getPersonId());
        newSportMan.setCarnet(generateCarnet);
        newSportMan.setNumberShirt(sportsMan.getNumberShirt());
        newSportMan.setNameShirt(sportsMan.getNameShirt());
        newSportMan.setWeight(sportsMan.getWeight());
        newSportMan.setHeight(sportsMan.getHeight());
        newSportMan.setBloodType(sportsMan.getBloodType());
        newSportMan.setStartingPlayingPosition(sportsMan.getStartingPlayingPosition());
        newSportMan.setCaptain(sportsMan.getCaptain());
        newSportMan.setDescription(sportsMan.getDescription());
        newSportMan.setStatus(true); //* Por defecto entra en true
        newSportMan.setUserCreated(dummiesUser); //! Ajustar cuando se implemente Security
        newSportMan.setDateCreated(new Date()); //! Ajustar cuando se implemente Security
        newSportMan.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
        newSportMan.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

        //? Guardamos y devolvemos al deportista
        logger.info("Deportista guardado correctamente");
        return new ResponseWrapper<>(
                sportsManRepository.save(newSportMan),
                "El deportista fue creado correctamente"
        );

    }

    @Override
    @Transactional(readOnly = true)
    public Page<SportsManEntity> findAll(String search, Pageable pageable) {

        logger.info("Obtener todos los deportistas paginados y con filtro");

        // Si tenemos criterio de búsqueda entonces hacemos validaciones
        Page<SportsManEntity> sportsManPage;
        List<Long> personIds;
        if (search != null && !search.isEmpty()) {

            // Buscamos primero en el MS de personas para traer la información que coincida con el criterio
            logger.info("Obtener todos los deportistas - Con criterio de búsqueda en MS Persons y SportsMan");
            ApiResponseConsolidation<List<Long>> personIdsResponse = personClientRest.findPersonIdsByCriteria(search);
            personIds = personIdsResponse.getData();

            // Ahora realizamos la búsqueda en este MS tanto con los ID hallados desde MS de Personas como
            // con la posibilidad de que también haya un criterio adicional de coincidencia acá.
            logger.info("Obtener todos los deportistas - Aplicando la paginación luego de filtro");
            sportsManPage = sportsManRepository.findFilteredSportsMen(search, personIds, pageable);

        }else{

            // Si llegamos a este punto paginamos normal sin el buscador.
            logger.info("Obtener todos los deportistas - Sin criterio de búsqueda");
            sportsManPage = sportsManRepository.findNoFilteredSportsMen(pageable);

        }

        // Ajustamos los elementos hallados en el content para que aparezcan junto con la información
        // que viene desde el MS de personas.
        logger.info("Aplicamos contra llamado a MS Persons para adecuar response a Frontend");
        List<SportsManEntity> sportsManDtos = sportsManPage.getContent().stream()
                .map(sportsMan -> {
                    Long personId = sportsMan.getPersonId();
                    Persons person = getPersonOfMsPersons(personId);
                    sportsMan.setPerson(person);
                    return sportsMan;
                })
                .toList();

        // Retornamos los elementos con la paginación y filtro aplicado.
        logger.info("Listado de personas obtenido con toda la data requerida");
        return new PageImpl<>(sportsManDtos, pageable, sportsManPage.getTotalElements());

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseWrapper<SportsManEntity> findById(Long id) {

        logger.info("Iniciando Acción - Obtener un deportista dado su ID");

        try{

            Optional<SportsManEntity> sportsManOptional = sportsManRepository.findById(id);

            if( sportsManOptional.isPresent() ){
                SportsManEntity sportMan = sportsManOptional.orElseThrow();
                logger.info("Deportista obtenido por su ID");

                Long personIdMs = sportMan.getPersonId();
                Persons personData = this.getPersonOfMsPersons(personIdMs);
                sportMan.setPerson(personData);

                return new ResponseWrapper<>(sportMan, "Deportista encontrado por ID correctamente");

            }

            logger.info("El deportista no pudo ser encontrada cone el ID {}", id);
            return new ResponseWrapper<>(null, "El deportista no pudo ser encontrado por el ID " + id);

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar obtener deportista por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "El deportista no pudo ser encontrado por el ID");

        }

    }

    @Override
    @Transactional
    public ResponseWrapper<SportsManEntity> update(Long id, UpdateSportsManDto sportsMan) {

        logger.info("Iniciando Acción - Actualizar un deportista dado su ID");

        try{

            Optional<SportsManEntity> sportsManOptional = sportsManRepository.findById(id);
            if( sportsManOptional.isPresent() ){

                SportsManEntity sportsMantDb = sportsManOptional.orElseThrow();

                //? Validamos primero que la persona asociada esté activa en el MS Persons
                Long personIdMs = sportsMan.getPersonId();
                Persons personData = this.getPersonOfMsPersons(personIdMs);

                if( personData == null ){
                    logger.warn("Ocurrió algo en el servicio de MS Persons, persona no hallada o MS caído para actualización");
                    return new ResponseWrapper<>(
                            null,
                            "La persona para ser asociada al deportista no fue hallada"
                    );
                }

                //? Validamos que no se repita la persona a menos de que se trate del mismo.
                Long personId = sportsMan.getPersonId();
                Optional<SportsManEntity> optionalSportManPersonId = sportsManRepository.getSportsManByPersonForEdit(personId, id);

                if( optionalSportManPersonId.isPresent() ){
                    logger.info("El deportista no se puede actualizar porque la persona que intenta asociar ya está registrada como deportista");
                    return new ResponseWrapper<>(null, "La persona que está intentando asociar como deportista ya está registrado");
                }

                //? Vamos a actualizar si llegamos hasta acá
                sportsMantDb.setPersonId(sportsMan.getPersonId());
                sportsMantDb.setNumberShirt(sportsMan.getNumberShirt());
                sportsMantDb.setNameShirt(sportsMan.getNameShirt());
                sportsMantDb.setWeight(sportsMan.getWeight());
                sportsMantDb.setHeight(sportsMan.getHeight());
                sportsMantDb.setBloodType(sportsMan.getBloodType());
                sportsMantDb.setStartingPlayingPosition(sportsMan.getStartingPlayingPosition());
                sportsMantDb.setCaptain(sportsMan.getCaptain());
                sportsMantDb.setDescription(sportsMan.getDescription());
                sportsMantDb.setStatus(true); //* Por defecto entra en true
                sportsMantDb.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
                sportsMantDb.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

                logger.info("El deportista fue actualizado correctamente");
                return new ResponseWrapper<>(sportsManRepository.save(sportsMantDb), "Deportista Actualizado Correctamente");

            }else{

                logger.warn("El deportista por el ID no fue encontrado");
                return new ResponseWrapper<>(null, "El deportista no fue encontrado");

            }

        }catch (Exception err){

            logger.error("Ocurrió un error al intentar actualizar deportista por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "El deportista no pudo ser actualizado");

        }

    }

    @Override
    public ResponseWrapper<SportsManEntity> delete(Long id) {

        try{

            Optional<SportsManEntity> sportsManOptional = sportsManRepository.findById(id);

            if( sportsManOptional.isPresent() ){

                SportsManEntity sportManDb = sportsManOptional.orElseThrow();

                //? Vamos a actualizar si llegamos hasta acá
                //? ESTO SERÁ UN ELIMINADO LÓGICO!
                sportManDb.setStatus(false);
                sportManDb.setUserUpdated("usuario123");
                sportManDb.setDateUpdated(new Date());

                return new ResponseWrapper<>(sportsManRepository.save(sportManDb), "Deportista Eliminado Correctamente");

            }else{

                return new ResponseWrapper<>(null, "El deportista no fue encontrado");

            }

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar eliminar lógicamente deportista por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "El deportista no pudo ser eliminada");

        }

    }

    //? *********************
    //? MÉTODOS ADICIONALES.
    //? *********************
    //Generar elemento random para construcción de carnet.
    private String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder();

        // Generar 4 dígitos aleatorios
        for (int i = 0; i < 4; i++) {
            int digitIndex = RANDOM.nextInt(DIGITS.length());
            stringBuilder.append(DIGITS.charAt(digitIndex));
        }

        // Generar 2 letras aleatorias
        for (int i = 0; i < 2; i++) {
            int letterIndex = RANDOM.nextInt(LETTERS.length());
            stringBuilder.append(LETTERS.charAt(letterIndex));
        }

        return stringBuilder.toString();
    }

    //Centralizamos el obtener Person por ID para reusarlo.
    private Persons getPersonOfMsPersons(Long personId){

        try{

            //? Ahora hallemos la persona en su MS
            Persons personData;
            ApiResponseConsolidation<Persons> personMsvc =
                    personClientRest.getPerson(personId);
            personData = personMsvc.getData();

            return personData;

        }catch (FeignException fe){
            logger.error("No pudimos obtener la persona del MS Persons, error: ", fe);
            return null;
        }

    }

}
