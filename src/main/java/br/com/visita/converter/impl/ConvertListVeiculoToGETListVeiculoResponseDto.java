package br.com.visita.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.visita.converter.Converter;
import br.com.visita.dto.GETVeiculoResponseDto;
import br.com.visita.dto.GETVisitanteSemVeiculosResponseDto;
import br.com.visita.entities.Veiculo;
import br.com.visita.entities.VinculoVeiculo;
import br.com.visita.mappers.VisitanteMapper;
import br.com.visita.repositories.VinculoVeiculoRepository;

@Component
public class ConvertListVeiculoToGETListVeiculoResponseDto implements Converter<List<GETVeiculoResponseDto>, List<Veiculo>> {
	
	@Autowired
	private VisitanteMapper visitanteMapper;
	
	@Autowired
	private VinculoVeiculoRepository veiculoRepository;
	
	@Override
	public List<GETVeiculoResponseDto> convert(List<Veiculo> veiculos) {

		List<GETVeiculoResponseDto> response = new ArrayList<GETVeiculoResponseDto>();
		
		if (veiculos.size() > 0) {
			veiculos.forEach(m -> {
				GETVeiculoResponseDto visitante = GETVeiculoResponseDto.builder()
					.id(m.getId())
					.placa(m.getPlaca().toUpperCase())
					.marca(m.getMarca().toUpperCase())
					.modelo(m.getModelo().toUpperCase())
					.cor(m.getCor() != null ? m.getCor().toUpperCase() : "")
					.ano(m.getAno() != null ? m.getAno() : 0 )
					.posicao(m.getPosicao())
					.visitantes(convertVisitantesToGETVisitanteSemVeiculosResponseDto(m.getId()))
					.guide(m.getGuide())
					.build();
				
				response.add(visitante);
				
			});	
		}
		
		return response;
		
	}
	
	private List<GETVisitanteSemVeiculosResponseDto> convertVisitantesToGETVisitanteSemVeiculosResponseDto(Long id){
		
		List<GETVisitanteSemVeiculosResponseDto> visitantes = new ArrayList<>();
		List<VinculoVeiculo> vinculos = veiculoRepository.findByVeiculoId(id);
		
		for (VinculoVeiculo vinculo : vinculos) {
			GETVisitanteSemVeiculosResponseDto visitante = visitanteMapper.visitanteToGETVisitanteSemVeiculosResponseDto(vinculo.getVisitante());
			visitante.setNome(visitante.getNome().toUpperCase());
			visitante.setEndereco(visitante.getEndereco().toUpperCase());
			visitante.setComplemento(visitante.getComplemento() != null ? visitante.getComplemento().toUpperCase() : "");
			visitante.setBairro(visitante.getBairro().toUpperCase());
			visitante.setCidade(visitante.getCidade().toUpperCase());
			visitante.setUf(visitante.getUf().toUpperCase());
			visitantes.add(visitante);
		}
		
		return visitantes;
	}


}
