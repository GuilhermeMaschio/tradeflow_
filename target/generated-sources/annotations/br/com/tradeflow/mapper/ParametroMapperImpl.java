package br.com.tradeflow.mapper;

import br.com.tradeflow.domain.entity.Parametro;
import br.com.tradeflow.dto.ParametroDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-01T14:30:12-0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class ParametroMapperImpl implements ParametroMapper {

    @Override
    public ParametroDTO map(Parametro parametro) {
        if ( parametro == null ) {
            return null;
        }

        ParametroDTO parametroDTO = new ParametroDTO();

        parametroDTO.setId( parametro.getId() );
        parametroDTO.setAmbiente( parametro.getAmbiente() );
        parametroDTO.setChave( parametro.getChave() );
        parametroDTO.setValor( parametro.getValor() );

        return parametroDTO;
    }

    @Override
    public Parametro map(ParametroDTO parametroDTO) {
        if ( parametroDTO == null ) {
            return null;
        }

        Parametro parametro = new Parametro();

        parametro.setId( parametroDTO.getId() );
        parametro.setAmbiente( parametroDTO.getAmbiente() );
        parametro.setChave( parametroDTO.getChave() );
        parametro.setValor( parametroDTO.getValor() );

        return parametro;
    }
}
