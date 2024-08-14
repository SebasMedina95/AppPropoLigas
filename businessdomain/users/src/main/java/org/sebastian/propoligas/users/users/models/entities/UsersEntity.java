package org.sebastian.propoligas.users.users.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.sebastian.propoligas.users.users.models.Persons;

import java.util.Date;

@Entity
@Table(name = "TBL_SPORTSMAN")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Comment("Clave primaria")
    private Long id;

    @Column(name = "PERSON_ID", unique = true, nullable = false )
    @Comment("ID Referencia de la Persona (MS Persons)")
    private Long personId;

    @Column(name = "USER", unique = true, nullable = false, length = 25 )
    @Comment("Nombre de usuario")
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 3 )
    @Comment("Contraseña de usuario")
    private String password;

    @Column(name = "CONFIRM", nullable = false)
    @Comment("¿Usuario confirmado?")
    private Boolean validate;

    @Column(name = "DESCRIPTION", nullable = true, length = 3000)
    @Comment("Anotaciones Adicionales del usuario")
    private String description;

    @Column(name = "STATUS" )
    @Comment("Estado del usuario")
    private Boolean status;

    @Column(name = "USER_CREATED", nullable = true, length = 100 )
    @Comment("Usuario que creó al usuario")
    private String userCreated;

    @Column(name = "DATE_CREATED", nullable = true )
    @Comment("Fecha creación del usuario")
    private Date dateCreated;

    @Column(name = "USER_UPDATED", nullable = true, length = 100 )
    @Comment("Usuario que actualizó al usuario")
    private String userUpdated;

    @Column(name = "DATE_UPDATED", nullable = true )
    @Comment("Fecha actualización del usuario")
    private Date dateUpdated;

    @Transient //No hace parte directa,no mapeado a la persistencia.
    private Persons person;

}
