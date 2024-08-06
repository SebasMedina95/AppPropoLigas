package org.sebastian.propoligas.persons.persons.data;

import org.sebastian.propoligas.persons.persons.entities.PersonEntity;
import org.sebastian.propoligas.persons.persons.entities.dtos.create.CreatePersonDto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PersonMock {

    public static CreatePersonDto createValidPersonDto() {
        return CreatePersonDto.builder()
                .documentType("CC")
                .documentNumber("123456789")
                .firstName("John")
                .secondName("Michael")
                .firstSurname("Doe")
                .secondSurname("Smith")
                .email("john.doe@example.com")
                .gender("M")
                .phone1("1234567890")
                .phone2("0987654321")
                .address("123 Main St")
                .contactPerson("Jane Doe")
                .phoneContactPerson("1234567890")
                .description("Test description")
                .civilStatus("Single")
                .build();
    }

    public static PersonEntity createValidPersonEntity() {
        return PersonEntity.builder()
                .id(1L)
                .documentType("CC")
                .documentNumber("123456789")
                .firstName("John")
                .secondName("Michael")
                .firstSurname("Doe")
                .secondSurname("Smith")
                .email("john.doe@example.com")
                .gender("M")
                .phone1("1234567890")
                .phone2("0987654321")
                .address("123 Main St")
                .contactPerson("Jane Doe")
                .phoneContactPerson("1234567890")
                .description("Test description")
                .civilStatus("Single")
                .status(true)
                .userCreated("testUser")
                .dateCreated(new Date())
                .userUpdated("testUser")
                .dateUpdated(new Date())
                .build();
    }

    public static List<PersonEntity> createMockPersonsList() {
        return Arrays.asList(
                PersonEntity.builder()
                        .id(1L)
                        .documentType("CC")
                        .documentNumber("123456")
                        .firstName("John")
                        .firstSurname("Doe")
                        .email("john.doe@example.com")
                        .gender("M")
                        .phone1("123456789")
                        .address("123 Main St")
                        .civilStatus("Single")
                        .status(true)
                        .build(),
                PersonEntity.builder()
                        .id(2L)
                        .documentType("CC")
                        .documentNumber("789012")
                        .firstName("Jane")
                        .firstSurname("Doe")
                        .email("jane.doe@example.com")
                        .gender("F")
                        .phone1("987654321")
                        .address("456 Main St")
                        .civilStatus("Married")
                        .status(true)
                        .build()
                // Añadir más registros si es necesario
        );
    }

}
