package br.com.tradeflow.domain.repository;

import br.com.tradeflow.domain.entity.LogAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAcessoRepository extends JpaRepository<LogAcesso, Long> {

	@Query(nativeQuery = true, value =
			"select id from log_acesso order by id desc offset ?1 limit 1"
	)
	Integer getIdExclusao(int numPreservar);

	@Modifying
	@Query(nativeQuery = true, value =
			"delete from log_acesso where id < ?1"
	)
	int excluirAnteriorA(int idInicio);
}
