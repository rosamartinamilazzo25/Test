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

import com.acme.test.clienti.pa.ClientePa;
import com.acme.test.clienti.pa.ClientePaDto;
import com.acme.test.clienti.pa.ClientePaRepository;
import com.acme.test.clienti.pa.ClientePaService;
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

public class ClientePaServiceTest extends TestBase{
	@Autowired
	ClientePaService paService;
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
	
	@Test
	@DisplayName("Inserisco il cliente e lo trovo con il servizio ")
	public void testGetOk() {
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
		ClientePa pa= new ClientePa(null, "EverGrenn", "902515121", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		paRepo.save(pa);
		ClientePa pafound= paService.getClientePaById(pa.getId());
		assertThat(  pafound).isNotNull();

	}
	
	@Test
	@DisplayName("Cerco un cliente inesistente")
	public void testGetKo() {
		assertThatThrownBy( () -> paService.getClientePaById(10000000000l))		
		.isInstanceOf(EntityNotFoundException.class)
		.hasMessage("Cliente non esistente");
}
	
	@Test
	@DisplayName("Inserisco un cliente usando il servizio")
	public void testPostOk() {
		ClientePaDto padto= new ClientePaDto("Mario rossi s.n.c", "221018921", "mario.rossi@libero.it",
				"05/06/2021", "05/03/2022", 50000.0, "rossi.mario@pec.it", "3288848151", 
				"giuseppe.verdi@hotmail.it", "Giuseppe", "Verdi", "328488181");
	 	paService.postClientePa(padto);
	 	ClientePa pa= paRepo.findByPartitaIva(padto.getPartitaIva());
	 	assertThat(pa).isNotNull();
	 	assertThat(pa.getPartitaIva()).isEqualTo(padto.getPartitaIva());
	 	
	}
	
	@Test
	@DisplayName("Provo ad inserire un cliente dando dei valori non validi")
	public void testPostWithWrongValues() {
		ClientePaDto padto= new ClientePaDto("", "221018921", "mario.rossi@libero.it",
				"05/06/2021", "05/03/2022", 50000.0, "rossi.mario@pec.it", "3288848151", 
				"giuseppe.verdi@hotmail.it", "", "Verdi", "328488181");
				assertThatThrownBy(() -> paService.postClientePa(padto))
				.isInstanceOf(ConstraintViolationException.class)
				.hasMessageContaining("dto.ragioneSociale")
				.hasMessageContaining("dto.nomeContatto");
	}

	
	@Test
	@DisplayName("Provo ad inserire un cliente usando il servizio ed ottengo un eccezione di Tipo AlreadyInsertedException")
	public void testPostKoAlreadyPresent() {
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
		ClientePa pa= new ClientePa(null, "EverGrenn", "90254545415121", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		paRepo.save(pa);
		
		ClientePaDto padto= new ClientePaDto(pa.getRagioneSociale(), pa.getPartitaIva(), pa.getEmail(), pa.getDataInserimento(),
				pa.getDataUltimoContatto(), pa.getFatturatoAnnuale(), pa.getPec(), pa.getNumeroTelefono(),
				pa.getEmailContatto(), pa.getNomeContatto(), pa.getCognomeContatto(), pa.getTelefonoContatto());
		assertThatThrownBy(() -> paService.postClientePa(padto))
		.isInstanceOf(AlreadyInsertedException.class)
		.hasMessage("Cliente già inserito");
			
	}
	
	@Test
	@DisplayName("Elimino un cliente")
	public void testDeletOk(){
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
		ClientePa pa= new ClientePa(null, "EverGrenn", "900000000121", "mario.rossi@gmail.com", "02/05/2022",
				"05/07/2022", 100000.0, "mario.rossi@pec.it", "3245895145", "emanuele.bianchi@gmail.com",
				"Emanuele", "Bianchi", "3288541218252", indirizzoSedeLegale, indirizzoSedeOperativa, f);
		paRepo.save(pa);
		
		paService.deleteClientePa(pa.getId());
		assertThatThrownBy( () -> paService.getClientePaById(pa.getId()) )
        .isInstanceOf(  EntityNotFoundException.class )
        .hasMessage("Cliente non esistente");
		
	}
	
	
	
}
