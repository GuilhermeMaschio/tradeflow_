package br.com.tradeflow.controller;

import br.com.tradeflow.controller.api.UtilApi;
import br.com.tradeflow.domain.entity.EnderecoCep;
import br.com.tradeflow.domain.service.EnderecoCepService;
import br.com.tradeflow.dto.EnderecoCepDTO;
import br.com.tradeflow.mapper.EnderecoCepMapper;
import br.com.tradeflow.util.DummyUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Controller
public class UtilContoller implements UtilApi {

	@Autowired
	private EnderecoCepService enderecoCepService;

	@Autowired
	private EnderecoCepMapper enderecoCepMapper;

	@Override
	public ResponseEntity<EnderecoCepDTO> consultaCep(String cep) {

		EnderecoCep enderecoCep = enderecoCepService.getByCep(cep);

		EnderecoCepDTO enderecoCepDTO = enderecoCepMapper.map(enderecoCep);

		return ResponseEntity.ok(enderecoCepDTO);
	}
}
