package com.acme.test.indirizzo.provincia;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.acme.test.general.TestControllerBase;
import com.acme.test.indirizzo.comune.Comune;


public class ProvinciaControllerTest extends TestControllerBase{
	@Autowired
	ProvinciaRepository provRepo;
	
	
	@Value("${test.entry.point}/provincia")
	private String URL;
	
	

	@Test
	@DisplayName("cerco l'elenco delle province")
	public void testGetAll() {
		Provincia prov= new Provincia(null, "Firenze", "FR");
		provRepo.save(prov);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.GET, getAdminEntity(), String.class);
		assertThat( r.getStatusCode() ).isEqualTo(  HttpStatus.OK );
	}
	
	
	@Test
	@DisplayName("Provo a cercare  le province senza autorizzazione")
	public void testGetAllKo() {
		Provincia prov= new Provincia(null, "Arezzo", "AR");
		provRepo.save(prov);
		
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				HttpEntity.EMPTY,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.UNAUTHORIZED );
		
		
	}
	 
	
	@Test
	@DisplayName("Cerco e trovo una provincia by id")
	public void getByIdOk() {
		Provincia prov = new Provincia (null, "Catania", "CT");
		provRepo.save(prov);

		
		ResponseEntity<Provincia> r= getRestTemplate().exchange(URL+"/admin/"+prov.getId(), HttpMethod.GET, getAdminEntity(), Provincia.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		Provincia prov1= r.getBody();
		
		assertThat(  prov1.getId() ).isEqualTo( prov.getId()  );
		assertThat(  prov1.getNome() ).isEqualTo( prov.getNome()  );
		assertThat( prov1.getSigla()).isEqualTo(prov.getSigla());
		
	}
	
	@Test
	@DisplayName("Cerco una provincia inesistente e ottengo status NOT_FOUND")
	public void getByIdKo() {
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/100000", HttpMethod.GET, getAdminEntity(), String.class);
		assertThat(  r.getStatusCode() ).isEqualTo( HttpStatus.NOT_FOUND );
	}
	
	
	
	@Test
	@DisplayName("Inserisco una provincia con successo")
	public void testPostOk() {
		ProvinciaDto provdto= new ProvinciaDto("Padova", "PD");
		HttpEntity<ProvinciaDto> provEntity = new HttpEntity<ProvinciaDto>(provdto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, provEntity , String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.OK);
		
		Provincia prov= provRepo.findBySigla(provdto.getSigla());
		assertThat(prov).isNotNull();	
		String messaggio = r.getBody();
		
		assertThat( messaggio ).isEqualTo( "Provincia Inserita" );
	}
	
	@Test
	@DisplayName("User tenta di inserire  senza successo una provincia")
	public void testInsertKoAdmin() {
		ProvinciaDto provdto= new ProvinciaDto("Bologna", "BO");
		HttpEntity<ProvinciaDto> provEntity = new HttpEntity<ProvinciaDto>(provdto, getUserHeaders());
		
		
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				provEntity,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.FORBIDDEN );
	}
	
	@Test
	@DisplayName("Utente non loggato tenta di inserire  senza successo una provincia")
	public void testInsertKoNotLoggedIn() {
		ProvinciaDto provdto= new ProvinciaDto("Biella", "BI");
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				HttpEntity.EMPTY,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.UNAUTHORIZED );
		
	}
	
	@Test
	@DisplayName("Provo ad inserire una provincia già esistente ottengo status BAD_REQUEST")
	public void postKoAlreadyInsertedException() {
		Provincia prov= new Provincia (null, "Trapani", "TP");
		provRepo.save(prov);
		
		ProvinciaDto provdto= new ProvinciaDto("Trapani", "TP");
		HttpEntity<ProvinciaDto> provEntity = new HttpEntity<ProvinciaDto>(provdto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, provEntity , String.class);
		assertThat( r.getStatusCode() ).isEqualTo( HttpStatus.BAD_REQUEST );
	}
	

	@Test
	@DisplayName("Provo ad inserire una provincia ma il nome è vuoto e ottengo status BAD_REQUEST")
	public void postKoValidationErrors() {
		ProvinciaDto provdto= new ProvinciaDto("", "AG");
		HttpEntity<ProvinciaDto> provEntity = new HttpEntity<ProvinciaDto>(provdto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, provEntity , String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.BAD_REQUEST );
		
	}
	
	
		@Test
		@DisplayName("Elimino una provincia by id")
		public void DeleteByIdOk() {
			Provincia prov = new Provincia (null, "Campobasso", "CB");
			provRepo.save(prov);	
			ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/"+prov.getId(),
					HttpMethod.DELETE, getAdminEntity(), String.class);
			assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		}
		


}

