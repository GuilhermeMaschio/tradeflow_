package br.com.tradeflow.dto;

import br.com.tradeflow.domain.entity.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {

	private Long id;
	private String keyCloakId;
	private String nome;
	private Usuario.Status status;
	private String login;
	private String email;
	private String roles;
}
