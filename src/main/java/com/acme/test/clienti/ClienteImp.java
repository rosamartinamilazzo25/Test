package com.acme.test.clienti;

import java.sql.Date;
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

import org.springframework.format.annotation.DateTimeFormat;

import com.acme.test.fattura.FatturaImp;
import com.acme.test.indirizzo.IndirizzoImp;
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
@Inheritance(strategy =InheritanceType.TABLE_PER_CLASS)
public class ClienteImp implements Cliente{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, length = 60)
	private String ragioneSociale;
	@Column(nullable = false, length = 20)
	private String partitaIva;
	@Column(nullable = false, length = 60)
	private String email;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Column(nullable = false)
	private String dataInserimento;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Column(nullable = false)
	private String dataUltimoContatto;
	@Column(nullable = false)
	private double fatturatoAnnuale;
	@Column(nullable = false, length = 20)
	private String pec;
	@Column(nullable = false, length = 20)
	private String numeroTelefono;
	@Column(nullable = false, length = 60)
	private String emailContatto;
	@Column(nullable = false, length = 30)
	private String nomeContatto;
	@Column(nullable = false, length = 30)
	private String cognomeContatto;
	@Column(nullable = false, length = 20)
	private String telefonoContatto;


	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnoreProperties({"clienteSedeLegale"})
	@OneToOne
	private IndirizzoImp  indirizzoSedeLegale;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnoreProperties({"clienteSedeOperativa"})
	@OneToOne
	private IndirizzoImp  indirizzoSedeOperativa;

	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnoreProperties({"cliente"})
	@OneToMany(mappedBy = "cliente")
	private List<FatturaImp> fatture;


}
