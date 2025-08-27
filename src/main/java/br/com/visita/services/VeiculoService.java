package br.com.visita.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.theokanning.openai.service.OpenAiService;

import br.com.visita.abstracts.VeiculoBase;
import br.com.visita.amqp.producer.impl.AtualizaVeiculoProducer;
import br.com.visita.amqp.producer.impl.VeiculoProducer;
import br.com.visita.converter.Converter;
import br.com.visita.dto.AtualizaVeiculoDto;
import br.com.visita.dto.CabecalhoResponsePublisherDto;
import br.com.visita.dto.GETVeiculoResponseDto;
import br.com.visita.dto.ProdutoRequestDto;
import br.com.visita.dto.ResponsePublisherDto;
import br.com.visita.dto.VeiculoDto;
import br.com.visita.entities.Veiculo;
import br.com.visita.errorheadling.RegistroException;
import br.com.visita.filter.VeiculoFilter;
import br.com.visita.repositories.VeiculoRepository;
import br.com.visita.response.Response;
import br.com.visita.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VeiculoService extends VeiculoBase {
	
	public VeiculoService(OpenAiService openAiService, Builder webClientBuilder, @Value("${openai.api.key}") String apiKey) {
		super(openAiService, webClientBuilder, apiKey);
	}

	@Autowired
	private VeiculoProducer producer;
	
	@Autowired
	private AtualizaVeiculoProducer atualizaProducer;
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private Validators<VeiculoDto, AtualizaVeiculoDto> validator;
	
	@Autowired
	private Converter<List<GETVeiculoResponseDto>, List<Veiculo>> converter;
	
	public ResponsePublisherDto salvar(VeiculoDto veiculoRequestBody) throws RegistroException {
		
		log.info("Cadastrando um veiculo: {}", veiculoRequestBody.toString());
		
		veiculoRequestBody.setGuide(UUID.randomUUID().toString()); 	
		
		this.validator.validarPost(veiculoRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  veiculoRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(veiculoRequestBody);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(veiculoRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}
	
	public ResponsePublisherDto assistenteCriacao(ProdutoRequestDto request) throws RegistroException  {
		
		VeiculoDto veiculoRequest = this.createProductFromChatGpt(request);
		
		return this.salvar(veiculoRequest);
		
	}
	
	public ResponsePublisherDto atualizar(AtualizaVeiculoDto veiculoRequestBody) throws RegistroException {
		
		log.info("Atualizando cadastro de um veiculo: {}", veiculoRequestBody.toString());
		
		veiculoRequestBody.setGuide(UUID.randomUUID().toString()); 	
		
		this.validator.validarPut(veiculoRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  veiculoRequestBody.toString() + " para o consumer.");
		
		this.atualizaProducer.producerAsync(veiculoRequestBody);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(veiculoRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}
	
	public Page<GETVeiculoResponseDto> buscar(VeiculoFilter filtros, Pageable paginacao){
		
		log.info("Buscando veiculo(s)...");
		
		Response<List<GETVeiculoResponseDto>> response = new Response<List<GETVeiculoResponseDto>>(); 
		
		if (filtros.getPlaca() != null)
			filtros.setPlaca(filtros.getPlaca().replace("-", ""));
		
		List<Veiculo> veiculos = veiculoRepository.findVeiculosBy(filtros);
		
		response.setData(this.converter.convert(veiculos));
		
		return new PageImpl<>(response.getData(), paginacao, this.veiculoRepository.totalRegistros(filtros));
		
	}

}
