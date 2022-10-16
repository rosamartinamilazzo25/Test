package com.acme.test.indirizzo;

import com.acme.test.indirizzo.comune.Comune;

public interface Indirizzo {

	Comune getComune();

	String getLocalita();

	String getNumeroCivico();

	String getVia();

	void setComune(Comune comune);

	void setLocalita(String localita);

	void setNumeroCivico(String numeroCivico);

	void setVia(String via);

}