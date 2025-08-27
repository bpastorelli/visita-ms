package br.com.visita.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDto implements Serializable {
	
	private static final long serialVersionUID = 183293829392L;
	
	private String userQuery;

}
