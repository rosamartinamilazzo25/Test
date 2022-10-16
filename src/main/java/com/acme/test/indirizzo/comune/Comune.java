package com.acme.test.indirizzo.comune;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.acme.test.indirizzo.IndirizzoImp;
import com.acme.test.indirizzo.provincia.Provincia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comune {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(nullable = false, length = 60)
	private String nome;
	
	@ToString.Exclude
	@JsonIgnoreProperties({"comune"})
	@ManyToOne
	private Provincia provincia;
	
	@ToString.Exclude
	@JsonIgnoreProperties({"comune"})
	@OneToMany(mappedBy = "comune")
	private List<IndirizzoImp> indirizzi;

	public Comune(Long id, String nome, Provincia provincia) {
		super();
		this.id = id;
		this.nome = nome;
		this.provincia = provincia;
	}
	
}
