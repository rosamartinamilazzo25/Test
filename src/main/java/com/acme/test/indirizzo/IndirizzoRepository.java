package com.acme.test.indirizzo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.acme.test.indirizzo.comune.Comune;
import com.acme.test.indirizzo.provincia.Provincia;

@Repository
public interface IndirizzoRepository extends PagingAndSortingRepository<IndirizzoImp, Long> {
	public IndirizzoImp findByViaAndNumeroCivicoAndLocalitaAndCapAndComune_id(String via, String numeroCivico,String localita, String cap, Long idcomune);
	public boolean existsByViaAndNumeroCivicoAndLocalitaAndCapAndComune_id(String via, String numeroCivico,String localita, String cap, Long idcomune);
	

}
