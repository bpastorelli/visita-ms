package br.com.visita.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.visita.entities.VinculoVeiculo;

@Repository
@Transactional(readOnly = true)
public interface VinculoVeiculoRepository  extends JpaRepository<VinculoVeiculo, Long>{

	public List<VinculoVeiculo> findByVisitanteId(Long idVisitante);
	
}
