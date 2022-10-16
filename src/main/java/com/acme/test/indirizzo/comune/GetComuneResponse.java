package com.acme.test.indirizzo.comune;

import java.util.List;

import javax.persistence.Column;

import com.acme.test.indirizzo.provincia.Provincia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetComuneResponse {
	private long id;
	private String nome;
	private String nomeProvincia;
	private String siglaProvincia;
}
