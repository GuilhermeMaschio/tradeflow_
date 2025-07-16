package br.com.tradeflow.domain.job;

import br.com.tradeflow.domain.entity.LogAcesso;
import br.com.tradeflow.util.job.AbstractJob;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class LimpaLogAcessoJob extends AbstractJob {

	@Scheduled(cron="0 15 01 * * ?")//1:15h
	public void executar() {

		LogAcesso logA = logAcessoService.criaLogJob(getClass());
		try {
			log.info("iniciando job...");

			logAcessoService.limpaLogAcesso();
		}
		catch (Exception e) {
			handleException(logA, e);
		}
		finally {
			doFinally(logA);
		}
	}
}
