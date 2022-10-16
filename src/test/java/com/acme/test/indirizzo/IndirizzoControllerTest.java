package com.acme.test.indirizzo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.acme.test.general.TestControllerBase;
import com.acme.test.indirizzo.comune.Comune;
import com.acme.test.indirizzo.comune.ComuneRepository;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaDto;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;

public class IndirizzoControllerTest extends TestControllerBase{
	@Autowired
	IndirizzoRepository indirizzoRepo;
	@Autowired
	ComuneRepository comuneRepo;
	@Autowired
	ProvinciaRepository provinciaRepo;
	
	@Value("${test.entry.point}/indirizzo")
	private String URL;
	
	
	@Test
	@DisplayName("cerco l'elenco degli indirizzi")
	public void testGetAll() {
		Provincia prov= new Provincia(null, "Palermo", "PA");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Misilmeri", prov);
		comuneRepo.save(com);
		IndirizzoImp ind= new IndirizzoImp(null, "Contrada Feotto cannita", "32", "Montana", "90036", com);
		indirizzoRepo.save(ind);
		
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.GET, getAdminEntity(), String.class);
		assertThat( r.getStatusCode() ).isEqualTo(  HttpStatus.OK );
	}
	
	@Test
	@DisplayName("Provo a cercare  gli indirizzi senza autorizzazione")
	public void testGetAllKo() {
		Provincia prov= new Provincia(null, "Arezzo", "AR");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Cortona", prov);
		comuneRepo.save(com);
		IndirizzoImp ind= new IndirizzoImp(null, "via cortona", "8", "città", "52100", com);
		indirizzoRepo.save(ind);
		
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				HttpEntity.EMPTY,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.UNAUTHORIZED );
		
	}
	
	
	
	@Test
	@DisplayName("Cerco e trovo un indirizzo by id")
	public void getByIdOk() {
		Provincia prov= new Provincia(null, "Nuoro", "NU");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Belvi", prov);
		comuneRepo.save(com);
		IndirizzoImp ind= new IndirizzoImp(null, "Via belva", "35", "Montana", "08100", com);
		indirizzoRepo.save(ind);
		
		ResponseEntity<IndirizzoImp> r= getRestTemplate().exchange(URL+"/admin/"+ind.getId(), HttpMethod.GET, getAdminEntity(), IndirizzoImp.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		IndirizzoImp ind1 = r.getBody();
		assertThat( ind1.getId()).isEqualTo(ind.getId());
		assertThat(ind1.getVia()).isEqualTo(ind.getVia());
		assertThat(ind1.getNumeroCivico()).isEqualTo(ind.getNumeroCivico());
		assertThat(ind1.getLocalita()).isEqualTo(ind.getLocalita());
		assertThat(ind1.getCap()).isEqualTo(ind.getCap());
		assertThat(ind1.getComune().getNome()).isEqualTo(ind.getComune().getNome());
		assertThat(ind1.getComune().getProvincia().getNome()).isEqualTo(ind.getComune().getProvincia().getNome());
		assertThat(ind1.getComune().getProvincia().getSigla()).isEqualTo(ind.getComune().getProvincia().getSigla());

	}
	

	@Test
	@DisplayName("Cerco un indirizzo inesistente e ottengo status NOT_FOUND")
	public void getByIdKo() {
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/100000", HttpMethod.GET, getAdminEntity(), String.class);
		assertThat(  r.getStatusCode() ).isEqualTo( HttpStatus.NOT_FOUND );
}
	@Test
	@DisplayName("Inserisco un indirizzo con successo")
	public void testPostOk() {
		Provincia prov= new Provincia(null, "Lecco", "LC");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Varenna", prov);
		comuneRepo.save(com);
		IndirizzoDto inddto= new IndirizzoDto("Via alcide de gasperi", "49", "città", "23900", "Varenna", "Lecco", "LC");
		
		HttpEntity<IndirizzoDto> indEntity = new HttpEntity<IndirizzoDto>(inddto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, indEntity , String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.OK);
		
		
		IndirizzoImp ind= indirizzoRepo.findByViaAndNumeroCivicoAndLocalitaAndCapAndComune_id(inddto.getVia(), inddto.getNumeroCivico(), inddto.getLocalita(), inddto.getCap(),com.getId() );
		assertThat(ind).isNotNull();
		String messaggio = r.getBody();
		assertThat( messaggio ).isEqualTo( "Indirizzo Inserito" );
	}
	
	@Test
	@DisplayName("User tenta di inserire  senza successo una provincia")
	public void testInsertKoAdmin() {
		
		Provincia prov= new Provincia(null, "Arezzo", "AZ");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Bibbiena", prov);
		comuneRepo.save(com);
		IndirizzoDto inddto= new IndirizzoDto("Via alcide de gasperi", "49", "città", "52011", "Bibbiena","Arezzo", "AZ");
		
		HttpEntity<IndirizzoDto> indEntity = new HttpEntity<IndirizzoDto>(inddto, getUserHeaders());
		
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				indEntity,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.FORBIDDEN );
		
	}
	
	
	@Test
	@DisplayName("Provo ad inserire un indirizzo già esistente ottengo status BAD_REQUEST")
	public void postKoAlreadyInsertedException() {
		Provincia prov= new Provincia(null, "Foggia", "FG");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Accadia", prov);
		comuneRepo.save(com);
		IndirizzoImp ind= new IndirizzoImp(null,"San giovanni rotondo", "9", "città", "71100", com);
		indirizzoRepo.save(ind);
		
		IndirizzoDto inddto= new IndirizzoDto ("San giovanni rotondo", "9", "città", "71100",  "Accadia", "Foggia", "FG");
		
		HttpEntity<IndirizzoDto> indEntity = new HttpEntity<IndirizzoDto>(inddto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, indEntity , String.class);
		assertThat( r.getStatusCode() ).isEqualTo( HttpStatus.BAD_REQUEST );
	}
	
	@Test
	@DisplayName("Provo ad inserire un indirizzo ma il nome è vuoto e ottengo status BAD_REQUEST")
	public void postKoValidationErrors() {
		IndirizzoDto inddto= new IndirizzoDto ("", "49", "città", "90039", "Villabate", "Palermo", "PA");
		HttpEntity<IndirizzoDto> indEntity = new HttpEntity<IndirizzoDto>(inddto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, indEntity , String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.BAD_REQUEST );
	}
	
	
	@Test
	@DisplayName("Elimino un indirizzo by id")
	public void DeleteByIdOk() {
		Provincia prov= new Provincia (null, "Campobasso", "CB");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Campobasso", prov);
		comuneRepo.save(com);
		IndirizzoImp ind= new IndirizzoImp(null,"Via campobasso gasperi", "9", "città", "000158", com);
		indirizzoRepo.save(ind);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/"+ind.getId(),
				HttpMethod.DELETE, getAdminEntity(), String.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		
	}
	
	
}
