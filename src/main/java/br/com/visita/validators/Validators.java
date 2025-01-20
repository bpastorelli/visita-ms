package br.com.visita.validators;

import java.util.List;

import br.com.visita.errorheadling.RegistroException;

public interface Validators<T, X> {
	
	void validarPost(T dto) throws RegistroException;
	
	void validarPost(List<T> listDto) throws RegistroException;
	
	void validarPut(X dto) throws RegistroException;
	
	void validarPut(List<X> listDto) throws RegistroException;
}
