package org.sebastian.propoligas.sportsman.sportsman.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ApiResponse;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ErrorsValidationsResponse;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ResponseWrapper;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.create.CreateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.sebastian.propoligas.sportsman.sportsman.services.SportsManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sportsman")
@Tag(name = "Controlador de Deportistas", description = "Operaciones relacionadas con los deportistas")
public class SportsManController {

    private final SportsManService sportsManService;

    @Autowired
    public SportsManController(
            SportsManService sportsManService
            ){
        this.sportsManService = sportsManService;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear un Deportista", description = "Creación de un deportista")
    public ResponseEntity<ApiResponse<Object>> create(
            @Valid
            @RequestBody CreateSportsManDto sportManRequest,
            BindingResult result
    ){

        //Validación de campos
        if(result.hasFieldErrors()){
            ErrorsValidationsResponse errors = new ErrorsValidationsResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            errors.validation(result),
                            new ApiResponse.Meta(
                                    "Errores en los campos de creación",
                                    HttpStatus.BAD_REQUEST.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        //? Intentamos realizar el registro
        ResponseWrapper<SportsManEntity> sportManNew = sportsManService.create(sportManRequest);

        //? Si no ocurre algún error, entonces registramos :)
        if( sportManNew.getData() != null ){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            sportManNew.getData(),
                            new ApiResponse.Meta(
                                    "Deportista Registrado Correctamente.",
                                    HttpStatus.CREATED.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        //? Estamos en este punto, el registro no fue correcto
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        null,
                        new ApiResponse.Meta(
                                sportManNew.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @GetMapping("/find-by-id/{id}")
    @Operation(summary = "Obtener deportista por ID", description = "Obtener un deportista dado el ID")
    public ResponseEntity<ApiResponse<Object>> findById(
            @PathVariable("id") String id
    ){

        ResponseWrapper<SportsManEntity> comfort;

        //Validamos que el ID que nos proporcionan por la URL sea válido
        try {
            Long comfortId = Long.parseLong(id);
            comfort = sportsManService.findById(comfortId);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            null,
                            new ApiResponse.Meta(
                                    "El ID proporcionado para la búsqueda es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        if( comfort.getData() != null ){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(
                            comfort.getData(),
                            new ApiResponse.Meta(
                                    "Deportista obtenido por ID.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        null,
                        new ApiResponse.Meta(
                                comfort.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

}
