package com.projects.petshopNew.resources;

import com.projects.petshopNew.dto.ClientDTO;
import com.projects.petshopNew.services.ClientService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "clients-API")
@RestController
@RequestMapping(value = "/clients")
public class ClientResource {

    @Autowired
    private ClientService service;

    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully search"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAllPaged(Pageable pageable){
        Page<ClientDTO> list = service.findAllPaged(pageable);
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
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id){
        ClientDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
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
    public ResponseEntity<ClientDTO> update(@PathVariable Long id, @RequestBody ClientDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }
}
