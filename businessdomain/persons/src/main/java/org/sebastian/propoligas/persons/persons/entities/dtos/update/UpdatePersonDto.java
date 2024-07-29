package org.sebastian.propoligas.persons.persons.entities.dtos.update;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePersonDto {

    @NotEmpty(message = "El tipo de documento de la persona es requerido")
    @Size(min = 2, max = 5, message = "El tipo de documento de la persona debe ser mínimo de 2 caracteres y máximo de 5")
    private String documentType;

    @NotEmpty(message = "El numero de documento de la persona es requerido")
    @Size(min = 6, max = 30, message = "El numero de documento de la persona debe ser mínimo de 6 caracteres y máximo de 30")
    private String documentNumber;

    @NotEmpty(message = "El primer nombre de la persona es requerido")
    @Size(min = 3, max = 50, message = "El primer nombre de la persona debe ser mínimo de 3 caracteres y máximo de 50")
    private String firstName;

    private String secondName;

    @NotEmpty(message = "El primer apellido de la persona es requerido")
    @Size(min = 3, max = 50, message = "El primer apellido de la persona debe ser mínimo de 3 caracteres y máximo de 50")
    private String firstSurname;

    private String secondSurname;

    @NotEmpty(message = "El email de la persona es requerido")
    @Size(min = 3, max = 150, message = "El email de la persona no debe sobrepasar los 150")
    private String email;

    @NotEmpty(message = "El género de la persona es requerido")
    @Size(min = 1, max = 1, message = "El género de la persona no debe sobrepasar los 1")
    private String gender;

    @NotEmpty(message = "El primer número contacto de la persona es requerido")
    @Size(min = 7, max = 20, message = "El primer numero contacto de la persona debe ser mínimo de 7 caracteres y máximo de 20")
    private String phone1;

    private String phone2;

    @NotEmpty(message = "La dirección de la persona es requerido")
    @Size(min = 5, max = 150, message = "La dirección de la persona debe ser mínimo de 5 caracteres y máximo de 150")
    private String address;

    private String contactPerson;
    private String phoneContactPerson;
    private String description;

    @NotEmpty(message = "El estado civil de la persona es requerido")
    @Size(min = 5, max = 30, message = "El estado civil de la persona debe ser mínimo de 5 caracteres y máximo de 50")
    private String civilStatus;

}
