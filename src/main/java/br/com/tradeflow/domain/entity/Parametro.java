package br.com.tradeflow.domain.entity;

import br.com.tradeflow.util.ddd.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "parametro")
public class Parametro extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ambiente", nullable = false)
	private String ambiente;

	@Column(name = "chave", nullable = false)
	private String chave;

	@Column(name = "valor", nullable = false)
	private String valor;
}
