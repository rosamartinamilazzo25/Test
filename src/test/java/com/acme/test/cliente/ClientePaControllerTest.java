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
import com.acme.test.clienti.pa.ClientePaRepository;
import com.acme.test.fattura.FatturaImp;
import com.acme.test.general.TestControllerBase;
import com.acme.test.indirizzo.IndirizzoImp;
import com.acme.test.indirizzo.IndirizzoRepository;
import com.acme.test.indirizzo.comune.Comune;
import com.acme.test.indirizzo.comune.ComuneRepository;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaDto;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;

public class ClientePaControllerTest extends TestControllerBase {
	@Autowired
	ClientePaRepository paRepo;
	
	@Autowired
	ProvinciaRepository provRepo;
	@Autowired
	ComuneRepository comRepo;
	@Autowired
	IndirizzoRepository indirizzoSedeLegaleRepo;
	@Autowired
	IndirizzoRepository indirizzoSedeOperativaRepo;
	
	@Value("${test.entry.point}/clientePa")
	private String URL;
	
	
	
	@Test
	@DisplayName("cerco l'elenco dei clienti")
	public void testGetAll() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Palermo", "PA");
		provRepo.save(prov);
		Comune com= new Comune(null, "Misilmeri", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		Comune com1= new Comune(null, "Bagheria", prov);
		comRepo.save(com1);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com1);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClientePa pa= new ClientePa(null, "EverGrenn", "90251555121", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		paRepo.save(pa);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.GET, getAdminEntity(), String.class);
		assertThat( r.getStatusCode() ).isEqualTo(  HttpStatus.OK );
	}
	
	
	@Test
	@DisplayName("Provo a cercare  un cliente senza autorizzazione")
	public void testGetAllKo() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Palermo", "PA");
		provRepo.save(prov);
		Comune com= new Comune(null, "Misilmeri", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		Comune com1= new Comune(null, "Bagheria", prov);
		comRepo.save(com1);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com1);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClientePa pa= new ClientePa(null, "EverGrenn", "90888880881", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		paRepo.save(pa);
		
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
		Provincia prov= new Provincia(null, "Palermo", "PA");
		provRepo.save(prov);
		Comune com= new Comune(null, "Misilmeri", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		Comune com1= new Comune(null, "Bagheria", prov);
		comRepo.save(com1);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com1);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClientePa pa= new ClientePa(null, "EverGrenn", "901111555121", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		paRepo.save(pa);
		System.out.println("------------------stampa-----------");
		System.out.println(pa.getId());
		
		ResponseEntity<ClientePa> r= getRestTemplate().exchange(URL+"/admin/"+pa.getId(), HttpMethod.GET, getAdminEntity(), ClientePa.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		ClientePa pa1= r.getBody();
		assertThat(  pa1.getId() ).isEqualTo(pa.getId());	
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
		ClientePaDto padto= new ClientePaDto("Serena s.n.c", "88865611", "serena@libero.it", "02/04/2022", "03/05/2022",
				800000.0, 	"serena@pec.it", "32841589321", "Edoardo@libero.it", "Edoardo", "macchiavelli", "369484848");
		
		HttpEntity<ClientePaDto> paEntity = new HttpEntity<ClientePaDto>(padto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, paEntity , String.class);
		
		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.OK);
		ClientePa pa= paRepo.findByPartitaIva(padto.getPartitaIva());
		assertThat(pa).isNotNull();	
		String messaggio = r.getBody();
		assertThat( messaggio ).isEqualTo( "Cliente inserito");
		
	}
	
	@Test
	@DisplayName("User tenta di inserire  senza successo una provincia")
	public void testInsertKoAdmin() {
		ClientePaDto padto= new ClientePaDto("Serena s.n.c", "855595559", "serena@libero.it", "02/04/2022", "03/05/2022",
				800000.0, 	"serena@pec.it", "32841589321", "Edoardo@libero.it", "Edoardo", "macchiavelli", "369484848");
		
		HttpEntity<ClientePaDto> paEntity = new HttpEntity<ClientePaDto>(padto, getUserHeaders());
		
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				paEntity,
				String.class);		
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.FORBIDDEN );

		
	}
	
	
	
	
	@Test
	@DisplayName("Provo ad inserire un cliente già esistente ottengo status BAD_REQUEST")
	public void postKoAlreadyInsertedException() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Palermo", "PA");
		provRepo.save(prov);
		Comune com= new Comune(null, "Misilmeri", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		Comune com1= new Comune(null, "Bagheria", prov);
		comRepo.save(com1);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com1);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClientePa pa= new ClientePa(null, "EverGrenn", "9055895555121", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		paRepo.save(pa);
		ClientePaDto padto= new ClientePaDto("EverGrenn", "9055895555121", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252");
		
		HttpEntity<ClientePaDto> paEntity = new HttpEntity<ClientePaDto>(padto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, paEntity , String.class);
		assertThat( r.getStatusCode() ).isEqualTo( HttpStatus.BAD_REQUEST );
		
		
	}
	
	@Test
	@DisplayName("Provo ad inserire un cliente ma il nome è vuoto e ottengo status BAD_REQUEST")
	public void postKoValidationErrors() {
		ClientePaDto padto= new ClientePaDto("", "9055895555121", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252");
		HttpEntity<ClientePaDto> paEntity = new HttpEntity<ClientePaDto>(padto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, paEntity , String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.BAD_REQUEST );
		
	}
	
	@Test
	@DisplayName("Elimino un cliente by id")
	public void DeleteByIdOk() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Palermo", "PA");
		provRepo.save(prov);
		Comune com= new Comune(null, "Misilmeri", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		Comune com1= new Comune(null, "Bagheria", prov);
		comRepo.save(com1);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Contrada", "32", "Città", "90036", com1);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClientePa pa= new ClientePa(null, "EverGrenn", "900000008951", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		paRepo.save(pa);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/"+pa.getId(),
				HttpMethod.DELETE, getAdminEntity(), String.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		
	}
		
	
}
