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

import br.com.visita.entities.Visitante;
import br.com.visita.filter.VisitanteFilter;

@Repository
@Transactional(readOnly = true)
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
	
	
	Optional<Visitante> findById(Long id);
	
	Optional<Visitante> findByNome(String nome);
	
	Optional<List<Visitante>> findByCpf(String cpf);
	
	Optional<Visitante> findByRg(String rg);
	
	Optional<Visitante> findByRgOrCpf(String rg, String cpf);
	
	@Transactional(readOnly = true)
	Page<Visitante> findByIdOrNomeContainsOrCpfOrRg(Long id, String nome, String cpf, String rg, Pageable pageable);

	Optional<Visitante> findByGuide(String guide);
	
	@Query(value = "select * "
			+ "from visitante v "
			+ "where (v.id = :#{#filter.id} OR :#{#filter.id} IS NULL) "
			+ "and (v.nome like concat('%',:#{#filter.nome},'%') OR :#{#filter.nome} IS NULL) "
			+ "and (v.rg = :#{#filter.rg} OR :#{#filter.rg} IS NULL) "
			+ "and (v.cpf = :#{#filter.cpf} OR :#{#filter.cpf} IS NULL) "
			+ "and (v.posicao =:#{#filter.posicao} OR :#{#filter.posicao} IS NULL) "	
			, nativeQuery = true)
	public List<Visitante> findVisitanteBy(@Param("filter") VisitanteFilter filter);
	
	@Query(value = "select count(*)"
			+ " from visitante v "
			+ " where (v.id = :#{#filter.id} OR :#{#filter.id} IS NULL) "
			+ " and (v.nome like concat('%',:#{#filter.nome},'%') OR :#{#filter.nome} IS NULL)"
			+ " and (v.rg = :#{#filter.rg} OR :#{#filter.rg} IS NULL) "
			+ " and (v.cpf = :#{#filter.cpf} OR :#{#filter.cpf} IS NULL) "
			+ " and (v.posicao =:#{#filter.posicao} OR :#{#filter.posicao} IS NULL) "
			, nativeQuery = true)
	public Long totalRegistros(@Param("filter") VisitanteFilter filter);
}
