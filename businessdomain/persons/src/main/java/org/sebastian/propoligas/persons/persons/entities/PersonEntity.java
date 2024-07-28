package org.sebastian.propoligas.persons.persons.entities;

import jakarta.persistence.*;
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
public class PersonEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Comment("Clave primaria")
    private Long id;

    @Column(name = "DOCUMENT_TYPE", nullable = false, length = 5 )
    @Comment("Tipo Documento de la persona")
    private String documentType;

    @Column(name = "DOCUMENT_NUMBER", unique = true, nullable = false, length = 30 )
    @Comment("Número Documento de la persona")
    private String documentNumber;

    @Column(name = "FIRST_NAME", nullable = false, length = 50 )
    @Comment("Primer Nombre de la persona")
    private String firstName;

    @Column(name = "SECOND_NAME", nullable = true, length = 50 )
    @Comment("Primer Nombre de la persona")
    private String secondName;

    @Column(name = "FIRST_SURNAME", nullable = false, length = 50 )
    @Comment("Primer Apellido de la persona")
    private String firstSurname;

    @Column(name = "SECOND_SURNAME", nullable = true, length = 50 )
    @Comment("Primer Nombre de la persona")
    private String secondSurname;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 200 )
    @Comment("Email de la persona")
    private String email;

    @Column(name = "PHONE_1", nullable = false, length = 20 )
    @Comment("Primer Teléfono de la persona")
    private String phone1;

    @Column(name = "PHONE_2", nullable = true, length = 20 )
    @Comment("Segundo Teléfono de la persona")
    private String phone2;

    @Column(name = "ADDRESS", nullable = false, length = 150 )
    @Comment("Dirección Residencial de la persona")
    private String address;

    @Column(name = "CONTACT_PERSON", nullable = true, length = 300 )
    @Comment("Contacto Adicional de la persona")
    private String contactPerson;

    @Column(name = "PHONE_CONTACT_PERSON", nullable = true, length = 300 )
    @Comment("Número Contacto Adicional de la persona")
    private String phoneContactPerson;

    @Column(name = "DESCRIPTION", nullable = true, length = 1000 )
    @Comment("Anotaciones adicionales de la persona")
    private String description;

    @Column(name = "CIVIL_STATUS", nullable = false, length = 30 )
    @Comment("Estado Civil de la persona")
    private String civilStatus;

    @Column(name = "STATUS" )
    @Comment("Estado eliminación lógica")
    private Boolean status;

    @Column(name = "USER_CREATED", nullable = true, length = 100 )
    @Comment("Usuario que creó")
    private String userCreated;

    @Column(name = "DATE_CREATED", nullable = true )
    @Comment("Fecha creación")
    private Date dateCreated;

    @Column(name = "USER_UPDATED", nullable = true, length = 100 )
    @Comment("Usuario que actualizó")
    private String userUpdated;

    @Column(name = "DATE_UPDATED", nullable = true )
    @Comment("Fecha actualización")
    private Date dateUpdated;

}
