package com.acme.test.indirizzo.provincia;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.acme.test.indirizzo.comune.Comune;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity 
public class Provincia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(nullable = false, length = 60)
	private String nome;
	@Column(nullable = false, length = 2)
	private String sigla;
	
	@ToString.Exclude
	@JsonIgnoreProperties({"provincia"})
	@OneToMany(mappedBy = "provincia")
	private List<Comune> comuni;

	public Provincia(Long id, String nome, String sigla) {
		super();
		this.id = id;
		this.nome = nome;
		this.sigla = sigla;
	}


}
