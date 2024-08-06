package org.sebastian.propoligas.sportsman.sportsman.models.dtos.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSportsManDto {

    @NotNull(message = "El id de referencia de la persona no puede ir vacío")
    private Long personId;

    @NotEmpty(message = "El número de camisa del deportista es requerido")
    @Size(min = 1, max = 3, message = "El número de camisa debe ser mínimo de 1 caracteres y máximo de 3")
    private String numberShirt;

    @NotEmpty(message = "El nombre de camisa del deportista es requerido")
    @Size(min = 1, max = 30, message = "El nombre de camisa debe ser mínimo de 1 caracteres y máximo de 30")
    private String nameShirt;

    @NotNull(message = "El peso del deportista no puede ir vacío")
    @Min(value = 1, message = "El peso del deportista debe ser al menos 1")
    private Float weight;

    @NotNull(message = "La altura del deportista no puede ir vacío")
    @Min(value = 1, message = "La altura del deportista debe ser al menos 1")
    private Float height;

    @NotEmpty(message = "El tipo de sangre del deportista es requerido")
    @Size(min = 2, max = 3, message = "El tipo de sangre debe ser mínimo de 2 caracteres y máximo de 3")
    private String bloodType;

    @NotEmpty(message = "La posición inicial del deportista es requerido")
    @Size(min = 5, max = 40, message = "La posición inicial debe ser mínimo de 5 caracteres y máximo de 40")
    private String startingPlayingPosition;

    @NotNull(message = "El campo de capitanía es requerido")
    private Boolean captain;

    private String description;

}
