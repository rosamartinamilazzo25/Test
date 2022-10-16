package com.acme.test.indirizzo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.acme.test.clienti.ClienteImp;
import com.acme.test.indirizzo.comune.Comune;
//import com.acme.test.clienti.ClienteImp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class IndirizzoImp implements Indirizzo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, length = 60)
	private String via;
	@Column(nullable = false, length = 60)
	private String numeroCivico;
	@Column(nullable = false, length = 60)
	private String localita;
	@Column(nullable = false, length = 20)
	private String cap;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnoreProperties({"indirizzi"})
	@ManyToOne
	private Comune comune;
	

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnoreProperties({"indirizzoSedeLegale"})
	@OneToOne(mappedBy = "indirizzoSedeLegale")
	private ClienteImp clienteSedeLegale;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnoreProperties({"indirizzoSedeOperativa"})
	@OneToOne(mappedBy = "indirizzoSedeOperativa")
	private ClienteImp clienteSedeOperativa;

	public IndirizzoImp(Long id, String via, String numeroCivico, String localita, String cap, Comune comune) {
		super();
		this.id = id;
		this.via = via;
		this.numeroCivico = numeroCivico;
		this.localita = localita;
		this.cap = cap;
		this.comune = comune;
	}

	
	
}
