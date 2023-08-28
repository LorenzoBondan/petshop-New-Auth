package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.PetDTO;
import com.projects.petshopNew.entities.Breed;
import com.projects.petshopNew.entities.Client;
import com.projects.petshopNew.entities.Pet;
import com.projects.petshopNew.repositories.BreedRepository;
import com.projects.petshopNew.repositories.ClientRepository;
import com.projects.petshopNew.repositories.PetRepository;
import com.projects.petshopNew.services.exceptions.DataBaseException;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import com.projects.petshopNew.tests.BreedFactory;
import com.projects.petshopNew.tests.ClientFactory;
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
public class PetServiceTests {

    @InjectMocks
    private PetService service;

    @Mock
    private PetRepository repository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BreedRepository breedRepository;

    private PetDTO petDTO;

    private Long existingId, nonExistingId, dependentId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;

        Pet pet = PetFactory.createPet();

        PageImpl<Pet> page = new PageImpl<>(List.of(pet));

        Client client = ClientFactory.createClient();
        pet.setClient(client);

        Breed breed = BreedFactory.createBreed();
        pet.setBreed(breed);

        Pet dependentPet = PetFactory.createDependentPet();
        dependentPet.setBreed(breed);
        dependentPet.setClient(client);

        petDTO = new PetDTO(pet);

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(pet));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(pet);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(pet);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true); // <-
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

        //

        Mockito.when(clientRepository.findById(existingId)).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(clientRepository.save(ArgumentMatchers.any())).thenReturn(client);

        Mockito.when(clientRepository.getReferenceById(existingId)).thenReturn(client);
        Mockito.when(clientRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //

        Mockito.when(breedRepository.findById(existingId)).thenReturn(Optional.of(breed));
        Mockito.when(breedRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(breedRepository.save(ArgumentMatchers.any())).thenReturn(breed);

        Mockito.when(breedRepository.getReferenceById(existingId)).thenReturn(breed);
        Mockito.when(breedRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<PetDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getSize(), 1);
        Assertions.assertEquals(result.getContent().get(0).getId(), existingId);
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists(){
        PetDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Mockito.verify(repository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void insertShouldReturnObject(){
        PetDTO result = service.insert(petDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), petDTO.getId());
        Assertions.assertEquals(result.getName(), petDTO.getName());
    }

    @Test
    public void updateShouldReturnObjectWhenIdExists(){
        PetDTO result = service.update(existingId, petDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getName(), petDTO.getName());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, petDTO);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExistsAndNotDependent(){
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
