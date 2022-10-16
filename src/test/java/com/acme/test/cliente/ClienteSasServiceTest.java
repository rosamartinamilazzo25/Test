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

import com.acme.test.clienti.sas.ClienteSas;
import com.acme.test.clienti.sas.ClienteSasDto;
import com.acme.test.clienti.sas.ClienteSasRepository;
import com.acme.test.clienti.sas.ClienteSasService;
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

public class ClienteSasServiceTest extends TestBase{
	@Autowired
	ClienteSasService sasService;
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
		IndirizzoImp indirizzoSedeOperativa= new IndirizzoImp (null, "Vis tempio della fortuna", "6", "Città", "00054", com);
		indirizzoSedeOperativaRepo.save(indirizzoSedeOperativa);
		ClienteSas sas= new ClienteSas(null, "Sas Entreprise", "148851510", "rosa.viola@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "rosa.viola@pec.it", "327889951", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		sasRepo.save(sas);
		ClienteSas sasfound= sasService.getClienteSasById(sas.getId());
		assertThat(  sasfound).isNotNull();

	}
	
	@Test
	@DisplayName("Cerco un cliente inesistente")
	public void testGetKo() {
		assertThatThrownBy( () -> sasService.getClienteSasById(10000000000l))		
		.isInstanceOf(EntityNotFoundException.class)
		.hasMessage("Cliente non esistente");
}
	
	@Test
	@DisplayName("Inserisco un cliente usando il servizio")
	public void testPostOk() {
		ClienteSasDto sasdto= new ClienteSasDto("Sas Entreprise", "144845148851510", "rosa.viola@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "rosa.viola@pec.it", "327889951", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252");
	 	sasService.postClienteSas(sasdto);
	 	ClienteSas sas= sasRepo.findByPartitaIva(sasdto.getPartitaIva());
	 	assertThat(sas).isNotNull();
	 	assertThat(sas.getPartitaIva()).isEqualTo(sasdto.getPartitaIva());
	 	
	}
	
	@Test
	@DisplayName("Provo ad inserire un cliente dando dei valori non validi")
	public void testPostWithWrongValues() {
		ClienteSasDto sasdto= new ClienteSasDto("", "11111845148851510", "rosa.viola@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "rosa.viola@pec.it", "327889951", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252");
				assertThatThrownBy(() -> sasService.postClienteSas(sasdto))
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
		ClienteSas sas= new ClienteSas(null, "Sas Entreprise", "1488888851510", "rosa.viola@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "rosa.viola@pec.it", "327889951", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		sasRepo.save(sas);
		
		ClienteSasDto sasdto= new ClienteSasDto(sas.getRagioneSociale(), sas.getPartitaIva(), sas.getEmail(), sas.getDataInserimento(),
				sas.getDataUltimoContatto(), sas.getFatturatoAnnuale(), sas.getPec(), sas.getNumeroTelefono(),
				sas.getEmailContatto(), sas.getNomeContatto(), sas.getCognomeContatto(), sas.getTelefonoContatto());
		assertThatThrownBy(() -> sasService.postClienteSas(sasdto))
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
		ClienteSas sas= new ClienteSas(null, "Sas Entreprise", "148000001510", "rosa.viola@gmail.com", "01/08/2021",
				"05/07/2022", 800000.0, "rosa.viola@pec.it", "327889951", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		sasRepo.save(sas);
		
		sasService.deleteClienteSas(sas.getId());
		assertThatThrownBy( () -> sasService.getClienteSasById(sas.getId()) )
        .isInstanceOf(  EntityNotFoundException.class )
        .hasMessage("Cliente non esistente");
	}
	
	
}
