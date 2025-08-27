package br.com.visita.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class ApiSecurity {

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public boolean isAutenticated() {
		return getAuthentication().isAuthenticated();
	}
	
	public Long getUsuarioId() {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();

		Object usuarioId = jwt.getClaim("usuario_id");

		if (usuarioId == null) {
			return null;
		}

		return Long.valueOf(usuarioId.toString());
	}
	
	public String getClientId() {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();

		Object clientId = jwt.getClaim("client_id");

		if (clientId == null) {
			return null;
		}

		return String.valueOf(clientId.toString());
	}
	
	public boolean isAuthenticateUserEquals(Long usuarioId) {
		return getUsuarioId() != null && usuarioId != null
				&& getUsuarioId().equals(usuarioId);
	}
	
	public boolean hasAuthority(String authorityName) {
		
		Collection<? extends GrantedAuthority> authorities = getAuthentication().getAuthorities();
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}
	
	public boolean hasWriteScope() {
		return hasAuthority("SCOPE_WRITE");
	}
	
	public boolean hasReadScope() {
		return hasAuthority("SCOPE_READ");
	}
	
	
	public boolean canReadUsuariosGruposPermissoes() {
		return hasReadScope() && hasAuthority("ROLE_CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
	}
	
	public boolean canWriteUsuariosGruposPermissoes() {
		return hasWriteScope() && hasAuthority("ROLE_EDITAR_USUARIOS_GRUPOS_PERMISSOES");
	}
	
	public boolean canReadWriteAndIsAuthenticated() {
		return hasReadScope() && hasWriteScope() && isAutenticated();
	}
	
}
