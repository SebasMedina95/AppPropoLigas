package org.sebastian.propoligas.sportsman.sportsman.services.impls;

import feign.FeignException;
import org.sebastian.propoligas.sportsman.sportsman.clients.PersonClientRest;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ApiResponse;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ResponseWrapper;
import org.sebastian.propoligas.sportsman.sportsman.models.Persons;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.CreateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.PersonsSportsManRelation;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.sebastian.propoligas.sportsman.sportsman.repositories.PersonsSportsManRelationRepository;
import org.sebastian.propoligas.sportsman.sportsman.repositories.SportsManServiceRepository;
import org.sebastian.propoligas.sportsman.sportsman.services.SportsManService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class SportsManServiceImpl implements SportsManService {

    static String dummiesUser = "usuario123";
    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();
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
    public ResponseWrapper<SportsManEntity> create(CreateSportsManDto sportsMan) {

        Persons personData;
        Long personIdMs = sportsMan.getPersonsSportsManRelationId();
        try{
            ApiResponse<Persons> personMsvc = personClientRest.getPerson(personIdMs);
            personData = personMsvc.getData();
            if( personData == null ){
                return new ResponseWrapper<>(null, "La persona para ser asociada al deportista no fue hallada");
            }
        }catch (FeignException fe){
            logger.error("Ocurrió un error al intentar obtener la persona del MS Persons, error: {}", fe);
            return new ResponseWrapper<>(null, "La persona para ser asociada al deportista no fue hallada");
        }

        //? Debemos revisar que el deportista no se halle registrado a nivel de persona, no puede estar más de una vez.
        Optional<PersonsSportsManRelation> optionalGetRelationalPerson = personsSportsManRelationRepository.getRelationalPerson(personIdMs);
        if(optionalGetRelationalPerson.isPresent())
            return new ResponseWrapper<>(null, "La persona ya se encuentra asociada como deportista");

        //? Generemos el carnet (Combinación de documento con 4 dígitos aleatorios)
        String generateCarnet = "CT-" + personData.getDocumentNumber() + "-" + this.generateRandomString();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(generateCarnet);
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        return null;

    }

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

}
