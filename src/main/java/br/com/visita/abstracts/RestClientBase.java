package br.com.visita.abstracts;



import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import br.com.visita.constants.Constants;
import br.com.visita.errorheadling.FileNameNotFoundException;
import jakarta.persistence.MappedSuperclass;
import lombok.extern.slf4j.Slf4j;

@Slf4j		
@Service
@MappedSuperclass
public abstract class RestClientBase<T, R> {

	protected ResponseEntity<R> post(T body, String url, String apiKey, String token, Class<R> responseType) throws FileNameNotFoundException {
		
		RestClient restClient = RestClient.builder()
				.baseUrl(url)
				.build();
		
		try {
			return restClient.post()
					.uri(uriBuilder -> uriBuilder.build())
						.body(body)
						.contentType(MediaType.APPLICATION_JSON)
						.header(Constants.TOKEN, token)
						.retrieve()
					.onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(), (req, res) -> {
						 String json = new String(res.getBody().readAllBytes());
						 log.error("{} - Erro: {}", res.getStatusCode(), json);
					})
					.onStatus(httpStatusCode -> httpStatusCode.is5xxServerError(), (req, res) -> {
					     String json = new String(res.getBody().readAllBytes());
					     log.error("{} - ERRO: {}", res.getStatusCode(), json);
					})	
					.toEntity(responseType);
			
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP Erro {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao realizar POST: {}", e.getLocalizedMessage(), e);
            throw new RuntimeException("Erro ao processar requisição API", e); // Envolva em uma exceção de runtime
        }
		
	}
	
	protected ResponseEntity<R> get(String url, String apiKey, String token,  Class<R> responseType) throws FileNameNotFoundException {
		
		return get(null, url, apiKey, token, responseType);
	
	}
	
	protected ResponseEntity<R> get(HttpHeaders headers, String url, String apiKey, String token,  Class<R> responseType) throws FileNameNotFoundException {
		
		RestClient restClient = RestClient.builder()
				.baseUrl(url)
				.build();
		
		try {
			return restClient.get()
					.uri(uriBuilder -> uriBuilder
						.build())
						.headers(httpHeaders -> {
							httpHeaders.add(Constants.TOKEN, token);
		                    httpHeaders.addAll(headers);
		                })
						.retrieve()
					.onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(), (req, res) -> {
						 String json = new String(res.getBody().readAllBytes());
						 log.error("{} - Erro {} : {}", res.getStatusCode(), json);
					})
					.onStatus(httpStatusCode -> httpStatusCode.is5xxServerError(), (req, res) -> {
					     String json = new String(res.getBody().readAllBytes());
					     log.error("{} - ERRO {}: {}", res.getStatusCode(), json);
					})	
					.toEntity(responseType);			
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP Erro {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao realizar POST: {}", e.getLocalizedMessage(), e);
            throw new RuntimeException("Erro ao processar requisição API", e); // Envolva em uma exceção de runtime
        }
		
	}
	
}
