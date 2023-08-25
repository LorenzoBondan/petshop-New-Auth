package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.RoleDTO;
import com.projects.petshopNew.dto.UserDTO;
import com.projects.petshopNew.dto.UserInsertDTO;
import com.projects.petshopNew.entities.*;
import com.projects.petshopNew.projections.UserDetailsProjection;
import com.projects.petshopNew.repositories.*;
import com.projects.petshopNew.services.exceptions.DataBaseException;
import com.projects.petshopNew.services.exceptions.ForbiddenException;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AssistanceRepository assistanceRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(String name, Pageable pageable){
        Page<User> list = repository.find(name, pageable);
        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findByCpf(String cpf){
        User entity = repository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("At UserService, User Cpf not found " + cpf));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto){
        User entity = new User();
        entity.setCpf(dto.getCpf());

        copyDtoToEntity(dto, entity, 1);

        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepository.getReferenceById(1L); // adding ROLE_CLIENT as default to the created user
        entity.addRole(role);

        Client client = new Client();
        Address address = new Address();
        Contact contact = new Contact();

        client.setName(dto.getName());
        client.setRegisterDate(Instant.now());
        client = clientRepository.save(client);

        address.setClient(client);
        address = addressRepository.save(address);

        contact.setClient(client);
        contact = contactRepository.save(contact);

        entity.setClient(client);

        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(String cpf, UserDTO dto){
        User entity = repository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("At UserService, User Cpf not found " + cpf));
        copyDtoToEntity(dto, entity, 2);
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public void delete(String cpf) {
        try {
            // delete contact, address, assistances, pets and contact from user
            User user = repository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("At UserService, User Cpf not found " + cpf));
            addressRepository.deleteById(user.getClient().getAddress().getId());
            contactRepository.deleteById(user.getClient().getContact().getId());
            for(Pet pet : user.getClient().getPets()){
                for(Assistance assistance : pet.getAssistances()){
                    assistanceRepository.deleteById(assistance.getId());
                }
                petRepository.deleteById(pet.getId());
            }
            clientRepository.deleteById(user.getClient().getId());
            repository.deleteByCpf(cpf);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity Violation");
        }
    }

    public void copyDtoToEntity(UserDTO dto, User entity, Integer operation){
        entity.setCpf(dto.getCpf());
        entity.setName(dto.getName());

        if(dto.getClientId() != null){
            entity.setClient(clientRepository.getReferenceById(dto.getClientId()));
        }

        if (operation == 2 && !authService.isAdmin()) {
            List<Role> updatedRoles = new ArrayList<>();
            for (RoleDTO roleDTO : dto.getRoles()) {
                Role role = roleRepository.getReferenceById(roleDTO.getId());
                updatedRoles.add(role);
            }
            if (!entity.getRoles().containsAll(updatedRoles) || !new HashSet<>(updatedRoles).containsAll(entity.getRoles())) {
                throw new ForbiddenException("You can't change your roles");
            }
        }

        entity.getRoles().clear();
        for(RoleDTO roleDTO : dto.getRoles()){
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = repository.searchUserAndRolesByCpf(username);
        if (result.size() == 0) {
            throw new UsernameNotFoundException("Cpf not found");
        }

        User user = new User();
        user.setCpf(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }
}
