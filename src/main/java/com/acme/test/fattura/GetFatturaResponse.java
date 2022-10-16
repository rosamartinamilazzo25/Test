package com.acme.test.fattura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFatturaResponse {
	private long id;
	private int anno;
	private String data;
	private double importo;
	private String numeroFattura;
	private String stato;

//
}
