package com.acme.test.indirizzo.provincia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;
import com.acme.test.general.TestBase;
import com.acme.test.indirizzo.comune.Comune;

public class ProvinciaServiceTest extends TestBase{
	@Autowired
	ProvinciaService provService;

	@Autowired
	ProvinciaRepository provRepo;


	@Test
	@DisplayName("Inserisco una provincia e la trovo con il servizio")
	public void testGetOk(){
		Provincia prov =new Provincia(null, "Brescia", "BS");
		provRepo.save(prov);

		Provincia provFound = provService.getProvinciaById(prov.getId());
		assertThat(provFound).isNotNull();
		assertThat(provFound.getId()).isEqualTo(prov.getId());
	}

	@Test
	@DisplayName("Cerco una provincia inesistente")
	public void testGetKo() {
		assertThatThrownBy( () -> provService.getProvinciaById(10000000000l))		
		.isInstanceOf(EntityNotFoundException.class)
		.hasMessage("Provincia non esistente");

	}

	@Test
	@DisplayName("Inserisco una provincia usando il servizio")
	public void testPostOk() {
		ProvinciaDto provdto= new ProvinciaDto("Varese", "VA");

		provService.postProvincia(provdto);
		Provincia prov= provRepo.findBySigla(provdto.getSigla());
		assertThat(prov).isNotNull();
		assertThat(prov.getSigla()).isEqualTo(provdto.getSigla());
	}

	@Test
	@DisplayName("Provo ad inserire una provincia dando dei valori non validi")
	public void testPostWithWrongValues() {
		ProvinciaDto provdto= new ProvinciaDto("", "");
		assertThatThrownBy(() -> provService.postProvincia(provdto))
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("dto.nome")
		.hasMessageContaining("dto.sigla");
	}

	@Test
	@DisplayName("Provo ad inserire una provincia usando il servizio ed ottengo un eccezione di Tipo AlreadyInsertedException")
	public void testPostKoAlreadyPresent() {
		Provincia prov= new Provincia (null, "Milano", "MI");
		provRepo.save(prov);

		ProvinciaDto provdto= new ProvinciaDto(prov.getNome(), prov.getSigla());
		assertThatThrownBy(() -> provService.postProvincia(provdto))
		.isInstanceOf(AlreadyInsertedException.class)
		.hasMessage("Provincia già inserita");
	}

	@Test
	@DisplayName("Cerco di inserire una provincia ma il dato errato del nome provocano un eccezione di tipo CostraintValidationException")
	public void testValidationKo() {
		ProvinciaDto provdto= new ProvinciaDto("L", "TO");
		assertThatThrownBy(() -> provService.postProvincia(provdto))
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("dto.nome");

	}

	@Test
	@DisplayName("Elimino una provincia")
	public void testDeletOk(){
		Provincia prov =new Provincia(null, "Alcamo", "AL");
		provRepo.save(prov);

		provService.deleteProvincia(prov.getId());
		assertThatThrownBy( () -> provService.getProvinciaById(prov.getId()) )
        .isInstanceOf(  EntityNotFoundException.class )
        .hasMessage("Provincia non esistente");
		

	}
	
}