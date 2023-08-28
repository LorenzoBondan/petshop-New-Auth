package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.AssistanceDTO;
import com.projects.petshopNew.entities.Assistance;
import com.projects.petshopNew.entities.Pet;
import com.projects.petshopNew.repositories.AssistanceRepository;
import com.projects.petshopNew.repositories.PetRepository;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import com.projects.petshopNew.tests.AssistanceFactory;
import com.projects.petshopNew.tests.PetFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class AssistanceServiceTests {

    @InjectMocks
    private AssistanceService service;

    @Mock
    private AssistanceRepository repository;

    @Mock
    private PetRepository petRepository;

    private Assistance assistance;
    private AssistanceDTO assistanceDTO;

    private Long existingId, nonExistingId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;

        assistance = AssistanceFactory.createAssistance();
        assistanceDTO = new AssistanceDTO(assistance);

        List<Assistance> list = new ArrayList<>();
        list.add(assistance);

        PageImpl<Assistance> page = new PageImpl<>(List.of(assistance));

        // dependence
        Pet pet = PetFactory.createPet();
        assistance.setPet(pet);

        Mockito.when(repository.findAll()).thenReturn(list);

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(assistance));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(assistance);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(assistance);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

        //

        Mockito.when(petRepository.findById(existingId)).thenReturn(Optional.of(pet));
        Mockito.when(petRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(petRepository.save(ArgumentMatchers.any())).thenReturn(pet);

        Mockito.when(petRepository.getReferenceById(existingId)).thenReturn(pet);
        Mockito.when(petRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void findAllShouldReturnList(){
        List<AssistanceDTO> result = service.findAll();
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertEquals(result.get(0).getId(), assistance.getId());
        Assertions.assertEquals(result.get(0).getDescription(), assistance.getDescription());
    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<AssistanceDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getSize(), 1);
        Assertions.assertEquals(result.getContent().get(0).getId(), assistance.getId());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists(){
        AssistanceDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
        Mockito.verify(repository).findById(nonExistingId);
    }

    @Test
    public void insertShouldReturnObject(){
        AssistanceDTO result = service.insert(assistanceDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), assistance.getId());
    }

    @Test
    public void updateShouldReturnObjectWhenIdExists(){
        AssistanceDTO result = service.update(existingId, assistanceDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getDescription(), assistanceDTO.getDescription());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, assistanceDTO);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }
}
