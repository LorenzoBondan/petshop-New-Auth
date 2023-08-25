package com.projects.petshopNew.resources;

import com.projects.petshopNew.dto.PetDTO;
import com.projects.petshopNew.services.PetService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "pets-API")
@RestController
@RequestMapping(value = "/pets")
public class PetResource {

    @Autowired
    private PetService service;

    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully search"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PetDTO>> findAllPaged(Pageable pageable){
        Page<PetDTO> list = service.findAllPaged(pageable);
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
    @GetMapping(value = "/{id}")
    public ResponseEntity<PetDTO> findById(@PathVariable Long id){
        PetDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Created"),
    @ApiResponse(responseCode = "400", description = "Bad Request"), // invalid data, String in Integer field
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PetDTO> insert(@RequestBody PetDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
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
    @PutMapping(value = "/{id}")
    public ResponseEntity<PetDTO> update(@PathVariable Long id, @RequestBody PetDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "No content, request successfully executed"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
    @ApiResponse(responseCode = "409", description = "Integrity Violation"), // when a pet has assistances
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<PetDTO> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
