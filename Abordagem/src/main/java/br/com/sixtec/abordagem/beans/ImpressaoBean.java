/**
 * 
 */
package br.com.sixtec.abordagem.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.sixtec.abordagem.dao.AbordagemDAO;
import br.com.sixtec.abordagem.entidades.Abordagem;

/**
 * @author maicon
 *
 */
@ManagedBean(name="impressaoBean")
@ViewScoped
public class ImpressaoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(ImpressaoBean.class);

	/* impressão */
	private Date dataInicio = new Date();
	
	private Date dataFim = new Date();
	
	private String equipe;
	
	private List<Abordagem> abordagensReport = new ArrayList<Abordagem>();
	
	public ImpressaoBean() {
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<Abordagem> getAbordagensReport() {
		return abordagensReport;
	}

	public void setAbordagensReport(List<Abordagem> abordagensReport) {
		this.abordagensReport = abordagensReport;
	}
	
	public void imprimir(){
		if (equipe == null || equipe.isEmpty())
			abordagensReport = 
				AbordagemDAO.getInstance().buscarAbordagensPorData(dataInicio, dataFim);		
		else
			abordagensReport = 
				AbordagemDAO.getInstance().buscarAbordagensPorDataEquipe(dataInicio, dataFim, equipe);
	}

	public String getEquipe() {
		return equipe;
	}

	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}
	
	public StreamedContent printReport() throws JRException, IOException, ClassNotFoundException, SQLException {  
		
		InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		ServletContext contextS = (ServletContext) externalContext.getContext();
		String arquivo = contextS.getRealPath("/resources/reports/jasper/abordagem_report.jasper");
		
		try {
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("equipe", equipe);
			parametros.put("DataInicio", dataInicio);
			parametros.put("DataFim", dataFim);
			
			JasperPrint print = JasperFillManager.fillReport(
					new FileInputStream(new File(arquivo)), parametros, getConnection());
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			is = new ByteArrayInputStream(baos.toByteArray());
		
		} catch (JRException e) {
			log.error("Erro ao gerar relatório jasper", e);
		} catch (IOException e) {
			log.error("Erro ao gerar relatório jasper", e);
		}
		
		return new DefaultStreamedContent(is, "application/pdf", "abordagem.pdf");
		
    }  
	
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName( "org.postgresql.Driver" );
		String url = "jdbc:postgresql:abordagem";
		Properties props = new Properties( );
		props.setProperty( "user", "postgres" );
		//props.setProperty( "password", "postgres" );
		props.setProperty( "password", "rs232c" );
		return DriverManager.getConnection( url, props );
	}
	

}
