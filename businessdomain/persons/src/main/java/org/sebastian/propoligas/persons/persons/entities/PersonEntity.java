package org.sebastian.propoligas.persons.persons.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Date;

@Entity
@Table(name = "TBL_PERSONS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa a las personas en BD")
public class PersonEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Comment("Clave primaria")
    @Schema(description = "Clave primaria autogenerada")
    private Long id;

    @Column(name = "DOCUMENT_TYPE", nullable = false, length = 5 )
    @Comment("Tipo Documento de la persona")
    @Schema(defaultValue = "CC", description = "Tipo de Documento")
    @NotNull
    private String documentType;

    @Column(name = "DOCUMENT_NUMBER", unique = true, nullable = false, length = 30 )
    @Comment("Número Documento de la persona")
    @Schema(description = "Número de Documento")
    @NotNull
    private String documentNumber;

    @Column(name = "FIRST_NAME", nullable = false, length = 50 )
    @Comment("Primer Nombre de la persona")
    @Schema(description = "Primer Nombre de la Persona")
    @NotNull
    private String firstName;

    @Column(name = "SECOND_NAME", nullable = true, length = 50 )
    @Comment("Primer Nombre de la persona")
    @Schema(description = "Segundo Nombre de la Persona")
    private String secondName;

    @Column(name = "FIRST_SURNAME", nullable = false, length = 50 )
    @Comment("Primer Apellido de la persona")
    @Schema(description = "Primer Apellido de la Persona")
    @NotNull
    private String firstSurname;

    @Column(name = "SECOND_SURNAME", nullable = true, length = 50 )
    @Comment("Primer Nombre de la persona")
    @Schema(description = "Segundo Apellido de la Persona")
    private String secondSurname;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 200 )
    @Comment("Email de la persona")
    @Schema(description = "Correo electrónico de la Persona")
    @NotNull
    private String email;

    @Column(name = "GENDER", nullable = false, length = 1 )
    @Comment("Sexo de la persona")
    @Schema(defaultValue = "F", description = "Sexo de la Persona")
    @NotNull
    private String gender;

    @Column(name = "PHONE_1", nullable = false, length = 20 )
    @Comment("Primer Teléfono de la persona")
    @Schema(description = "Teléfono primario de la Persona")
    @NotNull
    private String phone1;

    @Column(name = "PHONE_2", nullable = true, length = 20 )
    @Comment("Segundo Teléfono de la persona")
    @Schema(description = "Teléfono secundario de la Persona")
    private String phone2;

    @Column(name = "ADDRESS", nullable = false, length = 150 )
    @Comment("Dirección Residencial de la persona")
    @Schema(description = "Dirección de residencia de la Persona")
    @NotNull
    private String address;

    @Column(name = "CONTACT_PERSON", nullable = true, length = 300 )
    @Comment("Contacto Adicional de la persona")
    @Schema(description = "Nombre completo del acudiente de la Persona")
    private String contactPerson;

    @Column(name = "PHONE_CONTACT_PERSON", nullable = true, length = 300 )
    @Comment("Número Contacto Adicional de la persona")
    @Schema(description = "Número de teléfono del acudiente de la Persona")
    private String phoneContactPerson;

    @Column(name = "DESCRIPTION", nullable = true, length = 1000 )
    @Comment("Anotaciones adicionales de la persona")
    @Schema(description = "Descripciones adicionales de la Persona")
    private String description;

    @Column(name = "CIVIL_STATUS", nullable = false, length = 30 )
    @Comment("Estado Civil de la persona")
    @Schema(defaultValue = "Soltero", description = "Estado Civil de la Persona")
    @NotNull
    private String civilStatus;

    @Column(name = "STATUS" )
    @Comment("Estado eliminación lógica")
    @Schema(defaultValue = "true", description = "Estado lógico de eliminación de la Persona")
    @NotNull
    private Boolean status;

    @Column(name = "USER_CREATED", nullable = true, length = 100 )
    @Comment("Usuario que creó")
    @Schema(defaultValue = "123456789", description = "Usuario que creó la Persona")
    private String userCreated;

    @Column(name = "DATE_CREATED", nullable = true )
    @Comment("Fecha creación")
    @Schema(description = "Fecha creación de la Persona")
    private Date dateCreated;

    @Column(name = "USER_UPDATED", nullable = true, length = 100 )
    @Comment("Usuario que actualizó")
    @Schema(defaultValue = "123456789", description = "Usuario que actualizó la Persona")
    private String userUpdated;

    @Column(name = "DATE_UPDATED", nullable = true )
    @Comment("Fecha actualización")
    @Schema(description = "Fecha actualización de la Persona")
    private Date dateUpdated;

}
