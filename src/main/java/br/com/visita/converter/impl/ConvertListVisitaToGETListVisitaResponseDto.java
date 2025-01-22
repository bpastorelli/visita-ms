package br.com.visita.converter.impl;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.visita.converter.Converter;
import br.com.visita.dto.GETVisitaResponseDto;
import br.com.visita.dto.QueryResidenciaResponseDto;
import br.com.visita.dto.ResidenciaRequestDto;
import br.com.visita.entities.Visita;
import br.com.visita.mappers.VeiculoMapper;
import br.com.visita.senders.ResidenciaSender;
import br.com.visita.utils.Utils;

@Component
public class ConvertListVisitaToGETListVisitaResponseDto implements Converter<List<GETVisitaResponseDto>, List<Visita>> {
	
	@Autowired
	private VeiculoMapper veiculoMapper;
	
	@Autowired
	private ResidenciaSender residenciaSender;
	
	@Override
	public List<GETVisitaResponseDto> convert(List<Visita> visitas) {

		List<GETVisitaResponseDto> response = new ArrayList<GETVisitaResponseDto>();
		
		if (visitas.size() > 0) {
			visitas.forEach(m -> {
				
				ResidenciaRequestDto request = ResidenciaRequestDto.builder()
						.id(m.getResidenciaId().toString())
						.build();
				QueryResidenciaResponseDto residencia = null;
				try {
					residencia = residenciaSender.buscarResidenciasPorFiltro(request);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				GETVisitaResponseDto visitante = GETVisitaResponseDto.builder()
						.id(m.getId())
						.nome(m.getVisitante().getNome().toUpperCase())
						.rg(m.getVisitante().getRg())
						.cpf(m.getVisitante().getCpf() != null ? m.getVisitante().getCpf() : "")
						.dataEntrada(Utils.dateFormat(m.getDataEntrada(), "dd/MM/yyyy"))
						.horaEntrada(new Time(m.getDataEntrada().getTime()))
						.dataSaida(m.getDataSaida() != null ? Utils.dateFormat(m.getDataSaida(), "dd/MM/yyyy") : "")
						.horaSaida(m.getHoraSaida() != null ? new Time(m.getDataSaida().getTime()) : null)
						.endereco(residencia.getResidencias().get(0).getEndereco() != null ? residencia.getResidencias().get(0).getEndereco().toUpperCase() : "")
						.numero(residencia.getResidencias().get(0).getNumero().toString() != null ? residencia.getResidencias().get(0).getNumero().toString() : "")
						.complemento(residencia.getResidencias().get(0).getComplemento() != null ? residencia.getResidencias().get(0).getComplemento().toUpperCase() : "")
						.bairro(residencia.getResidencias().get(0).getBairro().toUpperCase())
						.cidade(residencia.getResidencias().get(0).getCidade().toUpperCase())
						.uf(residencia.getResidencias().get(0).getUf().toUpperCase())
						.placa(Utils.formatPlaca(m.getPlaca()))
						.posicao(m.getPosicao())
						.veiculo(!m.getPlaca().isBlank() ? 
								veiculoMapper.veiculoToGETVeiculoSemVisitantesResponseDto(
										m.getVisitante().getVeiculos()
											.stream()
											.filter(p -> p.getVeiculo().getPlaca().trim().toUpperCase().equals(m.getPlaca().trim().toUpperCase()))
											.findFirst()
											.get()
											.getVeiculo()) : null)
						.guide(m.getGuide())
						.build();
				
				response.add(visitante);
				
			});	
		}
		
		return response;
		
	}

}
