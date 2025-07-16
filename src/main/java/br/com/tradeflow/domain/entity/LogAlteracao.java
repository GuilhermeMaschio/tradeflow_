package br.com.tradeflow.domain.entity;

import br.com.tradeflow.util.ddd.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "log_alteracao")
public class LogAlteracao extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tipo_registro", nullable = false)
	private String tipoRegistro;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_alteracao", nullable = false)
	private TipoAlteracao tipoAlteracao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	private Date data;

	@Column(name = "dados", nullable = false)
	private String dados;

	@Column(name = "registro_id", nullable = false)
	private Long registroId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	public enum TipoAlteracao {

		EXCLUSAO,
		CRIACAO,
		ATUALIZACAO;

		public static TipoAlteracao getCriacaoOuAtualizacao(AbstractEntity entity) {

			Long id = entity.getId();
			TipoAlteracao tipoAlteracao = id == null ? TipoAlteracao.CRIACAO : TipoAlteracao.ATUALIZACAO;
			return tipoAlteracao;
		}
	}
}
