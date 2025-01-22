package br.com.visita.validators.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.visita.dto.AtualizaVeiculoDto;
import br.com.visita.dto.VeiculoDto;
import br.com.visita.entities.Visitante;
import br.com.visita.errorheadling.ErroRegistro;
import br.com.visita.errorheadling.RegistroException;
import br.com.visita.repositories.VeiculoRepository;
import br.com.visita.repositories.VisitanteRepository;
import br.com.visita.validators.Validators;

@Component
public class ValidarCadastroVeiculo implements Validators<VeiculoDto, AtualizaVeiculoDto> {

	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private VisitanteRepository visitanteRepository;
	
	private static final String TITULO = "Cadastro de veículo recusado!";
	
	@Override
	public void validarPost(VeiculoDto t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		/*if(t.getTicketVisitante() == null && t.getVisitanteId() == null)
			errors.getErros().add(new ErroRegistro("", TITULO, " Visitante responsável não informado!"));
		
		if(t.getTicketVisitante() == "" && t.getVisitanteId() == null)
			errors.getErros().add(new ErroRegistro("", TITULO, " Visitante responsável não informado!"));*/
		
		if(t.getTicketVisitante() != "" && t.getTicketVisitante() != null) {
			Optional<Visitante> visitante = this.visitanteRepository.findByGuide(t.getTicketVisitante());
			if(!visitante.isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " O visitante informado não existe!"));
			else {
				t.setVisitanteId(visitante.get().getId());
			}
		}
		
		if(t.getVisitanteId() != null) {
			if(!this.visitanteRepository.findById(t.getVisitanteId()).isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " O visitante informado (" + t.getVisitanteId() + ") não existe!"));			
		}
		
		if(t.getPlaca() == null) 
			errors.getErros().add(new ErroRegistro("", TITULO, " Não existem veículos para cadastro!"));
		
		this.veiculoRepository.findByPlaca(t.getPlaca().replace("-", "")).
			ifPresent(res -> errors.getErros().add(new ErroRegistro("", TITULO, "A placa informada (" + t.getPlaca() + ") já existe para o veiculo id " + res.getId() + "!") ));
		
		t.setPlaca(t.getPlaca().replace("-", ""));
		t.setPlaca(t.getPlaca().toUpperCase());
		t.setMarca(t.getMarca().toUpperCase());
		t.setModelo(t.getModelo().toUpperCase());
		t.setCor(t.getCor().toUpperCase());
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPut(AtualizaVeiculoDto dto) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPost(List<VeiculoDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPut(List<AtualizaVeiculoDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

}
