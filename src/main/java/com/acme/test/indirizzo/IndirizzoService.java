package com.acme.test.indirizzo;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.acme.test.errors.AlreadyDeletedException;
import com.acme.test.errors.AlreadyInsertedException;
import com.acme.test.indirizzo.comune.Comune;
import com.acme.test.indirizzo.comune.ComuneRepository;
import com.acme.test.indirizzo.comune.ComuneService;
import com.acme.test.indirizzo.provincia.Provincia;
import com.acme.test.indirizzo.provincia.ProvinciaRepository;
import com.acme.test.indirizzo.provincia.ProvinciaService;

@Service
@Validated
public class IndirizzoService {
	@Autowired
	IndirizzoRepository indirizzoRepo;
	@Autowired
	ComuneRepository comuneRepo;
	@Autowired
	ProvinciaRepository provinciaRepo;
	@Autowired
	ComuneService comuneService;
	@Autowired
	ProvinciaService provinciaService;

	//GET ALL
	public List<IndirizzoImp> getAllIndirizzi(){
		return (List<IndirizzoImp>) indirizzoRepo.findAll();
	}

	//GET (id)
	public IndirizzoImp getIndirizzoById(long id) {
		if(! indirizzoRepo.existsById(id)) throw new EntityNotFoundException ("Indirizzo non esistente");
		return indirizzoRepo.findById(id).get();

	}

	//POST
	public void postIndirizzo (@Valid IndirizzoDto dto) {
		Provincia prov = provinciaService.getProvincia(dto.getSiglaProvincia());
		Comune com= comuneService.getComune(dto.getNomeComune(), prov.getId());
		if(indirizzoRepo.existsByViaAndNumeroCivicoAndLocalitaAndCapAndComune_id(dto.getVia(), dto.getNumeroCivico(), dto.getLocalita(),
				dto.getCap(), com.getId())) throw new AlreadyInsertedException ("Indirizzo già inserito");
		IndirizzoImp indirizzo= new IndirizzoImp();
		BeanUtils.copyProperties(dto, indirizzo);
		indirizzo.setComune(com);
		indirizzoRepo.save(indirizzo);

	}
	
	//DELETE (id)	
	public void deleteIndirizzo (long id) {
		if (! indirizzoRepo.existsById(id)) throw new AlreadyDeletedException ("Indirizzo già cancellato");
		indirizzoRepo.deleteById(id);
	}
	
	
	//PUT (id)
	public void putIndirizzo(long id, @Valid IndirizzoDto dto) {
		Provincia prov = provinciaService.getProvincia(dto.getSiglaProvincia());
		Comune com= comuneService.getComune(dto.getNomeComune(), prov.getId());
		IndirizzoImp indirizzoInDb= indirizzoRepo.findById(id).get();
		BeanUtils.copyProperties(dto, indirizzoInDb);
		indirizzoInDb.setComune(com);
		indirizzoRepo.save(indirizzoInDb);
		
	}
	
	public IndirizzoImp getIndirizzo(String via, String numeroCivico,String localita, String cap, Long idcomune) {
	if(! indirizzoRepo.existsByViaAndNumeroCivicoAndLocalitaAndCapAndComune_id(via, numeroCivico, localita, cap, idcomune) ) throw new EntityNotFoundException("indirizzo non esistente");
	return indirizzoRepo.findByViaAndNumeroCivicoAndLocalitaAndCapAndComune_id(via, numeroCivico, localita, cap, idcomune);
}
	

}
