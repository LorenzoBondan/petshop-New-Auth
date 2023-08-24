package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.ContactDTO;
import com.projects.petshopNew.entities.Contact;
import com.projects.petshopNew.repositories.ClientRepository;
import com.projects.petshopNew.repositories.ContactRepository;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public ContactDTO update(Long id, ContactDTO dto){
        Contact entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact Id not found: " + id));
        authService.validateSelfOrAdmin(entity.getClient().getUser().getCpf());
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ContactDTO(entity);
    }

    public void copyDtoToEntity(ContactDTO dto, Contact entity){
        entity.setTag(dto.getTag());
        entity.setType(dto.getType());
        entity.setContactValue(dto.getContactValue());
        entity.setClient(clientRepository.getReferenceById(dto.getClientId()));
    }
}
