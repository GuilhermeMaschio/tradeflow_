package br.com.tradeflow.mapper;

import br.com.tradeflow.domain.entity.Parametro;
import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.dto.ParametroDTO;
import br.com.tradeflow.dto.UsuarioCadastroDTO;
import br.com.tradeflow.dto.UsuarioDTO;
import br.com.tradeflow.util.DummyUtils;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper( componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO map(Usuario usuario);

    Usuario map(UsuarioDTO usuarioDTO);

    Usuario map(UsuarioCadastroDTO usuarioDTO);

    void map(UsuarioCadastroDTO usuarioDTO, @MappingTarget Usuario usuario);

    default String mapRoles(List<String> roles) {
        return DummyUtils.objectToJson(roles);
    }

    default List<String> mapRoles(String roles) {
        return DummyUtils.jsonToObject(roles, List.class);
    }
}
