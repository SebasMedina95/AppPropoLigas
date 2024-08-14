package org.sebastian.propoligas.users.users.models.dtos.update;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUsersDto {

    @NotNull(message = "El id de referencia del usuario no puede ir vacío")
    private Long personId;

}
