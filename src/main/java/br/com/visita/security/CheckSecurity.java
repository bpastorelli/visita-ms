package br.com.visita.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

public @interface CheckSecurity {

	public @interface UsuariosGruposPermissoes {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and "
				+ "@apiSecurity.isAuthenticateUserEquals(#usuarioId)")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface CanChangeOwnerPassword { }
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and (hasAuthority('ROLE_EDITAR_USUARIOS_GRUPOS_PERMISSOES') or "
				+ "@apiSecurity.isAuthenticateUserEquals(#usuarioId))")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface CanEditUser { }

		@PreAuthorize("@apiSecurity.canWriteUsuariosGruposPermissoes()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface CanEdit { }
		

		@PreAuthorize("@apiSecurity.canReadUsuariosGruposPermissoes()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface CanConsult { }
		
	}
	
	public @interface AllCliendIdPermissioes {
		
		@PreAuthorize("@apiSecurity.canReadWriteAndIsAuthenticated()")
		@Target(METHOD)
		public @interface CanReadWriteAndIsAuthenticated { }
		
	}
		
}
