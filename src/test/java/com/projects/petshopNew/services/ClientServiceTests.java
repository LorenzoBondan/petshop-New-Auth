package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.ClientDTO;
import com.projects.petshopNew.entities.*;
import com.projects.petshopNew.repositories.*;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import com.projects.petshopNew.tests.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ContactRepository contactRepository;

    private Client client;
    private ClientDTO clientDTO;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;

        String existingCpf = "123.456.789-10";
        String nonExistingCpf = "999.999.999-99";

        client = ClientFactory.createClient();

        PageImpl<Client> page = new PageImpl<>(List.of(client));

        User user = UserFactory.createClientUser();
        client.setUser(user);

        Address address = AddressFactory.createAddress();
        client.setAddress(address);

        Contact contact = ContactFactory.createContact();
        client.setContact(contact);

        clientDTO = new ClientDTO(client);

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(client));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(client);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(client);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //

        Mockito.when(userRepository.findByCpf(existingCpf)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByCpf(nonExistingCpf)).thenReturn(Optional.empty());

        //

        Mockito.when(addressRepository.findById(existingId)).thenReturn(Optional.of(address));
        Mockito.when(addressRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(addressRepository.save(ArgumentMatchers.any())).thenReturn(address);

        Mockito.when(addressRepository.getReferenceById(existingId)).thenReturn(address);
        Mockito.when(addressRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //

        Mockito.when(contactRepository.findById(existingId)).thenReturn(Optional.of(contact));
        Mockito.when(contactRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(contactRepository.save(ArgumentMatchers.any())).thenReturn(contact);

        Mockito.when(contactRepository.getReferenceById(existingId)).thenReturn(contact);
        Mockito.when(contactRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ClientDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getSize(), 1);
        Assertions.assertEquals(result.getContent().get(0).getId(), client.getId());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists(){
        ClientDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), client.getId());
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
    public void updateShouldReturnObjectWhenIdExists(){
        ClientDTO result = service.update(existingId, clientDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getAddress(), clientDTO.getAddress());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, clientDTO);
        });
    }
}
