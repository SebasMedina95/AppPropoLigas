package org.sebastian.propoligas.sportsman.sportsman.common.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;

import java.util.List;

@Data
@Schema(description = "Respuesta con la lista paginada de personas")
public class SportManResponseList {

    @Schema(description = "Datos paginados de la respuesta")
    private PagedData data;

    @Schema(description = "Meta información sobre la respuesta")
    private Meta meta;

    @Data
    @Schema(description = "Datos de paginación y lista de personas")
    public static class PagedData {
        @Schema(description = "Enlaces para paginación")
        private List<Link> links;

        @Schema(description = "Contenido de la respuesta")
        private List<SportsManEntity> content;

        @Schema(description = "Información de la página")
        private PageInfo page;

        @Data
        @Schema(description = "Enlace para paginación")
        public static class Link {
            @Schema(description = "Relación del enlace")
            private String rel;

            @Schema(description = "URL del enlace")
            private String href;
        }

        @Data
        @Schema(description = "Información de la página")
        public static class PageInfo {
            @Schema(description = "Tamaño de la página")
            private int size;

            @Schema(description = "Número total de elementos")
            private long totalElements;

            @Schema(description = "Número total de páginas")
            private int totalPages;

            @Schema(description = "Número de la página actual")
            private int number;
        }
    }

    @Data
    @Schema(description = "Meta información de la respuesta")
    public static class Meta {
        @Schema(description = "Mensaje de la respuesta")
        private String message;

        @Schema(description = "Código HTTP de la respuesta")
        private int code;

        @Schema(description = "Fecha y hora de la respuesta")
        private String date;
    }

}
