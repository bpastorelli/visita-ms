package br.com.visita.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.visita.entities.Visita;
import br.com.visita.filter.VisitaFilter;

@Repository
@Transactional(readOnly = true)
@NamedQueries({
		@NamedQuery(name = "VisitaRepository.findByPosicaoAndVisitanteRgAndVisitanteCpf", 
					query = "SELECT a FROM Visitante a "
							+ "WHERE a.visita.posicao = :posicao "
							+ "AND a.rg = :rg "
							+ "AND a.cpf = :cpf "
							+ "AND a.nome like %:nome%")})
		
public interface VisitaRepository extends JpaRepository<Visita, Long> {
	
	Optional<Visita> findById(Long id);
	
	List<Visita> findByPosicaoAndVisitanteRgAndVisitanteNomeContaining(@Param("posicao") Integer posicao, 
																					  @Param("rg") String rg, 
																					  @Param("nome") String nome);
	
	@Transactional(readOnly = true)
	Page<Visita> findAll(Pageable pageable);
	
	Optional<Visita> findFirstByVisitanteIdOrderByDataEntradaDesc(Long id);
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.dataEntrada BETWEEN :dataInicio AND :dataFim ")
	Page<Visita> findByDataEntradaBetween(@Param("dataInicio") Date dataInicio, 
										  @Param("dataFim") Date dataFim,
										  Pageable pageable);
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.dataEntrada = :dataInicio ")
	Page<Visita> findByDataEntrada(@Param("dataInicio") Date dataInicio, 
								   Pageable pageable);
	
