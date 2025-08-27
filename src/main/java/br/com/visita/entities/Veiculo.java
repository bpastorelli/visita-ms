package br.com.visita.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "veiculo")
public class Veiculo implements Serializable {
	
	private static final long serialVersionUID = 3960436649365666213L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long   id;
	
	@Column(name = "placa", nullable = false)
	private String placa;
	
	@Column(name = "marca", nullable = false)
	private String marca;
	
	@Column(name = "modelo", nullable = false)
	private String modelo;
	
	@Column(name = "cor", nullable = true)
	private String cor;
	
	@Column(name = "ano", nullable = true)
	private Long   ano;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao", nullable = false)
	private Date   dataCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_atualizacao", nullable = false)
	private Date   dataAtualizacao;
	
	@Column(name = "posicao", nullable = false)
	private Long   posicao;

	@Column(name = "guide", nullable = true)
	private String guide;
	
	@OneToMany(mappedBy = "veiculo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)	
	private List<VinculoVeiculo> visitantes;
	
	@PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = new Date();
    }
     
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        this.dataCriacao = atual;
        this.dataAtualizacao = atual;
        this.posicao = 1L;
    }

}
