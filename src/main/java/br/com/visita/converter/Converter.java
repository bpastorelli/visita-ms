package br.com.visita.converter;

public interface Converter<T, Z> {
	
	T convert(Z object);

}
