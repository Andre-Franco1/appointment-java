  package com.abutua.appointment.unit.domain.services;
  
  import static org.junit.jupiter.api.Assertions.assertEquals;
  import static org.junit.jupiter.api.Assertions.assertNotNull;
  import static org.junit.jupiter.api.Assertions.assertThrows;
  import static org.junit.jupiter.api.Assertions.assertTrue;
  import static org.mockito.ArgumentMatchers.any;
  import static org.mockito.ArgumentMatchers.anyLong;
  import static org.mockito.ArgumentMatchers.eq;
  import static org.mockito.BDDMockito.given;
  import static org.mockito.Mockito.mock;
  import static org.mockito.Mockito.never;
  import static org.mockito.Mockito.verify;
  import static org.mockito.Mockito.verifyNoInteractions;
  
  import java.time.LocalDate;
  import java.util.List;
  import java.util.Optional;
  import java.util.Set;
  
  import org.junit.jupiter.api.Test;
  import org.junit.jupiter.api.extension.ExtendWith;
  import org.mockito.InjectMocks;
  import org.mockito.Mock;
  import org.mockito.junit.jupiter.MockitoExtension;
  
  import com.abutua.appointment.domain.entities.Area;
  import com.abutua.appointment.domain.entities.Professional;
  import com.abutua.appointment.domain.repositories.AreaRepository;
  import com.abutua.appointment.domain.repositories.ProfessionalRepository;
  import com.abutua.appointment.domain.services.ProfessionalService;
  import com.abutua.appointment.domain.services.usecases.read.SearchProfessionalAvailabilityDaysUseCase;
  import com.abutua.appointment.domain.services.usecases.read.SearchProfessionalAvailabilityTimesUseCase;
  import com.abutua.appointment.dto.AreaResponse;
  import com.abutua.appointment.dto.IntegerDTO;
  import com.abutua.appointment.dto.ProfessionalRequest;
  import com.abutua.appointment.dto.ProfessionalResponse;
  
  import jakarta.persistence.EntityNotFoundException;
  
  @ExtendWith(MockitoExtension.class)
  public class ProfessionalServiceTest {
    
    @Mock
    private ProfessionalRepository professionalRepository;
    
    @InjectMocks
    private ProfessionalService professionalService;
    
    @Mock
    private AreaRepository areaRepository;
    
    @Mock
    private SearchProfessionalAvailabilityDaysUseCase searchProfessionalAvailabilityDays;
    
    @Mock
    private SearchProfessionalAvailabilityTimesUseCase searchProfessionalAvailabilityTimes;
    
    @Test
    void getByIdShouldReturnProfessional() {
        long professionalId = 1L;
      
        Area area = new Area(1, "Fisioterapia");
        Set<Area> areas = Set.of(area);
      
        Professional professional = new Professional(professionalId, "Maria", "11929643234", true);
        professional.setAreas(areas);
      
        given(professionalRepository.findById(professionalId)).willReturn(Optional.of(professional));
      
        ProfessionalResponse response = professionalService.getById(professionalId);
      
        assertNotNull(response);
        assertEquals(professionalId, response.id());
        assertEquals("Maria", response.name());
        assertEquals("11929643234", response.phone());
        assertTrue(response.active());
      
        assertEquals(1, response.areas().size());
        assertTrue(response.areas().stream().anyMatch(a -> a.id() == 1 && a.name().equals("Fisioterapia")));
        
        verify(professionalRepository).findById(professionalId);
      
    }
    
    @Test
    void getByIdShouldThrowEntityNotFoundExceptionWhenProfessionalDoesNotExist() {
        long professionalId = 99L;
        
        given(professionalRepository.findById(professionalId)).willReturn(Optional.empty());
        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> professionalService.getById(professionalId));
        
        assertEquals("Profissional não encontrado.", exception.getMessage());
        
        verify(professionalRepository).findById(professionalId);
    }
    
    @Test
    void saveShouldPersistProfessional() {
      
        Set<IntegerDTO> areas = Set.of(
        new IntegerDTO(1),
        new IntegerDTO(2));
        
        Set<AreaResponse> responseAreas = Set.of(
        new AreaResponse(1, "Fisioterapia"),
        new AreaResponse(2, "Terapia Ocupacional"));
        
        given(areaRepository.findAllById(Set.of(1, 2))).willReturn(
        List.of(
        new Area(1, "Fisioterapia"),
        new Area(2, "Terapia Ocupacional")));
        
        given(professionalRepository.save(any(Professional.class)))
        .willAnswer(invocation -> {
                Professional p = invocation.getArgument(0);
                p.setId(1L);
                return p;
        });
        
        ProfessionalRequest newProfessionalRequest = new ProfessionalRequest("Maria", "11929643234", true, areas);
        ProfessionalResponse expProfessionalResponse = new ProfessionalResponse(1L, "Maria", "11929643234",
        responseAreas, true);
        
        var foundProfessionalResponse = professionalService.save(newProfessionalRequest);
        
        verify(professionalRepository).save(any(Professional.class));
        
        assertNotNull(foundProfessionalResponse);
        assertEquals(expProfessionalResponse.id(), foundProfessionalResponse.id());
        assertEquals(expProfessionalResponse.name(), foundProfessionalResponse.name());
        assertEquals(expProfessionalResponse.phone(), foundProfessionalResponse.phone());
        assertEquals(expProfessionalResponse.active(), foundProfessionalResponse.active());
        assertEquals(expProfessionalResponse.areas(), foundProfessionalResponse.areas());
    }
    
    @Test
    void updateShouldThrowEntityNotFoundException() {
        given(professionalRepository.getReferenceById(anyLong())).willThrow(EntityNotFoundException.class);
        ProfessionalRequest professionalRequest = mock(ProfessionalRequest.class);
        assertThrows(EntityNotFoundException.class, () -> professionalService.update(anyLong(), professionalRequest));
        verify(professionalRepository).getReferenceById(any(Long.class));
    }
    
    @Test
    void deleteShouldRemoveProfessional() {
        long professionalId = 1L;
        
        given(professionalRepository.existsById(professionalId)).willReturn(true);
        
        
        professionalService.deleteById(professionalId);
        
        verify(professionalRepository).existsById(professionalId);
        verify(professionalRepository).deleteById(professionalId);
    }
    
    @Test
    void deleteByIdShouldThrowEntityNotFoundExceptionWhenProfessionalDoesNotExist() {
        long professionalId = 1L;
        
        given(professionalRepository.existsById(professionalId))
        .willReturn(false);
        
        EntityNotFoundException exception = assertThrows(
        EntityNotFoundException.class,
        () -> professionalService.deleteById(professionalId)
        );
        
        assertEquals("Profissional não encontrado.", exception.getMessage());
        
        verify(professionalRepository).existsById(professionalId);
        verify(professionalRepository, never()).deleteById(anyLong());
    }
    
    
    
    @Test
    void getAvailabilityDaysFromProfessionalShouldReturnAvailableDays() {
        long professionalId = 1L;
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        
        List<Integer> availableDays = List.of(5, 10, 15, 20);
        
        given(professionalRepository.existsById(professionalId)).willReturn(true);
        
        given(searchProfessionalAvailabilityDays.executeUseCase(
        eq(professionalId),
        any(LocalDate.class),
        any(LocalDate.class)))
        .willReturn(availableDays);
        
        List<Integer> result = professionalService.getAvailabilityDaysFromProfessional(professionalId, month, year);
        
        assertEquals(availableDays, result);
        
        verify(professionalRepository).existsById(professionalId);
        verify(searchProfessionalAvailabilityDays).executeUseCase(eq(professionalId), any(LocalDate.class),
        any(LocalDate.class));
    }
    
    @Test
    void getAvailabilityDaysFromProfessionalShouldThrowExceptionWhenProfessionalDoesNotExist() {
        long professionalId = 1L;
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        
        given(professionalRepository.existsById(professionalId)).willReturn(false);
        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
        () -> professionalService.getAvailabilityDaysFromProfessional(professionalId, month, year));
        
        assertEquals("Profissional não encontrado.", exception.getMessage());
        
        verify(professionalRepository).existsById(professionalId);
        verifyNoInteractions(searchProfessionalAvailabilityDays);
    }
    
  }
  