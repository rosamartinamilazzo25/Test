package com.acme.test.indirizzo.comune;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComuneRepository extends PagingAndSortingRepository<Comune, Long> {
	public Comune findByNomeAndProvincia_id(String nome, long id);
	public boolean existsByNomeAndProvincia_id(String nome, long id);

}
