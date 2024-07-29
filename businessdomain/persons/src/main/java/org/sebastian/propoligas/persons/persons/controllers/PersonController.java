package org.sebastian.propoligas.persons.persons.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sebastian.propoligas.persons.persons.common.dtos.PaginationDto;
import org.sebastian.propoligas.persons.persons.common.utils.ApiResponse;
import org.sebastian.propoligas.persons.persons.common.utils.CustomPagedResourcesAssembler;
import org.sebastian.propoligas.persons.persons.common.utils.ErrorsValidationsResponse;
import org.sebastian.propoligas.persons.persons.common.utils.ResponseWrapper;
import org.sebastian.propoligas.persons.persons.entities.PersonEntity;
import org.sebastian.propoligas.persons.persons.entities.dtos.create.CreatePersonDto;
import org.sebastian.propoligas.persons.persons.entities.dtos.update.UpdatePersonDto;
import org.sebastian.propoligas.persons.persons.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/persons")
@Tag(name = "Controlador de MicroServicio Personas", description = "Operaciones relacionadas con el micro servicio de personas")
public class PersonController {

    private final PersonService personService;
    private final CustomPagedResourcesAssembler<PersonEntity> customPagedResourcesAssembler;

    @Autowired
    public PersonController(
            PersonService personService,
            CustomPagedResourcesAssembler<PersonEntity> customPagedResourcesAssembler){
        this.personService = personService;
        this.customPagedResourcesAssembler = customPagedResourcesAssembler;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear una Persona", description = "Creación de una persona")
    public ResponseEntity<ApiResponse<Object>> create(
            @Valid
            @RequestBody CreatePersonDto personRequest,
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

        //Validación de número de documento
        try{
            Long.parseLong(personRequest.getDocumentNumber());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            null,
                            new ApiResponse.Meta(
                                    "El número de documento proporcionado es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        //? Intentamos realizar el registro
        ResponseWrapper<PersonEntity> personNew = personService.create(personRequest);

        //? Si no ocurre algún error, entonces registramos :)
        if( personNew.getData() != null ){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            personNew.getData(),
                            new ApiResponse.Meta(
                                    "Persona Registrada Correctamente.",
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
                                personNew.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @GetMapping("/find-all")
    @Operation(summary = "Obtener todas las personas", description = "Obtener todas las personas con paginación y también aplicando filtros")
    public ResponseEntity<ApiResponse<Object>> findAll(
            @Valid
            @RequestBody PaginationDto paginationDto,
            BindingResult result
    ){

        //Validación de campos
        if(result.hasFieldErrors()){
            ErrorsValidationsResponse errors = new ErrorsValidationsResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            errors.validation(result),
                            new ApiResponse.Meta(
                                    "Errores en los campos de paginación",
                                    HttpStatus.BAD_REQUEST.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        null,
                        new ApiResponse.Meta(
                                "Listado personas - Prueba método",
                                HttpStatus.OK.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @GetMapping("/find-by-id/{id}")
    @Operation(
            summary = "Obtener comodidades por ID",
            description = "Obtener una persona dado el ID",
            parameters = {
                    @Parameter(name = "id", description = "ID de la persona a obtener", required = true)
            }
    )
    public ResponseEntity<ApiResponse<PersonEntity>> findById(
            @PathVariable String id
    ){

        ResponseWrapper<PersonEntity> personGet;

        //Validación del ID
        try {
            Long personId = Long.parseLong(id);
            personGet = personService.findById(personId);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            null,
                            new ApiResponse.Meta(
                                    "El ID proporcionado es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        null,
                        new ApiResponse.Meta(
                                "Obtener persona por ID - Prueba método",
                                HttpStatus.OK.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @PutMapping("update-by-id/{id}")
    @Operation(
            summary = "Actualizar una persona",
            description = "Actualizar una persona dado el ID",
            parameters = {
                    @Parameter(name = "id", description = "ID para la actualización", required = true)
            }
    )
    public ResponseEntity<ApiResponse<Object>> update(
            @Valid
            @RequestBody UpdatePersonDto personRequest,
            BindingResult result,
            @PathVariable String id
    ){

        ResponseWrapper<PersonEntity> personUpdate;

        //Validación del ID
        try {
            Long personId = Long.parseLong(id);
            personUpdate = personService.update(personId, personRequest);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            null,
                            new ApiResponse.Meta(
                                    "El ID proporcionado es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        //Validación de campos
        if(result.hasFieldErrors()){
            ErrorsValidationsResponse errors = new ErrorsValidationsResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            errors.validation(result),
                            new ApiResponse.Meta(
                                    "Errores en los campos de actualización",
                                    HttpStatus.BAD_REQUEST.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        null,
                        new ApiResponse.Meta(
                                "Actualizar persona por ID - Prueba método",
                                HttpStatus.OK.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @DeleteMapping("/delete-by-id/{id}")
    @Operation(
            summary = "Eliminar una comodidad",
            description = "Eliminar una persona pero de manera lógica",
            parameters = {
                    @Parameter(name = "id", description = "ID de la persona a eliminar", required = true)
            }
    )
    public ResponseEntity<ApiResponse<PersonEntity>> delete(
            @PathVariable String id
    ){

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        null,
                        new ApiResponse.Meta(
                                "Eliminar persona por ID - Prueba método",
                                HttpStatus.OK.value(),
                                LocalDateTime.now()
                        )
                ));

    }

}
