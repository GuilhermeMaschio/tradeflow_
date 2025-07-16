package br.com.tradeflow.controller;

import br.com.tradeflow.controller.api.UsuarioApi;
import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.domain.service.UsuarioService;
import br.com.tradeflow.domain.service.UsuarioService;
import br.com.tradeflow.dto.UsuarioCadastroDTO;
import br.com.tradeflow.dto.UsuarioDTO;
import br.com.tradeflow.dto.filter.UsuarioFilter;
import br.com.tradeflow.mapper.UsuarioMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
public class UsuarioContoller implements UsuarioApi {

	@Autowired
	private UsuarioMapper usuarioMapper;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public ResponseEntity<Page<UsuarioDTO>> listar(Pageable pageable, UsuarioFilter filter) {
		Page<Usuario> page = usuarioService.findByFilterPageable(pageable, filter);
		Page<UsuarioDTO> page2 = (Page<UsuarioDTO>) page.map(usuarioMapper::map);
		return ResponseEntity.ok(page2);
	}

	@Override
	public ResponseEntity<UsuarioDTO> cadastrar(UsuarioCadastroDTO usuarioDTO, JwtAuthenticationToken principal) {

		Usuario usuarioLogado = usuarioService.getByPrincipal(principal);

		Usuario usuario = usuarioMapper.map(usuarioDTO);
		usuarioService.cadastrar(usuario, usuarioLogado);

		Long usuarioId = usuario.getId();
		Usuario usuario2 = usuarioService.getById(usuarioId);
		UsuarioDTO dto = usuarioMapper.map(usuario2);
		return ResponseEntity.status(201).body(dto);
	}

	@Override
	public ResponseEntity<UsuarioDTO> atualizar(Long id, UsuarioCadastroDTO usuarioDTO, JwtAuthenticationToken principal) {

		Usuario usuarioLogado = usuarioService.getByPrincipal(principal);

		Usuario usuario = usuarioService.getById(id);
		if (usuario == null) {
			return ResponseEntity.status(404).build();
		}

		usuarioMapper.map(usuarioDTO, usuario);

		Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario, usuarioLogado);
		UsuarioDTO dto = usuarioMapper.map(usuarioAtualizado);
		return ResponseEntity.ok(dto);
	}

	@Override
	public ResponseEntity<Void> excluir(Long id, JwtAuthenticationToken principal) {

		Usuario usuarioLogado = usuarioService.getByPrincipal(principal);

		boolean deleted = usuarioService.excluir(id, usuarioLogado);

		if (!deleted) {
			return ResponseEntity.status(404).build();
		}
		return ResponseEntity.status(204).build();
	}
}
