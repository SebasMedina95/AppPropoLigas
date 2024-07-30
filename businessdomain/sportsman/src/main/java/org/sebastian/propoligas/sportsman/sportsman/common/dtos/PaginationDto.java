package org.sebastian.propoligas.sportsman.sportsman.common.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationDto {

    @NotNull(message = "El número de página actual es requerido")
    @Min(value = 1, message = "Número de página")
    private Integer page;

    @NotNull(message = "El tamaño de página es requerido")
    @Min(value = 1, message = "Tamaño de la página")
    private Integer size;

    @NotNull(message = "El valor de ordenamiento de página actual es requerido")
    @Pattern(regexp = "asc|desc", message = "Tipo de ordenamiento (Asc o Desc)")
    private String order;

    @NotNull(message = "La columna para el ordenamiento de página actual es requerido")
    @Size(min = 2, max = 100, message = "El tag ícono debe ser mínimo de 2 caracteres y máximo de 100")
    private String sort;

    private String search;

}
