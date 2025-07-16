package br.com.tradeflow.domain.service;

import br.com.tradeflow.domain.entity.ConsultaExterna;
import br.com.tradeflow.domain.repository.ConsultaExternaRepository;
import br.com.tradeflow.util.DummyUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class ConsultaExternaService {

    @Autowired
    private ConsultaExternaRepository consultaExternaRepository;

	@Lazy
	@Autowired
	private ConsultaExternaService consultaExternaService;

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public ConsultaExterna criaInicio(ConsultaExterna.Tipo tipo, String url, Map<String, Object> parametros) {

		ConsultaExterna ce = new ConsultaExterna();
		ce.setThread(Thread.currentThread().getName());
		ce.setData(new Date());
		ce.setStatus(ConsultaExterna.Status.INICIADA);
		ce.setTipo(tipo);
		ce.setUrl(url);
		ce.setParametros(DummyUtils.objectToJson(parametros));

		consultaExternaRepository.saveAndFlush(ce);

		return ce;
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void finalizaSucesso(ConsultaExterna ce, Object result, StopWatch sw) {

		ce.setStatus(ConsultaExterna.Status.SUCESSO);

		if(result instanceof String) {
			ce.setResultado((String) result);
		} else {
			String resultJson = DummyUtils.objectToJson(result);
			ce.setResultado(resultJson);
		}

		ce.setTempoExecucao(sw.getTime(TimeUnit.MILLISECONDS));

		consultaExternaRepository.saveAndFlush(ce);
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void finalizarErro(ConsultaExterna ce, Exception e, StopWatch sw) {

		log.error("erro na consulta externa " + ce.getTipo() + ", thread: " + ce.getThread() + ", url: " + ce.getUrl() + ", parametros: " + ce.getParametros(), e);

		ce.setStatus(ConsultaExterna.Status.ERRO);
		ce.setTempoExecucao(sw.getTime(TimeUnit.MILLISECONDS));

		String exceptionMessage = DummyUtils.getExceptionMessage(e);
		ce.setMensagem(exceptionMessage);

		String stackTrace = ExceptionUtils.getStackTrace(e);
		ce.setStackTrace(stackTrace);

		consultaExternaRepository.saveAndFlush(ce);
	}

	@Transactional(rollbackFor = Exception.class)
	public <T> T executar(ConsultaExterna.Tipo tipo, String url, Map<String, Object> parametros, Executar<T> executar) {

		ConsultaExterna ce = consultaExternaService.criaInicio(tipo, url, parametros);

		T result = null;
		StopWatch sw = StopWatch.createStarted();
		try {
			result = executar.executar();

			consultaExternaService.finalizaSucesso(ce, result, sw);
		}
		catch (Exception e) {
			consultaExternaService.finalizarErro(ce, e, sw);
			throw e;
		}

		return result;
	}

	public interface Executar<T> {

		T executar();
	}
}
