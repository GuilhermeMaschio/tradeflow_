package br.com.tradeflow.util.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class JpaUtils {

	public static Query createQuery(StringBuilder hql, Class<?> clazz, Map<String, Object> params, EntityManager entityManager) {

		TypedQuery<?> query = entityManager.createQuery(hql.toString(), clazz);

		setParameters(query, params);

		return query;
	}

	private static void setParameters(Query query, Map<String, Object> params) {

		for (Map.Entry<String, Object> entry : params.entrySet()) {

			String key = entry.getKey();
			Object value = entry.getValue();

			if (value != null) {
				query.setParameter(key, value);
			}
		}
	}

	public static void setPageable(Query query, Pageable pageable) {

		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
	}
}
