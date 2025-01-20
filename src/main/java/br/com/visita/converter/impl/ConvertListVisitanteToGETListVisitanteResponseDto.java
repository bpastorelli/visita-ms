package br.com.visita.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.visita.converter.Converter;
import br.com.visita.dto.GETVeiculoSemVisitantesResponseDto;
import br.com.visita.dto.GETVisitanteResponseDto;
import br.com.visita.entities.VinculoVeiculo;
import br.com.visita.entities.Visitante;
import br.com.visita.mappers.VeiculoMapper;

@Component
public class ConvertListVisitanteToGETListVisitanteResponseDto implements Converter<List<GETVisitanteResponseDto>, List<Visitante>> {
	
	@Autowired
	private VeiculoMapper veiculoMapper;
	
	@Override
	public List<GETVisitanteResponseDto> convert(List<Visitante> visitantes) {

		List<GETVisitanteResponseDto> response = new ArrayList<GETVisitanteResponseDto>();
		
		if (visitantes.size() > 0) {
			visitantes.forEach(m -> {
				GETVisitanteResponseDto visitante = GETVisitanteResponseDto.builder()
					.id(m.getId())
					.nome(m.getNome().toUpperCase())
					.rg(m.getRg())
					.cpf(m.getCpf() != null ? m.getCpf() : "")
					.endereco(m.getEndereco() != null ? m.getEndereco().toUpperCase() : "")
					.numero(m.getNumero().toString() != null ? m.getNumero().toString() : "")
					.complemento(m.getComplemento() != null ? m.getComplemento().toUpperCase() : "")
					.bairro(m.getBairro().toUpperCase())
					.cidade(m.getCidade().toUpperCase())
					.uf(m.getUf().toUpperCase())
					.celular(m.getCelular())
					.telefone(m.getTelefone())
					.posicao(m.getPosicao())
					.veiculos(convertVeiculosToGETVeiculoSemVisitantesResponseDto(m.getVeiculos()))
					.guide(m.getGuide())
					.build();
				
				response.add(visitante);
				
			});	
		}
		
		return response;
		
	}
	
	private List<GETVeiculoSemVisitantesResponseDto> convertVeiculosToGETVeiculoSemVisitantesResponseDto(List<VinculoVeiculo> vinculos){
		
		List<GETVeiculoSemVisitantesResponseDto> veiculos = new ArrayList<>();
		
		for (VinculoVeiculo vinculo : vinculos) {
			GETVeiculoSemVisitantesResponseDto veiculo = veiculoMapper.veiculoToGETVeiculoSemVisitantesResponseDto(vinculo.getVeiculo());
			veiculos.add(veiculo);
		}
		
		return veiculos;
	}

}
