package com.acme.test.indirizzo.comune;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.acme.test.general.TestControllerBase;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaDto;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;

public class ComuneControllerTest extends TestControllerBase{
	@Autowired
	ComuneRepository comuneRepo;

	@Autowired
	ProvinciaRepository provinciaRepo;

	@Value("${test.entry.point}/comune")
	private String URL;


	@Test
	@DisplayName("cerco l'elenco dei comuni")
	public void testGetAll() {
		Provincia prov= new Provincia(null, "Palermo", "PA");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Misilmeri", prov);
		comuneRepo.save(com);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.GET, getAdminEntity(), String.class);
		assertThat( r.getStatusCode() ).isEqualTo(  HttpStatus.OK );	
	}

	@Test
	@DisplayName("Provo a cercare un comune senza autorizzazione")
	public void testGetAllKo() {
		Provincia prov= new Provincia(null, "Caserta", "CE");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Cellole", prov);
		comuneRepo.save(com);
		
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				HttpEntity.EMPTY,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.UNAUTHORIZED );
		
	}
	
	@Test
	@DisplayName("Cerco e trovo un comune by id")
	public void getByIdOk() {
		Provincia prov= new Provincia (null, "Roma", "RM");
		provinciaRepo.save(prov);
		Comune com=new Comune(null, "Fiumicino", prov);
		comuneRepo.save(com);
		
		ResponseEntity<GetComuneResponse> r= getRestTemplate().exchange(URL+"/admin/"+com.getId(), HttpMethod.GET, getAdminEntity(), GetComuneResponse.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		GetComuneResponse com1=r.getBody();
		assertThat( com1.getNome()).isEqualTo(com.getNome());
		assertThat( com1.getNomeProvincia()).isEqualTo(com.getProvincia().getNome());
		assertThat( com1.getSiglaProvincia()).isEqualTo(com.getProvincia().getSigla());
	}
	


	@Test
	@DisplayName("Cerco un comune inesistente e ottengo status NOT_FOUND")
	public void getByIdKo() {
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/100000", HttpMethod.GET, getAdminEntity(), String.class);
		assertThat(  r.getStatusCode() ).isEqualTo( HttpStatus.NOT_FOUND );
	}
	
	

	@Test
	@DisplayName("Inserisco un comune con successo")
	public void testPostOk() {
		Provincia prov= new Provincia (null, "Torino", "TO");
		provinciaRepo.save(prov);
		ComuneDto comdto= new ComuneDto("Santià", "Torino", "TO");
		HttpEntity<ComuneDto> comEntity = new HttpEntity<ComuneDto>(comdto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, comEntity, String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.OK);

		Comune com= comuneRepo.findByNomeAndProvincia_id(comdto.getNome(), prov.getId());
		assertThat(com).isNotNull();
		String messaggio = r.getBody();
		
		assertThat( messaggio ).isEqualTo( "Comune Inserito" );

	}
	
	@Test
	@DisplayName("User tenta di inserire  senza successo un comune")
	public void testInsertKoAdmin() {
		Provincia prov= new Provincia (null, "Cremona", "CR");
		provinciaRepo.save(prov);
		ComuneDto comdto= new ComuneDto("Crema", "Cremona", "CR");
		
		HttpEntity<ComuneDto> comEntity = new HttpEntity<ComuneDto>(comdto, getUserHeaders());

		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				comEntity,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.FORBIDDEN );
	}
	
	@Test
	@DisplayName("Utente non loggato tenta di inserire  senza successo una provincia")
	public void testInsertKoNotLoggedIn() {
		Provincia prov= new Provincia (null, "Enna", "EN");
		provinciaRepo.save(prov);
		ComuneDto comdto= new ComuneDto("Assoro", "Enna", "EN");
	
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				HttpEntity.EMPTY,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.UNAUTHORIZED );
		
		
	}
	
	@Test
	@DisplayName("Provo ad inserire un comune già esistente ottengo status BAD_REQUEST")
	public void postKoAlreadyInsertedException() {
		Provincia prov= new Provincia (null, "Padova", "PD");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Limena", prov);
		comuneRepo.save(com);

		ComuneDto comdto= new ComuneDto("Limena", "Padova", "PD");
		HttpEntity<ComuneDto> comEntity = new HttpEntity<ComuneDto>(comdto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, comEntity, String.class);
		assertThat( r.getStatusCode() ).isEqualTo( HttpStatus.BAD_REQUEST );
	}
	

	@Test
	@DisplayName("Provo ad inserire un comune ma il nome è vuoto e ottengo status BAD_REQUEST")
	public void postKoValidationErrors() {
		ComuneDto comdto= new ComuneDto("", "Agrigento", "AG");
		HttpEntity<ComuneDto> comEntity = new HttpEntity<ComuneDto>(comdto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, comEntity, String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.BAD_REQUEST );
	}
	
	@Test
	@DisplayName("Elimino un comune by id")
	public void DeleteByIdOk() {
		Provincia prov= new Provincia (null, "Campobasso", "CB");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Campobasso", prov);
		comuneRepo.save(com);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/"+com.getId(),
				HttpMethod.DELETE, getAdminEntity(), String.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
	}
	
}
