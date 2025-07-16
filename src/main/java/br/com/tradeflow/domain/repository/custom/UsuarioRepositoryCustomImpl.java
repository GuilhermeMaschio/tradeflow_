package br.com.tradeflow.domain.repository.custom;

import br.com.tradeflow.domain.entity.Parametro;
import br.com.tradeflow.domain.entity.Usuario;
import br.com.tradeflow.dto.filter.UsuarioFilter;
import br.com.tradeflow.util.jpa.JpaUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UsuarioRepositoryCustomImpl implements UsuarioRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Usuario> findByFilter(Pageable pageable, UsuarioFilter filtro) {

		Map<String, Object> params = new LinkedHashMap<>();
		StringBuilder hql = new StringBuilder(" select u from Usuario u ");

		makeWhere(hql, filtro, params);

		hql.append(" order by u.nome ");

		Query query = JpaUtils.createQuery(hql, Usuario.class, params, entityManager);

		JpaUtils.setPageable(query, pageable);

		return query.getResultList();
	}

	private static void makeWhere(StringBuilder hql, UsuarioFilter filtro, Map<String, Object> params) {

		String login = filtro.getLogin();
		String nome = filtro.getNome();

		hql.append(" where 1=1 ");

		if (StringUtils.isNotBlank(login)) {
			hql.append(" and u.login = :login ");
			params.put("login", login);
		}

		if (StringUtils.isNotBlank(nome)) {
			hql.append(" and upper(u.nome) like :nome ");
			params.put("nome", "%" + nome.toUpperCase() + "%");
		}
	}

	@Override
	public int countByFilter(UsuarioFilter filtro) {

		Map<String, Object> params = new LinkedHashMap<>();
		StringBuilder hql = new StringBuilder(" select count(*) FROM Usuario u ");

		makeWhere(hql, filtro, params);

		Query query = JpaUtils.createQuery(hql, Long.class, params, entityManager);

		return ((Number) query.getSingleResult()).intValue();
	}
}
