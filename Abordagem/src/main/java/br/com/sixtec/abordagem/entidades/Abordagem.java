/**
 * 
 */
package br.com.sixtec.abordagem.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author maicon
 *
 */
@Entity
@Table(name="abordagem")
public class Abordagem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seqAbordagem", sequenceName="abordagem_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqAbordagem")
	@Column(name="id")	
	private Long id;
	
	@Column(name="data_abordagem")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAbordagem;
	
	@Column(name="placa")
	private String placa;
	
	@Column(name="num_docs")
	private Integer numDocs;
	
	@Column(name="daems")
	private String daems;
	
	@Column(name="valor_icms")
	private BigDecimal valorICMS;
	
	@Column(name="valor_multa")
	private BigDecimal valorMulta;
	
	@Column(name="equipe")
	private String equipe;

	/**
	 * 
	 */
	public Abordagem() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataAbordagem() {
		return dataAbordagem;
	}

	public void setDataAbordagem(Date dataAbordagem) {
		this.dataAbordagem = dataAbordagem;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = (placa != null) ? placa.toUpperCase() : placa;
	}

	public String getDaems() {
		return daems;
	}

	public void setDaems(String daems) {
		this.daems = daems;
	}

	public BigDecimal getValorICMS() {
		return valorICMS;
	}

	public void setValorICMS(BigDecimal valorICMS) {
		this.valorICMS = valorICMS;
	}

	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}
	
	public String getEquipe() {
		return equipe;
	}

	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}

	public Integer getNumDocs() {
		return numDocs;
	}

	public void setNumDocs(Integer numDocs) {
		this.numDocs = numDocs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Abordagem other = (Abordagem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
