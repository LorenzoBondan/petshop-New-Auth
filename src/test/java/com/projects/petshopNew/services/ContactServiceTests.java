package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.ContactDTO;
import com.projects.petshopNew.entities.Client;
import com.projects.petshopNew.entities.Contact;
import com.projects.petshopNew.repositories.ClientRepository;
import com.projects.petshopNew.repositories.ContactRepository;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import com.projects.petshopNew.tests.ClientFactory;
import com.projects.petshopNew.tests.ContactFactory;
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
public class ContactServiceTests {

    @InjectMocks
    private ContactService service;

    @Mock
    private ContactRepository repository;

    @Mock
    private ClientRepository clientRepository;

    private ContactDTO contactDTO;

    private Long existingId, nonExistingId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;

        Contact contact = ContactFactory.createContact();
        contactDTO = new ContactDTO(contact);

        // dependence
        Client client = ClientFactory.createClient();
        client.setContact(contact);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(contact));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(contact);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(contact);
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
        ContactDTO result = service.update(existingId, contactDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getContactValue(), contactDTO.getContactValue());
        Assertions.assertEquals(result.getTag(), contactDTO.getTag());
    }

    @Test
    public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, contactDTO);
        });
    }
}
