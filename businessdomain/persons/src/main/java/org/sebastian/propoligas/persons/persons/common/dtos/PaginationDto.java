package org.sebastian.propoligas.persons.persons.common.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos de paginación y búsqueda")
public class PaginationDto {

    @NotNull(message = "El número de página actual es requerido")
    @Min(value = 1, message = "Número de página")
    @Schema(description = "Número de página para la paginación", example = "1", required = true)
    private Integer page;

    @NotNull(message = "El tamaño de página es requerido")
    @Min(value = 1, message = "Tamaño de la página")
    @Schema(description = "Número de elementos por página", example = "2", required = true)
    private Integer size;

    @NotNull(message = "El valor de ordenamiento de página actual es requerido")
    @Pattern(regexp = "asc|desc", message = "Tipo de ordenamiento (Asc o Desc)")
    @Schema(description = "Orden de la paginación: 'asc' para ascendente, 'desc' para descendente", example = "asc", required = true)
    private String order;

    @NotNull(message = "La columna para el ordenamiento de página actual es requerido")
    @Size(min = 2, max = 100, message = "El tag ícono debe ser mínimo de 2 caracteres y máximo de 100")
    @Schema(description = "Campo por el que se debe ordenar", example = "id", required = true)
    private String sort;

    @Schema(description = "Cadena de búsqueda para filtrar resultados", example = "", required = false)
    private String search;

}
