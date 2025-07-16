package br.com.tradeflow.domain.repository;

import br.com.tradeflow.domain.entity.LogAlteracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAlteracaoRepository extends JpaRepository<LogAlteracao, Long> {
}
