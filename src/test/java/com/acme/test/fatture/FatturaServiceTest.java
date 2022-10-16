package com.acme.test.fatture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;
import com.acme.test.fattura.FatturaDto;
import com.acme.test.fattura.FatturaImp;
import com.acme.test.fattura.FatturaRepository;
import com.acme.test.fattura.FatturaService;
import com.acme.test.general.TestBase;

public class FatturaServiceTest extends TestBase{
	@Autowired
	FatturaService fatturaService;
	
	@Autowired
	FatturaRepository fatturaRepo;
	
	@Test
	@DisplayName("Inserisco una fattura e la trovo con il servizio")
	public void testGetOk() {
		FatturaImp fattura= new FatturaImp(null, 2022, "06/07/2022", 150.0, "01", "Pagata");
		fatturaRepo.save(fattura);
		
		FatturaImp fatturaFound= fatturaService.getFatturaById(fattura.getId());
		assertThat(fatturaFound).isNotNull();
		assertThat(fatturaFound.getId()).isEqualTo(fattura.getId());
		
	}
	
	
	@Test
	@DisplayName("Cerco una fattura inesistente")
	public void testGetKo() {
		assertThatThrownBy( () -> fatturaService.getFatturaById(10000000000l))		
		.isInstanceOf(EntityNotFoundException.class)
		.hasMessage("Fattura non esistente");
		
	}
	
	@Test
	@DisplayName("Provo ad inserire una fattura dando dei valori non validi")
	public void testPostWithWrongValues() {
		FatturaDto fatturadto= new FatturaDto(2022, "", 150.0, "01","Pagata");
		assertThatThrownBy(() -> fatturaService.postFattura(fatturadto))
		.isInstanceOf(ConstraintViolationException.class)
		.hasMessageContaining("dto.data");
		
	}
	
	@Test
	@DisplayName("Provo ad inserire una fattura usando il servizio ed ottengo un eccezione di Tipo AlreadyInsertedException")
	public void testPostKoAlreadyPresent() {
		FatturaImp fattura= new FatturaImp(null, 2021, "06/07/2021", 55.0, "02", "Pagata");
		fatturaRepo.save(fattura);
		
		FatturaDto fatturadto= new FatturaDto(fattura.getAnno(), fattura.getData(), fattura.getImporto(), fattura.getNumeroFattura(), fattura.getStato());
		assertThatThrownBy(() -> fatturaService.postFattura(fatturadto))
		.isInstanceOf(AlreadyInsertedException.class)
		.hasMessage("Fattura già inserita");
	}

	
	@Test
	@DisplayName("Elimino una fattura")
	public void testDeletOk(){
		FatturaImp fattura= new FatturaImp(null, 2021, "06/07/2021", 55.0, "12", "Pagata");
		fatturaRepo.save(fattura);
		
		fatturaService.deleteFattura(fattura.getId());
		assertThatThrownBy( () -> fatturaService.getFatturaById(fattura.getId()) )
        .isInstanceOf(  EntityNotFoundException.class )
        .hasMessage("Fattura non esistente");
		
		
	}
	

}
