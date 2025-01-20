package br.com.visita.validators.impl;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.visita.dto.EncerraVisitaDto;
import br.com.visita.dto.QueryResidenciaResponseDto;
import br.com.visita.dto.ResidenciaRequestDto;
import br.com.visita.dto.VisitaDto;
import br.com.visita.entities.Veiculo;
import br.com.visita.entities.Visita;
import br.com.visita.entities.Visitante;
import br.com.visita.errorheadling.ErroRegistro;
import br.com.visita.errorheadling.RegistroException;
import br.com.visita.repositories.VeiculoRepository;
import br.com.visita.repositories.VisitaRepository;
import br.com.visita.repositories.VisitanteRepository;
import br.com.visita.senders.ResidenciaSender;
import br.com.visita.utils.Utils;
import br.com.visita.validators.Validators;

@Component
public class ValidarCadastroVisita implements Validators<VisitaDto, EncerraVisitaDto> {

	@Autowired
	private VisitanteRepository visitanteRepository;
	
	@Autowired
	private ResidenciaSender residenciaSender;
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	private static final String TITULO = "Cadastro de visita recusado!";
	
	@Override
	public void validarPost(VisitaDto t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		Optional<Veiculo> veiculo = null;
		Optional<Visitante> visitante = null;
		
		t.setPlaca(t.getPlaca().replace("-", ""));
		
		if(t.getRg().equals(""))
			errors.getErros().add(new ErroRegistro("", TITULO, " Você deve infomar o RG do visitante" ));
		
		//Valida se o visitante existe por RG ou CPF
		if(t.getRg() != null) {			
			visitante = visitanteRepository.findByRg(t.getRg());
			if(!visitante.isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " Visitante não encontrado para o RG " + t.getRg() + "!"));				
		}
		
		//Valida se a residência existe
		ResidenciaRequestDto request = ResidenciaRequestDto.builder()
				.id(t.getResidenciaId().toString())
				.build();
		QueryResidenciaResponseDto residencia = null;
		try {
			residencia = residenciaSender.buscarResidenciasPorFiltro(request);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if(residencia.getResidencias().size() == 0)
			errors.getErros().add(new ErroRegistro("", TITULO, " Código de residência " + t.getResidenciaId() + " inexistente"));
		
		//Valida se já existem visitas não encerradas.
		if(errors.getErros().size() == 0) {
			
			List<Visita> listVisitas = new ArrayList<Visita>();			
			listVisitas = visitaRepository.findByPosicaoAndVisitanteRgAndVisitanteNomeContaining(1, visitante.get().getRg(), visitante.get().getNome());
			
			if(listVisitas.size() > 0) {
				errors.getErros().add(new ErroRegistro("", TITULO, " Este visitante já possui " + listVisitas.size() + " registro(s) ativo(s) de entrada!" ));	
			}
		}
		
		veiculo = veiculoRepository.findByPlaca(t.getPlaca().replace("-", ""));
		
		if(!t.getPlaca().equals("") && !Optional.ofNullable(t.getVeiculoVisita()).isPresent()) {
			if(!veiculo.isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " Veiculo não cadastrado. É necessário informar os dados cadastrais do veículo!" ));
		}
		
		//Validação dos campos de marca e modelo do veiculo
		if(t.getPlaca() != null && t.getPlaca() != "" && !veiculo.isPresent() && t.getVeiculoVisita() != null) {
				
			if(t.getVeiculoVisita().getMarca().isEmpty()) {
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo Marca é obrigatório!"));	
			}
				
			if(t.getVeiculoVisita().getModelo().isEmpty()) {
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo Modelo é obrigatório!"));	
			}
			
			if(t.getVeiculoVisita().getCor().isEmpty()) {
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo Cor é obrigatório!"));	
			}
				
		}else if(t.getVeiculoVisita() != null) {
			
			if(t.getVeiculoVisita().getMarca().isEmpty()) {
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo Marca é obrigatório!"));	
			}
				
			if(t.getVeiculoVisita().getModelo().isEmpty()) {
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo Modelo é obrigatório!"));	
			}
			
			if(t.getVeiculoVisita().getCor().isEmpty()) {
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo Cor é obrigatório!"));	
			}
			
			if(t.getVeiculoVisita().getAno() == null) {
				errors.getErros().add(new ErroRegistro("", TITULO, " O campo Ano é obrigatório!"));	
			}
			
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPut(EncerraVisitaDto t) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		if(t.getId() != null || t.getId() != 0) {
			Optional<Visita> visita = visitaRepository.findById(t.getId());
			
			if(!visita.isPresent())
				errors.getErros().add(new ErroRegistro("", TITULO, " Visita não encontrada!"));
			else {
				if(visita.get().getPosicao() == 0) 
					errors.getErros().add(new ErroRegistro("", TITULO, " Esta visita já foi encerrada em " + Utils.dateFormat(visita.get().getDataSaida(), "dd/MM/yyyy") + " às " + new Time(visita.get().getDataSaida().getTime()) + "!"));					
			}
		
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPost(List<VisitaDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validarPut(List<EncerraVisitaDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

}
