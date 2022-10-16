package com.acme.test.fattura;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;

@Service
@Validated
public class FatturaService {
	@Autowired
	FatturaRepository fatturaRepo;
	
	//GET ALL
	public List<FatturaImp> getAllFattura(){
		return (List<FatturaImp>) fatturaRepo.findAll();
	}
	
	//GET (id)
	public FatturaImp getFatturaById(long id) {
		if(!fatturaRepo.existsById(id)) throw new EntityNotFoundException ("Fattura non esistente");
		return fatturaRepo.findById(id).get();
	}

	//POST
	public void postFattura (@Valid FatturaDto dto) {
		if(fatturaRepo.existsByNumeroFatturaAndAnno(dto.getNumeroFattura(), dto.getAnno())) throw new AlreadyInsertedException("Fattura già inserita");
		FatturaImp fattura= new FatturaImp();
		BeanUtils.copyProperties(dto, fattura);
		fatturaRepo.save(fattura);
	}
	
	//DELETE (id)
	public void deleteFattura(long id) {
		if(!fatturaRepo.existsById(id))throw new EntityNotFoundException ("fattura non esistente");
		fatturaRepo.deleteById(id);
	}
	
	//PUT (id)
	public void putFattura(long id, @Valid FatturaDto dto) {
		FatturaImp fatturaInDb= fatturaRepo.findById(id).get();
		BeanUtils.copyProperties(dto, fatturaInDb);
		fatturaRepo.save(fatturaInDb);
		
	}
	
	
	
	public FatturaImp getFattura(String numeroFattura, int anno) {
		if(!fatturaRepo.existsByNumeroFatturaAndAnno(numeroFattura, anno)) throw new EntityNotFoundException("Fattura non esistente");
		return fatturaRepo.findByNumeroFatturaAndAnno(numeroFattura, anno);
	}
}
