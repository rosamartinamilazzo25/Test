package com.acme.test.indirizzo.provincia;

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
public class ProvinciaService {
	@Autowired
	ProvinciaRepository provinciaRepo;

	//GET ALL
	public List<Provincia> getAllProvincia(){
		return (List<Provincia>) provinciaRepo.findAll();
	}

	//GET (id)
	public Provincia getProvinciaById(long id) {
		if(! provinciaRepo.existsById(id)) throw new EntityNotFoundException ("Provincia non esistente");
		return provinciaRepo.findById(id).get();

	}

	//POST
	public void postProvincia (@Valid ProvinciaDto dto) {
		if(provinciaRepo.existsBySigla(dto.getSigla()))throw new AlreadyInsertedException("Provincia già inserita");
		Provincia prov= new Provincia();
		BeanUtils.copyProperties(dto, prov);
		provinciaRepo.save(prov);

	}
	
	//DELETE (id)	
	public void deleteProvincia (long id) {
		if (! provinciaRepo.existsById(id)) throw new EntityNotFoundException ("Comune non esistente");
		provinciaRepo.deleteById(id);
	}
	
	
	//PUT (id)
	public void putProvincia(long id, @Valid ProvinciaDto dto) {
		Provincia provInDb= provinciaRepo.findById(id).get();
		BeanUtils.copyProperties(dto, provInDb);
		provinciaRepo.save(provInDb);
		
	}
	
	
	public Provincia getProvincia(String sigla) {
		if(! provinciaRepo.existsBySigla(sigla)) throw new EntityNotFoundException("Provincia non esistente");
		return provinciaRepo.findBySigla(sigla);
		
	}
}


