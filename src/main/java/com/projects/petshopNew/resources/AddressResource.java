package com.projects.petshopNew.resources;

import com.projects.petshopNew.dto.AddressDTO;
import com.projects.petshopNew.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "addresses-API")
@RestController
@RequestMapping(value = "/addresses")
public class AddressResource {

    @Autowired
    private AddressService service;

    @Operation(summary = "Update the user address", method = "PUT")
    @ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated successfully"),
			@ApiResponse(responseCode = "400", description = "Bad Request"), // invalid data, String in Integer field
			@ApiResponse(responseCode = "401", description = "Unauthorized"), // when not logged
			@ApiResponse(responseCode = "403", description = "Forbidden"), // when nonAdmin try to update other User Address
			@ApiResponse(responseCode = "404", description = "Not found"), // when nonExisting id
			@ApiResponse(responseCode = "500", description = "Internal Server Error") // when nonExisting clientId
	})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<AddressDTO> update(@PathVariable Long id, @RequestBody AddressDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }
}
