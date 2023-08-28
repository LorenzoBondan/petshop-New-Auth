package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.BreedDTO;
import com.projects.petshopNew.entities.Breed;
import com.projects.petshopNew.repositories.BreedRepository;
import com.projects.petshopNew.services.exceptions.DataBaseException;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import com.projects.petshopNew.tests.BreedFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class BreedServiceTests {

    @InjectMocks
    private BreedService service;

    @Mock
    private BreedRepository repository;

    private Breed breed;
    private BreedDTO breedDTO;

    private Long existingId, nonExistingId, dependentId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;

        breed = BreedFactory.createBreed();
        breedDTO = new BreedDTO(breed);
        PageImpl<Breed> page = new PageImpl<>(List.of(breed));

        Breed dependentBreed = BreedFactory.createDependentBreed();

        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(breed));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(breed);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(breed);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true); // <-
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
    }

    @Test
    public void findAllShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<BreedDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getSize(), 1);
        Assertions.assertEquals(result.getContent().get(0).getId(), breed.getId());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists(){
        BreedDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findById(existingId);
    }

    @Test
    public void findByIdShouldReturnNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
        Mockito.verify(repository).findById(nonExistingId);
    }

    @Test
    public void insertShouldReturnObject(){
        BreedDTO result = service.insert(breedDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), breed.getId());
    }

    @Test
    public void updateShouldReturnObjectWhenIdExists(){
        BreedDTO result = service.update(existingId, breedDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getDescription(), breedDTO.getDescription());
    }

    @Test
    public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, breedDTO);
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

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenIdIsDependent(){
        Assertions.assertThrows(DataBaseException.class, () -> {
            service.delete(dependentId);
        });
    }
}
