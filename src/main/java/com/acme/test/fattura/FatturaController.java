package com.acme.test.fattura;

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
@RequestMapping("/fattura")
public class FatturaController {
	@Autowired
	FatturaService fatturaService;
	
	
	//GET ALL	
			@Operation(summary = "Trova tutte le fatture",security = @SecurityRequirement(name = "bearer-authentication"))
			@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Fattura trovata",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FatturaImp.class)) }),  
			  @ApiResponse(responseCode = "404", description = "Fattura non trovata",   content = @Content) })
			
			@GetMapping("/admin")
			@PreAuthorize("hasRole('ADMIN')")
			public ResponseEntity<?> gettAllFattura(){
				List<FatturaImp> listFatture= fatturaService.getAllFattura();
				List<GetFatturaResponse> listResp= new ArrayList<GetFatturaResponse>();
				for(FatturaImp fattura : listFatture) {
					GetFatturaResponse resp= new GetFatturaResponse();
					BeanUtils.copyProperties(fattura, resp);
					listResp.add(resp);
				}
				return ResponseEntity.ok(listResp);
			}
			
		//GET (id)	
			@Operation(summary = "Trova la fattura per id",security = @SecurityRequirement(name = "bearer-authentication"))
			@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Fattura trovata",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FatturaImp.class)) }),  
			  @ApiResponse(responseCode = "400", description = "id non valido",   content = @Content),
			  @ApiResponse(responseCode = "404", description = "Fattura non trovata",   content = @Content) })
			
			@GetMapping("/admin/{id}")
			@PreAuthorize("hasRole('ADMIN')")
			public ResponseEntity<?> getFatturaById(@PathVariable long id){
				FatturaImp fattura= fatturaService.getFatturaById(id);
				GetFatturaResponse resp= new GetFatturaResponse();
				BeanUtils.copyProperties(fattura, resp);
				return ResponseEntity.ok(resp);
				
			}
		
		//POST
			@Operation(summary = "Inserisci una fatture",security = @SecurityRequirement(name = "bearer-authentication"))
			@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Fattura inserita",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FatturaImp.class)) }),  
			  @ApiResponse(responseCode = "404", description = "Fattura non inserita",   content = @Content) })
			
			@PostMapping("/admin")
			@PreAuthorize("hasRole('ADMIN')")
			public ResponseEntity<?> postFattura(@RequestBody FatturaDto dto){
				fatturaService.postFattura(dto);
				return ResponseEntity.ok("Fattura inserita");
				
				
				
				
			}
			
		//DELETE
			@Operation(summary = "Elimina una fattura",security = @SecurityRequirement(name = "bearer-authentication"))
			@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Fattura eliminata",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FatturaImp.class)) }),  
			  @ApiResponse(responseCode = "404", description = "Fattura non eliminata",   content = @Content) })
			@DeleteMapping("/admin/{id}")
			@PreAuthorize("hasRole('ADMIN')")
			public ResponseEntity<?> deleteFattura(@PathVariable long id){
				fatturaService.deleteFattura(id);
				return ResponseEntity.ok("Fattura eliminata");
			}
			
		//PUT
			@Operation(summary = "Modifica una fatture",security = @SecurityRequirement(name = "bearer-authentication"))
			@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Fattura modificata",   content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FatturaImp.class)) }),  
			  @ApiResponse(responseCode = "404", description = "Fattura non modificata",   content = @Content) })
			
			@PutMapping("/admin/{id}")
			@PreAuthorize("hasRole('ADMIN')")
			public ResponseEntity<?> putFattura(@RequestBody FatturaDto dto, @PathVariable long id){
				fatturaService.putFattura(id, dto);;
				return ResponseEntity.ok("Fattura modificata");
			}
}
