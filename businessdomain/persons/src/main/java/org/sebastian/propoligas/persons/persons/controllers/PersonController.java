package org.sebastian.propoligas.persons.persons.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sebastian.propoligas.persons.persons.common.dtos.PaginationDto;
import org.sebastian.propoligas.persons.persons.common.swagger.*;
import org.sebastian.propoligas.persons.persons.common.utils.ApiResponseConsolidation;
import org.sebastian.propoligas.persons.persons.common.utils.CustomPagedResourcesAssembler;
import org.sebastian.propoligas.persons.persons.common.utils.ErrorsValidationsResponse;
import org.sebastian.propoligas.persons.persons.common.utils.ResponseWrapper;
import org.sebastian.propoligas.persons.persons.entities.PersonEntity;
import org.sebastian.propoligas.persons.persons.entities.dtos.create.CreatePersonDto;
import org.sebastian.propoligas.persons.persons.entities.dtos.update.UpdatePersonDto;
import org.sebastian.propoligas.persons.persons.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/api/persons")
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Persona Registrada Correctamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseCreate.class))),
            @ApiResponse(responseCode = "406", description = "Errores en los campos de creación.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseCreateErrorFields.class))),
            @ApiResponse(responseCode = "400", description = "Cualquier otro caso de error, incluyendo: " +
                    "El número de documento proporcionado es inválido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseCreateErrorGeneric.class))),
    })
    public ResponseEntity<ApiResponseConsolidation<Object>> create(
            @Valid
            @RequestBody CreatePersonDto personRequest,
            BindingResult result
    ){

        //Validación de campos
        if(result.hasFieldErrors()){
            ErrorsValidationsResponse errors = new ErrorsValidationsResponse();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ApiResponseConsolidation<>(
                            errors.validation(result),
                            new ApiResponseConsolidation.Meta(
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
                    .body(new ApiResponseConsolidation<>(
                            null,
                            new ApiResponseConsolidation.Meta(
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
                    .body(new ApiResponseConsolidation<>(
                            personNew.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Persona Registrada Correctamente.",
                                    HttpStatus.CREATED.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        //? Estamos en este punto, el registro no fue correcto
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseConsolidation<>(
                        null,
                        new ApiResponseConsolidation.Meta(
                                personNew.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @PostMapping("/find-all")
    @Operation(
            summary = "Obtener todas las personas",
            description = "Obtener todas las personas con paginación y también aplicando filtros",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para la paginación y búsqueda",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaginationDto.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de personas.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseList.class))),
            @ApiResponse(responseCode = "400", description = "Errores en los campos de paginación.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseListError.class)))
    })
    public ResponseEntity<ApiResponseConsolidation<Object>> findAll(
            @Valid @RequestBody PaginationDto paginationDto,
            BindingResult result
    ){

        //Validación de campos
        if(result.hasFieldErrors()){
            ErrorsValidationsResponse errors = new ErrorsValidationsResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseConsolidation<>(
                            errors.validation(result),
                            new ApiResponseConsolidation.Meta(
                                    "Errores en los campos de paginación",
                                    HttpStatus.BAD_REQUEST.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        if (paginationDto.getPage() < 1) paginationDto.setPage(1); //Para controlar la página 0, y que la paginación arranque en 1.

        Sort.Direction direction = paginationDto.getOrder().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(paginationDto.getPage() - 1, paginationDto.getSize(), Sort.by(direction, paginationDto.getSort())); //Generando el esquema de paginación para aplicar y ordenamiento
        Page<PersonEntity> comforts = personService.findAll(paginationDto.getSearch(), pageable); //Aplicando la paginación JPA -> Incorporo el buscador
        UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri(); //Para la obtención de la URL

        PagedModel<PersonEntity> pagedModel = customPagedResourcesAssembler.toModel(comforts, uriBuilder);

        return ResponseEntity.ok(new ApiResponseConsolidation<>(
                pagedModel,
                new ApiResponseConsolidation.Meta(
                        "Listado de personas.",
                        HttpStatus.OK.value(),
                        LocalDateTime.now()
                )
        ));

    }

    @GetMapping("/find-by-id/{id}")
    @Operation(
            summary = "Obtener persona por ID",
            description = "Obtener una persona dado el ID",
            parameters = {
                    @Parameter(name = "id", description = "ID de la persona a obtener", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Persona encontrada.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponseCreate.class))),
                    @ApiResponse(responseCode = "404", description = "Persona no encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponseCreateErrorGeneric.class))),
                    @ApiResponse(responseCode = "400", description = "Error al realizar la búsqueda",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponseCreateErrorGeneric.class)))
            }
    )
    public ResponseEntity<ApiResponseConsolidation<PersonEntity>> findById(
            @PathVariable("id") String id
    ){

        ResponseWrapper<PersonEntity> personGet;

        //Validación del ID
        try {
            Long personId = Long.parseLong(id);
            personGet = personService.findById(personId);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseConsolidation<>(
                            null,
                            new ApiResponseConsolidation.Meta(
                                    "El ID proporcionado para obtener una persona es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        if( personGet.getData() != null ){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseConsolidation<>(
                            personGet.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Persona obtenida por ID.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseConsolidation<>(
                        null,
                        new ApiResponseConsolidation.Meta(
                                personGet.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona Actualizada Correctamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseCreate.class))),
            @ApiResponse(responseCode = "406", description = "Errores en los campos de creación.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseCreateErrorFields.class))),
            @ApiResponse(responseCode = "400", description = "Cualquier otro caso de error, incluyendo: " +
                    "El número de documento proporcionado es inválido. El ID proporcionado para actualizar es inválido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseCreateErrorGeneric.class))),
    })
    public ResponseEntity<ApiResponseConsolidation<Object>> update(
            @Valid
            @RequestBody UpdatePersonDto personRequest,
            BindingResult result,
            @PathVariable("id") String id
    ){

        ResponseWrapper<PersonEntity> personUpdate;

        //Validación del ID
        try {
            Long personId = Long.parseLong(id);
            personUpdate = personService.update(personId, personRequest);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseConsolidation<>(
                            null,
                            new ApiResponseConsolidation.Meta(
                                    "El ID proporcionado para actualizar es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        //Validación de número de documento
        try{
            Long.parseLong(personRequest.getDocumentNumber());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseConsolidation<>(
                            null,
                            new ApiResponseConsolidation.Meta(
                                    "El número de documento proporcionado es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        //Validación de campos
        if(result.hasFieldErrors()){
            ErrorsValidationsResponse errors = new ErrorsValidationsResponse();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ApiResponseConsolidation<>(
                            errors.validation(result),
                            new ApiResponseConsolidation.Meta(
                                    "Errores en los campos de actualización",
                                    HttpStatus.BAD_REQUEST.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        if( personUpdate.getData() != null ){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseConsolidation<>(
                            personUpdate.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Persona Actualizada Correctamente.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseConsolidation<>(
                        null,
                        new ApiResponseConsolidation.Meta(
                                personUpdate.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @DeleteMapping("/delete-by-id/{id}")
    @Operation(
            summary = "Eliminar una persona",
            description = "Eliminar una persona pero de manera lógica",
            parameters = {
                    @Parameter(name = "id", description = "ID de la persona a eliminar", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona Eliminada Correctamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseCreate.class))),
            @ApiResponse(responseCode = "400", description = "Cualquier otro caso de error, incluyendo: " +
                    "El ID proporcionado para actualizar es inválido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseCreateErrorGeneric.class))),
    })
    public ResponseEntity<ApiResponseConsolidation<PersonEntity>> delete(
            @PathVariable("id") String id
    ){

        ResponseWrapper<PersonEntity> personUpdate;

        try {
            Long personId = Long.parseLong(id);
            personUpdate = personService.delete(personId);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseConsolidation<>(
                            null,
                            new ApiResponseConsolidation.Meta(
                                    "El ID proporcionado es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        if( personUpdate.getData() != null ){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseConsolidation<>(
                            personUpdate.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Persona Eliminada Correctamente.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseConsolidation<>(
                        null,
                        new ApiResponseConsolidation.Meta(
                                personUpdate.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @GetMapping("/find-by-search")
    @Operation(
            summary = "Buscar una persona por criterio",
            description = "Buscar una persona dado un criterio de búsqueda, usado principalmente desde otros MS",
            parameters = {
                    @Parameter(name = "search", description = "Criterio proporcionado para la búsqueda", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ids detectados para filtrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseIdsList.class))),
            @ApiResponse(responseCode = "400", description = "Cualquier otro caso de error, incluyendo.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponseCreateErrorGeneric.class))),
    })
    public ResponseEntity<ApiResponseConsolidation<List<Long>>> findPersonIdsByCriteria(
            @RequestParam("search") String search
    ) {

        List<Long> personIds = personService.findPersonIdsByCriteria(search);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseConsolidation<>(
                        personIds,
                        new ApiResponseConsolidation.Meta(
                                "Ids detectados para filtrado.",
                                HttpStatus.OK.value(),
                                LocalDateTime.now()
                        )
                ));

    }

}
