package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.AssistanceDTO;
import com.projects.petshopNew.entities.Assistance;
import com.projects.petshopNew.repositories.AssistanceRepository;
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

import java.util.List;

@Service
public class AssistanceService {

    @Autowired
    private AssistanceRepository repository;

    @Autowired
    private PetRepository petRepository;

    @Transactional(readOnly = true)
    public Page<AssistanceDTO> findAllPaged(Pageable pageable){
        Page<Assistance> list = repository.findAll(pageable);
        return list.map(AssistanceDTO::new);
    }

    @Transactional(readOnly = true)
    public List<AssistanceDTO> findAll(){
        return repository.findAll().stream().map(AssistanceDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public AssistanceDTO findById(Long id){
        Assistance entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Assistance Id not found: " + id));
        return new AssistanceDTO(entity);
    }

    @Transactional
    public AssistanceDTO insert(AssistanceDTO dto){
        Assistance entity = new Assistance();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new AssistanceDTO(entity);
    }

    @Transactional
    public AssistanceDTO update(Long id, AssistanceDTO dto){
        Assistance entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Assistance Id not found: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new AssistanceDTO(entity);
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

    public void copyDtoToEntity(AssistanceDTO dto, Assistance entity){
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setAssistanceValue(dto.getAssistanceValue());
        entity.setPet(petRepository.getReferenceById(dto.getPetId()));
    }
}
