package br.com.visita.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.visita.dto.GETVeiculoResponseDto;
import br.com.visita.dto.GETVeiculoSemVisitantesResponseDto;
import br.com.visita.dto.VeiculoDto;
import br.com.visita.entities.Veiculo;
import br.com.visita.utils.Utils;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {
	
	
	@Mapping(target = "placa", source = "placa", qualifiedByName = "FormatarPlaca")
	public abstract GETVeiculoResponseDto veiculoToGETVeiculoResponseDto(Veiculo veiculo);
	
	public abstract VeiculoDto veiculoToVeiculoDto(Veiculo veiculo);
	
	public abstract GETVeiculoSemVisitantesResponseDto veiculoToGETVeiculoSemVisitantesResponseDto(Veiculo veiculo);
	
	@Named("FormatarPlaca")
	default String formatarPlaca(String placa) {
		
		return Utils.formatPlaca(placa);
		
	}

}
