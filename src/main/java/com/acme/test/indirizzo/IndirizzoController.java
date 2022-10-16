package com.acme.test.indirizzo;

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
@RequestMapping("/indirizzo")
public class IndirizzoController {
	@Autowired
	IndirizzoService indirizzoService;
	
	
	
	//GET ALL	
	@Operation(summary = "Trova tutti gli indirizzi",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Indirizzo trovato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = IndirizzoImp.class)) }),  
	  @ApiResponse(responseCode = "404", description = "Indirizzo non trovato",   content = @Content) })
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllIndirizzi(){
		List<IndirizzoImp> listIndirizzi= indirizzoService.getAllIndirizzi();
		List<GetIndirizzoResponse> listResp= new ArrayList<GetIndirizzoResponse>();
		for (IndirizzoImp indirizzo : listIndirizzi) {
			GetIndirizzoResponse resp= new GetIndirizzoResponse();
			BeanUtils.copyProperties(indirizzo, resp);
			resp.setNomeComune(indirizzo.getComune().getNome());
			resp.setNomeProvincia(indirizzo.getComune().getProvincia().getNome());
			resp.setSiglaProvincia(indirizzo.getComune().getProvincia().getSigla());
			listResp.add(resp);			
		}
		return ResponseEntity.ok(listResp);
	}
	
	
	//GET ID
	@Operation(summary = "Trova l'indirizzo per id",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Indirizzo trovato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = IndirizzoImp.class)) }),  
	  @ApiResponse(responseCode = "400", description = "id non valido",   content = @Content),
	  @ApiResponse(responseCode = "404", description = "Indirizzo non trovato",   content = @Content) })
	
	@GetMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getIndirizzoById(@PathVariable long id){
		IndirizzoImp indirizzo= indirizzoService.getIndirizzoById(id);
		GetIndirizzoResponse resp=new GetIndirizzoResponse();
		BeanUtils.copyProperties(indirizzo, resp);
		resp.setNomeComune(indirizzo.getComune().getNome());
		resp.setNomeProvincia(indirizzo.getComune().getProvincia().getNome());
		resp.setSiglaProvincia(indirizzo.getComune().getProvincia().getSigla());
		return ResponseEntity.ok(resp);
		
	}
	
	//POST	
	@Operation(summary = "Inserisci un indirizzo",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	 @ApiResponse(responseCode = "200", description = "Indirizzo inserito",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Indirizzo.class)) }),  
	 @ApiResponse(responseCode = "400", description = "Indirizzo non inserito",   content = @Content) })
	
	@PostMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> postIndirizzo(@RequestBody IndirizzoDto dto){
		indirizzoService.postIndirizzo(dto);
		return ResponseEntity.ok("Indirizzo Inserito");
	}
	
	//DELETE
	@Operation(summary = "Elimina un indirizzo",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	 @ApiResponse(responseCode = "200", description = "Indirizzo eliminato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Indirizzo.class)) }),  
	 @ApiResponse(responseCode = "400", description = "Indirizzo non eliminato",   content = @Content) })
	
	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteIndirizzo(@PathVariable long id){
		indirizzoService.deleteIndirizzo(id);
		return ResponseEntity.ok("Indirizzo eliminato");
	}
	
	
	//PUT
	@Operation(summary = "Modifica un indirizzo",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	 @ApiResponse(responseCode = "200", description = "Indirizzo Modificato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Indirizzo.class)) }),  
	 })
	
	@PutMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> putIndirizzo(@RequestBody IndirizzoDto dto, @PathVariable long id){
		indirizzoService.putIndirizzo(id, dto);
		return ResponseEntity.ok("Utente modificato");
	}
	
}
