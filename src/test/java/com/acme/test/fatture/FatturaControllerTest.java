package com.acme.test.fatture;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.acme.test.fattura.FatturaDto;
import com.acme.test.fattura.FatturaImp;
import com.acme.test.fattura.FatturaRepository;
import com.acme.test.general.TestControllerBase;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaDto;

public class FatturaControllerTest extends TestControllerBase{
	@Autowired
	FatturaRepository fatturaRepo;

	@Value("${test.entry.point}/fattura")
	private String URL;


	@Test
	@DisplayName("cerco l'elenco delle fatture")
	public void testGetAll() {
		FatturaImp fattura= new FatturaImp(null, 2019, "06/07/2019", 58.0, "003", "Non Pagata");
		fatturaRepo.save(fattura);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin",HttpMethod.GET, getAdminEntity(), String.class);
		assertThat( r.getStatusCode() ).isEqualTo(  HttpStatus.OK );

	}

	@Test
	@DisplayName("Provo a cercare  le fatture senza autorizzazione")
	public void testGetAllKo() {
		FatturaImp fattura= new FatturaImp(null, 2019, "06/07/2019", 58.0, "89", "Non Pagata");
		fatturaRepo.save(fattura);

		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				HttpEntity.EMPTY,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.UNAUTHORIZED );

	}

	@Test
	@DisplayName("Cerco e trovo una fattura by id")
	public void getByIdOk() {
		FatturaImp fattura= new FatturaImp(null, 2019, "06/07/2019", 50.0, "004", "Non Pagata");
		fatturaRepo.save(fattura);

		ResponseEntity<FatturaImp> r= getRestTemplate().exchange(URL+"/admin/"+fattura.getId(), HttpMethod.GET, getAdminEntity(), FatturaImp.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		FatturaImp fattura1= r.getBody();
		assertThat(  fattura1.getId() ).isEqualTo( fattura.getId()  );

	}
	@Test
	@DisplayName("Cerco una fattura inesistente e ottengo status NOT_FOUND")
	public void getByIdKo() {
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/100000", HttpMethod.GET, getAdminEntity(), String.class);
		assertThat(  r.getStatusCode() ).isEqualTo( HttpStatus.NOT_FOUND );
	}

	@Test
	@DisplayName("Inserisco una fattura con successo")
	public void testPostOk() {
		FatturaDto fatturadto= new FatturaDto(2018, "06/07/2018", 65.0, "006", "Pagata");
		HttpEntity<FatturaDto > fatturaEntity = new HttpEntity<FatturaDto >(fatturadto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, fatturaEntity , String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.OK);

		FatturaImp fattura= fatturaRepo.findByNumeroFatturaAndAnno(fatturadto.getNumeroFattura(), fatturadto.getAnno());
		assertThat(fattura).isNotNull();	
		String messaggio = r.getBody();

		assertThat( messaggio ).isEqualTo( "Fattura inserita" );
	}

	@Test
	@DisplayName("User tenta di inserire  senza successo una fattura")
	public void testPostKoAdmin() {
		FatturaDto fatturadto= new FatturaDto(2018, "06/07/2018", 65.0, "015", "Pagata");
		
		HttpEntity<FatturaDto> fatturaEntity = new HttpEntity<FatturaDto>(fatturadto, getUserHeaders());

		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				fatturaEntity,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.FORBIDDEN );

	}
	
	@Test
	@DisplayName("Utente non loggato tenta di inserire  senza successo una fattura")
	public void testInsertKoNotLoggedIn() {
		FatturaDto fatturadto= new FatturaDto(2018, "06/07/2018", 65.0, "035", "Pagata");
		
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				HttpEntity.EMPTY,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.UNAUTHORIZED );
		
	}



	@Test
	@DisplayName("Provo ad inserire una fattura già esistente ottengo status BAD_REQUEST")
	public void postKoAlreadyInsertedException() {
		FatturaImp fattura= new FatturaImp(null, 2018, "06/07/2018", 125.0, "008", "Pagata");
		fatturaRepo.save(fattura);

		FatturaDto fatturadto= new FatturaDto(2018, "06/07/2018", 125.0, "008", "Pagata");
		HttpEntity<FatturaDto > fatturaEntity = new HttpEntity<FatturaDto >(fatturadto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, fatturaEntity , String.class);
		assertThat( r.getStatusCode() ).isEqualTo( HttpStatus.BAD_REQUEST );
	}

	@Test
	@DisplayName("Provo ad inserire una provincia ma la data è vuota è vuoto e ottengo status BAD_REQUEST")
	public void postKoValidationErrors() {
		FatturaDto fatturadto= new FatturaDto(2018, "", 125.0, "008", "Pagata");
		HttpEntity<FatturaDto > fatturaEntity = new HttpEntity<FatturaDto >(fatturadto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, fatturaEntity , String.class);
		assertThat( r.getStatusCode() ).isEqualTo( HttpStatus.BAD_REQUEST );

	}
	
	@Test
	@DisplayName("Elimino una fattura by id")
	public void DeleteByIdOk() {
		FatturaImp fattura= new FatturaImp(null, 2019, "06/07/2019", 50.0, "0084", "Non Pagata");
		fatturaRepo.save(fattura);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/"+fattura.getId(),
				HttpMethod.DELETE, getAdminEntity(), String.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		
	}
	
}
