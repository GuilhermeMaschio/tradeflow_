package br.com.tradeflow.domain.service;

import br.com.tradeflow.domain.entity.LogAlteracao;
import br.com.tradeflow.domain.entity.Parametro;
import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.domain.repository.ParametroRepository;
import br.com.tradeflow.dto.filter.ParametroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParametroService {

	public static final String URL_CONSULTA_CEP = "URL_CONSULTA_CEP";

	@Autowired
    private ParametroRepository parametroRepository;

	@Autowired
	private LogAlteracaoService logAlteracaoService;

	public Page<Parametro> findByFilterPageable(Pageable pageable, ParametroFilter filtro) {

		List<Parametro> list = parametroRepository.findByFilter(pageable, filtro);

		int count = parametroRepository.countByFilter(filtro);

		return new PageImpl<>(list, pageable, count);
	}

	@Transactional(rollbackFor = Exception.class)
	public void cadastrar(Parametro parametro, Usuario usuario) {

		parametroRepository.saveAndFlush(parametro);

		logAlteracaoService.registrarAlteracao(parametro, usuario, LogAlteracao.TipoAlteracao.CRIACAO);
	}

	@Transactional(rollbackFor = Exception.class)
	public Parametro atualizar(Long id, Parametro parametro, Usuario usuario) {

		if (!parametroRepository.existsById(id)) {
			return null;
		}
		parametro.setId(id);
		parametroRepository.saveAndFlush(parametro);

		logAlteracaoService.registrarAlteracao(parametro, usuario, LogAlteracao.TipoAlteracao.ATUALIZACAO);

		return parametro;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean excluir(Long id, Usuario usuario) {

		if (!parametroRepository.existsById(id)) {
			return false;
		}
		parametroRepository.deleteById(id);

		logAlteracaoService.registrarAlteracao(usuario, usuario, LogAlteracao.TipoAlteracao.EXCLUSAO);

		return true;
	}

	public String getValorByChave(String ambiente, String chave) {
		return parametroRepository.getValorByChave(ambiente, chave);
	}
}
