package model;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "resultados")
public class Resultado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@OneToOne
	@JoinColumn(name = "captura_id")
	private Captura captura;
	@Column(name="media_cpu")
	private double usoMedioCpu;
	@Column(name="min_cpu")
	private double usoMinCpu;
	@Column(name="max_cpu")
	private double usoMaxCpu;
	@Column(name="cont_msgs")
	private int quantidadeMensagens;
	@Column(name="cont_voos")
	private int quantidadeVoos;
	@Temporal(TemporalType.TIME)
	private Date inicio;
	@Temporal(TemporalType.TIME)
	private Date fim;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Captura getCaptura() {
		return captura;
	}
	public void setCaptura(Captura captura) {
		this.captura = captura;
	}
	public double getUsoMedioCpu() {
		return usoMedioCpu;
	}
	public void setUsoMedioCpu(double usoMedioCpu) {
		this.usoMedioCpu = usoMedioCpu;
	}
	public double getUsoMinCpu() {
		return usoMinCpu;
	}
	public void setUsoMinCpu(double usoMinCpu) {
		this.usoMinCpu = usoMinCpu;
	}
	public double getUsoMaxCpu() {
		return usoMaxCpu;
	}
	public void setUsoMaxCpu(double usoMaxCpu) {
		this.usoMaxCpu = usoMaxCpu;
	}
	public int getQuantidadeMensagens() {
		return quantidadeMensagens;
	}
	public void setQuantidadeMensagens(int quantidadeMensagens) {
		this.quantidadeMensagens = quantidadeMensagens;
	}
	public int getQuantidadeVoos() {
		return quantidadeVoos;
	}
	public void setQuantidadeVoos(int quantidadeVoos) {
		this.quantidadeVoos = quantidadeVoos;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFim() {
		return fim;
	}
	public void setFim(Date fim) {
		this.fim = fim;
	}
	
	
}
