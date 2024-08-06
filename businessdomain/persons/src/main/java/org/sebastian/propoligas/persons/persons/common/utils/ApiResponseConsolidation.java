package org.sebastian.propoligas.persons.persons.common.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estándar para todas las operaciones")
public class ApiResponseConsolidation<T> {

    @Schema(description = "Los datos específicos de la respuesta")
    private T data;

    @Schema(description = "Información adicional sobre la respuesta")
    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Información sobre el estado de la respuesta")
    public static class Meta {

        private String message;
        private Integer code;
        private LocalDateTime date;

    }

}
