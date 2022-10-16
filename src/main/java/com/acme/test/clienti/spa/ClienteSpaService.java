package com.acme.test.clienti.spa;

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
public class ClienteSpaService {
	@Autowired
	ClienteSpaRepository spaRepo;


	//GET ALL
	public List<ClienteSpa> getAllClientiSpa(){
		return (List<ClienteSpa>) spaRepo.findAll();
	}
	
	
	//GET (id)
	public ClienteSpa getClienteSpaById(long id) {
		if(! spaRepo.existsById(id)) throw new EntityNotFoundException ("Cliente non esistente");
		return spaRepo.findById(id).get();
	}
	
	
	
	//POST
	public void postClienteSpa (@Valid ClienteSpaDto dto) {
		if(spaRepo.existsByPartitaIva(dto.getPartitaIva()))throw new AlreadyInsertedException("Cliente già inserito");
		ClienteSpa spa= new ClienteSpa();
		BeanUtils.copyProperties(dto, spa);
		spaRepo.save(spa);
	}
	
	//DELETE (id)
	public void deleteClienteSpa (long id) {
		if(! spaRepo.existsById(id))throw new EntityNotFoundException ("Cliente non esistente");
		spaRepo.deleteById(id);
	}
	
	//PUT (id)
	public void putClienteSpa(long id, @Valid ClienteSpaDto dto) {
		ClienteSpa clienteIndDb= spaRepo.findById(id).get();
		BeanUtils.copyProperties(dto, clienteIndDb);
		spaRepo.save(clienteIndDb);
	}
}


