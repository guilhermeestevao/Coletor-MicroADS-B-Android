package model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "voos")
public class Voo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String icao;
	@ManyToOne 
	@JoinColumn(name = "captura_id")
	private Captura captura;
	@OneToMany(mappedBy = "voo", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
	private List<Mensagens> mensagens;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIcao() {
		return icao;
	}
	public void setIcao(String icao) {
		this.icao = icao;
	}
	public List<Mensagens> getMensagens() {
		return mensagens;
	}
	public void setMensagens(List<Mensagens> mensagens) {
		this.mensagens = mensagens;
	}
	public Captura getCaptura() {
		return captura;
	}
	public void setCaptura(Captura captura) {
		this.captura = captura;
	}

}
