package br.com.visita.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.visita.entities.Veiculo;
import br.com.visita.filter.VeiculoFilter;

@Repository
@Transactional(readOnly = true)
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
	
	Optional<Veiculo> findById(Long id);
	
	@Transactional(readOnly = true)
	Page<Veiculo> findByIdOrPlacaOrMarcaOrModelo(Long id, String placa, String marca, String modelo, Pageable pageable);
	
	Optional<Veiculo> findByPlaca(String placa);
	
	Optional<Veiculo> findByGuide(String guide);
	
	@Query(value = "select * "
			+ "from veiculo v "
			+ "where (v.id = :#{#filter.id} OR :#{#filter.id} IS NULL) "
			+ "and (v.placa = :#{#filter.placa} OR :#{#filter.placa} IS NULL) "
			+ "and (v.marca = :#{#filter.marca} OR :#{#filter.marca} IS NULL) "
			+ "and (v.modelo = :#{#filter.modelo} OR :#{#filter.modelo} IS NULL) "
			+ "and (v.cor = :#{#filter.cor} OR :#{#filter.cor} IS NULL) "
			+ "and (v.ano = :#{#filter.ano} OR :#{#filter.ano} IS NULL) "
			+ "and (v.posicao =:#{#filter.posicao} OR :#{#filter.posicao} IS NULL) "
			+ "and (v.guide =:#{#filter.guide} OR :#{#filter.guide} IS NULL)"
			, nativeQuery = true)
	public List<Veiculo> findVeiculosBy(@Param("filter") VeiculoFilter filter);
	
	@Query(value = "select count(*)"
			+ " from veiculo v "
			+ " where (v.id = :#{#filter.id} OR :#{#filter.id} IS NULL) "
			+ "and (v.placa = :#{#filter.placa} OR :#{#filter.placa} IS NULL) "
			+ "and (v.marca = :#{#filter.marca} OR :#{#filter.marca} IS NULL) "
			+ "and (v.modelo = :#{#filter.modelo} OR :#{#filter.modelo} IS NULL) "
			+ "and (v.cor = :#{#filter.cor} OR :#{#filter.cor} IS NULL) "
			+ "and (v.ano = :#{#filter.ano} OR :#{#filter.ano} IS NULL) "
			+ "and (v.posicao =:#{#filter.posicao} OR :#{#filter.posicao} IS NULL) "
			+ "and (v.guide =:#{#filter.guide} OR :#{#filter.guide} IS NULL)"
			, nativeQuery = true)
	public Long totalRegistros(@Param("filter") VeiculoFilter filter);

}
