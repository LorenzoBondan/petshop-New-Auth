package com.projects.petshopNew.resources;

import com.projects.petshopNew.dto.UserDTO;
import com.projects.petshopNew.dto.UserInsertDTO;
import com.projects.petshopNew.services.AuthService;
import com.projects.petshopNew.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "users-API")
@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService service;

    @Autowired
    private AuthService authService;

    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully search"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAllPaged(@RequestParam(value = "name", defaultValue = "") String name, Pageable pageable){
        Page<UserDTO> list = service.findAllPaged(name, pageable);
        return ResponseEntity.ok().body(list);
    }

    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Request successfully executed"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping(value = "/{cpf}")
    public ResponseEntity<UserDTO> findByCpf(@PathVariable String cpf) {
        authService.validateSelfOrAdmin(cpf);
        UserDTO dto = service.findByCpf(cpf);
        return ResponseEntity.ok().body(dto);
    }

    @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Created"),
    @ApiResponse(responseCode = "400", description = "Bad Request"), // invalid data, String in Integer field
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto){
        UserDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{cpf}")
                .buildAndExpand(dto.getCpf()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Request successfully executed"),
    @ApiResponse(responseCode = "400", description = "Bad Request"), // invalid data, String in Integer field
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @PutMapping(value = "/{cpf}")
    public ResponseEntity<UserDTO> update(@PathVariable String cpf, @Valid @RequestBody UserDTO dto){
        authService.validateSelfOrAdmin(cpf);
        dto = service.update(cpf, dto);
        return ResponseEntity.ok().body(dto);
    }

    @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "No content, request successfully executed"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @DeleteMapping(value = "/{cpf}")
    public ResponseEntity<UserDTO> delete(@PathVariable String cpf){
        authService.validateSelfOrAdmin(cpf);
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }
}
