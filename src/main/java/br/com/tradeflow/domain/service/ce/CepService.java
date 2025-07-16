package br.com.tradeflow.domain.service.ce;

import br.com.tradeflow.domain.entity.ConsultaExterna;
import br.com.tradeflow.domain.service.ConsultaExternaService;
import br.com.tradeflow.domain.service.ParametroService;
import br.com.tradeflow.util.DummyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CepService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ParametroService parametroService;

    @Autowired
    private ConsultaExternaService consultaExternaService;

    @Autowired
    private Environment environment;

    //@Retryable(value = { HttpClientErrorException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public Map<String, String> consultaCep(String cep) {

        String[] activeProfiles = environment.getActiveProfiles();
        String ambiente = activeProfiles != null && activeProfiles.length > 0 ? activeProfiles[0] : null;

        String urlConsultaCep = parametroService.getValorByChave(ambiente, ParametroService.URL_CONSULTA_CEP);

        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("cep", cep);

        String url = urlConsultaCep + cep;
        String result = consultaExternaService.executar(ConsultaExterna.Tipo.CONSULTA_CEP, urlConsultaCep, parametros,
            () -> restTemplate.getForObject(url, String.class)
        );

        if(StringUtils.isBlank(result)) {
            return null;
        }

        Map<String, Object> resultMap = (Map<String, Object>) DummyUtils.jsonStringToMap(result);
        Map<String, String> resultMap2 = new LinkedHashMap<>();

        resultMap.forEach((key, value) -> {
            if(!(value instanceof String)) {
                value = DummyUtils.objectToJson(value);
            }
            resultMap2.put(key, (String) value);
        });

        return resultMap2;
    }
}
