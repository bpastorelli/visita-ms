package br.com.visita.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GETVisitasPaginadoResponseDto {

	private List<GETVisitaResponseDto> visitas;
	
	private PaginacaoDto paginacao;
	
}
