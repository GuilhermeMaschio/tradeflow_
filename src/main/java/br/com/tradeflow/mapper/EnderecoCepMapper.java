package br.com.tradeflow.mapper;

import br.com.tradeflow.domain.entity.EnderecoCep;
import br.com.tradeflow.dto.EnderecoCepDTO;
import jakarta.persistence.Column;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper( componentModel = "spring")
public interface EnderecoCepMapper {

    EnderecoCepDTO map(EnderecoCep source);

    @Mapping(target = "cep", source = "cep")
    @Mapping(target = "uf", source = "state")
    @Mapping(target = "cidade", source = "city")
    @Mapping(target = "bairro", source = "neighborhood")
    @Mapping(target = "logradouro", source = "street")
    @Mapping(target = "servico", source = "service")
    @Mapping(target = "localizacao", source = "location")
    EnderecoCep map(Map<String, String> map);
}
