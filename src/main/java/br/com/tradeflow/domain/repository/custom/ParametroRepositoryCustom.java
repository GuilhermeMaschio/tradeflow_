package br.com.tradeflow.domain.repository.custom;

import br.com.tradeflow.dto.filter.ParametroFilter;
import br.com.tradeflow.domain.entity.Parametro;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParametroRepositoryCustom {

	List<Parametro> findByFilter(Pageable pageable, ParametroFilter filtro);

	int countByFilter(ParametroFilter filtro);
}
