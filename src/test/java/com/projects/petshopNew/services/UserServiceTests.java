package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.RoleDTO;
import com.projects.petshopNew.dto.UserDTO;
import com.projects.petshopNew.dto.UserInsertDTO;
import com.projects.petshopNew.entities.*;
import com.projects.petshopNew.repositories.*;
import com.projects.petshopNew.services.exceptions.ForbiddenException;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import com.projects.petshopNew.tests.AddressFactory;
import com.projects.petshopNew.tests.ClientFactory;
import com.projects.petshopNew.tests.ContactFactory;
import com.projects.petshopNew.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private AuthService authService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserDTO userDTO, userDTOAdmin;

    private String existingCpf, nonExistingCpf, existingAdminCpf;

    PageImpl<User> page;

    private String userName;

    private Client client;

    @BeforeEach
    void setUp() throws Exception{
        existingCpf = "123.456.789-10";
        nonExistingCpf = "999.999.999-99";
        existingAdminCpf = "000.111.222-33";

        userName = "Alex";

        Long existingId = 1L;
        Long existingAdminId = 2L;
        Long nonExistingId = 1000L;

        User user = UserFactory.createClientUser();

        page = new PageImpl<>(List.of(user));

        client = ClientFactory.createClient();
        user.setClient(client);

        Role role = new Role(1L, "ROLE_CLIENT");
        user.addRole(role);

        Contact contact = ContactFactory.createContact();
        user.getClient().setContact(contact);

        Address address = AddressFactory.createAddress();
        user.getClient().setAddress(address);

        userDTO = new UserDTO(user);

        //

        User userAdmin = UserFactory.createAdminUser();

        Client clientAdmin = ClientFactory.createAdminClient();
        userAdmin.setClient(clientAdmin);

        Role roleAdmin = new Role(2L, "ROLE_ADMIN");
        userAdmin.addRole(role);

        userAdmin.getClient().setAddress(address);
        userAdmin.getClient().setContact(contact);

        userDTOAdmin = new UserDTO(userAdmin);

        //

        Mockito.when(repository.find(ArgumentMatchers.any(),(Pageable) ArgumentMatchers.any())).thenReturn(page); // different method in repository, because of the name filter

        Mockito.when(repository.findByCpf(existingCpf)).thenReturn(Optional.of(user));
        Mockito.when(repository.findByCpf(existingAdminCpf)).thenReturn(Optional.of(userAdmin));
        Mockito.when(repository.findByCpf(nonExistingCpf)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(user);

        Mockito.doNothing().when(repository).deleteByCpf(existingCpf);
        Mockito.doNothing().when(repository).deleteByCpf(existingAdminCpf);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteByCpf(nonExistingCpf);

        //

        Mockito.when(clientRepository.findById(existingId)).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.findById(existingAdminId)).thenReturn(Optional.of(clientAdmin));
        Mockito.when(clientRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(clientRepository.save(ArgumentMatchers.any())).thenReturn(client);

        Mockito.when(clientRepository.getReferenceById(existingId)).thenReturn(client);
        Mockito.when(clientRepository.getReferenceById(existingAdminId)).thenReturn(clientAdmin);
        Mockito.when(clientRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //

        Mockito.when(roleRepository.findById(existingId)).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.findById(existingAdminId)).thenReturn(Optional.of(roleAdmin));
        Mockito.when(roleRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(roleRepository.save(ArgumentMatchers.any())).thenReturn(role);

        Mockito.when(roleRepository.getReferenceById(existingId)).thenReturn(role);
        Mockito.when(roleRepository.getReferenceById(existingAdminId)).thenReturn(roleAdmin);
        Mockito.when(roleRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }


    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<UserDTO> result = service.findAllPaged(userName, pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getSize(), 1);
        Assertions.assertEquals(result.getContent().get(0).getCpf(), existingCpf);
    }


    @Test
    public void findByCpfShouldReturnObjectWhenCpfExists(){
        UserDTO result = service.findByCpf(existingCpf);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCpf(), existingCpf);
        Mockito.verify(repository).findByCpf(existingCpf);
    }

    @Test
    public void findByCpfShouldThrowResourceNotFoundExceptionWhenCpfDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findByCpf(nonExistingCpf);
        });
    }

    @Test
    public void insertShouldReturnObject(){
        UserInsertDTO dto = UserFactory.createUserInsertDTO(client);
        UserDTO result = service.insert(dto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCpf(), existingCpf);
        Assertions.assertEquals(result.getName(), userDTO.getName());
    }

    @Test
    public void updateShouldReturnObjectWhenCpfExistsAndUserClientDidNotUpdatedHisRoles(){
        UserDTO result = service.update(existingCpf, userDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCpf(), existingCpf);
    }

    @Test
    public void updateShouldThrowForbiddenExceptionWhenCpfExistsAndUserClientHasUpdatedHisRoles(){
        userDTO.getRoles().add(new RoleDTO(2L, "ROLE_ADMIN"));
        Assertions.assertThrows(ForbiddenException.class, () -> {
            service.update(existingCpf, userDTO);
        });
    }

    @Test
    public void updateShouldReturnObjectWhenCpfExistsAndUserAdminHasUpdatedHisRoles(){
        userDTOAdmin.getRoles().add(new RoleDTO(3L, "ROLE_TEACHER"));
        UserDTO result = service.update(existingAdminCpf, userDTOAdmin);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenCpfDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingCpf, userDTO);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenCpfExists(){
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingCpf);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenCpfDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingCpf);
        });
    }
}
