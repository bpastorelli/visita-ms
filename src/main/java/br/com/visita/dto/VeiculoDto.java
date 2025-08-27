package br.com.visita.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoDto implements Serializable {
	

	private static final long serialVersionUID = 1423232434532327L;
	
	private Long id;
	
	@Size(min = 7, max = 8, message = "A placa deve ter entre 7 e 8 caracteres.")
	@NotNull(message = "Campo Placa é obrigatório!")
	private String placa;
	
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "A marca contém caracteres inválidos.")
	private String marca;
	
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "O modelo contém caracteres inválidos.")
	@NotNull(message = "O campo Modelo é obrigatório!")
	private String modelo;
	
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "A cor contém caracteres inválidos.")
	@NotNull(message = "O campo Cor é obrigatório!")
	private String cor;
	
	@Pattern(regexp = "^[0-9]*$", message = "O ano contém caracteres inválidos.")
	private Long   ano;
	
	@Pattern(regexp = "^[0-9]*$", message = "O ano contém caracteres inválidos.")
	@NotNull(message = "O campo visitante é obrigatório!")
	private Long   visitanteId;
	
	private Long posicao;
	
	private String guide;
	
	private String ticketVisitante;

}
