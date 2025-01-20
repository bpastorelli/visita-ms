package br.com.visita.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeiculoFilter {

	private Long id;
	
	private String placa;
	
	private String marca;
	
	private String modelo;
	
	private String cor;
	
	private Long ano;
	
	private String guide;
	
	private Long posicao;
	
	private boolean content;
	
}
