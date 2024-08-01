package org.sebastian.propoligas.sportsman.sportsman.services.impls;

import feign.FeignException;
import org.sebastian.propoligas.sportsman.sportsman.clients.PersonClientRest;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ApiResponse;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ResponseWrapper;
import org.sebastian.propoligas.sportsman.sportsman.models.Persons;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.create.CreateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.other.ListSportsManEntity;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.PersonsSportsManRelation;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.sebastian.propoligas.sportsman.sportsman.repositories.PersonsSportsManRelationRepository;
import org.sebastian.propoligas.sportsman.sportsman.repositories.SportsManServiceRepository;
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
    private static final String MICRO_API = "/api/sportsman/find-all";
    private static final String MICRO_API_PAGE = "?page=";
    private static final String MICRO_API_SIZE = "&size=";
    private static final Logger logger = LoggerFactory.getLogger(SportsManServiceImpl.class);

    private final SportsManServiceRepository sportsManServiceRepository;
    private final PersonsSportsManRelationRepository personsSportsManRelationRepository;
    private final PersonClientRest personClientRest;

    @Autowired
    public SportsManServiceImpl(
            SportsManServiceRepository sportsManServiceRepository,
            PersonsSportsManRelationRepository personsSportsManRelationRepository,
            PersonClientRest personClientRest
    ){
        this.sportsManServiceRepository = sportsManServiceRepository;
        this.personsSportsManRelationRepository = personsSportsManRelationRepository;
        this.personClientRest = personClientRest;
    }

    @Override
    @Transactional
    public ResponseWrapper<SportsManEntity> create(CreateSportsManDto sportsMan) {

        logger.info("Iniciando guardado de deportista");
        Persons personData;
        Long personIdMs = sportsMan.getPersonsSportsManRelationId();
        try{

            personData = this.getPersonOfMsPersons(personIdMs);
            if( personData == null ){
                return new ResponseWrapper<>(
                        null,
                        "La persona para ser asociada al deportista no fue hallada"
                );
            }

        }catch (FeignException fe){
            logger.error("Ocurrió un error al intentar obtener la persona del MS Persons, error: ", fe);
            return new ResponseWrapper<>(
                    null, "La persona para ser asociada al deportista no fue hallada"
            );
        }

        //? Debemos revisar que el deportista no se halle registrado a nivel de persona, no puede estar más de una vez.
        Optional<PersonsSportsManRelation> optionalGetRelationalPerson =
                personsSportsManRelationRepository.getRelationalPerson(personIdMs);

        if(optionalGetRelationalPerson.isPresent()){
            logger.error("La persona ya se encuentra registrada a nivel de deportista");
            return new ResponseWrapper<>(
                    null, "La persona ya se encuentra asociada como deportista");
        }

        //? Generemos el carnet (Combinación de documento con 4 dígitos aleatorios)
        String generateCarnet = "CT-" + personData.getDocumentNumber() + "-" + this.generateRandomString();

        //? ******************************
        //? Pendiente el tema de la imagen
        //? ******************************

        //? Guardamos primero la relación
        PersonsSportsManRelation newPersonsSportsManRelation = new PersonsSportsManRelation();
        newPersonsSportsManRelation.setPersonId(personData.getId());
        PersonsSportsManRelation relationPerson =
                personsSportsManRelationRepository.save(newPersonsSportsManRelation);

        SportsManEntity newSportMan = new SportsManEntity();
        newSportMan.setCarnet(generateCarnet);
        newSportMan.setNumberShirt(sportsMan.getNumberShirt());
        newSportMan.setWeight(sportsMan.getWeight());
        newSportMan.setHeight(sportsMan.getHeight());
        newSportMan.setBloodType(sportsMan.getBloodType());
        newSportMan.setStartingPlayingPosition(sportsMan.getStartingPlayingPosition());
        newSportMan.setCaptain(sportsMan.getCaptain());
        newSportMan.setPhotoUrl("default.png");
        newSportMan.setDescription(sportsMan.getDescription());
        newSportMan.setPersonsSportsManRelation(relationPerson);
        newSportMan.setStatus(true); //* Por defecto entra en true
        newSportMan.setUserCreated(dummiesUser); //! Ajustar cuando se implemente Security
        newSportMan.setDateCreated(new Date()); //! Ajustar cuando se implemente Security
        newSportMan.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
        newSportMan.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

        //? Guardamos y devolvemos al deportista
        logger.info("Deportista guardado correctamente");
        return new ResponseWrapper<>(
                sportsManServiceRepository.save(newSportMan),
                "El deportista fue creado correctamente"
        );

    }

    //? Este método lo planteamos así porque vamos a presentar en paralelo información de otro MS combinado.
    //? La idea es mostrar al deportista con la información de la persona relacionada.
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findAll(Integer page, Integer size, String search, String order, String sort) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(order), sort));
        List<SportsManEntity> sportsMans = sportsManServiceRepository.findAll();

        List<ListSportsManEntity> filteredList = sportsMans.stream()
                .map(this::toDTO)
                .filter(dto -> search == null || search.isEmpty() || matchesSearch(dto, search))
                .toList();

        int totalElements = filteredList.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Verificar si la página solicitada está dentro del rango válido
        if (page < 1 || page > totalPages)
            return Collections.emptyMap();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredList.size());
        List<ListSportsManEntity> paginatedList = filteredList.subList(start, end);

        //Conservemos la misma mecánica como si la paginación fuera de tipo JPA/Hibernate
        Map<String, Object> response = new HashMap<>();
        response.put("content", paginatedList);
        response.put(
                "page",
                Map.of(
                "size", size,
                "totalElements", filteredList.size(),
                "totalPages", (int) Math.ceil((double) filteredList.size() / size),
                "number", page
        ));
        response.put("links", createLinks(page, size, filteredList.size()));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseWrapper<SportsManEntity> findById(Long id) {

        logger.info("Iniciando Acción - Obtener un deportista dado su ID");

        try{

            Optional<SportsManEntity> sportsManOptional = sportsManServiceRepository.findById(id);

            if( sportsManOptional.isPresent() ){
                SportsManEntity sportMan = sportsManOptional.orElseThrow();
                logger.info("Deportista obtenido por su ID");

                Persons getPersonMS = this.getPersonOfMsPersons(sportMan.getPersonsSportsManRelation().getPersonId());
                sportMan.setPerson(getPersonMS);

                return new ResponseWrapper<>(sportMan, "Deportista encontrado por ID correctamente");

            }

            logger.info("El deportista no pudo ser encontrada cone el ID {}", id);
            return new ResponseWrapper<>(null, "El deportista no pudo ser encontrado por el ID " + id);

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar obtener deportista por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "El deportista no pudo ser encontrado por el ID");

        }

    }

    // MÉTODOS ADICIONALES.
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
            ApiResponse<Persons> personMsvc =
                    personClientRest.getPerson(personId);
            personData = personMsvc.getData();

            return personData;

        }catch (FeignException fe){
            logger.error("No pudimos obtener la persona del MS Persons, error: ", fe);
            return null;
        }

    }

    private ListSportsManEntity toDTO(SportsManEntity sportsMan) {
        Persons person = getPersonOfMsPersons(sportsMan.getPersonsSportsManRelation().getPersonId());
        if (person == null) {
            // Manejar el caso donde no se pudo obtener la información de la persona
            logger.warn("No se pudo obtener la información de la persona con ID: {}",
                    sportsMan.getPersonsSportsManRelation().getPersonId());
            return null;
        }
        return ListSportsManEntity.builder()
                .id(sportsMan.getId())
                .carnet(sportsMan.getCarnet())
                .numberShirt(sportsMan.getNumberShirt())
                .weight(sportsMan.getWeight())
                .height(sportsMan.getHeight())
                .bloodType(sportsMan.getBloodType())
                .startingPlayingPosition(sportsMan.getStartingPlayingPosition())
                .captain(sportsMan.getCaptain())
                .photoUrl(sportsMan.getPhotoUrl())
                .description(sportsMan.getDescription())
                .status(sportsMan.getStatus())
                .userCreated(sportsMan.getUserCreated())
                .dateCreated(sportsMan.getDateCreated())
                .userUpdated(sportsMan.getUserUpdated())
                .dateUpdated(sportsMan.getDateUpdated())
                .firstName(person.getFirstName())
                .secondName(person.getSecondName())
                .firstSurname(person.getFirstSurname())
                .secondSurname(person.getSecondSurname())
                .documentNumber(person.getDocumentNumber())
                .email(person.getEmail())
                .build();
    }

    private boolean matchesSearch(ListSportsManEntity dto, String search) {
        String lowerCaseSearch = search.toLowerCase();
        return  dto.getCarnet().toLowerCase().contains(lowerCaseSearch) ||
                dto.getNumberShirt().toLowerCase().contains(lowerCaseSearch) ||
                dto.getDescription().toLowerCase().contains(lowerCaseSearch) ||
                dto.getStartingPlayingPosition().toLowerCase().contains(lowerCaseSearch) ||
                dto.getFirstName().toLowerCase().contains(lowerCaseSearch) ||
                dto.getSecondName().toLowerCase().contains(lowerCaseSearch) ||
                dto.getFirstSurname().toLowerCase().contains(lowerCaseSearch) ||
                dto.getSecondSurname().toLowerCase().contains(lowerCaseSearch) ||
                dto.getDocumentNumber().toLowerCase().contains(lowerCaseSearch) ||
                dto.getEmail().toLowerCase().contains(lowerCaseSearch);
    }

    private List<Map<String, String>> createLinks(int page, int size, int totalElements) {
        List<Map<String, String>> links = new ArrayList<>();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        links.add(Map.of("rel", "self" , "href", MICRO_API + MICRO_API_PAGE + page + MICRO_API_SIZE + size));
        links.add(Map.of("rel", "first", "href", MICRO_API + MICRO_API_PAGE + 1 + MICRO_API_SIZE + size));
        links.add(Map.of("rel", "last" , "href", MICRO_API + MICRO_API_PAGE + totalPages + MICRO_API_SIZE + size));

        if (page > 1)
            links.add(Map.of("rel", "prev", "href", MICRO_API + MICRO_API_PAGE + (page - 1) + MICRO_API_SIZE + size));

        if (page < totalPages)
            links.add(Map.of("rel", "next", "href", MICRO_API + MICRO_API_PAGE + (page + 1) + MICRO_API_SIZE + size));

        return links;

    }

}
