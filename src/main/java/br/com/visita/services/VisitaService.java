package br.com.visita.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.visita.amqp.producer.impl.VisitaProducer;
import br.com.visita.converter.Converter;
import br.com.visita.dto.CabecalhoResponsePublisherDto;
import br.com.visita.dto.EncerraVisitaDto;
import br.com.visita.dto.GETVisitaResponseDto;
import br.com.visita.dto.GETVisitasPaginadoResponseDto;
import br.com.visita.dto.PaginacaoDto;
import br.com.visita.dto.ResponsePublisherDto;
import br.com.visita.dto.VisitaDto;
import br.com.visita.entities.Visita;
import br.com.visita.errorheadling.RegistroException;
import br.com.visita.filter.VisitaFilter;
import br.com.visita.repositories.VisitaRepository;
import br.com.visita.response.Response;
import br.com.visita.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitaService {
	
	@Autowired
	private VisitaProducer producer;
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	@Autowired
	private Validators<VisitaDto, EncerraVisitaDto> validator;
	
	@Autowired
	private Converter<List<GETVisitaResponseDto>, List<Visita>> converter;
	
	public ResponsePublisherDto salvar(VisitaDto visitaRequestBody) throws RegistroException {
		
		log.info("Cadastrando uma visita: {}", visitaRequestBody.toString());
		
		visitaRequestBody.setGuide(UUID.randomUUID().toString());
		tratarPlaca(visitaRequestBody);
		
		this.validator.validarPost(visitaRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitaRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(visitaRequestBody);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(visitaRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}
	
	public ResponsePublisherDto atualizar(EncerraVisitaDto visitaRequestBody) throws RegistroException {

		log.info("Cadastrando um veículo: {}", visitaRequestBody.toString());
		
		this.validator.validarPut(visitaRequestBody);
		
		Visita visita = visitaRepository.findById(visitaRequestBody.getId()).get();
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitaRequestBody.toString() + " para o consumer.");
		
		VisitaDto visitaRequest = VisitaDto.builder()
				.id(visitaRequestBody.getId())
				.guide(visita.getGuide())
				.build();
		
		this.producer.producerAsync(visitaRequest);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(visita.getGuide())
						.build())
				.build();
		
		return response;
	}
	
	public GETVisitasPaginadoResponseDto buscar(VisitaFilter filtros, Pageable pageable) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException {

		log.info("Buscando visita(s)...");
		
		Response<List<GETVisitaResponseDto>> response = new Response<List<GETVisitaResponseDto>>(); 
		
		PageRequest request = PageRequest.of(pageable.getPageNumber() == 0 ? 0 : (pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0), pageable.getPageSize());
		
		if (filtros.getDataFim() == null && filtros.getDataInicio() != null)
			filtros.setDataFim(filtros.getDataInicio().plusDays(1));
		else if (filtros.getDataFim() != null && filtros.getDataInicio() != null)
			filtros.setDataFim(filtros.getDataFim().plusDays(1));
			
		Page<Visita> visitas = visitaRepository.findVisitaBy(filtros, request);
		
		response.setData(this.converter.convert(visitas.getContent()));
		
		int page = visitas.getNumber() == 0 ? 1 : (visitas.getNumber() >= 1 ? visitas.getNumber()+1 : 1);
		
		PaginacaoDto paginacao = PaginacaoDto.builder()
				.pagina(page)
				.paginaAnterior(page == 1 ? 1 : page-1)
				.proximaPagina(page < visitas.getTotalPages() ? page+1 : visitas.getTotalPages())
				.totalPaginas(visitas.getTotalPages())
				.build();
		
		GETVisitasPaginadoResponseDto responsePaginado = GETVisitasPaginadoResponseDto.builder()
				.visitas(response.getData())
				.paginacao(paginacao)
				.build();
		
		return responsePaginado;
		
	}
	
	private void tratarPlaca(VisitaDto dto) {
		
		dto.getPlaca().replace("-", "");
		
	}
	
}
