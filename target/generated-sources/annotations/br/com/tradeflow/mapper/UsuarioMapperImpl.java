package br.com.tradeflow.mapper;

import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.dto.UsuarioCadastroDTO;
import br.com.tradeflow.dto.UsuarioDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-01T14:30:12-0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioDTO map(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId( usuario.getId() );
        usuarioDTO.setKeyCloakId( usuario.getKeyCloakId() );
        usuarioDTO.setNome( usuario.getNome() );
        usuarioDTO.setStatus( usuario.getStatus() );
        usuarioDTO.setLogin( usuario.getLogin() );
        usuarioDTO.setEmail( usuario.getEmail() );
        usuarioDTO.setRoles( usuario.getRoles() );

        return usuarioDTO;
    }

    @Override
    public Usuario map(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( usuarioDTO.getId() );
        usuario.setKeyCloakId( usuarioDTO.getKeyCloakId() );
        usuario.setNome( usuarioDTO.getNome() );
        usuario.setStatus( usuarioDTO.getStatus() );
        usuario.setLogin( usuarioDTO.getLogin() );
        usuario.setEmail( usuarioDTO.getEmail() );
        usuario.setRoles( usuarioDTO.getRoles() );

        return usuario;
    }

    @Override
    public Usuario map(UsuarioCadastroDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setNome( usuarioDTO.getNome() );
        usuario.setStatus( usuarioDTO.getStatus() );
        usuario.setLogin( usuarioDTO.getLogin() );
        usuario.setEmail( usuarioDTO.getEmail() );
        usuario.setRoles( mapRoles( usuarioDTO.getRoles() ) );

        return usuario;
    }

    @Override
    public void map(UsuarioCadastroDTO usuarioDTO, Usuario usuario) {
        if ( usuarioDTO == null ) {
            return;
        }

        usuario.setNome( usuarioDTO.getNome() );
        usuario.setStatus( usuarioDTO.getStatus() );
        usuario.setLogin( usuarioDTO.getLogin() );
        usuario.setEmail( usuarioDTO.getEmail() );
        usuario.setRoles( mapRoles( usuarioDTO.getRoles() ) );
    }
}
