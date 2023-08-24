package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.AssistanceDTO;
import com.projects.petshopNew.dto.PetDTO;
import com.projects.petshopNew.entities.Assistance;
import com.projects.petshopNew.entities.Pet;
import com.projects.petshopNew.repositories.AssistanceRepository;
import com.projects.petshopNew.repositories.BreedRepository;
import com.projects.petshopNew.repositories.ClientRepository;
import com.projects.petshopNew.repositories.PetRepository;
import com.projects.petshopNew.services.exceptions.DataBaseException;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetService {

    @Autowired
    private PetRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private AssistanceRepository assistanceRepository;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public Page<PetDTO> findAllPaged(Pageable pageable){
        Page<Pet> list = repository.findAll(pageable);
        return list.map(PetDTO::new);
    }

    @Transactional(readOnly = true)
    public PetDTO findById(Long id){
        Pet entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found " + id));
        authService.validateSelfOrAdmin(entity.getClient().getUser().getCpf());
        return new PetDTO(entity);
    }

    @Transactional
    public PetDTO insert(PetDTO dto){
        Pet entity = new Pet();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new PetDTO(entity);
    }

    @Transactional
    public PetDTO update(Long id, PetDTO dto){
        Pet entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found " + id));
        authService.validateSelfOrAdmin(entity.getClient().getUser().getCpf());
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new PetDTO(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }
        try{
            repository.deleteById(id);
        } catch(DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    public void copyDtoToEntity(PetDTO dto, Pet entity){
        entity.setName(dto.getName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setClient(clientRepository.getReferenceById(dto.getClientId()));
        entity.setBreed(breedRepository.getReferenceById(dto.getBreed().getId()));

        entity.getAssistances().clear();
        for(AssistanceDTO assistanceDTO : dto.getAssistances()){
            Assistance assistance = assistanceRepository.getReferenceById(assistanceDTO.getId());
            entity.getAssistances().add(assistance);
        }
    }
}