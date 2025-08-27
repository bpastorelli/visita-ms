package br.com.visita.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visita")
public class Visita implements Serializable {

	private static final long serialVersionUID = -5754246207015712520L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long       id;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Visitante visitante;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Column(name = "data_entrada", nullable = false)
	private Date dataEntrada;
	
	@Column(name = "residencia_id")
	private Long residenciaId;
	
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "hora_entrada", nullable = false)
	private Date horaEntrada;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Column(name = "data_saida", nullable = false)
	private Date       dataSaida;
	
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIME)
	@Column(name = "hora_saida", nullable = false)
	private Date       horaSaida;
	
	@Column(name = "placa", nullable = true)
	private String     placa;
	
	@Column(name = "posicao", nullable = false)
	private Integer    posicao;
	
	@Column(name = "guide", nullable = true)
	private String     guide;
	
	@PrePersist
	public void prePersist() {
		
		final Date dataAtual = new Date();
        final Time time = new Time(dataAtual.getTime());
        final int status = 1;
        
        dataEntrada = dataAtual;
        horaEntrada = time;
        
        posicao = status;
        
	}
	
	@PreUpdate
	public void preUpdate() {
		
		final Date dataAtual = new Date();
        final Time time = new Time(dataAtual.getTime());
        final int status = 0;
        
        dataSaida = dataAtual;
        horaSaida = time;
        
        posicao = status;
        
	}
	
}
