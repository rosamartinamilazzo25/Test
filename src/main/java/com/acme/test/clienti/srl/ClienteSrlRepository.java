package com.acme.test.clienti.srl;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteSrlRepository extends PagingAndSortingRepository<ClienteSrl, Long> {
	public ClienteSrl findByPartitaIva (String partitaIva);
	public boolean existsByPartitaIva(String partitaIva);

}
