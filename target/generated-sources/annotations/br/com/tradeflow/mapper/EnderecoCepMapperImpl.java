package br.com.tradeflow.mapper;

import br.com.tradeflow.domain.entity.EnderecoCep;
import br.com.tradeflow.dto.EnderecoCepDTO;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-01T14:30:12-0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class EnderecoCepMapperImpl implements EnderecoCepMapper {

    @Override
    public EnderecoCepDTO map(EnderecoCep source) {
        if ( source == null ) {
            return null;
        }

        EnderecoCepDTO enderecoCepDTO = new EnderecoCepDTO();

        enderecoCepDTO.setId( source.getId() );
        enderecoCepDTO.setCep( source.getCep() );
        enderecoCepDTO.setUf( source.getUf() );
        enderecoCepDTO.setCidade( source.getCidade() );
        enderecoCepDTO.setBairro( source.getBairro() );
        enderecoCepDTO.setLogradouro( source.getLogradouro() );
        enderecoCepDTO.setServico( source.getServico() );
        enderecoCepDTO.setLocalizacao( source.getLocalizacao() );

        return enderecoCepDTO;
    }

    @Override
    public EnderecoCep map(Map<String, String> map) {
        if ( map == null ) {
            return null;
        }

        EnderecoCep enderecoCep = new EnderecoCep();

        if ( map.containsKey( "cep" ) ) {
            enderecoCep.setCep( map.get( "cep" ) );
        }
        if ( map.containsKey( "state" ) ) {
            enderecoCep.setUf( map.get( "state" ) );
        }
        if ( map.containsKey( "city" ) ) {
            enderecoCep.setCidade( map.get( "city" ) );
        }
        if ( map.containsKey( "neighborhood" ) ) {
            enderecoCep.setBairro( map.get( "neighborhood" ) );
        }
        if ( map.containsKey( "street" ) ) {
            enderecoCep.setLogradouro( map.get( "street" ) );
        }
        if ( map.containsKey( "service" ) ) {
            enderecoCep.setServico( map.get( "service" ) );
        }
        if ( map.containsKey( "location" ) ) {
            enderecoCep.setLocalizacao( map.get( "location" ) );
        }
        if ( map.containsKey( "id" ) ) {
            enderecoCep.setId( Long.parseLong( map.get( "id" ) ) );
        }

        return enderecoCep;
    }
}
