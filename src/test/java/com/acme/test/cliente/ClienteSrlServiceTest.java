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

import com.acme.test.clienti.srl.ClienteSrl;
import com.acme.test.clienti.srl.ClienteSrlDto;
import com.acme.test.clienti.srl.ClienteSrlRepository;
import com.acme.test.clienti.srl.ClienteSrlService;
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

public class ClienteSrlServiceTest extends TestBase{
	@Autowired
	ClienteSrlService srlService;
	@Autowired
	ClienteSrlRepository srlRepo;
	
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
		ClienteSrl srl= new ClienteSrl(null, "fuko s.r.l", "00000121212", "fuko@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "fuko@pec.it", "327889951", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		srlRepo.save(srl);
		ClienteSrl srlfound= srlService.getClienteSrlById(srl.getId());
		assertThat(  srlfound).isNotNull();

	}
	
	
	
	@Test
	@DisplayName("Cerco un cliente inesistente")
	public void testGetKo() {
		assertThatThrownBy( () -> srlService.getClienteSrlById(10000000000l))		
		.isInstanceOf(EntityNotFoundException.class)
		.hasMessage("Cliente non esistente");
}
	
	
	@Test
	@DisplayName("Inserisco un cliente usando il servizio")
	public void testPostOk() {
		ClienteSrlDto srldto= new ClienteSrlDto("Mika Yamamori s.p.a", "1448000000510", "Mika.Yamamori@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "mika.yamamoti@pec.it", "327889951", "Keiko.Iwashita@gmail.com",
				"keiko", "Iwashita", "3288541218252");
	 	srlService.postClienteSrl(srldto);
	 	ClienteSrl srl= srlRepo.findByPartitaIva(srldto.getPartitaIva());
	 	assertThat(srl).isNotNull();
	 	assertThat(srl.getPartitaIva()).isEqualTo(srldto.getPartitaIva());
	 	
	}
	

	@Test
	@DisplayName("Provo ad inserire un cliente dando dei valori non validi")
	public void testPostWithWrongValues() {
		ClienteSrlDto srldto= new ClienteSrlDto("", "12865110000", "Maki.Usami@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "Maki.Usami@pec.it", "327889951", "Keiko.Iwashita@gmail.com",
				"Keiko", "Iwashita", "3288541218252");
				assertThatThrownBy(() -> srlService.postClienteSrl(srldto))
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
		ClienteSrl srl= new ClienteSrl(null, "Hero s.p.a", "12345600000", "Hero@gmail.com", "01/08/2019",
				"06/07/2022", 1000000.0, "Hero@pec.it", "32101232", "Daisuke.Hagiwara", "Daisuke",
				"Hagiwara", "1112185228", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		srlRepo.save(srl);		
		ClienteSrlDto srldto= new ClienteSrlDto(srl.getRagioneSociale(), srl.getPartitaIva(), srl.getEmail(), srl.getDataInserimento(),
				srl.getDataUltimoContatto(), srl.getFatturatoAnnuale(), srl.getPec(), srl.getNumeroTelefono(),
				srl.getEmailContatto(), srl.getNomeContatto(), srl.getCognomeContatto(), srl.getTelefonoContatto());
		assertThatThrownBy(() -> srlService.postClienteSrl(srldto))
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
		ClienteSrl srl= new ClienteSrl(null, "Hero s.p.a", "12345600000", "Hero@gmail.com", "01/08/2019",
				"06/07/2022", 1000000.0, "Hero@pec.it", "32101232", "Daisuke.Hagiwara", "Daisuke",
				"Hagiwara", "1112185228", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		srlRepo.save(srl);	
		
		srlService.deleteClienteSrl(srl.getId());
		assertThatThrownBy( () -> srlService.getClienteSrlById(srl.getId()) )
        .isInstanceOf(  EntityNotFoundException.class )
        .hasMessage("Cliente non esistente");
		
	}
	
	
}
