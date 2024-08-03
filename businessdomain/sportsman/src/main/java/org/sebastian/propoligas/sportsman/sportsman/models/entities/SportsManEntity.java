package org.sebastian.propoligas.sportsman.sportsman.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.sebastian.propoligas.sportsman.sportsman.models.Persons;

import java.util.Date;

@Entity
@Table(name = "TBL_SPORTSMAN")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SportsManEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Comment("Clave primaria")
    private Long id;

    @Column(name = "PERSON_ID", unique = true, nullable = false )
    @Comment("ID Referencia de la Persona (MS Persons)")
    private Long personId;

    @Column(name = "CARNET", unique = true, nullable = false, length = 25 )
    @Comment("Número Carnet del deportista")
    private String carnet;

    @Column(name = "SHIRT_NUMBER", nullable = false, length = 3 )
    @Comment("Número Camisa del deportista")
    private String numberShirt;

    @Column(name = "SHIRT_NAME", unique = true, nullable = false, length = 30 )
    @Comment("Nombre Camisa del deportista")
    private String nameShirt;

    @Column(name = "WEIGHT", nullable = false )
    @Comment("Peso del deportista")
    private Float weight;

    @Column(name = "HEIGHT", nullable = false )
    @Comment("Altura del deportista")
    private Float height;

    @Column(name = "BLOOD_TYPE", nullable = false, length = 3)
    @Comment("Tipo de Sangre del deportista")
    private String bloodType;

    @Column(name = "STARTING_PLAYING_POSITION", nullable = false, length = 40)
    @Comment("Posición de juego inicial del deportista")
    private String startingPlayingPosition;

    @Column(name = "CAPTAIN", nullable = false)
    @Comment("¿Capitan de campo?")
    private Boolean captain;

    @Column(name = "PHOTO", nullable = true, length = 500)
    @Comment("Fotografía Carnet del deportista")
    private String photoUrl;

    @Column(name = "DESCRIPTION", nullable = true, length = 3000)
    @Comment("Anotaciones Adicionales del deportista")
    private String description;

    @Column(name = "STATUS" )
    @Comment("Estado del deportista")
    private Boolean status;

    @Column(name = "USER_CREATED", nullable = true, length = 100 )
    @Comment("Usuario que creó al deportista")
    private String userCreated;

    @Column(name = "DATE_CREATED", nullable = true )
    @Comment("Fecha creación del deportista")
    private Date dateCreated;

    @Column(name = "USER_UPDATED", nullable = true, length = 100 )
    @Comment("Usuario que actualizó al deportista")
    private String userUpdated;

    @Column(name = "DATE_UPDATED", nullable = true )
    @Comment("Fecha actualización del deportista")
    private Date dateUpdated;

    @Transient //No hace parte directa,no mapeado a la persistencia.
    private Persons person;

}
