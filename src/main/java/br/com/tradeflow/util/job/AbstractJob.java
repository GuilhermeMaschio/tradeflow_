package br.com.tradeflow.util.job;

import br.com.tradeflow.domain.entity.LogAcesso;
import br.com.tradeflow.domain.service.LogAcessoService;
import br.com.tradeflow.util.DummyUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Log4j2
public class AbstractJob {

	@Autowired
	protected LogAcessoService logAcessoService;

	protected void handleException(LogAcesso logAcesso, Exception e) {

		String messageError = DummyUtils.getExceptionMessage(e);
		log.error(getClass().getSimpleName() + " erro: " + messageError, e);

		if(logAcesso != null) {
			String exception = logAcesso.getException();
			exception = exception != null ? exception + "\n\n" : "";
			String stackTrace = ExceptionUtils.getStackTrace(e);
			exception += stackTrace;
			logAcesso.setException(exception);
		}
	}

	protected void doFinally(LogAcesso logAcesso) {
		Date inicio = logAcesso.getInicio();
		Date fim = new Date();
		long tempo = fim.getTime() - inicio.getTime();
		if(logAcesso != null) {
			logAcesso.setFim(fim);
			logAcesso.setTempo(tempo);
			logAcessoService.saveOrUpdateNewSession(logAcesso);
		}
	}
}
