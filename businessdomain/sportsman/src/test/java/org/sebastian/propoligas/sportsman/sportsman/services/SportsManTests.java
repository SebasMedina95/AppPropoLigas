package org.sebastian.propoligas.sportsman.sportsman.services;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.sebastian.propoligas.sportsman.sportsman.clients.PersonClientRest;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ApiResponse;
import org.sebastian.propoligas.sportsman.sportsman.common.utils.ResponseWrapper;
import org.sebastian.propoligas.sportsman.sportsman.data.SportsManMock;
import org.sebastian.propoligas.sportsman.sportsman.models.Persons;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.create.CreateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.dtos.update.UpdateSportsManDto;
import org.sebastian.propoligas.sportsman.sportsman.models.entities.SportsManEntity;
import org.sebastian.propoligas.sportsman.sportsman.repositories.SportsManRepository;
import org.sebastian.propoligas.sportsman.sportsman.services.impls.SportsManServiceImpl;

import org.springframework.data.domain.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SportsManTests {

    @Mock
    private SportsManRepository sportsManRepository;

    @Mock
    private PersonClientRest personClientRest;

    @InjectMocks
    private SportsManServiceImpl sportsManService;

    @BeforeEach
    void setUp() {
        sportsManRepository = Mockito.mock(SportsManRepository.class);
        personClientRest = Mockito.mock(PersonClientRest.class);
        sportsManService = new SportsManServiceImpl(sportsManRepository, personClientRest);
    }

    @Test
    @DisplayName("Test para registrar un deportista exitosamente")
    void testCreateSportsManSuccess() {
        // Preparar datos de prueba
        CreateSportsManDto sportsManDto = SportsManMock.createSportsManDto();
        SportsManEntity sportsManEntity = SportsManMock.createSportsManEntity();
        Persons personData = SportsManMock.createPersonDto();

        // Configurar comportamiento de los mocks
        // Generamos un ApiResponse extra para generar la estructura de salida de la petición Feign
        ApiResponse<Persons> apiResponse = new ApiResponse<>(personData, null);
        when(personClientRest.getPerson(sportsManDto.getPersonId())).thenReturn(apiResponse);
        when(sportsManRepository.getSportManByPersonId(sportsManDto.getPersonId())).thenReturn(Optional.empty());
        when(sportsManRepository.save(any(SportsManEntity.class))).thenReturn(sportsManEntity);

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> result = sportsManService.create(sportsManDto);

        // Verificar resultados
        assertNotNull(result.getData());
        assertEquals(sportsManEntity, result.getData());
        assertEquals("El deportista fue creado correctamente", result.getErrorMessage());
    }

    @Test
    @DisplayName("Test para registrar un deportista pero no encontramos a la persona")
    void testCreateSportsManPersonNotFound() {

        CreateSportsManDto createSportsManDto = SportsManMock.createSportsManDto();
        when(personClientRest.getPerson(anyLong())).thenReturn(new ApiResponse<>(null, null));

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.create(createSportsManDto);

        // Verificar resultados
        assertNull(response.getData());
        assertEquals("La persona para ser asociada al deportista no fue hallada en la búsqueda", response.getErrorMessage());
        verify(sportsManRepository, never()).save(any(SportsManEntity.class));

    }

    @Test
    @DisplayName("Test para registrar un deportista pero ya existía")
    void testCreateSportsManAlreadyRegistered() {

        CreateSportsManDto createSportsManDto = SportsManMock.createSportsManDto();
        SportsManEntity sportsManEntity = SportsManMock.createSportsManEntity();
        Persons personDto = SportsManMock.createPersonDto();

        // Configurar comportamiento de los mocks
        when(personClientRest.getPerson(anyLong())).thenReturn(new ApiResponse<>(personDto, null));
        when(sportsManRepository.getSportManByPersonId(anyLong())).thenReturn(Optional.of(sportsManEntity));

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.create(createSportsManDto);

        // Verificar resultados
        assertNull(response.getData());
        assertEquals("La persona ya se encuentra asociada como deportista", response.getErrorMessage());
        verify(sportsManRepository, never()).save(any(SportsManEntity.class));
    }

    @Test
    @DisplayName("Test para listar deportistas paginados pero con filtro")
    void testFindAllWithSearchCriteria() {

        String searchCriteria = "John";
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> personIds = List.of(12345L, 67890L);
        ApiResponse<List<Long>> personIdsResponse = new ApiResponse<>(personIds, null);
        List<SportsManEntity> sportsManEntities = SportsManMock.createSportsManEntities(); // Crear una lista de entidades
        Page<SportsManEntity> sportsManPage = new PageImpl<>(sportsManEntities, pageable, sportsManEntities.size());

        // Configurar comportamiento de los mocks
        when(personClientRest.findPersonIdsByCriteria(searchCriteria)).thenReturn(personIdsResponse);
        when(sportsManRepository.findFilteredSportsMen(searchCriteria, personIds, pageable)).thenReturn(sportsManPage);
        when(personClientRest.getPerson(anyLong())).thenAnswer(invocation -> {
            Long personId = invocation.getArgument(0);
            return new ApiResponse<>(SportsManMock.createPersonDto(personId), null);
        });

        // Llamar al método que se está probando
        Page<SportsManEntity> result = sportsManService.findAll(searchCriteria, pageable);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(sportsManEntities.size(), result.getTotalElements());
        verify(personClientRest, times(1)).findPersonIdsByCriteria(searchCriteria);
        verify(sportsManRepository, times(1)).findFilteredSportsMen(searchCriteria, personIds, pageable);
    }

    @Test
    @DisplayName("Test para listar deportistas paginados pero sin filtro")
    void testFindAllWithoutSearchCriteria() {

        String searchCriteria = "";
        Pageable pageable = PageRequest.of(0, 10);
        List<SportsManEntity> sportsManEntities = SportsManMock.createSportsManEntities(); // Crear una lista de entidades
        Page<SportsManEntity> sportsManPage = new PageImpl<>(sportsManEntities, pageable, sportsManEntities.size());

        // Configurar comportamiento de los mocks
        when(sportsManRepository.findNoFilteredSportsMen(pageable)).thenReturn(sportsManPage);
        when(personClientRest.getPerson(anyLong())).thenAnswer(invocation -> {
            Long personId = invocation.getArgument(0);
            return new ApiResponse<>(SportsManMock.createPersonDto(personId), null);
        });

        // Llamar al método que se está probando
        Page<SportsManEntity> result = sportsManService.findAll(searchCriteria, pageable);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(sportsManEntities.size(), result.getTotalElements());
        verify(sportsManRepository, times(1)).findNoFilteredSportsMen(pageable);
        verify(personClientRest, times(sportsManEntities.size())).getPerson(anyLong());
    }

    @Test
    @DisplayName("Test para listar deportistas pero generando una excepción por criterio de filtro")
    void testFindAllWithSearchCriteriaFeignException() {

        String searchCriteria = "John";
        Pageable pageable = PageRequest.of(0, 10);

        // Configurar comportamiento de los mocks
        when(personClientRest.findPersonIdsByCriteria(searchCriteria)).thenThrow(FeignException.NotFound.class);

        // Verificar resultados
        assertThrows(FeignException.NotFound.class, () -> {
            sportsManService.findAll(searchCriteria, pageable);
        });
        verify(sportsManRepository, times(0)).findFilteredSportsMen(anyString(), anyList(), any(Pageable.class));
    }

    @Test
    @DisplayName("Test para listar deportistas pero no se encontró información por filtro")
    void testFindAllWithSearchCriteriaNoResults() {

        String searchCriteria = "NonExisting";
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> personIds = List.of();
        ApiResponse<List<Long>> personIdsResponse = new ApiResponse<>(personIds, null);
        Page<SportsManEntity> sportsManPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        // Configurar comportamiento de los mocks
        when(personClientRest.findPersonIdsByCriteria(searchCriteria)).thenReturn(personIdsResponse);
        when(sportsManRepository.findFilteredSportsMen(searchCriteria, personIds, pageable)).thenReturn(sportsManPage);

        // Llamar al método que se está probando
        Page<SportsManEntity> result = sportsManService.findAll(searchCriteria, pageable);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(personClientRest, times(1)).findPersonIdsByCriteria(searchCriteria);
        verify(sportsManRepository, times(1)).findFilteredSportsMen(searchCriteria, personIds, pageable);

    }

    @Test
    @DisplayName("Test para obtener exitosamente un deportista por ID")
    void testFindByIdSuccess() {

        Long sportsManId = 1L;
        SportsManEntity mockSportsMan = SportsManMock.createSportsManEntity();
        mockSportsMan.setId(sportsManId);

        // Configurar comportamiento de los mocks
        Persons mockPerson = SportsManMock.createPersonDto(mockSportsMan.getPersonId());
        when(sportsManRepository.findById(sportsManId)).thenReturn(Optional.of(mockSportsMan));
        when(personClientRest.getPerson(mockSportsMan.getPersonId())).thenReturn(new ApiResponse<>(mockPerson, null));

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.findById(sportsManId);

        // Verificar resultados
        assertNotNull(response);
        assertEquals("Deportista encontrado por ID correctamente", response.getErrorMessage());
        assertNotNull(response.getData());
        assertEquals(sportsManId, response.getData().getId());
        assertEquals(mockPerson, response.getData().getPerson());
    }

    @Test
    @DisplayName("Test para obtener exitosamente un deportista por ID pero no encontré la persona desde su MS")
    void testFindByIdPersonNotFound() {

        Long sportsManId = 1L;
        SportsManEntity mockSportsMan = SportsManMock.createSportsManEntity();
        mockSportsMan.setId(sportsManId);

        // Configurar comportamiento de los mocks
        when(sportsManRepository.findById(sportsManId)).thenReturn(Optional.of(mockSportsMan));
        when(personClientRest.getPerson(mockSportsMan.getPersonId())).thenReturn(new ApiResponse<>(null, null));

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.findById(sportsManId);

        // Verificar resultados
        assertNotNull(response);
        assertEquals("Deportista encontrado por ID correctamente", response.getErrorMessage());
        assertNotNull(response.getData());
        assertNull(response.getData().getPerson());
    }

    @Test
    @DisplayName("Test para obtener un deportista pero no lo encontramos por ID")
    void testFindByIdNotFound() {

        // Configurar comportamiento de los mocks
        Long sportsManId = 1L;
        when(sportsManRepository.findById(sportsManId)).thenReturn(Optional.empty());

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.findById(sportsManId);

        // Verificar resultados
        assertNotNull(response);
        assertEquals("El deportista no pudo ser encontrado por el ID " + sportsManId, response.getErrorMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("Test para obtener un deportista pero se generó una excepción")
    void testFindByIdException() {

        // Configurar comportamiento de los mocks
        Long sportsManId = 1L;
        when(sportsManRepository.findById(sportsManId)).thenThrow(new RuntimeException("Database error"));

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.findById(sportsManId);

        // Verificar resultados
        assertNotNull(response);
        assertEquals("El deportista no pudo ser encontrado por el ID", response.getErrorMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("Test para actualizar un deportista exitosamente")
    void testUpdateSuccess() {

        Long sportsManId = 1L;
        UpdateSportsManDto updateSportsManDto = SportsManMock.createUpdateSportsManDto();
        SportsManEntity existingSportsMan = SportsManMock.createSportsManEntity();
        existingSportsMan.setId(sportsManId);

        // Configurar comportamiento de los mocks
        Persons mockPerson = SportsManMock.createPersonDto(updateSportsManDto.getPersonId());
        when(sportsManRepository.findById(sportsManId)).thenReturn(Optional.of(existingSportsMan));
        when(personClientRest.getPerson(updateSportsManDto.getPersonId())).thenReturn(new ApiResponse<>(mockPerson, null));
        when(sportsManRepository.getSportsManByPersonForEdit(updateSportsManDto.getPersonId(), sportsManId)).thenReturn(Optional.empty());
        when(sportsManRepository.save(any(SportsManEntity.class))).thenReturn(existingSportsMan);

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.update(sportsManId, updateSportsManDto);

        // Verificar resultados
        assertNotNull(response);
        assertEquals("Deportista Actualizado Correctamente", response.getErrorMessage());
        assertNotNull(response.getData());
        assertEquals(sportsManId, response.getData().getId());
        assertEquals(updateSportsManDto.getNumberShirt(), response.getData().getNumberShirt());
        assertEquals(updateSportsManDto.getNameShirt(), response.getData().getNameShirt());

    }

    @Test
    @DisplayName("Test para actualizar un deportista pero no encontré la persona")
    void testUpdatePersonNotFound() {

        Long sportsManId = 1L;
        UpdateSportsManDto updateSportsManDto = SportsManMock.createUpdateSportsManDto();
        SportsManEntity existingSportsMan = SportsManMock.createSportsManEntity();
        existingSportsMan.setId(sportsManId);

        // Configurar comportamiento de los mocks
        when(sportsManRepository.findById(sportsManId)).thenReturn(Optional.of(existingSportsMan));
        when(personClientRest.getPerson(updateSportsManDto.getPersonId())).thenReturn(new ApiResponse<>(null, null));

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.update(sportsManId, updateSportsManDto);

        // Verificar resultados
        assertNotNull(response);
        assertEquals("La persona para ser asociada al deportista no fue hallada", response.getErrorMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("Test para actualizar un deportista pero ya se encuentra registrado")
    void testUpdatePersonAlreadyRegistered() {

        Long sportsManId = 1L;
        UpdateSportsManDto updateSportsManDto = SportsManMock.createUpdateSportsManDto();
        SportsManEntity existingSportsMan = SportsManMock.createSportsManEntity();
        existingSportsMan.setId(sportsManId);

        // Configurar comportamiento de los mocks
        Persons mockPerson = SportsManMock.createPersonDto(updateSportsManDto.getPersonId());
        when(sportsManRepository.findById(sportsManId)).thenReturn(Optional.of(existingSportsMan));
        when(personClientRest.getPerson(updateSportsManDto.getPersonId())).thenReturn(new ApiResponse<>(mockPerson, null));
        when(sportsManRepository.getSportsManByPersonForEdit(updateSportsManDto.getPersonId(), sportsManId)).thenReturn(Optional.of(existingSportsMan));

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.update(sportsManId, updateSportsManDto);

        // Verificar resultados
        assertNotNull(response);
        assertEquals("La persona que está intentando asociar como deportista ya está registrado", response.getErrorMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("Test para actualizar un deportista pero no fue encontrado el deportista")
    void testUpdateNotFound() {

        Long sportsManId = 1L;
        UpdateSportsManDto updateSportsManDto = SportsManMock.createUpdateSportsManDto();

        // Configurar comportamiento de los mocks
        when(sportsManRepository.findById(sportsManId)).thenReturn(Optional.empty());

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.update(sportsManId, updateSportsManDto);

        // Verificar resultados
        assertNotNull(response);
        assertEquals("El deportista no fue encontrado", response.getErrorMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("Test para actualizar un deportista pero obtuve una excepción")
    void testUpdateException() {

        Long sportsManId = 1L;
        UpdateSportsManDto updateSportsManDto = SportsManMock.createUpdateSportsManDto();

        // Configurar comportamiento de los mocks
        when(sportsManRepository.findById(sportsManId)).thenThrow(new RuntimeException("Database error"));

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.update(sportsManId, updateSportsManDto);

        // Verificar resultados
        assertNotNull(response);
        assertEquals("El deportista no pudo ser actualizado", response.getErrorMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("Test para eliminar lógicamente un deportista exitosamente")
    void deleteSportsManSuccess() {

        Long id = 1L;
        SportsManEntity sportsManEntity = SportsManMock.createSportsManEntity();
        sportsManEntity.setStatus(true); // Estado inicial activo

        // Configurar comportamiento de los mocks
        when(sportsManRepository.findById(id)).thenReturn(Optional.of(sportsManEntity));
        when(sportsManRepository.save(any(SportsManEntity.class))).thenReturn(sportsManEntity);

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.delete(id);

        // Verificar resultados
        assertNotNull(response);
        assertFalse((boolean) response.getData().getStatus()); // Verifica que el estado es cambiado a false
        assertEquals("Deportista Eliminado Correctamente", response.getErrorMessage());
        verify(sportsManRepository, times(1)).findById(id);
        verify(sportsManRepository, times(1)).save(any(SportsManEntity.class));

    }

    @Test
    @DisplayName("Test para eliminar lógicamente un deportista pero no fue encontrado")
    void deleteSportsManNotFound() {

        Long id = 1L;
        when(sportsManRepository.findById(id)).thenReturn(Optional.empty());

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.delete(id);

        // Verificar resultados
        assertNotNull(response);
        assertNull(response.getData());
        assertEquals("El deportista no fue encontrado", response.getErrorMessage());
        verify(sportsManRepository, times(1)).findById(id);
        verify(sportsManRepository, never()).save(any(SportsManEntity.class));
    }

    @Test
    @DisplayName("Test para eliminar lógicamente un deportista pero se generó una excepción")
    void deleteSportsManException() {

        Long id = 1L;
        when(sportsManRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        // Llamar al método que se está probando
        ResponseWrapper<SportsManEntity> response = sportsManService.delete(id);

        // Verificar resultados
        assertNotNull(response);
        assertNull(response.getData());
        assertEquals("El deportista no pudo ser eliminada", response.getErrorMessage());
        verify(sportsManRepository, times(1)).findById(id);
        verify(sportsManRepository, never()).save(any(SportsManEntity.class));
    }

}
