package br.com.visita.mappers;

import org.mapstruct.Mapper;

import br.com.visita.dto.AtualizaVisitanteDto;
import br.com.visita.dto.GETVisitanteResponseDto;
import br.com.visita.dto.GETVisitanteSemVeiculosResponseDto;
import br.com.visita.dto.VisitanteDto;
import br.com.visita.entities.Visitante;

@Mapper(componentModel = "spring")
public abstract class VisitanteMapper {
	
	public abstract VisitanteDto visitanteToVisitanteDto(Visitante visitante);
	
	public abstract VisitanteDto atualizaVisitanteDtoToVisitanteDto(AtualizaVisitanteDto dto);
	
	public abstract GETVisitanteResponseDto visitanteToGETVisitanteResponseDto(Visitante visitante);
	
	public abstract GETVisitanteSemVeiculosResponseDto visitanteToGETVisitanteSemVeiculosResponseDto(Visitante visitante);
	
}
