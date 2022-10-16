package com.acme.test.clienti.pa;

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

import com.acme.test.indirizzo.Indirizzo;
import com.acme.test.indirizzo.IndirizzoImp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/clientePa")
public class ClientePaController {
	@Autowired
	ClientePaService paService;
	
	//GET ALL
	@Operation(summary = "Trova tutti i clienti",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Cliente trovato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClientePa.class)) }),  
	  @ApiResponse(responseCode = "404", description = "Cliente non trovato",   content = @Content) })
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllClientiPa(){
		List<ClientePa> listClienti = paService.getAllClientiPa();
		List<GetClientePaResponse> listResp= new ArrayList<GetClientePaResponse>();
		for (ClientePa pa : listClienti) {
			GetClientePaResponse resp= new GetClientePaResponse();
			BeanUtils.copyProperties(pa, resp);
			listResp.add(resp);	
			System.out.println("---------Lista resp--------");
			System.out.println(resp);
		}
		return ResponseEntity.ok(listResp);
		
	}
	
	
	//GET ID
	
	@Operation(summary = "Trova cliente per id",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Cliente trovato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClientePa.class)) }),  
	  @ApiResponse(responseCode = "400", description = "id non valido",   content = @Content),
	  @ApiResponse(responseCode = "404", description = "cliente non trovato",   content = @Content) })	
	
	@GetMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getClientePaById(@PathVariable long id){
		ClientePa pa= paService.getClientePaById(id);
		GetClientePaResponse resp= new GetClientePaResponse();
		BeanUtils.copyProperties(pa, resp);
		return ResponseEntity.ok(resp);
	}
	
	//POST
	@Operation(summary = "Inserisci cliente",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Cliente inserito",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClientePa.class)) }),  
	  @ApiResponse(responseCode = "404", description = "cliente non inserito",   content = @Content) })
	
	@PostMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> postClientePa(@RequestBody ClientePaDto dto){
		paService.postClientePa(dto);
		return ResponseEntity.ok("Cliente inserito");
	}
	
	
	//DELETE
	@Operation(summary = "Elimina un cliente",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	 @ApiResponse(responseCode = "200", description = "Cliente eliminato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClientePa.class)) }),  
	 @ApiResponse(responseCode = "400", description = "id non valido",   content = @Content),
	 @ApiResponse(responseCode = "400", description = "Cliente non eliminato",   content = @Content) })
	
	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteClientePa(@PathVariable long id) {
		paService.deleteClientePa(id);
		return ResponseEntity.ok("Cliente eliminato");
	}
	
	//PUT
	@Operation(summary = "Modifica un cliente",security = @SecurityRequirement(name = "bearer-authentication"))
	@ApiResponses(value = { 
	 @ApiResponse(responseCode = "200", description = "Cliente Modificato",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClientePa.class)) }),  
	 })
	
	
	@PutMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> putClientePa(@RequestBody ClientePaDto dto, @PathVariable long id){
		paService.putClientePa(id, dto);
		return ResponseEntity.ok("Cliente modificato");
	}
	
}

