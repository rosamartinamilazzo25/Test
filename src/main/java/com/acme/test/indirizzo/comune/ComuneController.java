package com.acme.test.indirizzo.comune;

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

import com.acme.test.indirizzo.GetIndirizzoResponse;
import com.acme.test.indirizzo.Indirizzo;
import com.acme.test.indirizzo.IndirizzoDto;
import com.acme.test.indirizzo.IndirizzoImp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/comune")
public class ComuneController {
	@Autowired
	ComuneService comuneService;
	
	
	//GET ALL	
	
		@Operation(summary = "Trova tutti i comuni",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		  @ApiResponse(responseCode = "200", description = "Comune trovato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comune.class)) }),  
		  @ApiResponse(responseCode = "404", description = "Comune non trovato",   content = @Content) })
		
		@GetMapping("/admin")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> getAllComune(){
			List<Comune> listComune= comuneService.getAllComune();
			List<GetComuneResponse> listResp= new ArrayList<GetComuneResponse>();
			for (Comune com : listComune) {
				GetComuneResponse resp= new GetComuneResponse();
				BeanUtils.copyProperties(com, resp);
				resp.setNomeProvincia(com.getProvincia().getNome());
				resp.setSiglaProvincia(com.getProvincia().getSigla());
				listResp.add(resp);			
			}
			return ResponseEntity.ok(listResp);
		}
		
		
		//GET ID
		@Operation(summary = "Trova i comuni id",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		  @ApiResponse(responseCode = "200", description = "Comune trovato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comune.class)) }),  
		  @ApiResponse(responseCode = "400", description = "id non valido",   content = @Content),
		  @ApiResponse(responseCode = "404", description = "Comune non trovato",   content = @Content) })
		
		@GetMapping("/admin/{id}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> getComuneById(@PathVariable long id){
			Comune com= comuneService.getComuneById(id);
			GetComuneResponse resp=new GetComuneResponse();
			BeanUtils.copyProperties(com, resp);
			resp.setNomeProvincia(com.getProvincia().getNome());
			resp.setSiglaProvincia(com.getProvincia().getSigla());
			return ResponseEntity.ok(resp);
			
		}
		
		//POST	
		@Operation(summary = "Inserisci un comune",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		 @ApiResponse(responseCode = "200", description = "Comune inserito",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comune.class)) }),  
		 @ApiResponse(responseCode = "400", description = "Comune non inserito",   content = @Content) })
		
		@PostMapping("/admin")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> postComune(@RequestBody ComuneDto dto){
			comuneService.postComune(dto);
			return ResponseEntity.ok("Comune Inserito");
		}
		
		//DELETE
		@Operation(summary = "Elimina un comune",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		 @ApiResponse(responseCode = "200", description = "Comune eliminato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comune.class)) }),  
		 @ApiResponse(responseCode = "400", description = "Comune non eliminato",   content = @Content) })
		
		@DeleteMapping("/admin/{id}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> deleteComune(@PathVariable long id){
			comuneService.deleteComune(id);
			return ResponseEntity.ok("Comune eliminato");
		}
		
		
		//PUT
		@Operation(summary = "Modifica un comune",security = @SecurityRequirement(name = "bearer-authentication"))
		@ApiResponses(value = { 
		 @ApiResponse(responseCode = "200", description = "Comune Modificato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Comune.class)) }),  
		 })
		
		@PutMapping("/admin/{id}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> putComune(@RequestBody ComuneDto dto, @PathVariable long id){
			comuneService.putComune(id, dto);
			return ResponseEntity.ok("Comune modificato");
		}
		
	}


