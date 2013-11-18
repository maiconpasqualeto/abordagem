/**
 * 
 */
package br.com.sixtec.abordagem.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author maicon
 *
 */
@ManagedBean(name="equipeBean")
@SessionScoped
public class EquipeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String equipe;

	public String getEquipe() {
		return equipe;
	}

	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}

}
