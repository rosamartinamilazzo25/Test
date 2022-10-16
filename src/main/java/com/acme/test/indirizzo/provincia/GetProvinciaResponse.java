package com.acme.test.indirizzo.provincia;

import java.util.List;

import javax.persistence.Column;

import com.acme.test.indirizzo.comune.Comune;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProvinciaResponse {
	private long id;
	private String nome;
	private String sigla;

}
