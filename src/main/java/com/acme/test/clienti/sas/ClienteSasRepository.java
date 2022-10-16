package com.acme.test.clienti.sas;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteSasRepository extends PagingAndSortingRepository<ClienteSas, Long> {
	public ClienteSas findByPartitaIva (String partitaIva);
	public boolean existsByPartitaIva(String partitaIva);

}
