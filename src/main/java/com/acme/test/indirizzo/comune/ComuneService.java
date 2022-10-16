package com.acme.test.indirizzo.comune;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;
import com.acme.test.indirizzo.provincia.ProvinciaService;


@Service
@Validated
public class ComuneService {
	@Autowired
	ComuneRepository comuneRepo;
	@Autowired
	ProvinciaRepository provinciaRepo;
	@Autowired
	ProvinciaService provinciaService;

	//GET ALL
	public List<Comune> getAllComune(){
		return (List<Comune>) comuneRepo.findAll();
	}

	//GET (id)
	public Comune getComuneById(long id) {
		if(! comuneRepo.existsById(id)) throw new EntityNotFoundException ("Comune non esistente");
		return comuneRepo.findById(id).get();

	}

	//POST 
	public void postComune (@Valid ComuneDto dto) {
		Provincia prov = provinciaService.getProvincia(dto.getSiglaProvincia());
		if (comuneRepo.existsByNomeAndProvincia_id(dto.getNome(), prov.getId()))throw new  AlreadyInsertedException("Comune già inserito");
		Comune com= new Comune();
		BeanUtils.copyProperties(dto, com);
		com.setProvincia(prov);
		comuneRepo.save(com);

	}
	
	//DELETE (id)	
	public void deleteComune (long id) {
		if (!comuneRepo.existsById(id)) throw new EntityNotFoundException ("Comune non esistente");
		comuneRepo.deleteById(id);
	}

	
	//PUT (id)
	public void putComune(long id, @Valid ComuneDto dto) {
		Provincia prov = provinciaService.getProvincia(dto.getSiglaProvincia());
		Comune comInDb= comuneRepo.findById(id).get();
		BeanUtils.copyProperties(dto, comInDb);
		comInDb.setProvincia(prov);
		comuneRepo.save(comInDb);
		
	}
	
	public Comune getComune(String nome, long id) {
		if(! comuneRepo.existsByNomeAndProvincia_id(nome, id)) throw new EntityNotFoundException("Comune non esistente");
		return comuneRepo.findByNomeAndProvincia_id(nome, id);
	}
}


