package com.acme.test.indirizzo.provincia;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinciaRepository extends PagingAndSortingRepository<Provincia, Long> {
	public Provincia findBySigla(String sigla);
	public boolean existsBySigla(String sigla);
}
