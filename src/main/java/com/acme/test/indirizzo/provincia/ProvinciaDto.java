package com.acme.test.indirizzo.provincia;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinciaDto {
	@NotBlank(message = "Nome obbligatorio")
	@Size(min = 2, max = 60,message = "Il nome della provincia deve essere minimo di 2 caratteri massimo di 60")
	private String nome;
	@NotBlank(message = "sigla provincia obbligatorio")
	@Size(min = 2, max = 2,message = "Il nome della provincia deve essere di 2 caratteri")
	private String sigla;


}
