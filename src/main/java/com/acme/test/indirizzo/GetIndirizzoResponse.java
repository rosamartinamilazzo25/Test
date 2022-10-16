package com.acme.test.indirizzo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetIndirizzoResponse {
	private long id;
	private String via;
	private String numeroCivico;
	private String localita;
	private String nomeComune;
	private String nomeProvincia;
	private String siglaProvincia; 

}
