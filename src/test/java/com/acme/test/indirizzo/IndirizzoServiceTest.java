package com.acme.test.indirizzo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;
import com.acme.test.general.TestBase;
import com.acme.test.indirizzo.comune.Comune;
import com.acme.test.indirizzo.comune.ComuneRepository;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;

public class IndirizzoServiceTest extends TestBase{
	
	@Autowired
	IndirizzoService indirizzoService;
	
	@Autowired
	IndirizzoRepository indirizzoRepo;
	@Autowired
	ComuneRepository comuneRepo;
	@Autowired
	ProvinciaRepository provinciaRepo;
	
	@Test
	@DisplayName("Inserisco un indirizzo e lo trovo con il servizio ")
	public void testGetOk() {
		Provincia prov= new Provincia(null, "Palermo", "PA");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Misilmeri", prov);
		comuneRepo.save(com);
		IndirizzoImp ind= new IndirizzoImp(null, "Contrada Feotto cannita", "32", "Montana", "90036", com);
		indirizzoRepo.save(ind);
		
		IndirizzoImp indfound= indirizzoService.getIndirizzoById(ind.getId());
		assertThat( indfound).isNotNull();
		assertThat(indfound.getId()).isEqualTo(ind.getId());

	}
	
	@Test
	@DisplayName("Cerco un indirizzo inesistente")
	public void testGetKo() {
		assertThatThrownBy( () -> indirizzoService.getIndirizzoById(10000000000l))		
		.isInstanceOf(EntityNotFoundException.class)
		.hasMessage("Indirizzo non esistente");
		
	
	}	
	

	@Test
	@DisplayName("Inserisco un indirizzo usando il servizio")
	public void testPostOk() {
		Provincia prov= new Provincia(null, "Ogliastra", "OG");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Elini", prov);
		comuneRepo.save(com);
		IndirizzoDto inddto= new IndirizzoDto("Viale", "2", "Città", "000125", "Elini", "Ogliastra", "OG");
		indirizzoService.postIndirizzo(inddto);
		
		IndirizzoImp ind= indirizzoRepo.findByViaAndNumeroCivicoAndLocalitaAndCapAndComune_id(inddto.getVia(), inddto.getNumeroCivico(), inddto.getLocalita(), inddto.getCap(),com.getId() );
		assertThat(ind).isNotNull();
		assertThat(ind.getVia()).isEqualTo(inddto.getVia());
		assertThat(ind.getNumeroCivico()).isEqualTo(inddto.getNumeroCivico());
		assertThat(ind.getLocalita()).isEqualTo(inddto.getLocalita());
		assertThat(ind.getCap()).isEqualTo(inddto.getCap());
}
	
	@Test
	@DisplayName("Provo ad inserire un indirizzo dando dei valori non validi")
	public void testPostWithWrongValues() {
		IndirizzoDto inddto= new IndirizzoDto("", "", "", "", "", "", "");
		assertThatThrownBy(() -> indirizzoService.postIndirizzo(inddto))
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("dto.via")
		.hasMessageContaining("dto.numeroCivico")
		.hasMessageContaining("dto.localita")
		.hasMessageContaining("dto.cap")
		.hasMessageContaining("dto.nomeComune")
		.hasMessageContaining("dto.siglaProvincia");
		
		
	}
	@Test
	@DisplayName("Provo ad inserire un indirizzo usando il servizio ed ottengo un eccezione di Tipo AlreadyInsertedException")
	public void testPostKoAlreadyPresent() {
		Provincia prov= new Provincia(null, "Alessadria", "AL");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Alessandria", prov);
		comuneRepo.save(com);
		IndirizzoImp ind= new IndirizzoImp(null, "Via Mattarella", "42", "Citta", "90011", com);
		indirizzoRepo.save(ind);
		IndirizzoDto inddto= new IndirizzoDto(ind.getVia(), ind.getNumeroCivico(), ind.getLocalita(), ind.getCap(), ind.getComune().getNome(),ind.getComune().getProvincia().getNome(), ind.getComune().getProvincia().getSigla());
		assertThatThrownBy(() -> indirizzoService.postIndirizzo(inddto))
		.isInstanceOf(AlreadyInsertedException.class)
		.hasMessage("Indirizzo già inserito");		
	}
	
	@Test
	@DisplayName("Cerco di inserire un cindirizzo ma il dato errato della via e del numero civico provocano un eccezione di tipo CostraintValidationException")
	public void testValidationKo() {
		IndirizzoDto inddto= new IndirizzoDto("L", "92485641235981", "Mare", "90034", "Misilmeri", "Palermo", "PA");	
		assertThatThrownBy(() -> indirizzoService.postIndirizzo(inddto))
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("dto.via")
		.hasMessageContaining("dto.numeroCivico");
	}
	
	
	@Test
	@DisplayName("Elimino un indirizzo")
	public void testDeletOk(){
		Provincia prov =new Provincia(null, "Cuneo", "CN");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Alba", prov);
		comuneRepo.save(com);
		IndirizzoImp ind= new IndirizzoImp(null, "Via alba maggiore", "8", "Citta", "12100", com);
		indirizzoRepo.save(ind);
		
		indirizzoService.deleteIndirizzo(ind.getId());
		assertThatThrownBy( () -> indirizzoService.getIndirizzoById(ind.getId()) )
        .isInstanceOf(  EntityNotFoundException.class )
        .hasMessage("Indirizzo non esistente");
		
	}
	

	
}