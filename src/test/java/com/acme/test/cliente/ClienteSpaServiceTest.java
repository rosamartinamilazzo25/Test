package com.acme.test.cliente;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.acme.test.clienti.spa.ClienteSpa;
import com.acme.test.clienti.spa.ClienteSpaDto;
import com.acme.test.clienti.spa.ClienteSpaRepository;
import com.acme.test.clienti.spa.ClienteSpaService;
import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;
import com.acme.test.fattura.Fattura;
import com.acme.test.fattura.FatturaImp;
import com.acme.test.general.TestBase;
import com.acme.test.indirizzo.IndirizzoImp;
import com.acme.test.indirizzo.IndirizzoRepository;
import com.acme.test.indirizzo.comune.Comune;
import com.acme.test.indirizzo.comune.ComuneRepository;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;

public class ClienteSpaServiceTest extends TestBase{
	@Autowired
	ClienteSpaService spaService;
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
	
	@Test
	@DisplayName("Inserisco il cliente e lo trovo con il servizio ")
	public void testGetOk() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Roma", "RM");
		provRepo.save(prov);
		Comune com= new Comune(null, "Fiumicino", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Via Orbetello", "3", "Città", "00054", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Via tempio della fortuna", "6", "Città", "00054", com);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClienteSpa spa= new ClienteSpa(null, "rosa viola s.p.a", "148828851510", "rosa.viola@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "rosa.viola@pec.it", "327889951", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		spaRepo.save(spa);
		ClienteSpa spafound= spaService.getClienteSpaById(spa.getId());
		assertThat(  spafound).isNotNull();

	}
	
	
	
	@Test
	@DisplayName("Cerco un cliente inesistente")
	public void testGetKo() {
		assertThatThrownBy( () -> spaService.getClienteSpaById(10000000000l))		
		.isInstanceOf(EntityNotFoundException.class)
		.hasMessage("Cliente non esistente");
}
	
	
	@Test
	@DisplayName("Inserisco un cliente usando il servizio")
	public void testPostOk() {
		ClienteSpaDto spadto= new ClienteSpaDto("Mika Yamamori s.p.a", "144845148851510", "Mika.Yamamori@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "mika.yamamoti@pec.it", "327889951", "Keiko.Iwashita@gmail.com",
				"keiko", "Iwashita", "3288541218252");
	 	spaService.postClienteSpa(spadto);
	 	ClienteSpa spa= spaRepo.findByPartitaIva(spadto.getPartitaIva());
	 	assertThat(spa).isNotNull();
	 	assertThat(spa.getPartitaIva()).isEqualTo(spadto.getPartitaIva());
	 	
	}
	

	@Test
	@DisplayName("Provo ad inserire un cliente dando dei valori non validi")
	public void testPostWithWrongValues() {
		ClienteSpaDto spadto= new ClienteSpaDto("", "1286511", "Maki.Usami@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Maki.Usami@pec.it", "327889951", "Keiko.Iwashita@gmail.com",
				"Keiko", "Iwashita", "3288541218252");
				assertThatThrownBy(() -> spaService.postClienteSpa(spadto))
				.isInstanceOf(ConstraintViolationException.class)
				.hasMessageContaining("dto.ragioneSociale");
	}

	
	
	@Test
	@DisplayName("Provo ad inserire un cliente usando il servizio ed ottengo un eccezione di Tipo AlreadyInsertedException")
	public void testPostKoAlreadyPresent() {
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Roma", "RM");
		provRepo.save(prov);
		Comune com= new Comune(null, "Fiumicino", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Via Orbetello", "3", "Città", "00054", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Vis tempio della fortuna", "6", "Città", "00054", com);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClienteSpa spa= new ClienteSpa(null, "Hero s.p.a", "123456", "Hero@gmail.com", "01/08/2019",
				"06/07/2022", 1000000.0, "Hero@pec.it", "32101232", "Daisuke.Hagiwara", "Daisuke",
				"Hagiwara", "1112185228", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		spaRepo.save(spa);
		
		ClienteSpaDto spadto= new ClienteSpaDto(spa.getRagioneSociale(), spa.getPartitaIva(), spa.getEmail(), spa.getDataInserimento(),
				spa.getDataUltimoContatto(), spa.getFatturatoAnnuale(), spa.getPec(), spa.getNumeroTelefono(),
				spa.getEmailContatto(), spa.getNomeContatto(), spa.getCognomeContatto(), spa.getTelefonoContatto());
		assertThatThrownBy(() -> spaService.postClienteSpa(spadto))
		.isInstanceOf(AlreadyInsertedException.class)
		.hasMessage("Cliente già inserito");
			
	}
	
	@Test
	@DisplayName("Elimino un cliente")
	public void testDeletOk(){
		List<FatturaImp> f= new ArrayList<FatturaImp>();
		Provincia prov= new Provincia(null, "Roma", "RM");
		provRepo.save(prov);
		Comune com= new Comune(null, "Fiumicino", prov);
		comRepo.save(com);
		IndirizzoImp indirizzoSedeLegale= new IndirizzoImp (null, "Via Orbetello", "3", "Città", "00054", com);
		indirizzoSedeLegaleRepo.save(indirizzoSedeLegale);
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Vis tempio della fortuna", "6", "Città", "00054", com);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClienteSpa spa= new ClienteSpa(null, "Hero s.p.a", "1230000456", "Hero@gmail.com", "01/08/2019",
				"06/07/2022", 1000000.0, "Hero@pec.it", "32101232", "Daisuke.Hagiwara", "Daisuke",
				"Hagiwara", "1112185228", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		spaRepo.save(spa);
		
		spaService.deleteClienteSpa(spa.getId());
		assertThatThrownBy( () -> spaService.getClienteSpaById(spa.getId()) )
        .isInstanceOf(  EntityNotFoundException.class )
        .hasMessage("Cliente non esistente");
		
	}
	
	
}
