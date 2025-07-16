package br.com.tradeflow.domain.repository;

import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.domain.repository.custom.UsuarioRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryCustom {

	Usuario getByLogin(String login);

	Usuario getByKeyCloakId(String keyCloakId);
}
