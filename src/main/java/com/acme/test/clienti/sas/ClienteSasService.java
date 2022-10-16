package com.acme.test.clienti.sas;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.acme.test.clienti.pa.ClientePa;
import com.acme.test.clienti.pa.ClientePaDto;
import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;

@Service
@Validated
public class ClienteSasService {
	@Autowired
	ClienteSasRepository sasRepo;
	
	
	//GET ALL
		public List<ClienteSas> getAllClientiSas(){
			return (List<ClienteSas>) sasRepo.findAll();
		}
		
		
		//GET (id)
		public ClienteSas getClienteSasById(long id) {
			if(! sasRepo.existsById(id)) throw new EntityNotFoundException ("Cliente non esistente");
			return sasRepo.findById(id).get();
		}
		
		
		
		//POST
		public void postClienteSas (@Valid ClienteSasDto dto) {
			if(sasRepo.existsByPartitaIva(dto.getPartitaIva()))throw new AlreadyInsertedException("Cliente già inserito");
			ClienteSas sas= new ClienteSas();
			BeanUtils.copyProperties(dto, sas);
			sasRepo.save(sas);
		}
		
		//DELETE (id)
		public void deleteClienteSas (long id) {
			if(! sasRepo.existsById(id))throw new EntityNotFoundException ("Cliente non esistente");
			sasRepo.deleteById(id);
		}
		
		//PUT (id)
		public void putClienteSas(long id, @Valid ClienteSasDto dto) {
			ClienteSas clienteIndDb= sasRepo.findById(id).get();
			BeanUtils.copyProperties(dto, clienteIndDb);
			sasRepo.save(clienteIndDb);
		}
	}

