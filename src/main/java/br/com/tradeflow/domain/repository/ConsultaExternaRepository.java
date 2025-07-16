package br.com.tradeflow.domain.repository;

import br.com.tradeflow.domain.entity.ConsultaExterna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaExternaRepository extends JpaRepository<ConsultaExterna, Long> {

}
