package br.com.tradeflow.domain.service;

import br.com.tradeflow.domain.entity.LogAlteracao;
import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.domain.repository.UsuarioRepository;
import br.com.tradeflow.dto.filter.UsuarioFilter;
import br.com.tradeflow.util.DummyUtils;
import lombok.extern.log4j.Log4j2;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

	@Autowired
	private LogAlteracaoService logAlteracaoService;

	@Autowired
	private UsuarioKeycloakService usuarioKeycloakService;

	public Usuario getById(Long usuarioId) {
		return usuarioRepository.getReferenceById(usuarioId);
	}

	@Transactional(rollbackFor = Exception.class)
	public Usuario getByPrincipal(JwtAuthenticationToken principal) {

		Jwt token = principal.getToken();
		String login = token.getClaim("preferred_username");

		Usuario usuario = usuarioRepository.getByLogin(login);
		if(usuario == null) {

			String keyCloakId = principal.getName();
			String nome = token.getClaim("name");
			String email = token.getClaim("email");
			List<String> roles = token.getClaim("groups");
			String rolesJson = DummyUtils.objectToJson(roles);

			usuario = new Usuario();
			usuario.setStatus(Usuario.Status.ATIVO);
			usuario.setKeyCloakId(keyCloakId);
			usuario.setLogin(login);
			usuario.setNome(nome);
			usuario.setEmail(email);
			usuario.setRoles(rolesJson);

			usuarioRepository.saveAndFlush(usuario);

			logAlteracaoService.registrarAlteracao(usuario, usuario, LogAlteracao.TipoAlteracao.CRIACAO);
		}

		return usuario;
	}

	public Page<Usuario> findByFilterPageable(Pageable pageable, UsuarioFilter filtro) {

		List<Usuario> list = usuarioRepository.findByFilter(pageable, filtro);

		int count = usuarioRepository.countByFilter(filtro);

		return new PageImpl<>(list, pageable, count);
	}

	@Transactional(rollbackFor = Exception.class)
	public void cadastrar(Usuario usuario, Usuario usuarioLogado) {

		UserRepresentation kcUser = usuarioKeycloakService.cadastrar(usuario);
		String kcUserId = kcUser.getId();
		usuario.setKeyCloakId(kcUserId);
		List<String> rolesList = kcUser.getRealmRoles();
		usuario.setRoles(DummyUtils.objectToJson(rolesList));

		Usuario usuario2 = usuarioRepository.getByKeyCloakId(kcUserId);
		if(usuario2 != null) {
			usuario.setId(usuario2.getId());
		}

		usuarioRepository.saveAndFlush(usuario);

		logAlteracaoService.registrarAlteracao(usuario, usuarioLogado, LogAlteracao.TipoAlteracao.CRIACAO);
	}

	@Transactional(rollbackFor = Exception.class)
	public Usuario atualizar(Long id, Usuario usuario, Usuario usuarioLogado) {

		if (!usuarioRepository.existsById(id)) {
			return null;
		}
		usuario.setId(id);

		usuarioKeycloakService.atualizar(usuario);

		usuarioRepository.saveAndFlush(usuario);

		logAlteracaoService.registrarAlteracao(usuario, usuarioLogado, LogAlteracao.TipoAlteracao.ATUALIZACAO);

		return usuario;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean excluir(Long id, Usuario usuarioLocado) {

		Usuario usuario = getById(id);
		if (usuario == null) {
			return false;
		}

		usuarioKeycloakService.excluir(usuario);

		usuarioRepository.deleteById(id);

		logAlteracaoService.registrarAlteracao(usuario, usuarioLocado, LogAlteracao.TipoAlteracao.EXCLUSAO);

		return true;
	}
}
