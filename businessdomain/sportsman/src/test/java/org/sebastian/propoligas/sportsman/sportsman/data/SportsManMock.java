package org.sebastian.propoligas.sportsman.sportsman.data;

import org.sebastian.propoligas.sportsman.sportsman.models.Persons;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.create.CreateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.update.UpdateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SportsManMock {

    public static SportsManEntity createSportsManEntity() {
        return SportsManEntity.builder()
                .id(1L)
                .personId(12345L)
                .carnet("CT-12345-ABCD")
                .numberShirt("10")
                .nameShirt("JohnDoe")
                .weight(75.0f)
                .height(1.80f)
                .bloodType("O+")
                .startingPlayingPosition("Forward")
                .captain(true)
                .description("Test description")
                .status(true)
                .userCreated("testUser")
                .dateCreated(new Date())
                .userUpdated("testUser")
                .dateUpdated(new Date())
                .build();
    }

    public static CreateSportsManDto createSportsManDto() {
        return CreateSportsManDto.builder()
                .personId(12345L)
                .numberShirt("10")
                .nameShirt("JohnDoe")
                .weight(75.0f)
                .height(1.80f)
                .bloodType("O+")
                .startingPlayingPosition("Forward")
                .captain(true)
                .description("Test description")
                .build();
    }

    public static Persons createPersonDto() {
        return Persons.builder()
                .id(12345L)
                .documentType("DNI")
                .documentNumber("12345678")
                .firstName("John")
                .secondName("A.")
                .firstSurname("Doe")
                .secondSurname("Smith")
                .email("john.doe@example.com")
                .gender("Male")
                .phone1("123456789")
                .phone2("987654321")
                .address("123 Main St")
                .contactPerson("Jane Doe")
                .phoneContactPerson("123456789")
                .description("Test description")
                .civilStatus("Single")
                .status(true)
                .userCreated("testUser")
                .dateCreated(new Date())
                .userUpdated("testUser")
                .dateUpdated(new Date())
                .build();
    }

    public static Persons createPersonDto(Long personId) {
        return Persons.builder()
                .id(personId)
                .documentType("DNI")
                .documentNumber("12345678")
                .firstName("John")
                .secondName("A.")
                .firstSurname("Doe")
                .secondSurname("Smith")
                .email("john.doe@example.com")
                .gender("Male")
                .phone1("123456789")
                .phone2("987654321")
                .address("123 Main St")
                .contactPerson("Jane Doe")
                .phoneContactPerson("123456789")
                .description("Test description")
                .civilStatus("Single")
                .status(true)
                .userCreated("testUser")
                .dateCreated(new Date())
                .userUpdated("testUser")
                .dateUpdated(new Date())
                .build();
    }

    public static List<SportsManEntity> createSportsManEntities() {
        List<SportsManEntity> sportsManEntities = new ArrayList<>();

        sportsManEntities.add(SportsManEntity.builder()
                .id(1L)
                .personId(12345L)
                .carnet("CT-12345-ABCD")
                .numberShirt("10")
                .nameShirt("JohnDoe")
                .weight(75.0f)
                .height(1.80f)
                .bloodType("O+")
                .startingPlayingPosition("Forward")
                .captain(true)
                .description("Test description 1")
                .status(true)
                .userCreated("testUser1")
                .dateCreated(new Date())
                .userUpdated("testUser1")
                .dateUpdated(new Date())
                .build());

        sportsManEntities.add(SportsManEntity.builder()
                .id(2L)
                .personId(67890L)
                .carnet("CT-67890-WXYZ")
                .numberShirt("7")
                .nameShirt("JaneDoe")
                .weight(65.0f)
                .height(1.70f)
                .bloodType("A+")
                .startingPlayingPosition("Midfielder")
                .captain(false)
                .description("Test description 2")
                .status(true)
                .userCreated("testUser2")
                .dateCreated(new Date())
                .userUpdated("testUser2")
                .dateUpdated(new Date())
                .build());

        // Agrega más datos si es necesario...

        return sportsManEntities;
    }

    public static UpdateSportsManDto createUpdateSportsManDto() {
        return UpdateSportsManDto.builder()
                .personId(12345L) // ID de la persona que se va a asociar al deportista
                .numberShirt("10") // Número de la camiseta
                .nameShirt("Sportsman Shirt") // Nombre de la camiseta
                .weight(80.0F) // Peso del deportista
                .height(1.85F) // Altura del deportista
                .bloodType("O+") // Tipo de sangre
                .startingPlayingPosition("Forward") // Posición de inicio del juego
                .captain(true) // ¿Es capitán?
                .description("Experienced forward") // Descripción del deportista
                .build();
    }

}
