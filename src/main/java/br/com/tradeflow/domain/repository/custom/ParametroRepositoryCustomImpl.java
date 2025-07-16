package br.com.tradeflow.domain.repository.custom;

import br.com.tradeflow.dto.filter.ParametroFilter;
import br.com.tradeflow.domain.entity.Parametro;
import br.com.tradeflow.util.jpa.JpaUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParametroRepositoryCustomImpl implements ParametroRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Parametro> findByFilter(Pageable pageable, ParametroFilter filtro) {

		Map<String, Object> params = new LinkedHashMap<>();
		StringBuilder hql = new StringBuilder(" select p from Parametro p ");

		makeWhere(hql, filtro, params);

		hql.append(" order by p.ambiente, p.chave ");

		Query query = JpaUtils.createQuery(hql, Parametro.class, params, entityManager);

		JpaUtils.setPageable(query, pageable);

		return query.getResultList();
	}

	private static void makeWhere(StringBuilder hql, ParametroFilter filtro, Map<String, Object> params) {

		String ambiente = filtro.getAmbiente();
		String chave = filtro.getChave();

		hql.append(" where 1=1 ");

		if (StringUtils.isNotBlank(ambiente)) {
			hql.append(" and p.ambiente = :ambiente ");
			params.put("ambiente", ambiente);
		}

		if (StringUtils.isNotBlank(chave)) {
			hql.append(" and p.chave = :chave ");
			params.put("chave", chave);
		}
	}

	@Override
	public int countByFilter(ParametroFilter filtro) {

		Map<String, Object> params = new LinkedHashMap<>();
		StringBuilder hql = new StringBuilder(" select count(*) FROM Parametro p ");

		makeWhere(hql, filtro, params);

		Query query = JpaUtils.createQuery(hql, Long.class, params, entityManager);

		return ((Number) query.getSingleResult()).intValue();
	}
}
