package com.acme.test.fattura;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FatturaRepository extends PagingAndSortingRepository<FatturaImp, Long> {
	public FatturaImp findByNumeroFatturaAndAnno( String numeroFattura, int anno);
	public boolean existsByNumeroFatturaAndAnno( String numeroFattura, int anno);

}
