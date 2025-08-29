package br.com.visita.senders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.visita.dto.GETVinculoMoradorResidenciaResponseDto;
import br.com.visita.dto.GETVinculoResidenciaMoradorResponseDto;
import br.com.visita.dto.VinculoResidenciaRequestDto;
import br.com.visita.security.service.TokenService;
import br.com.visita.utils.RestTemplateUtil;

@Service
public class VinculosSender {
	
	@Value("${vinculos-ms.url}")
	public String URL;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private TokenService tokenService;
	
	public GETVinculoMoradorResidenciaResponseDto buscarResidenciasPorMorador(VinculoResidenciaRequestDto request) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		
		RestTemplateUtil rest = RestTemplateUtil.builder()
				.URL(URL + "/consulta?%s")
				.jwtToken(tokenService.getCurrentToken())
				.mediaType(MediaType.APPLICATION_JSON)
				.method(HttpMethod.GET)
				.restTemplate(restTemplate)
				.params(request)
				.build();
		
		return (GETVinculoMoradorResidenciaResponseDto) rest.execute(GETVinculoMoradorResidenciaResponseDto.class);
		
	}
	
	public GETVinculoResidenciaMoradorResponseDto buscarMoradoresPorResidencia(VinculoResidenciaRequestDto request) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		
		RestTemplateUtil rest = RestTemplateUtil.builder()
				.URL(URL + "/consulta?%s")
				.mediaType(MediaType.APPLICATION_JSON)
				.method(HttpMethod.GET)
				.restTemplate(restTemplate)
				.params(request)
				.build();
		
		return (GETVinculoResidenciaMoradorResponseDto) rest.execute(GETVinculoResidenciaMoradorResponseDto.class);
		
	}
	
	public Boolean existeRelacao(VinculoResidenciaRequestDto request) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		
		RestTemplateUtil rest = RestTemplateUtil.builder()
				.URL(URL + "?%s")
				.mediaType(MediaType.APPLICATION_JSON)
				.method(HttpMethod.GET)
				.restTemplate(restTemplate)
				.params(request)
				.build();
		
		return (Boolean) rest.execute(Boolean.class);
		
	}

}
