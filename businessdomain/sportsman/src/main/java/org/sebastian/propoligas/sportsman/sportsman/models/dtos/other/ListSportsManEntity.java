package org.sebastian.propoligas.sportsman.sportsman.models.dtos.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListSportsManEntity {

    private Long id;
    private String carnet;
    private String numberShirt;
    private Float weight;
    private Float height;
    private String bloodType;
    private String startingPlayingPosition;
    private Boolean captain;
    private String photoUrl;
    private String description;
    private Boolean status;
    private String userCreated;
    private Date dateCreated;
    private String userUpdated;
    private Date dateUpdated;

    //Apartado que pertenece a MS Person
    private String firstName;
    private String secondName;
    private String firstSurname;
    private String secondSurname;
    private String documentNumber;
    private String email;

}
