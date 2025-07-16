package br.com.tradeflow.domain.entity;

import br.com.tradeflow.util.DummyUtils;
import br.com.tradeflow.util.ddd.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "usuario")
public class Usuario extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "key_cloak_id", length = 100, nullable = false)
	private String keyCloakId;

	@Column(name = "nome", length = 200, nullable = false)
	private String nome;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 30, nullable = false)
	private Status status;

	@Column(name = "login", length = 150, nullable = false)
	private String login;

	@Column(name = "email", length = 300, nullable = false)
	private String email;

	@Column(name = "roles", length = 600)
	private String roles;

	public enum Status {
		ATIVO, EXPIRADO, BLOQUEADO;
	}

	public enum Role {
		ADMIN,
	}
}
