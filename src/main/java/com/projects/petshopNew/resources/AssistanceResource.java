package com.projects.petshopNew.resources;

import com.projects.petshopNew.dto.AssistanceDTO;
import com.projects.petshopNew.services.AssistanceService;
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
import java.util.List;

@Tag(name = "assistances-API")
@RestController
@RequestMapping(value = "/assistances")
public class AssistanceResource {

    @Autowired
    private AssistanceService service;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully search"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
        @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<AssistanceDTO>> findAll() {
        List<AssistanceDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully search"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/pageable")
    public ResponseEntity<Page<AssistanceDTO>> findAllPageable(Pageable pageable){
        Page<AssistanceDTO> list = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Request successfully executed"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
        @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
        @ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<AssistanceDTO> findById(@PathVariable Long id){
        AssistanceDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request"), // invalid data, String in Integer field
        @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
        @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<AssistanceDTO> insert(@RequestBody AssistanceDTO dto){
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<AssistanceDTO> update(@PathVariable Long id, @RequestBody AssistanceDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "No content, request successfully executed"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
    @ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to execute the request
    @ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AssistanceDTO> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
