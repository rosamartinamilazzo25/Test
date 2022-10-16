package com.acme.test.clienti.pa;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;
import com.acme.test.indirizzo.IndirizzoImp;
import com.acme.test.indirizzo.IndirizzoRepository;
import com.acme.test.indirizzo.IndirizzoService;
import com.acme.test.indirizzo.comune.ComuneRepository;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;

@Service
@Validated
public class ClientePaService {
	@Autowired
	ClientePaRepository paRepo;
	
	@Autowired
	IndirizzoService indirizzoService;

	

	
	
	//GET ALL
	public List<ClientePa> getAllClientiPa(){
		return (List<ClientePa>) paRepo.findAll();
	}
	
	
	//GET (id)
	public ClientePa getClientePaById(long id) {
		if(! paRepo.existsById(id)) throw new EntityNotFoundException ("Cliente non esistente");
		return paRepo.findById(id).get();
	}
	
	
	
	//POST
	public void postClientePa (@Valid ClientePaDto dto) {
		if(paRepo.existsByPartitaIva(dto.getPartitaIva()))throw new AlreadyInsertedException("Cliente già inserito");
		ClientePa pa= new ClientePa();
		BeanUtils.copyProperties(dto, pa);
		paRepo.save(pa);
	}
	
	//DELETE (id)
	public void deleteClientePa (long id) {
		if(! paRepo.existsById(id))throw new EntityNotFoundException ("Cliente non esistente");
		paRepo.deleteById(id);
	}
	
	//PUT (id)
	public void putClientePa(long id, @Valid ClientePaDto dto) {
		ClientePa clienteIndDb= paRepo.findById(id).get();
		BeanUtils.copyProperties(dto, clienteIndDb);
		paRepo.save(clienteIndDb);
	}
	
	public ClientePa getClienteByPartitaIva(String partitaIva) {
		if( ! paRepo.existsByPartitaIva(partitaIva))throw new EntityNotFoundException ("Cliente non esistente");
		return paRepo.findByPartitaIva(partitaIva);
		
	}
	



}
