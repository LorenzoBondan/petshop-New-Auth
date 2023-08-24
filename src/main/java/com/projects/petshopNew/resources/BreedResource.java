package com.projects.petshopNew.resources;

import com.projects.petshopNew.dto.BreedDTO;
import com.projects.petshopNew.services.BreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/breeds")
public class BreedResource {

    @Autowired
    private BreedService service;

    /*@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully search"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<BreedDTO>> findAllPaged(Pageable pageable){
        Page<BreedDTO> list = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    /*@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Request successfully executed"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
        @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
        @ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<BreedDTO> findById(@PathVariable Long id){
        BreedDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    /*@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Request successfully executed"),
    @ApiResponse(responseCode = "400", description = "Bad Request"), // invalid data, String in Integer field
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<BreedDTO> insert(@RequestBody BreedDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    /*@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Request successfully executed"),
    @ApiResponse(responseCode = "400", description = "Bad Request"), // invalid data, String in Integer field
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<BreedDTO> update(@PathVariable Long id, @RequestBody BreedDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    /*@ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "No content, request successfully executed"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
    @ApiResponse(responseCode = "409", description = "Integrity Violation"), // when a breed has pets
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<BreedDTO> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
