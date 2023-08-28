package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.AddressDTO;
import com.projects.petshopNew.entities.Address;
import com.projects.petshopNew.entities.Client;
import com.projects.petshopNew.repositories.AddressRepository;
import com.projects.petshopNew.repositories.ClientRepository;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import com.projects.petshopNew.tests.AddressFactory;
import com.projects.petshopNew.tests.ClientFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class AddressServiceTests {

    @InjectMocks
    private AddressService service;

    @Mock
    private AddressRepository repository;

    @Mock
    private ClientRepository clientRepository;

    private AddressDTO addressDTO;

    private Long existingId, nonExistingId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;

        Address address = AddressFactory.createAddress();
        addressDTO = new AddressDTO(address);

        //dependence
        Client client = ClientFactory.createClient();
        client.setAddress(address);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(address));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(address);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(address);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        //

        Mockito.when(clientRepository.findById(existingId)).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(clientRepository.save(ArgumentMatchers.any())).thenReturn(client);

        Mockito.when(clientRepository.getReferenceById(existingId)).thenReturn(client);
        Mockito.when(clientRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void updateShouldReturnObjectWhenIdExists(){
        AddressDTO result = service.update(existingId, addressDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getStreet(), addressDTO.getStreet());
        Assertions.assertEquals(result.getCity(), addressDTO.getCity());
    }

    @Test
    public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, addressDTO);
        });
    }
}
