package br.com.visita.errorheadling;

import javax.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileNameNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public FileNameNotFoundException(String mensagem) {
		super(mensagem);
		log.warn(mensagem);
	}
	
}
