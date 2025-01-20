package br.com.visita.utils;

public interface RestTemplateInterface<T, O> {
	
	public T restTemplate(O clazz);

}
