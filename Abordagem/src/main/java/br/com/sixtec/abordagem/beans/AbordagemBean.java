/**
 * 
 */
package br.com.sixtec.abordagem.beans;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import br.com.sixtec.abordagem.dao.AbordagemDAO;
import br.com.sixtec.abordagem.entidades.Abordagem;

/**
 * @author maicon
 *
 */
@ManagedBean(name="abordagemBean")
@RequestScoped
public class AbordagemBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(AbordagemBean.class);
		
	private Abordagem a;
	
	@ManagedProperty(value="#{param.equipe}")
	private String equipe;
	
	@ManagedProperty(value="#{equipeBean}")
	private EquipeBean equipeBean;
	
	
	public AbordagemBean() {
	}
		
	public Abordagem getA() {
		return a;
	}

	public void setA(Abordagem a) {
		this.a = a;
	}
	
	public String getEquipe() {
		return equipe;
	}

	public void setEquipe(String equipe) {
		this.equipe = equipe != null ? equipe.toUpperCase() : equipe;
	}

	public EquipeBean getEquipeBean() {
		return equipeBean;
	}

	public void setEquipeBean(EquipeBean equipeBean) {
		this.equipeBean = equipeBean;
	}

	@PostConstruct
	public void setDadosIniciais(){
		a = new Abordagem();
		//a.setEquipe(equipe);
		a.setDataAbordagem(new Date());	
		if (equipeBean.getEquipe() == null || 
				(equipe != null && !equipeBean.getEquipe().equals(equipe)) ) {
			equipeBean.setEquipe(equipe);
		}
	}

	public String validaInicio(){
		try {
			if (!validaEquipe(a.getEquipe())){
				FacesContext.getCurrentInstance().addMessage(
						"Erro", 
						new FacesMessage(FacesMessage.SEVERITY_WARN, 
								"A Equipe: '" + a.getEquipe() + "' é inválida. Equipes válidas são: A1, A2, A3, B1, B2, B3, C1, C2, C3", "Erro"));
				return "inicio.xhtml";
			}
			
			Abordagem abordagem = 
					AbordagemDAO.getInstance().buscarAbordagemPorPlacaEData(
							a.getPlaca(), a.getDataAbordagem());
			if (abordagem == null) { // adiciona Abordagem
				AbordagemDAO.getInstance().adicionar(a);
				//a.setEquipe(equipe);
				abordagem = a;
			}
			a.setDaems(abordagem.getDaems());
			a.setValorICMS(abordagem.getValorICMS());
			a.setValorMulta(abordagem.getValorMulta());
		} catch (Exception e) {
			log.error("Erro ao validar abordagem.", e);
			FacesContext.getCurrentInstance().addMessage("geral",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao validar abordagem", "Erro ao validar abordagem"));
		}
		return "abordagem.xhtml";
	}
	
	public void salvarInicial(){
		try {
			a.setId(null);
			a.setPlaca(a.getPlaca().toUpperCase());
			
			// verifica se já existe a placa
			Abordagem abordagem = 
					AbordagemDAO.getInstance().buscarAbordagemPorPlacaEData(
							a.getPlaca(), a.getDataAbordagem());
			if (abordagem != null) {
				FacesContext.getCurrentInstance().addMessage(
					"Erro", 
					new FacesMessage(FacesMessage.SEVERITY_WARN, 
							"A placa '" + a.getPlaca() + "' já foi adicionada hoje. Para editar clique em 'Continuar'", "Erro"));
				return;
			}
			AbordagemDAO.getInstance().adicionar(a);
			FacesContext.getCurrentInstance().addMessage(
					"Registro adicionado", new FacesMessage("Registro adicionado. Placa: " + a.getPlaca()));
			
			a.setPlaca(null);
		} catch (Exception e) {
			log.error("Erro ao salvar abordagem.", e);
			FacesContext.getCurrentInstance().addMessage("geral",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao alterar abordagem", "Erro ao alterar abordagem"));
		}
		
	}
	
	public String salvarAbordagem(){
		try {
			// salva
			AbordagemDAO.getInstance().atualizaAbordagem(
					a.getPlaca(), a.getDataAbordagem(), a.getDaems(), a.getValorICMS(), a.getValorMulta());
			
			FacesContext.getCurrentInstance().addMessage("geral",
					new FacesMessage("Dados da placa: " + a.getPlaca() + " foram salvos.", "Registro Alterado."));
			
			// zera valores
			a.setPlaca(null);
			a.setNumDocs(null);
			a.setDaems(null);
			a.setValorMulta(null);
			a.setValorICMS(null);
			
		} catch (Exception e) {
			log.error("Erro ao salvar abordagem.", e);
			FacesContext.getCurrentInstance().addMessage("geral",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar abordagem", "Erro ao salvar abordagem"));
		}
		return "inicio.xhtml?equipe="+ a.getEquipe();
	}
	
	
	private boolean validaEquipe(String equipe){
		if (equipe == null || equipe.isEmpty())
			return true;
		if (!equipe.equals("A1") ||
			!equipe.equals("A2") ||
			!equipe.equals("A3") ||
			!equipe.equals("B1") ||
			!equipe.equals("B2") ||
			!equipe.equals("B3") ||
			!equipe.equals("C1") ||
			!equipe.equals("C2") ||
			!equipe.equals("C3")) {
			return true;
		}
		return false;
	}
		
	public String salvarNovaAbordagem(){
		try {
			// verifica se já existe a placa
			Abordagem abordagem = 
					AbordagemDAO.getInstance().buscarAbordagemPorPlacaEData(
							a.getPlaca(), a.getDataAbordagem());
			
			if (abordagem != null) { // abordagem já existe
				// atualiza valores
				AbordagemDAO.getInstance().atualizaAbordagem(
						a.getPlaca(), a.getDataAbordagem(), a.getDaems(), a.getValorICMS(), a.getValorMulta());
				
				FacesContext.getCurrentInstance().addMessage("geral",
						new FacesMessage("A placa: " + a.getPlaca() + " já existia, os dados foram alterados.", "Registro Alterado."));
			
			} else { // adiciona Abordagem
				a.setEquipe(equipeBean.getEquipe());
				AbordagemDAO.getInstance().adicionar(a);
				
				FacesContext.getCurrentInstance().addMessage("geral",
						new FacesMessage("Registro adicionado. Placa: " + a.getPlaca() + ".", "Registro Adicionado."));
			}
			
			// zera valores
			a.setPlaca(null);
			a.setNumDocs(null);
			a.setDaems(null);
			a.setValorMulta(null);
			a.setValorICMS(null);
			
		} catch (Exception e) {
			log.error("Erro ao salvar abordagem.", e);
			FacesContext.getCurrentInstance().addMessage("geral",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar abordagem", "Erro ao salvar abordagem"));
		}
		return "abordagem.xhtml";
	}

}
