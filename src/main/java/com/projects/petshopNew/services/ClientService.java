package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.ClientDTO;
import com.projects.petshopNew.dto.PetDTO;
import com.projects.petshopNew.entities.Client;
import com.projects.petshopNew.entities.Pet;
import com.projects.petshopNew.repositories.*;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(Pageable pageable){
        Page<Client> list = repository.findAll(pageable);
        return list.map(ClientDTO::new);
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Client entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client Id not found: " + id));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto){
        Client entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client Id not found: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }

    public void copyDtoToEntity(ClientDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setImgUrl(dto.getImgUrl());
        entity.setUser(userRepository.findByCpf(dto.getUserCpf()).orElseThrow(() -> new ResourceNotFoundException("At clientService, User Cpf not found: " + dto.getUserCpf())));
        entity.setAddress(addressRepository.getReferenceById(dto.getAddress().getId()));
        entity.setContact(contactRepository.getReferenceById(dto.getContact().getId()));

        entity.getPets().clear();
        for(PetDTO petDTO : dto.getPets()){
            Pet pet = petRepository.getReferenceById(petDTO.getId());
            entity.getPets().add(pet);
        }
    }
}
