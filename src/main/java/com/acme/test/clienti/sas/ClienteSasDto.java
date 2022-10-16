package com.acme.test.clienti.sas;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteSasDto {
	@NotBlank(message = "Ragione sociale obbligatoria")
	@Size(min = 2, max = 60,message = "La ragione sociale deve essere minimo di 2 caratteri massimo di 60")
	private String ragioneSociale;
	@NotBlank(message = "Partita iva obbligatoria")
	@Size(min = 2, max = 20,message = "La partita iva deve essere minimo di 2 caratteri massimo di 20")
	private String partitaIva;
	@NotBlank(message = "Email obbligatoria")
	@Size(min = 2, max = 60,message = "L'email deve essere minimo di 2 caratteri massimo di 60")
	private String email;
	@NotBlank(message = "Data inserimento obbligatoria")
	private String dataInserimento;
	@NotBlank(message = "Data ultimo contatto obbligatoria")
	private String dataUltimoContatto;
	@NotNull(message = "Fatturato annuale obbligatorio")
	private double fatturatoAnnuale;
	@NotBlank(message = "Pec obbligatoria")
	@Size(min = 2, max = 20,message = "La pec deve essere minimo di 2 caratteri massimo di 20")
	private String pec;
	@NotBlank(message = "Telefono obbligatorio")
	@Size(min = 2, max = 20,message = "Il numero di telefono deve essere minimo di 2 caratteri massimo di 20")
	private String numeroTelefono;
	@NotBlank(message = "Email contatto obbligatoria")
	@Size(min = 2, max = 60,message = "L'email del contatto deve essere minimo di 2 caratteri massimo di 60")
	private String emailContatto;
	@NotBlank(message = "Nome contatto obbligatorio")
	@Size(min = 2, max = 30,message = "Il nome del contatto deve essere minimo di 2 caratteri massimo di 30")
	private String nomeContatto;
	@NotBlank(message = "Cognome contatto obbligatorio")
	@Size(min = 2, max = 30,message = "Il cognome del contatto deve essere minimo di 2 caratteri massimo di 30")
	private String cognomeContatto;
	@NotBlank(message = "Telefomo contatto obbligatorio")
	@Size(min = 2, max = 20,message = "Il telefono del contatto deve essere minimo di 2 caratteri massimo di 20")
	private String telefonoContatto;

}
