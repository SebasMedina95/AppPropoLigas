package org.sebastian.propoligas.sportsman.sportsman.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSportsManDto {

    private String carnet;
    private String numberShirt;
    private Float weight;
    private Float height;
    private String bloodType;
    private String startingPlayingPosition;
    private Boolean captain;
    private String photoUrl;
    private String description;
    private Long personsSportsManRelationId;

}
