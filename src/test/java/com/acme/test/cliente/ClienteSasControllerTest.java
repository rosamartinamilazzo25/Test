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
import com.acme.test.clienti.sas.ClienteSas;
import com.acme.test.clienti.sas.ClienteSasDto;
import com.acme.test.clienti.sas.ClienteSasRepository;
import com.acme.test.fattura.FatturaImp;
import com.acme.test.general.TestControllerBase;
import com.acme.test.indirizzo.IndirizzoImp;
import com.acme.test.indirizzo.IndirizzoRepository;
import com.acme.test.indirizzo.comune.Comune;
import com.acme.test.indirizzo.comune.ComuneRepository;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;

public class ClienteSasControllerTest extends TestControllerBase {
	@Autowired
	ClienteSasRepository sasRepo;
	
	@Autowired
	ProvinciaRepository provRepo;
	@Autowired
	ComuneRepository comRepo;
	@Autowired
	IndirizzoRepository indirizzoSedeLegaleRepo;
	@Autowired
	IndirizzoRepository indirizzoSedeOperativaRepo;
	
	@Value("${test.entry.point}/clienteSas")
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
		ClienteSas sas= new ClienteSas(null, "Sas studio s.a.s", "32106589", "serena.delia@gmail.com", "02/01/2019",
				"03/05/2020", 10000.0, "serena@pec.it", "325894512", "simone.tremila@gotmial.com", 
				"Simone", "Tremila", "21528525", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		sasRepo.save(sas);
		
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
		ClienteSas sas= new ClienteSas(null, "Sas studio s.a.s", "300055859", "serena.delia@gmail.com", "02/01/2019",
				"03/05/2020", 10000.0, "serena@pec.it", "325894512", "simone.tremila@gotmial.com", 
				"Simone", "Tremila", "21528525", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		sasRepo.save(sas);
		
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
		ClienteSas sas= new ClienteSas(null, "Sas s.a.s", "32106589", "Elisabeth.Bennet@gmail.com", "02/01/2019",
				"03/05/2020", 10000.0, "Elisabeth@pec.it", "325894512", "Fitzwilliam.darcy@gotmial.com", 
				"Fitzwilliam", "Darcy", "21528525", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		sasRepo.save(sas);
		System.out.println("------------------stampa-----------");
		System.out.println(sas.getId());
		
		ResponseEntity<ClienteSas> r= getRestTemplate().exchange(URL+"/admin/"+sas.getId(), HttpMethod.GET, getAdminEntity(), ClienteSas.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		ClienteSas sas1= r.getBody();
		assertThat(  sas1.getId() ).isEqualTo(sas.getId());	
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
		ClienteSasDto sasdto= new ClienteSasDto("SAS s.n.c", "02561230", "robin.hood@libero.it", "05/06/2021", "02/07/2022", 200000.0, "robin.hood@pec.it", "3200000", "sas@hotmail.it", "Luigi", "Maggiore", "3285285");
		
		HttpEntity<ClienteSasDto> sasEntity = new HttpEntity<ClienteSasDto>(sasdto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, sasEntity , String.class);
		assertThat(r.getStatusCode()).isEqualTo( HttpStatus.OK);
		ClienteSas sas= sasRepo.findByPartitaIva(sasdto.getPartitaIva());
		assertThat(sas).isNotNull();
		String messaggio = r.getBody();
		assertThat( messaggio ).isEqualTo( "Cliente inserito");
		
	}
	
	@Test
	@DisplayName("User tenta di inserire  senza successo una provincia")
	public void testInsertKoAdmin() {
ClienteSasDto sasdto= new ClienteSasDto("SAS s.n.c", "02561235550", "robin.hood@libero.it", "05/06/2021", "02/07/2022", 200000.0, "robin.hood@pec.it", "3200000", "sas@hotmail.it", "Luigi", "Maggiore", "3285285");
		
		HttpEntity<ClienteSasDto> sasEntity = new HttpEntity<ClienteSasDto>(sasdto, getUserHeaders());
		ResponseEntity<String> r = getRestTemplate().exchange(
				URL+"/admin", 
				HttpMethod.GET, 
				sasEntity,
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
		ClienteSas sas= new ClienteSas(null, "Sas Studio", "55558888845545", "Elisabeth@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Elisabeth@pec.it", "327889951", "Fitzwilliam@gmail.com",
				"Fitzwilliam", "Darcy", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		sasRepo.save(sas);
		ClienteSasDto sasdto= new ClienteSasDto("Sas Studio", "55558888845545", "Elisabeth@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Elisabeth@pec.it", "327889951", "Fitzwilliam@gmail.com",
				"Fitzwilliam", "Darcy", "3288541218252");
		
		HttpEntity<ClienteSasDto> sasEntity = new HttpEntity<ClienteSasDto>(sasdto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, sasEntity , String.class);
		assertThat( r.getStatusCode() ).isEqualTo( HttpStatus.BAD_REQUEST );
		
		
	}
	
	@Test
	@DisplayName("Provo ad inserire un cliente ma il nome è vuoto e ottengo status BAD_REQUEST")
	public void postKoValidationErrors() {
		ClienteSasDto sasdto= new ClienteSasDto("", "55558888845545", "Elisabeth.Bennet@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Elisabeth.Bennet@pec.it", "327889951", "Fitzwilliam.darcy@gmail.com",
				"Fitzwilliam", "Darcy", "3288541218252");
		
		HttpEntity<ClienteSasDto> sasEntity = new HttpEntity<ClienteSasDto>(sasdto, getAdminHeaders());
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin", HttpMethod.POST, sasEntity , String.class);
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
		ClienteSas sas= new ClienteSas(null, "Sas Studio", "5522222245545", "Elisabeth@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Elisabeth@pec.it", "327889951", "Fitzwilliam@gmail.com",
				"Fitzwilliam", "Darcy", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		sasRepo.save(sas);
		ResponseEntity<String> r= getRestTemplate().exchange(URL+"/admin/"+sas.getId(),
				HttpMethod.DELETE, getAdminEntity(), String.class);
		assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK );
		
	}
	
}
