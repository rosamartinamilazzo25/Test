package com.acme.test.clienti.spa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteSpaRepository extends PagingAndSortingRepository<ClienteSpa, Long> {
	public ClienteSpa findByPartitaIva (String partitaIva);
	public boolean existsByPartitaIva(String partitaIva);

}
