package br.com.tradeflow.domain.repository;

import br.com.tradeflow.domain.entity.Parametro;
import br.com.tradeflow.domain.repository.custom.ParametroRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long>, ParametroRepositoryCustom {

	@Query(nativeQuery = true, value =
			" select valor from parametro where ambiente = :#{#ambiente} and chave = :#{#chave} "
	)
	String getValorByChave(@Param("ambiente") String ambiente, @Param("chave") String chave);
}
