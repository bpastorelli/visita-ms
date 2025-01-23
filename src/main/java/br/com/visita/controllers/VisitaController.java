package br.com.visita.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.visita.dto.EncerraVisitaDto;
import br.com.visita.dto.GETVisitasPaginadoResponseDto;
import br.com.visita.dto.ResponsePublisherDto;
import br.com.visita.dto.VisitaDto;
import br.com.visita.errorheadling.RegistroException;
import br.com.visita.errorheadling.RegistroExceptionHandler;
import br.com.visita.filter.VisitaFilter;
import br.com.visita.services.VisitaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "Cadastro de Visitas")
@RequestMapping("/visita-ms/visita")
@CrossOrigin(origins = "*")
class VisitaController extends RegistroExceptionHandler {
	
	@Autowired
	private VisitaService visitaService;
	
	@ApiOperation(value = "Produz uma nova mensagem no Kafka para cadastro de visita.")
	@PostMapping(value = "/amqp/novo")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody VisitaDto visitaRequestBody,
											   BindingResult result ) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.visitaService.salvar(visitaRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@ApiOperation(value = "Produz uma nova mensagem no Kafka para encerrar uma visita.")
	@PutMapping(value = "/amqp/encerrar")
	public ResponseEntity<?> encerrarVisitaAMQP(@Valid @RequestBody EncerraVisitaDto encerraVisitaDto,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = visitaService.atualizar(encerraVisitaDto);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@ApiOperation(value = "Pesquisa visitas a partir dos filtros informados.")
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarVisitasFiltro(
			VisitaFilter filters,
			@PageableDefault(page = 1, size = 30) Pageable paginacao) throws NoSuchAlgorithmException {
		
		GETVisitasPaginadoResponseDto visitas = null;
		
		try {
			visitas = this.visitaService.buscar(filters, paginacao);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return filters.isContent() ? new ResponseEntity<>(visitas.getVisitas(), HttpStatus.OK) :
					new ResponseEntity<>(visitas, HttpStatus.OK);
		
	}

}
