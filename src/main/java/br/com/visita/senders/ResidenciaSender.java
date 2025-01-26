package br.com.visita.senders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.visita.dto.QueryResidenciaResponseDto;
import br.com.visita.dto.ResidenciaRequestDto;
import br.com.visita.utils.RestTemplateUtil;

@Service
public class ResidenciaSender {
	
	@Value("${residencia-ms.url}")
	public String URL;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public QueryResidenciaResponseDto buscarResidencias(ResidenciaRequestDto request) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		
		RestTemplateUtil rest = RestTemplateUtil.builder()
				.URL(URL + "?%s")
				.mediaType(MediaType.APPLICATION_JSON)
				.method(HttpMethod.GET)
				.restTemplate(restTemplate)
				.params(request)
				.build();
		
		return (QueryResidenciaResponseDto) rest.execute(QueryResidenciaResponseDto.class);
		
	}
	
	public QueryResidenciaResponseDto buscarResidenciasPorFiltro(ResidenciaRequestDto request) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		
		RestTemplateUtil rest = RestTemplateUtil.builder()
				.URL(URL + "/filtro?%s")
				.mediaType(MediaType.APPLICATION_JSON)
				.method(HttpMethod.GET)
				.restTemplate(restTemplate)
				.params(request)
				.build();
		
		return (QueryResidenciaResponseDto) rest.execute(QueryResidenciaResponseDto.class);
		
	}

}
