package br.com.tradeflow.domain.repository;

import br.com.tradeflow.domain.entity.ConsultaExterna;
import br.com.tradeflow.domain.entity.EnderecoCep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoCepRepository extends JpaRepository<EnderecoCep, Long> {

	EnderecoCep getByCep(String cep);
}
