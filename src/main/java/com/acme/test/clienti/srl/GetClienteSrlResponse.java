package com.acme.test.clienti.srl;

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
public class GetClienteSrlResponse {
	private long id;
	private String ragioneSociale;
	private String partitaIva;
	private String email;
	private String dataInserimento;
	private String dataUltimoContatto;
	private double fatturatoAnnuale;
	private String pec;
	private String numeroTelefono;
	private String emailContatto;
	private String nomeContatto;
	private String cognomeContatto;
	private String telefonoContatto;

}
