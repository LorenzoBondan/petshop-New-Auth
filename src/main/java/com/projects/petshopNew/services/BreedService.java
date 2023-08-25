package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.BreedDTO;
import com.projects.petshopNew.entities.Breed;
import com.projects.petshopNew.entities.Pet;
import com.projects.petshopNew.repositories.BreedRepository;
import com.projects.petshopNew.repositories.PetRepository;
import com.projects.petshopNew.services.exceptions.DataBaseException;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BreedService {

    @Autowired
    private BreedRepository repository;

    @Autowired
    private PetRepository petRepository;

    @Transactional(readOnly = true)
    public Page<BreedDTO> findAllPaged(Pageable pageable){
        Page<Breed> list = repository.findAll(pageable);
        return list.map(BreedDTO::new);
    }

    @Transactional(readOnly = true)
    public BreedDTO findById(Long id){
        Breed entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Breed Id not found: " + id));
        return new BreedDTO(entity);
    }

    @Transactional
    public BreedDTO insert(BreedDTO dto){
        Breed entity = new Breed();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new BreedDTO(entity);
    }

    @Transactional
    public BreedDTO update(Long id, BreedDTO dto){
        Breed entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Breed Id not found: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new BreedDTO(entity);
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

    public void copyDtoToEntity(BreedDTO dto, Breed entity){
        entity.setDescription(dto.getDescription());

        entity.getPets().clear();
        for(Long petId : dto.getPetsId()){
            Pet pet = petRepository.getReferenceById(petId);
            entity.getPets().add(pet);
        }
    }
}
