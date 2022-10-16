package com.acme.test.indirizzo.comune;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;
import com.acme.test.general.TestBase;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;

public class ComuneServiceTest extends TestBase{
	@Autowired
	ComuneService comuneService;
	@Autowired
	ComuneRepository comuneRepo;
	@Autowired
	ProvinciaRepository provinciaRepo;


	@Test
	@DisplayName("Inserisco un comune e lo trovo con il servizio")
	public void testGetOk(){
		Provincia prov =new Provincia(null, "Napoli", "NA");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Procida", prov);
		comuneRepo.save(com);

		Comune comFound = comuneService.getComuneById(com.getId());
		assertThat(comFound).isNotNull();
		assertThat(comFound.getId()).isEqualTo(com.getId());
	}


	@Test
	@DisplayName("Cerco un comune inesistente")
	public void testGetKo() {
		assertThatThrownBy( () -> comuneService.getComuneById(10000000000l))		
		.isInstanceOf(EntityNotFoundException.class)
		.hasMessage("Comune non esistente");

	}	

	@Test
	@DisplayName("Inserisco un comune usando il servizio")
	public void testPostOk() {
		Provincia prov= new Provincia(null, "Messina", "ME");
		provinciaRepo.save(prov);
		ComuneDto comDto = new ComuneDto("Milazzo", "Messina", "ME");
		comuneService.postComune(comDto);
		Comune com= comuneRepo.findByNomeAndProvincia_id(comDto.getNome(), prov.getId());
		assertThat(com).isNotNull();
		assertThat(com.getNome()).isEqualTo(comDto.getNome());

	}
	

	@Test
	@DisplayName("Provo ad inserire un comune dando dei valori non validi")
	public void testPostWithWrongValues() {
		ComuneDto comdto= new ComuneDto("", "", "");
		assertThatThrownBy(() -> comuneService.postComune(comdto))
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("dto.nome")
		.hasMessageContaining("dto.siglaProvincia")
		.hasMessageContaining("dto.nomeProvincia");
	}
	
	

	@Test
	@DisplayName("Provo ad inserire un comune usando il servizio ed ottengo un eccezione di Tipo AlreadyInsertedException")
	public void testPostKoAlreadyPresent() {
		Provincia prov= new Provincia(null,"Vercelli", "VC");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Breia", prov);
		comuneRepo.save(com);
		ComuneDto comdto= new ComuneDto(com.getNome(), com.getProvincia().getNome(), com.getProvincia().getSigla());
		assertThatThrownBy(() -> comuneService.postComune(comdto))
		.isInstanceOf(AlreadyInsertedException.class)
		.hasMessage("Comune già inserito");	
	}
	

	@Test
	@DisplayName("Cerco di inserire un comune ma il dato errato del nome provocano un eccezione di tipo CostraintValidationException")
	public void testValidationKo() {
		ComuneDto comdto= new ComuneDto("F", "Roma", "RM");
		assertThatThrownBy(() -> comuneService.postComune(comdto))
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("dto.nome");		
	}
	
	@Test
	@DisplayName("Elimino un comune")
	public void testDeletOk(){
		Provincia prov =new Provincia(null, "Cuneo", "CN");
		provinciaRepo.save(prov);
		Comune com= new Comune(null, "Alba", prov);
		comuneRepo.save(com);
		
		comuneService.deleteComune(com.getId());
		assertThatThrownBy( () -> comuneService.getComuneById(com.getId()) )
        .isInstanceOf(  EntityNotFoundException.class )
        .hasMessage("Comune non esistente");

	}
	
}