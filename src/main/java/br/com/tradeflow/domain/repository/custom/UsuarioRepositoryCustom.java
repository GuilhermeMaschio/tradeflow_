package br.com.tradeflow.domain.repository.custom;

import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.dto.filter.UsuarioFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioRepositoryCustom {

	List<Usuario> findByFilter(Pageable pageable, UsuarioFilter filtro);

	int countByFilter(UsuarioFilter filtro);
}
