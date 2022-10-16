package com.acme.test.indirizzo.comune;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComuneDto {
	@NotBlank(message = "Nome comune obbligatorio")
	@Size(min = 2, max = 60,message = "Il nome del comune deve essere minimo di 2 caratteri massimo di 60")
	private String nome;
	@NotBlank(message = "Nome provincia obbligatorio")
	@Size(min = 2, max = 60,message = "Il nome della provincia deve essere minimo di 10 caratteri massimo di 60")
	private  String nomeProvincia;
	@NotBlank(message = "Sigla provincia obbligatorio")
	@Size(min = 2, max = 2,message = "La sigla della provincia deve essere minimo di 2 caratteri")
	private String siglaProvincia;

}
