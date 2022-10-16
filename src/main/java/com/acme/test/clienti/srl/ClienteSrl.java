package com.acme.test.clienti.srl;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;

import com.acme.test.clienti.ClienteImp;
import com.acme.test.fattura.FatturaImp;
import com.acme.test.indirizzo.IndirizzoImp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ClienteSrl extends ClienteImp {


	public ClienteSrl(Long id, String ragioneSociale, String partitaIva, String email, String dataInserimento,
			String dataUltimoContatto, double fatturatoAnnuale, String pec, String numeroTelefono, String emailContatto,
			String nomeContatto, String cognomeContatto, String telefonoContatto, IndirizzoImp indirizzoSedeLegale,
			IndirizzoImp indirizzoSedeOperativa, List<FatturaImp> fatture) {
		super(id, ragioneSociale, partitaIva, email, dataInserimento, dataUltimoContatto, fatturatoAnnuale, pec, numeroTelefono,
				emailContatto, nomeContatto, cognomeContatto, telefonoContatto, indirizzoSedeLegale, indirizzoSedeOperativa,
				fatture);
	}
	

}
