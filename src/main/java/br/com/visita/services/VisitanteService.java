package br.com.visita.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.visita.amqp.producer.impl.AtualizaVisitanteProducer;
import br.com.visita.amqp.producer.impl.VisitanteProducer;
import br.com.visita.converter.Converter;
import br.com.visita.dto.AtualizaVisitanteDto;
import br.com.visita.dto.CabecalhoResponsePublisherDto;
import br.com.visita.dto.GETVisitanteResponseDto;
import br.com.visita.dto.ResponsePublisherDto;
import br.com.visita.dto.VisitanteDto;
import br.com.visita.entities.Visitante;
import br.com.visita.errorheadling.RegistroException;
import br.com.visita.filter.VisitanteFilter;
import br.com.visita.repositories.VisitanteRepository;
import br.com.visita.response.Response;
import br.com.visita.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitanteService {
	
	@Autowired
	private VisitanteProducer producer;
	
	@Autowired
	private AtualizaVisitanteProducer atualizaProducer;
	
	@Autowired
	private VisitanteRepository visitanteRepository;
	
	@Autowired
	private Validators<VisitanteDto, AtualizaVisitanteDto> validator;
	
	@Autowired
	private Converter<List<GETVisitanteResponseDto>, List<Visitante>> converter;

	public ResponsePublisherDto salvar(VisitanteDto visitanteRequestBody) throws RegistroException {
		
		log.info("Cadastrando um visitante: {}", visitanteRequestBody.toString());
		
		visitanteRequestBody.setGuide(UUID.randomUUID().toString()); 	
		
		this.validator.validarPost(visitanteRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitanteRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(visitanteRequestBody);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(visitanteRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}
	
	public ResponsePublisherDto atualizar(AtualizaVisitanteDto visitanteRequestBody) throws RegistroException {
		
		log.info("Atualizando cadastro de um visitante: {}", visitanteRequestBody.toString());
		
		visitanteRequestBody.setGuide(UUID.randomUUID().toString()); 	
		
		this.validator.validarPut(visitanteRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  visitanteRequestBody.toString() + " para o consumer.");
		
		this.atualizaProducer.producerAsync(visitanteRequestBody);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(visitanteRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}
	
	public Page<GETVisitanteResponseDto> buscar(VisitanteFilter filtros, Pageable paginacao){
				
		log.info("Buscando visitante(s)...");
		
		Response<List<GETVisitanteResponseDto>> response = new Response<List<GETVisitanteResponseDto>>(); 
		
		List<Visitante> visitantes = visitanteRepository.findVisitanteBy(filtros);
		
		response.setData(this.converter.convert(visitantes));
		
		return new PageImpl<>(response.getData(), paginacao, this.visitanteRepository.totalRegistros(filtros));
		
	}
	
}
