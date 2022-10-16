package com.acme.test.fattura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.acme.test.clienti.ClienteImp;
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
public class FatturaImp implements Fattura {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false)
	private int anno;
	@Column(nullable = false, length = 10)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private String data;
	@Column(nullable = false)
	private double importo;
	@Column(nullable = false, length = 60)
	private String numeroFattura;
	@Column(nullable = false, length = 60)
	private String stato;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnoreProperties({"fatture"})
	@ManyToOne
	private ClienteImp cliente;

	public FatturaImp(Long id, int anno, String data, double importo, String numeroFattura, String stato) {
		this.id = id;
		this.anno = anno;
		this.data = data;
		this.importo = importo;
		this.numeroFattura = numeroFattura;
		this.stato = stato;
	}

	
	
	
}
