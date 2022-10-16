package com.acme.test.fattura;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FatturaDto {
	@NotNull(message = "Anno obbligatorio")
	private int anno;
	@NotBlank(message = "Data obbligatoria")
	private String data;
	@NotNull(message = "Importo Obbligatorio")
	private double importo;
	@NotBlank(message = "Numero fattura Obbligatorio")
	@Size(min = 2, max = 60,message = "Il numero della fattura deve essere minimo di 2 caratteri massimo di 60")
	private String numeroFattura;
	@NotBlank(message = "Stato obbligatorio")
	@Size(min = 2, max = 60,message = "Lo stato deve essere minimo di 2 caratteri massimo di 60")
	private String stato;

}
