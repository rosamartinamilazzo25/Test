package com.acme.test.fattura;

import com.acme.test.clienti.ClienteImp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public interface Fattura {

	int getAnno();

	String getData();

	double getImporto();

	String getNumeroFattura();

	String getStato();

	void setAnno(int anno);

	void setData(String data);

	void setImporto(double importo);

	void setNumeroFattura(String numeroFattura);

	void setStato(String stato);

}