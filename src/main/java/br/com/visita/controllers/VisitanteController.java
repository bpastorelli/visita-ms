package br.com.visita.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.visita.dto.AtualizaVisitanteDto;
import br.com.visita.dto.GETVisitanteResponseDto;
import br.com.visita.dto.ResponsePublisherDto;
import br.com.visita.dto.VisitanteDto;
import br.com.visita.errorheadling.RegistroException;
import br.com.visita.errorheadling.RegistroExceptionHandler;
import br.com.visita.filter.VisitanteFilter;
import br.com.visita.services.VisitanteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "Cadastro de Visitantes")
@RequestMapping("/visita-ms/visitante")
@CrossOrigin(origins = "*")
class VisitanteController extends RegistroExceptionHandler {
	
	
	@Autowired
	private VisitanteService visitanteService;
	
	@ApiOperation(value = "Produz uma nova mensagem no Kafka para cadastro de visitante.")
	@PostMapping(value = "/amqp/novo")
	public ResponseEntity<?> cadastrarNovoAMQP(@Valid @RequestBody VisitanteDto visitanteRequestBody,
											   BindingResult result ) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.visitanteService.salvar(visitanteRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@ApiOperation(value = "Produz uma nova mensagem no Kafka para alterar um visitante.")
	@PutMapping(value = "/amqp/alterar")
	public ResponseEntity<?> alterarAMQP( 
			@Valid @RequestBody AtualizaVisitanteDto visitanteRequestBody,
			@RequestParam(value = "id", defaultValue = "0") Long id,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		visitanteRequestBody.setId(id);
		ResponsePublisherDto response = this.visitanteService.atualizar(visitanteRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@ApiOperation(value = "Pesquisa visitantes a partir dos filtros informados.")
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarMoradoresFiltro(
			VisitanteFilter filters,
			@PageableDefault(sort = "nome", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException {
		
		Page<GETVisitanteResponseDto> visitantes = this.visitanteService.buscar(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(visitantes.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(visitantes, HttpStatus.OK);
		
	}

}
