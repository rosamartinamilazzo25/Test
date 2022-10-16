package com.acme.test.indirizzo.provincia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/provincia")
public class ProvinciaController {
	@Autowired
	ProvinciaService provinciaService;
	
	
	//GET ALL	
		@Operation(summary = "Trova tutte le province",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		  @ApiResponse(responseCode = "200", description = "Provincia trovata",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Provincia.class)) }),  
		  @ApiResponse(responseCode = "404", description = "Provincia non trovata",   content = @Content) })
		
		@GetMapping("/admin")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> getAllProvincia(){
			List<Provincia> listProvince= provinciaService.getAllProvincia();
			List<GetProvinciaResponse> listResp= new ArrayList<GetProvinciaResponse>();
			for (Provincia prov : listProvince) {
				GetProvinciaResponse resp= new GetProvinciaResponse();
				BeanUtils.copyProperties(prov, resp);
				listResp.add(resp);			
			}
			return ResponseEntity.ok(listResp);
		}
		
		
		//GET ID
		@Operation(summary = "Trova la provincia per id",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		  @ApiResponse(responseCode = "200", description = "Provincia trovata",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Provincia.class)) }),  
		  @ApiResponse(responseCode = "400", description = "id non valido",   content = @Content),
		  @ApiResponse(responseCode = "404", description = "Provincia non trovata",   content = @Content) })
		
		@GetMapping("/admin/{id}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> getProvinciaById(@PathVariable long id){
			Provincia prov= provinciaService.getProvinciaById(id);
			GetProvinciaResponse resp=new GetProvinciaResponse();
			BeanUtils.copyProperties(prov, resp);
			return ResponseEntity.ok(resp);
			
		}
		
		//POST	
		@Operation(summary = "Inserisci una provincia",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		 @ApiResponse(responseCode = "200", description = "Provincia inserita",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Provincia.class)) }),  
		 @ApiResponse(responseCode = "400", description = "Provincia non inserita",   content = @Content) })
		
		@PostMapping("/admin")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> postProvincia(@RequestBody ProvinciaDto dto){
			provinciaService.postProvincia(dto);
			return ResponseEntity.ok("Provincia Inserita");
		}
		
		//DELETE
		@Operation(summary = "Elimina una provincia",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		 @ApiResponse(responseCode = "200", description = "Provincia eliminata",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Provincia.class)) }),  
		 @ApiResponse(responseCode = "400", description = "Provincia non eliminata",   content = @Content) })
		
		@DeleteMapping("/admin/{id}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> deleteProvincia(@PathVariable long id){
			provinciaService.deleteProvincia(id);
			return ResponseEntity.ok("Provincia eliminata");
		}
		
		
		//PUT
		@Operation(summary = "Modifica una provincia",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		 @ApiResponse(responseCode = "200", description = "Provincia Modificata",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Provincia.class)) }),  
		 })
		
		@PutMapping("/admin/{id}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> putProvincia(@RequestBody ProvinciaDto dto, @PathVariable long id){
			provinciaService.putProvincia(id, dto);
			return ResponseEntity.ok("Provincia modificata");
		}
		
	}


