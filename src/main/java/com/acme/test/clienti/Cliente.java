package com.acme.test.clienti;

import com.acme.test.indirizzo.IndirizzoImp;

public interface Cliente {

	String getCognomeContatto();

	String getDataInserimento();

	String getDataUltimoContatto();

	String getEmail();

	String getEmailContatto();

	double getFatturatoAnnuale();

	Long getId();

	IndirizzoImp getIndirizzoSedeLegale();

	IndirizzoImp getIndirizzoSedeOperativa();

	String getNomeContatto();

	String getNumeroTelefono();

	String getPartitaIva();

	String getPec();

	String getRagioneSociale();

	String getTelefonoContatto();

	void setCognomeContatto(String cognomeContatto);

	void setDataInserimento(String dataInserimento);

	void setDataUltimoContatto(String DataUltimoContatto);

	void setEmail(String email);

	void setEmailContatto(String emailContatto);

	void setFatturatoAnnuale(double fatturatoAnnuale);

	void setId(Long id);

	void setIndirizzoSedeLegale(IndirizzoImp indirizzoSedeLegale);

	void setIndirizzoSedeOperativa(IndirizzoImp indirizzoSedeOperativa);

	void setNomeContatto(String nomeContatto);

	void setNumeroTelefono(String numeroTelefono);

	void setPartitaIva(String partitaIva);

	void setPec(String pec);

	void setRagioneSociale(String ragioneSociale);

	void setTelefonoContatto(String telefonoContatto);

}