package br.com.tradeflow.domain.entity;

import br.com.tradeflow.util.ddd.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Entity
@Table(name = "consulta_externa")
public class ConsultaExterna extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	private Date data;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false)
	private Tipo tipo;

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "thread", nullable = false)
	private String thread;

	@Column(name = "parametros", nullable = false)
	private String parametros;

	@Column(name = "resultado")
	private String resultado;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;

	@Column(name = "stack_trace")
	private String stackTrace;

	@Column(name = "mensagem")
	private String mensagem;

	@Column(name = "tempo_execucao")
	private Long tempoExecucao;

	public enum Tipo {
		CONSULTA_CEP()
	}

	public enum Status {
		INICIADA,
		SUCESSO,
		ERRO,
	}
}
