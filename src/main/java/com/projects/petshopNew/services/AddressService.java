package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.AddressDTO;
import com.projects.petshopNew.entities.Address;
import com.projects.petshopNew.repositories.AddressRepository;
import com.projects.petshopNew.repositories.ClientRepository;
import com.projects.petshopNew.services.exceptions.InvalidDataException;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public AddressDTO update(Long id, AddressDTO dto){
        Address entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address Id not found: " + id));
        authService.validateSelfOrAdmin(entity.getClient().getUser().getCpf());
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new AddressDTO(entity);
    }

    private void copyDtoToEntity(AddressDTO dto, Address entity){
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setComplement(dto.getComplement());
        entity.setNeighborhood(dto.getNeighborhood());
        entity.setTag(dto.getTag());
        entity.setClient(clientRepository.getReferenceById(dto.getClientId()));
    }
}
