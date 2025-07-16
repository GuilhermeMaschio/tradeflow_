package br.com.tradeflow.controller;

import br.com.tradeflow.controller.api.ParametroApi;
import br.com.tradeflow.domain.entity.Parametro;
import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.domain.service.ParametroService;
import br.com.tradeflow.domain.service.UsuarioService;
import br.com.tradeflow.dto.filter.ParametroFilter;
import br.com.tradeflow.dto.ParametroDTO;
import br.com.tradeflow.mapper.ParametroMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
public class ParametroContoller implements ParametroApi {

	@Autowired
	private ParametroMapper parametroMapper;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public ResponseEntity<Page<ParametroDTO>> listar(Pageable pageable, ParametroFilter filter) {
		Page<Parametro> page = parametroService.findByFilterPageable(pageable, filter);
		Page<ParametroDTO> page2 = (Page<ParametroDTO>) page.map(parametroMapper::map);
		return ResponseEntity.ok(page2);
	}

	@Override
	public ResponseEntity<ParametroDTO> cadastrar(ParametroDTO parametroDTO, JwtAuthenticationToken principal) {

		Usuario usuario = usuarioService.getByPrincipal(principal);

		Parametro parametro = parametroMapper.map(parametroDTO);
		parametroService.cadastrar(parametro, usuario);
		ParametroDTO dto = parametroMapper.map(parametro);
		return ResponseEntity.status(201).body(dto);
	}

	@Override
	public ResponseEntity<ParametroDTO> atualizar(Long id, ParametroDTO parametroDTO, JwtAuthenticationToken principal) {

		Usuario usuario = usuarioService.getByPrincipal(principal);

		Parametro parametro = parametroMapper.map(parametroDTO);
		Parametro parametroAtualizado = parametroService.atualizar(id, parametro, usuario);
		if (parametroAtualizado == null) {
			return ResponseEntity.status(404).build();
		}
		ParametroDTO dto = parametroMapper.map(parametroAtualizado);
		return ResponseEntity.ok(dto);
	}

	@Override
	public ResponseEntity<Void> excluir(Long id, JwtAuthenticationToken principal) {

		Usuario usuario = usuarioService.getByPrincipal(principal);

		boolean deleted = parametroService.excluir(id, usuario);
		if (!deleted) {
			return ResponseEntity.status(404).build();
		}
		return ResponseEntity.status(204).build();
	}
}
