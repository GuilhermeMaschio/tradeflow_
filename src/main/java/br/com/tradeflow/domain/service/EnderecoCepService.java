package br.com.tradeflow.domain.service;

import br.com.tradeflow.domain.entity.ConsultaExterna;
import br.com.tradeflow.domain.entity.EnderecoCep;
import br.com.tradeflow.domain.repository.ConsultaExternaRepository;
import br.com.tradeflow.domain.repository.EnderecoCepRepository;
import br.com.tradeflow.domain.service.ce.CepService;
import br.com.tradeflow.mapper.EnderecoCepMapper;
import br.com.tradeflow.util.DummyUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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
public class EnderecoCepService {

    @Autowired
    private EnderecoCepRepository enderecoCepRepository;

    @Autowired
    private CepService cepService;

    @Autowired
    private EnderecoCepMapper enderecoCepMapper;

    @Transactional(rollbackFor = Exception.class)
    public EnderecoCep getByCep(String cep) {

        EnderecoCep enderecoCep = enderecoCepRepository.getByCep(cep);

        if(enderecoCep == null) {
            Map<String, String> map = cepService.consultaCep(cep);

            enderecoCep = enderecoCepMapper.map(map);
            enderecoCepRepository.saveAndFlush(enderecoCep);
        }

        return enderecoCep;
    }
}
