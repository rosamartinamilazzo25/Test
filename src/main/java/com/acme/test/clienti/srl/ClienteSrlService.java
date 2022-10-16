package com.acme.test.clienti.srl;

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
public class ClienteSrlService {
	@Autowired
	ClienteSrlRepository srlRepo;
	
	//GET ALL
		public List<ClienteSrl> getAllClientiSrl(){
			return (List<ClienteSrl>) srlRepo.findAll();
		}
		
		
		//GET (id)
		public ClienteSrl getClienteSrlById(long id) {
			if(! srlRepo.existsById(id)) throw new EntityNotFoundException ("Cliente non esistente");
			return srlRepo.findById(id).get();
		}
		
		
		
		//POST
		public void postClienteSrl(@Valid ClienteSrlDto dto) {
			if(srlRepo.existsByPartitaIva(dto.getPartitaIva()))throw new AlreadyInsertedException("Cliente già inserito");
			ClienteSrl srl= new ClienteSrl();
			BeanUtils.copyProperties(dto, srl);
			srlRepo.save(srl);
		}
		
		//DELETE (id)
		public void deleteClienteSrl (long id) {
			if(! srlRepo.existsById(id))throw new AlreadyDeletedException ("Cliente già cancellato");
			srlRepo.deleteById(id);
		}
		
		//PUT (id)
		public void putClienteSrl(long id, @Valid ClienteSrlDto dto) {
			ClienteSrl clienteIndDb= srlRepo.findById(id).get();
			BeanUtils.copyProperties(dto, clienteIndDb);
			srlRepo.save(clienteIndDb);
		}
	}

