package br.com.tradeflow.domain.job;

import br.com.tradeflow.domain.entity.LogAcesso;
import br.com.tradeflow.domain.repository.LogAcessoRepository;
import br.com.tradeflow.util.DummyUtils;
import br.com.tradeflow.util.job.AbstractJob;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Component
public class ExemploAssincronoJob extends AbstractJob {

	@Lazy
	@Autowired
	private ExemploAssincronoJob exemploAssincronoJob;

	/** como isso é só um exemplo, está usando o reposotory aqui, mas isso nunca deve acontecer num job */
	@Autowired
	private LogAcessoRepository logAcessoRepository;

	//@Scheduled(cron="4/10 * * * * ?")//a cada 10 segundos
	public void performTask() {

		LogAcesso logA = logAcessoService.criaLogJob(getClass());
		try{
			List<LogAcesso> list = logAcessoRepository.findAll();

			Map<CompletableFuture<String>, Long> futures = new LinkedHashMap();

			list.stream().forEach(la -> {
				CompletableFuture<String> future = exemploAssincronoJob.processarAsync(la);
				Long laId = la.getId();
				futures.put(future, laId);
			});

			DummyUtils.ResultadoAsync<String, Long> resultadoAsync = DummyUtils.verificarFinalExecucao(futures, logA);

			String sssss = resultadoAsync.getRetornosMap().get(list.get(3));
		}
		catch (Exception e) {
			handleException(logA, e);
		}
		finally {
			doFinally(logA);
		}
	}

	/** esse metodo deve ficar aqui mesmo, esse taskExecutor deve ser o nome do pool configurado em AsyncConfig */
	@Async("exemploPool")
	public CompletableFuture<String> processarAsync(LogAcesso la) {

		try {
			Long id = la.getId();
			log.info("processando " + id);

			int sleep = (int) (Math.random() * 1000);
			Thread.sleep(sleep);

			exemploAssincronoJob.processar(la);

			log.info("FIM " + id + " sleep " + sleep);

			String result = "exemplo de retorno";
			return CompletableFuture.completedFuture(result);
		}
		catch (Exception e) {
			log.error("", e);
			return CompletableFuture.failedFuture(e);
		}
	}

	/** como isso é só um exemplo, esse método ficou aqui, mas ele deve ficar no service correspondente */
	@Transactional(rollbackFor=Exception.class)
	public void processar(LogAcesso la) {

		Long id = la.getId();

		la.setMetadados("processou");
		logAcessoRepository.saveAndFlush(la);

		if(id % 2 == 0) {
			throw new RuntimeException("ieh ieh " + id);
		}
	}
}