	//Busca por periodo de datas e posição
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.dataEntrada BETWEEN :dataInicio AND :dataFim "
			 + "AND v.posicao = :posicao")
	Page<Visita> findByDataEntradaBetweenAndPosicao(@Param("dataInicio") Date dataInicio, 
										            @Param("dataFim") Date dataFim,
										            @Param("posicao") Integer posicao,
										            Pageable pageable);
	
	//Busca por periodo de datas e posição
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.dataEntrada BETWEEN :dataInicio AND :dataFim "
		  	 + "AND v.visitante.rg = :rg ")
	Page<Visita> findByDataEntradaBetweenAndRg(@Param("dataInicio") Date dataInicio, 
										            @Param("dataFim") Date dataFim,
										            @Param("rg") String rg,
										            Pageable pageable);
	
	//Busca por periodo de datas e posição
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.dataEntrada BETWEEN :dataInicio AND :dataFim "
		  	 + "AND v.visitante.rg = :rg "
		  	 + "AND v.posicao = :posicao")
	Page<Visita> findByDataEntradaBetweenAndRgAndPosicao(@Param("dataInicio") Date dataInicio, 
										            @Param("dataFim") Date dataFim,
										            @Param("rg") String rg,
										            @Param("posicao") Integer posicao,
										            Pageable pageable);
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.nome like %:nome% "
			 + "AND v.visitante.rg = :rg "
			 + "AND v.dataEntrada BETWEEN :dataInicio AND :dataFim "
		  	 + "AND v.posicao = :posicao ")
	Page<Visita> findByNomeContainingAndRgAndDataEntradaBetweenAndPosicao(@Param("nome") String nome,
										                                  @Param("rg") String rg,
										                                  @Param("dataInicio") Date dataInicio, 
										                                  @Param("dataFim") Date dataFim,
										                                  @Param("posicao") Integer posicao,
										                                  Pageable pageable);
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.nome like %:nome% "
			 + "AND v.dataEntrada BETWEEN :dataInicio AND :dataFim "
		  	 + "AND v.posicao = :posicao ")
	Page<Visita> findByNomeContainingAndDataEntradaBetweenAndPosicao(@Param("nome") String nome,
										                             @Param("dataInicio") Date dataInicio, 
										                             @Param("dataFim") Date dataFim,
										                             @Param("posicao") Integer posicao,
										                             Pageable pageable);
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.nome like %:nome% "
			 + "AND v.dataEntrada BETWEEN :dataInicio AND :dataFim ")
	Page<Visita> findByNomeContainingAndDataEntradaBetween(@Param("nome") String nome,
										                   @Param("dataInicio") Date dataInicio, 
										                   @Param("dataFim") Date dataFim,
										                   Pageable pageable);
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.nome like %:nome% "
			 + "AND v.visitante.rg = :rg "
			 + "AND v.dataEntrada BETWEEN :dataInicio AND :dataFim ")
	Page<Visita> findByNomeContainingAndRgAndDataEntradaBetween(@Param("nome") String nome,
										                        @Param("rg") String rg,
										                        @Param("dataInicio") Date dataInicio, 
										                        @Param("dataFim") Date dataFim,
										                        Pageable pageable);		
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.nome like %:nome% "
			 + "AND v.visitante.rg = :rg ")
	Page<Visita> findByNomeContainingAndRg(@Param("nome") String nome,
										   @Param("rg") String rg,
										   Pageable pageable);	
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.nome like %:nome% ")
	Page<Visita> findByNomeContaining(@Param("nome") String nome,
									  Pageable pageable);	
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.nome like %:nome% "
			 + "AND v.posicao = :posicao")
	Page<Visita> findByNomeContainingAndPosicao(@Param("nome") String nome,
										        @Param("posicao") Integer posicao,
										        Pageable pageable);
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.nome like %:nome% "
			 + "AND v.visitante.rg = :rg "
			 + "AND v.posicao = :posicao")
	Page<Visita> findByNomeContainingAndRgAndPosicao(@Param("nome") String nome,
										             @Param("rg") String rg,
										             @Param("posicao") Integer posicao,
										             Pageable pageable);
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.rg = :rg "
			 + "AND v.posicao = :posicao ")
	Page<Visita> findByRgAndPosicao(@Param("rg") String nome,
									@Param("posicao") Integer posicao,
									Pageable pageable);	
	
	@Query(value = "FROM Visita v "
		  	 + "WHERE v.visitante.rg = :rg ")	
	Page<Visita> findByRg(@Param("rg") String nome, 
						  Pageable pageRequest);
	
	Page<Visita> findByPosicao(Integer posicao, Pageable pageRequest);
	
	Optional<Visita> findByGuide(String guide);
	
	@Query(value = "select * "
			+ "from visita v "
			+ "join visitante vt "
			+ "on v.visitante_id = vt.id "
			+ "where (v.id = :#{#filter.id} OR :#{#filter.id} IS NULL) "
			+ "and (v.posicao =:#{#filter.posicao} OR :#{#filter.posicao} IS NULL) "	
			+ "and (vt.nome like %:#{#filter.nome}% OR :#{#filter.nome} IS NULL) "
			+ "and (vt.rg = :#{#filter.rg} OR :#{#filter.rg} IS NULL) "
			+ "and (vt.cpf = :#{#filter.cpf} OR :#{#filter.cpf} IS NULL) "
			+ "and (v.data_entrada BETWEEN :#{#filter.dataInicio} AND :#{#filter.dataFim} OR :#{#filter.dataInicio} IS NULL OR :#{#filter.dataFim} IS NULL) "
			, nativeQuery = true)
	public List<Visita> findVisitaBy(@Param("filter") VisitaFilter filter);
	
	@Query(value = "select count(*)"
			+ " from visita v "
			+ " join visitante vt "
			+ " where (v.id = :#{#filter.id} OR :#{#filter.id} IS NULL) "
			+ " and (v.posicao =:#{#filter.posicao} OR :#{#filter.posicao} IS NULL) "
			+ " and (vt.nome like %:#{#filter.nome}% OR :#{#filter.nome} IS NULL)"
			+ " and (vt.rg = :#{#filter.rg} OR :#{#filter.rg} IS NULL) "
			+ " and (vt.cpf = :#{#filter.cpf} OR :#{#filter.cpf} IS NULL) "
			, nativeQuery = true)
	public Long totalRegistros(@Param("filter") VisitaFilter filter);

}
