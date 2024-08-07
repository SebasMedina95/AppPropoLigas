package org.sebastian.propoligas.sportsman.sportsman.common.swagger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportManResponseCreate {

    private SportsManEntity data;
    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {

        private String message;
        private int code;
        private LocalDateTime date;

    }

}
