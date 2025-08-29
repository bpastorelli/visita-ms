package br.com.visita.security.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.visita.errorheadling.RegistroException;
import br.com.visita.errorheadling.RegistroExceptionHandler;
import br.com.visita.response.Response;
import br.com.visita.security.dto.JwtAuthenticationDto;
import br.com.visita.security.dto.TokenDto;
import br.com.visita.security.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/sgc/token")
@CrossOrigin(origins = "*")
public class AuthenticationController extends RegistroExceptionHandler {
	
	@Autowired
	private AuthenticationService authenticationService;

	/**
	 * Gera e retorna um novo token JWT.
	 * 
	 * @param authenticationDto
	 * @param result
	 * @return ResponseEntity<Response<TokenDto>>
	 * @throws AuthenticationException
	 * @throws RegistroException 
	 */
	@PostMapping
	public ResponseEntity<?> gerarTokenJwt(
			@Valid @RequestBody JwtAuthenticationDto authenticationDto)
			throws RegistroException, IOException {
		Response<TokenDto> response = new Response<TokenDto>();
		
		log.info("Autenticando...");
		
		response.setData(this.authenticationService.atenticar(authenticationDto));

		return ResponseEntity.ok(response.getData());
	}

	/**
	 * Gera um novo token com uma nova data de expiração.
	 * 
	 * @param request
	 * @return ResponseEntity<Response<TokenDto>>
	 * @throws RegistroException 
	 */
	@PostMapping(value = "/refresh")
	public ResponseEntity<?> gerarRefreshTokenJwt(
			@RequestParam(defaultValue = "null") String email, @RequestParam(defaultValue = "null") String token) throws RegistroException {
		
		log.info("Gerando refresh token JWT.");
		
		Response<TokenDto> response = new Response<TokenDto>();
		
		response.setData(this.authenticationService.refreshToken(email, token));
		
		return ResponseEntity.ok(response.getData());
	}

}
