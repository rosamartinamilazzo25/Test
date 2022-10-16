package com.acme.test.clienti.pa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.acme.test.indirizzo.IndirizzoImp;

@Repository
public interface ClientePaRepository extends PagingAndSortingRepository<ClientePa, Long> {
	public ClientePa findByPartitaIva (String partitaIva);
	public boolean existsByPartitaIva(String partitaIva);

}
