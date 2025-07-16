package br.com.tradeflow.domain.service;

import br.com.tradeflow.domain.entity.LogAlteracao;
import br.com.tradeflow.domain.entity.Parametro;
import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.domain.repository.LogAlteracaoRepository;
import br.com.tradeflow.util.DummyUtils;
import br.com.tradeflow.util.ddd.AbstractEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LogAlteracaoService {

    @Autowired
    private LogAlteracaoRepository logAlteracaoRepository;

	@Transactional(rollbackFor = Exception.class)
	public void registrarAlteracao(AbstractEntity entity, Usuario usuario, LogAlteracao.TipoAlteracao tipoAlteracao) {

		Long registroId = entity.getId();
		String tipoRegistro = entity.getClass().getSimpleName();
		//TODO: tem que refinar aqui pra não registrar no log os campos lazy e outras coisas do hibernate
		String dados = DummyUtils.objectToJson(entity);

		LogAlteracao log = new LogAlteracao();
		log.setData(new Date());
		log.setTipoAlteracao(tipoAlteracao);
		log.setTipoRegistro(tipoRegistro);
		log.setUsuario(usuario);
		log.setRegistroId(registroId);
		log.setDados(dados);

		logAlteracaoRepository.saveAndFlush(log);
	}
}
