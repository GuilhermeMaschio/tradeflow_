package br.com.tradeflow.domain.entity;

import br.com.tradeflow.util.ddd.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "endereco_cep")
public class EnderecoCep extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cep", nullable = false)
	private String cep;

	@Column(name = "uf")
	private String uf;

	@Column(name = "cidade")
	private String cidade;

	@Column(name = "bairro")
	private String bairro;

	@Column(name = "logradouro")
	private String logradouro;

	@Column(name = "servico")
	private String servico;

	@Column(name = "localizacao")
	private String localizacao;
}
