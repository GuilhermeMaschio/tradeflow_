package br.com.tradeflow.dto;

import br.com.tradeflow.domain.entity.Usuario;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioCadastroDTO {

	private String nome;
	private Usuario.Status status;
	private String login;
	private String email;
	private List<String> roles;
}
