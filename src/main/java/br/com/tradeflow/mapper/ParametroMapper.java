package br.com.tradeflow.mapper;

import br.com.tradeflow.domain.entity.Parametro;
import br.com.tradeflow.dto.ParametroDTO;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring")
public interface ParametroMapper {

    ParametroDTO map(Parametro parametro);

    Parametro map(ParametroDTO parametroDTO);
}
