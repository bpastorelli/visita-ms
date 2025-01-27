package br.com.visita.senders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.visita.dto.GETMoradoresResponseDto;
import br.com.visita.dto.MoradorRequestDto;
import br.com.visita.utils.RestTemplateUtil;

@Service
public class MoradorSender {
	
	@Value("${morador-ms.url}")
	public String URL;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public GETMoradoresResponseDto buscarPorFiltros(MoradorRequestDto request) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		
		RestTemplateUtil rest = RestTemplateUtil.builder()
				.URL(URL + "/query?%s")
				.mediaType(MediaType.APPLICATION_JSON)
				.method(HttpMethod.GET)
				.restTemplate(restTemplate)
				.params(request)
				.build();
		
		return (GETMoradoresResponseDto) rest.execute(GETMoradoresResponseDto.class);
		
	}
	
	public GETMoradoresResponseDto buscarPorIds(MoradorRequestDto request) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		
		RestTemplateUtil rest = RestTemplateUtil.builder()
				.URL(URL + "/buscar?%s")
				.mediaType(MediaType.APPLICATION_JSON)
				.method(HttpMethod.GET)
				.restTemplate(restTemplate)
				.params(request)
				.build();
		
		return (GETMoradoresResponseDto) rest.execute(GETMoradoresResponseDto.class);
		
	}
	
	public GETMoradoresResponseDto buscarPorResidenciaId(MoradorRequestDto request) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		
		RestTemplateUtil rest = RestTemplateUtil.builder()
				.URL(URL + "/residencia?%s")
				.mediaType(MediaType.APPLICATION_JSON)
				.method(HttpMethod.GET)
				.restTemplate(restTemplate)
				.params(request)
				.build();
		
		return (GETMoradoresResponseDto) rest.execute(GETMoradoresResponseDto.class);
		
	}

}
