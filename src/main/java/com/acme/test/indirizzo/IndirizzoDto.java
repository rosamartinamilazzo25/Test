package com.acme.test.indirizzo;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.acme.test.indirizzo.comune.Comune;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndirizzoDto {
	@NotBlank(message = "Via obbligatoria")
	@Size(min = 2, max = 60,message = "La via deve essere minimo di 2 caratteri massimo di 60")
	private String via;
	@NotBlank(message = "Numero civico obbligatorio")
	@Size(min = 1, max = 10,message = "Il numero civico deve essere minimo di 2 caratteri massimo di 60")
	private String numeroCivico;
	@NotBlank(message = "Località obbligatoria")
	@Size(min = 2, max = 60,message = "La località deve essere minimo di 2 caratteri massimo di 60")
	private String localita;
	@NotBlank(message = "Cap obbligatorio")
	@Size(min = 2, max = 20,message = "Il cap deve essere minimo di 2 caratteri massimo di 20")
	private String cap;
	@NotBlank(message = "Nome comune obbligatorio")
	@Size(min = 2, max = 60,message = "Il nome del comune deve essere minimo di 2 caratteri massimo di 60")
	private String nomeComune;
	@NotBlank(message = "Nome provincia obbligatorio")
	@Size(min = 2, max = 60,message = "Il nome della provincia deve essere minimo di 2 caratteri massimo di 60")
	private String nomeProvincia;
	@NotBlank(message = "Sigla provincia obbligatorio")
	@Size(min = 2, max = 2,message = "Il nome della provincia deve essere minimo di 2")
	private String siglaProvincia; 
}
