package org.sebastian.propoligas.persons.persons.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sebastian.propoligas.persons.persons.common.utils.ResponseWrapper;
import org.sebastian.propoligas.persons.persons.data.PersonMock;
import org.sebastian.propoligas.persons.persons.entities.PersonEntity;
import org.sebastian.propoligas.persons.persons.entities.dtos.create.CreatePersonDto;
import org.sebastian.propoligas.persons.persons.entities.dtos.update.UpdatePersonDto;
import org.sebastian.propoligas.persons.persons.repositories.PersonRepository;
import org.sebastian.propoligas.persons.persons.services.impls.PersonServiceImpl;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonTests {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private CreatePersonDto validPersonDto;
    private PersonEntity personEntity;
    private List<PersonEntity> createMockPersonsList;

    @BeforeEach
    void setUp() {
        validPersonDto = PersonMock.createValidPersonDto();
        personEntity = PersonMock.createValidPersonEntity();
        createMockPersonsList = PersonMock.createMockPersonsList();
    }

    @Test
    @DisplayName("Test para registrar una Persona exitosamente")
    void shouldCreatePersonSuccessfully() {
        // Configurar el mock para que devuelva un Optional vacío cuando se busca una persona por documento y correo
        when(personRepository.getPersonByDocumentAndByEmail(
                anyString(),
                anyString()
        )).thenReturn(Optional.empty());
        when(personRepository.save(any(PersonEntity.class))).thenReturn(personEntity);

        // Ejecutar la creación de la persona
        ResponseWrapper<PersonEntity> response = personService.create(validPersonDto);

        // Verificar resultados
        assertNotNull(response.getData());
        assertEquals("Persona guardada correctamente", response.getErrorMessage());
        assertEquals(personEntity, response.getData());
    }

    @Test
    @DisplayName("Test para registrar una Persona pero se repite el Documento o el Email")
    void shouldReturnErrorWhenPersonAlreadyExists() {
        // Configurar el mock para que devuelva un Optional con la persona existente
        when(personRepository.getPersonByDocumentAndByEmail(anyString(), anyString())).thenReturn(Optional.of(personEntity));

        // Ejecutar la creación de la persona
        ResponseWrapper<PersonEntity> response = personService.create(validPersonDto);

        // Verificar resultados
        assertNull(response.getData());
        assertEquals("El documento de la persona o su email ya está registrado", response.getErrorMessage());
    }

    @Test
    @DisplayName("Test para registrar una Persona pero ocurrió una excepción")
    void shouldReturnErrorWhenRepositoryThrowsException() {
        // Configurar el mock para que lance una excepción al guardar la persona
        when(personRepository.getPersonByDocumentAndByEmail(anyString(), anyString())).thenReturn(Optional.empty());
        when(personRepository.save(any(PersonEntity.class))).thenThrow(new RuntimeException("Error de prueba"));

        // Ejecutar la creación de la persona
        ResponseWrapper<PersonEntity> response = personService.create(validPersonDto);

        // Verificar resultados
        assertNull(response.getData());
        assertEquals("La persona no pudo ser creada", response.getErrorMessage());
    }

    @Test
    @DisplayName("Test para para retornar los resultados paginados y con filtro")
    void findAll_WithValidPaginationAndFilter_ShouldReturnPagedResults() {

        String search = "Doe"; // Ejemplo de búsqueda
        Pageable pageable = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "firstName"));
        List<PersonEntity> persons = PersonMock.createMockPersonsList();
        Page<PersonEntity> personPage = new PageImpl<>(persons, pageable, persons.size());

        // Uso de especificación de argumentos
        when(personRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(personPage);

        // Acción de paginación y listado
        Page<PersonEntity> result = personService.findAll(search, pageable);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertEquals(persons.size(), result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(persons.get(0).getId(), result.getContent().get(0).getId());

    }

    @Test
    @DisplayName("Test para para retornar los resultados paginados pero sin filtro")
    void findAll_WithEmptyFilter_ShouldReturnPagedResults() {

        String search = ""; // Filtro vacío
        Pageable pageable = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "firstName"));
        List<PersonEntity> persons = PersonMock.createMockPersonsList(); // Mock de personas con filtro vacío
        Page<PersonEntity> personPage = new PageImpl<>(persons, pageable, persons.size());

        // Realizamos la ejecución (Sin el filtro esto será transparente
        when(personRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(personPage);

        // Acción de traída de información paginada pero sin filtro
        Page<PersonEntity> result = personService.findAll(search, pageable);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertEquals(persons.size(), result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(persons.get(0).getId(), result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("Test para para retornar los resultados paginados pero hay error pagina")
    void findAll_WithValidPaginationAndFilter_ShouldReturnEmptyPage() {

        String search = "Nonexistent"; // Filtro que no coincide con ningún registro
        Pageable pageable = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "firstName"));
        List<PersonEntity> persons = Collections.emptyList(); // Página vacía
        Page<PersonEntity> personPage = new PageImpl<>(persons, pageable, 0);

        // Realizamos la ejecución (Sin el filtro esto será transparente
        when(personRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(personPage);

        // Acción de traída de información paginada pero sin filtro
        Page<PersonEntity> result = personService.findAll(search, pageable);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Test para para retornar los resultados paginados pero el filtro no coincide")
    void findAll_WithFilterNotMatchingAnyResult_ShouldReturnEmptyPage() {

        String search = "NoMatch"; // Filtro que no debería devolver resultados
        Pageable pageable = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "firstName"));
        List<PersonEntity> persons = Collections.emptyList(); // Página vacía
        Page<PersonEntity> personPage = new PageImpl<>(persons, pageable, 0);

        // Realizamos la ejecución (Sin el filtro esto será transparente
        when(personRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable)))
                .thenReturn(personPage);

        // Acción de traída de información paginada pero sin filtro
        Page<PersonEntity> result = personService.findAll(search, pageable);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Test para para retornar la persona por ID")
    void findById_WithValidId_ShouldReturnPerson() {

        Long id = 1L;
        List<PersonEntity> mockPersons = PersonMock.createMockPersonsList();
        PersonEntity expectedPerson = mockPersons.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst()
                .orElse(null);

        // Factor esperado de transacción esperado
        when(personRepository.findById(id)).thenReturn(Optional.of(expectedPerson));

        // Acción de buscado contra la base de datos
        ResponseWrapper<PersonEntity> result = personService.findById(id);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(expectedPerson, result.getData());
        assertEquals("Persona encontrada por ID correctamente", result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para para retornar la persona por ID pero no pudimos encontrar")
    void findById_WithNonexistentId_ShouldReturnErrorMessage() {

        Long id = 3L; // Un ID que no existe en la lista mock

        // Factor esperado de transacción esperado
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        // Acción de buscado contra la base de datos
        ResponseWrapper<PersonEntity> result = personService.findById(id);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals("La persona no pudo ser encontrado por el ID " + id, result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para para retornar la persona por ID pero obtuvimos una excepción")
    void findById_WithException_ShouldReturnErrorMessage() {

        Long id = 2L;

        // Factor esperado de transacción esperado
        when(personRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        // Acción de buscado contra la base de datos
        ResponseWrapper<PersonEntity> result = personService.findById(id);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals("La persona no pudo ser encontrado por el ID", result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para ejecutar actualización exitosa de persona")
    void update_WithValidIdAndData_ShouldReturnUpdatedPerson() {

        Long id = 1L;
        //Datos generales para la actualización
        UpdatePersonDto updateDto = new UpdatePersonDto(
                "CC",
                "123456",
                "John",
                null,
                "Doe",
                null,
                "john.doe@example.com",
                "M",
                "123456789",
                null,
                "123 Main St",
                null,
                null,
                null,
                "Single"
        );

        //Efectuamos el espejo lógico de actualización
        PersonEntity existingPerson = PersonMock.createMockPersonsList().get(0);
        PersonEntity updatedPerson = PersonEntity.builder()
                .id(id)
                .documentType(updateDto.getDocumentType())
                .documentNumber(updateDto.getDocumentNumber())
                .firstName(updateDto.getFirstName())
                .secondName(updateDto.getSecondName())
                .firstSurname(updateDto.getFirstSurname())
                .secondSurname(updateDto.getSecondSurname())
                .email(updateDto.getEmail())
                .gender(updateDto.getGender())
                .phone1(updateDto.getPhone1())
                .phone2(updateDto.getPhone2())
                .address(updateDto.getAddress())
                .contactPerson(updateDto.getContactPerson())
                .phoneContactPerson(updateDto.getPhoneContactPerson())
                .description(updateDto.getDescription())
                .civilStatus(updateDto.getCivilStatus())
                .status(true)
                .build();

        //Casos de evaluación dados
        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
        when(personRepository.getPersonByDocumentAndEmailForEdit(
                anyString(),
                anyString(),
                anyLong())
        ).thenReturn(Optional.empty());
        when(personRepository.save(any(PersonEntity.class))).thenReturn(updatedPerson);

        // Ejecución de actualización contra BD
        ResponseWrapper<PersonEntity> result = personService.update(id, updateDto);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(updatedPerson, result.getData());
        assertEquals("Persona Actualizada Correctamente", result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para ejecutar actualización de persona pero no la encontramos por ID")
    void update_WithNonexistentId_ShouldReturnErrorMessage() {

        Long id = 2L; // Un ID que no existe en la lista mock
        UpdatePersonDto updateDto = new UpdatePersonDto(
                "CC",
                "789012",
                "Jane",
                null,
                "Doe",
                null,
                "jane.doe@example.com",
                "F",
                "987654321",
                null,
                "456 Main St",
                null,
                null,
                null,
                "Married"
        );

        //Casos de evaluación dados
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        // Ejecución de actualización contra BD
        ResponseWrapper<PersonEntity> result = personService.update(id, updateDto);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals("La persona no fue encontrada", result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para ejecutar actualización de persona pero Email/Documento repetido")
    void update_WithDuplicateDocumentOrEmail_ShouldReturnErrorMessage() {

        Long id = 1L;
        UpdatePersonDto updateDto = new UpdatePersonDto(
                "CC",
                "123456",
                "John",
                null,
                "Doe",
                null,
                "john.doe@example.com",
                "M",
                "123456789",
                null,
                "123 Main St",
                null,
                null,
                null,
                "Single"
        );

        //Casos de evaluación dados
        PersonEntity existingPerson = PersonMock.createMockPersonsList().get(0);
        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
        when(personRepository.getPersonByDocumentAndEmailForEdit(anyString(), anyString(), anyLong()))
                .thenReturn(Optional.of(existingPerson)); // Simula que existe una persona con el mismo documento o email

        // Ejecución de actualización contra BD
        ResponseWrapper<PersonEntity> result = personService.update(id, updateDto);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals("El email o documento de la persona ya está registrado", result.getErrorMessage());

    }

    @Test
    @DisplayName("Test para ejecutar actualización de persona pero obtuvimos una excepción")
    void update_WithException_ShouldReturnErrorMessage() {

        Long id = 1L;
        UpdatePersonDto updateDto = new UpdatePersonDto(
                "CC",
                "123456",
                "John",
                null,
                "Doe",
                null,
                "john.doe@example.com",
                "M",
                "123456789",
                null,
                "123 Main St",
                null,
                null,
                null,
                "Single"
        );

        // Ejecución de actualización contra BD
        when(personRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        // Ejecución de actualización contra BD
        ResponseWrapper<PersonEntity> result = personService.update(id, updateDto);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals("La persona no pudo ser actualizada", result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para ejecutar eliminación exitosa de persona")
    void delete_WithValidId_ShouldReturnDeletedPerson() {

        Long id = 1L;
        PersonEntity existingPerson = PersonMock.createMockPersonsList().get(0);
        PersonEntity deletedPerson = PersonEntity.builder()
                .status(false) // Cambiado a falso para indicar eliminación lógica
                .userUpdated("usuario123")
                .dateUpdated(new Date())
                .build();

        // Ejecución de actualización contra BD
        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(any(PersonEntity.class))).thenReturn(deletedPerson);

        // Ejecución de actualización contra BD
        ResponseWrapper<PersonEntity> result = personService.delete(id);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(deletedPerson, result.getData());
        assertEquals("Persona Eliminada Correctamente", result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para ejecutar eliminación pero la persona no se encontró por ID")
    void delete_WithNonexistentId_ShouldReturnErrorMessage() {

        Long id = 2L; // Un ID que no existe en la lista mock
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        // Ejecución de actualización contra BD
        ResponseWrapper<PersonEntity> result = personService.delete(id);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals("La persona no fue encontrado", result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para ejecutar eliminación pero obtuvimos una excepción")
    void delete_WithException_ShouldReturnErrorMessage() {

        Long id = 1L;
        when(personRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        // Ejecución de actualización contra BD
        ResponseWrapper<PersonEntity> result = personService.delete(id);

        // Evaluación de casos de prueba
        assertNotNull(result);
        assertNull(result.getData());
        assertEquals("La persona no pudo ser eliminada", result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para buscar por criterio y encontramos información")
    void findPersonIdsByCriteria_WithMatchingResults_ShouldReturnListOfIds() {

        String searchCriteria = "John";
        List<Long> expectedIds = Arrays.asList(1L, 2L); // IDs esperados de la búsqueda
        when(personRepository.findIdsByCriteria(searchCriteria)).thenReturn(expectedIds);

        // Ejecución de actualización contra BD
        List<Long> resultIds = personService.findPersonIdsByCriteria(searchCriteria);

        // Evaluación de casos de prueba
        assertNotNull(resultIds);
        assertEquals(expectedIds.size(), resultIds.size());
        assertTrue(resultIds.containsAll(expectedIds));
    }

    @Test
    @DisplayName("Test para buscar por criterio y no encontramos información")
    void findPersonIdsByCriteria_WithNoMatchingResults_ShouldReturnEmptyList() {

        String searchCriteria = "NonExistent";
        List<Long> expectedIds = Collections.emptyList();
        when(personRepository.findIdsByCriteria(searchCriteria)).thenReturn(expectedIds);

        // Ejecución de actualización contra BD
        List<Long> resultIds = personService.findPersonIdsByCriteria(searchCriteria);

        // Evaluación de casos de prueba
        assertNotNull(resultIds);
        assertTrue(resultIds.isEmpty());
    }

    @Test
    @DisplayName("Test para buscar por criterio pero el criterio está vacío")
    void findPersonIdsByCriteria_WithEmptySearchCriteria_ShouldReturnEmptyList() {

        String searchCriteria = "";
        List<Long> expectedIds = Collections.emptyList();
        when(personRepository.findIdsByCriteria(searchCriteria)).thenReturn(expectedIds);

        // Ejecución de actualización contra BD
        List<Long> resultIds = personService.findPersonIdsByCriteria(searchCriteria);

        // Evaluación de casos de prueba
        assertNotNull(resultIds);
        assertTrue(resultIds.isEmpty());
    }

}
