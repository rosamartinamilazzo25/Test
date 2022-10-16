package com.acme.test.cliente;

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

import com.acme.test.clienti.pa.ClientePa;
import com.acme.test.clienti.pa.ClientePaDto;
import com.acme.test.clienti.spa.ClienteSpa;
import com.acme.test.clienti.spa.ClienteSpaDto;
import com.acme.test.clienti.spa.ClienteSpaRepository;
import com.acme.test.fattura.FatturaImp;
import com.acme.test.general.TestControllerBase;
import com.acme.test.indirizzo.IndirizzoImp;
import com.acme.test.indirizzo.IndirizzoRepository;
import com.acme.test.indirizzo.comune.Comune;
import com.acme.test.indirizzo.comune.ComuneRepository;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;

public class ClienteSpaControllerTest extends TestControllerBase {
	@Autowired
	ClienteSpaRepository spaRepo;
	
	@Autowired
	ProvinciaRepository provRepo;
	@Autowired
	ComuneRepository comRepo;
	@Autowired
	IndirizzoRepository indirizzoSedeLegaleRepo;
	@Autowired
	IndirizzoRepository indirizzoSedeOperativaRepo;
	
	@Value("${test.entry.point}/clienteSpa")
	private String URL;
	
	
	
	@Test
	@DisplayName("cerco l'elenco dei clienti")
	public void testGetAll() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Milano", "MI");
		provRepo.save(prov);
		Comune com= new Comune(null, "Cinisello Balsamo", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Via Cilea", "3", "Città", "20092", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Viale Macchiavelli", "9", "Città", "20092", com);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClienteSpa spa= new ClienteSpa(null, "Fujita s.p.pa", "321065892222", "fujita@gmail.com", "02/01/2019",
				"03/05/2020", 10000.0, "fujita@pec.it", "325894512", "simone.tremila@gotmial.com", 
				"Simone", "Tremila", "21528525", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		spaRepo.save(spa);
		
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.GET, getAdminEntity(), String.class);
		assertThat( r.getStatusCode() ).isEqualTo(  HttpStatus.OK );
		
		
	}
	
	@Test
	@DisplayName("Provo a cercare  un cliente senza autorizzazione")
	public void testGetAllKo() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Milano", "MI");
		provRepo.save(prov);
		Comune com= new Comune(null, "Cinisello Balsamo", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Via Cilea", "3", "Città", "20092", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Viale Macchiavelli", "9", "Città", "20092", com);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClienteSpa spa= new ClienteSpa(null, "Fujita s.p.pa", "3210650000222", "fujita@gmail.com", "02/01/2019",
				"03/05/2020", 10000.0, "fujita@pec.it", "325894512", "simone.tremila@gotmial.com", 
				"Simone", "Tremila", "21528525", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		spaRepo.save(spa);
		
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				HttpEntity.EMPTY,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.UNAUTHORIZED );
		
	}
	
	
	@Test
	@DisplayName("Cerco e trovo un cliente by id")
	public void getByIdOk() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Milano", "MI");
		provRepo.save(prov);
		Comune com= new Comune(null, "Cinisello Balsamo", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Via Cilea", "3", "Città", "20092", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Viale Macchiavelli", "9", "Città", "20092", com);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClienteSpa spa= new ClienteSpa(null, "Fujita s.p.a", "000012555", "fujita@gmail.com", "02/01/2019",
				"03/05/2020", 10000.0, "fujita@pec.it", "325894512", "simone.tremila@gotmial.com", 
				"Simone", "Tremila", "21528525", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		spaRepo.save(spa);
				System.out.println("------------------stampa-----------");
		System.out.println(spa.getId());
		
		ResponseEntity<ClienteSpa> r= getRestTemplate().exchange(URL+"/admin/"+spa.getId(), HttpMethod.GET, getAdminEntity(), ClienteSpa.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		ClienteSpa spa1= r.getBody();
		assertThat(  spa1.getId() ).isEqualTo(spa.getId());	
	}
	
	
	@Test
	@DisplayName("Cerco un cliente inesistente e ottengo status NOT_FOUND")
		public void getByIdKo() {
			ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/100000", HttpMethod.GET, getAdminEntity(), String.class);
			assertThat(  r.getStatusCode() ).isEqualTo( HttpStatus.NOT_FOUND );	
	}
	
	
	@Test
	@DisplayName("Inserisco un cliente con successo")
	public void testPostOk() {
		ClienteSpaDto spadto= new ClienteSpaDto("Kotoko s.p.a", "0256123888880", "kotoko@libero.it", "05/06/2021", "02/07/2022", 
				200000.0, "kotoko@pec.it", "3200000", "Irie-kun@hotmail.it", "Irie", "Kun", "3285285");
		
		HttpEntity<ClienteSpaDto> spaEntity = new HttpEntity<ClienteSpaDto>(spadto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, spaEntity , String.class);

		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.OK);
		ClienteSpa spa= spaRepo.findByPartitaIva(spadto.getPartitaIva());
		assertThat(spa).isNotNull();
		String messaggio = r.getBody();
		assertThat( messaggio ).isEqualTo( "Cliente inserito");
		
	}
	
	@Test
	@DisplayName("User tenta di inserire  senza successo una provincia")
	public void testInsertKoAdmin() {
		ClienteSpaDto spadto= new ClienteSpaDto("Kotoko s.p.a", "02561255555880", "kotoko@libero.it", "05/06/2021", "02/07/2022", 
				200000.0, "kotoko@pec.it", "3200000", "Irie-kun@hotmail.it", "Irie", "Kun", "3285285");
		
		HttpEntity<ClienteSpaDto> spaEntity = new HttpEntity<ClienteSpaDto>(spadto, getUserHeaders());
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				spaEntity,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.FORBIDDEN );

		
	}
	
	
	
	
	@Test
	@DisplayName("Provo ad inserire un cliente già esistente ottengo status BAD_REQUEST")
	public void postKoAlreadyInsertedException() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Roma", "RM");
		provRepo.save(prov);
		Comune com= new Comune(null, "Fiumicino", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Via Orbetello", "3", "Città", "00054", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Viale Italia", "9", "Città", "00054", com);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClienteSpa spa= new ClienteSpa(null, "Fujiko s.pa", "55000888845545", "Elisabeth@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Elisabeth@pec.it", "327889951", "Fitzwilliam@gmail.com",
				"Fitzwilliam", "Darcy", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		spaRepo.save(spa);
		ClienteSpaDto spadto= new ClienteSpaDto("Fujiko s.pa", "55000888845545", "Elisabeth@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Elisabeth@pec.it", "327889951", "Fitzwilliam@gmail.com",
				"Fitzwilliam", "Darcy", "3288541218252");
		
		HttpEntity<ClienteSpaDto> spaEntity = new HttpEntity<ClienteSpaDto>(spadto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, spaEntity , String.class);
		assertThat( r.getStatusCode() ).isEqualTo( HttpStatus.BAD_REQUEST );
		
		
	}
	
	
	@Test
	@DisplayName("Provo ad inserire un cliente ma il nome è vuoto e ottengo status BAD_REQUEST")
	public void postKoValidationErrors() {
		ClienteSpaDto spadto= new ClienteSpaDto("", "55845545", "Elisabeth.Bennet@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Elisabeth.Bennet@pec.it", "327889951", "Fitzwilliam.darcy@gmail.com",
				"Fitzwilliam", "Darcy", "3288541218252");

		HttpEntity<ClienteSpaDto> spaEntity = new HttpEntity<ClienteSpaDto>(spadto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, spaEntity , String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.BAD_REQUEST );
		
	}
	
	@Test
	@DisplayName("Elimino un cliente by id")
	public void DeleteByIdOk() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Roma", "RM");
		provRepo.save(prov);
		Comune com= new Comune(null, "Fiumicino", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Via Orbetello", "3", "Città", "00054", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Viale Italia", "9", "Città", "00054", com);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClienteSpa spa= new ClienteSpa(null, "Fujiko s.pa", "5501111115545", "Elisabeth@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Elisabeth@pec.it", "327889951", "Fitzwilliam@gmail.com",
				"Fitzwilliam", "Darcy", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		spaRepo.save(spa);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/"+spa.getId(),
				HttpMethod.DELETE, getAdminEntity(), String.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		
	}
		
	
}
