/**
 * 
 */
package br.com.sixtec.abordagem.facade;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.sixtec.abordagem.dao.AbordagemDAO;
import br.com.sixtec.abordagem.dao.base.DAOException;
import br.com.sixtec.abordagem.entidades.Abordagem;

/**
 * @author maicon
 *
 */
public class AbordagemFacade {

	private static final Logger log = Logger.getLogger(AbordagemFacade.class);
	
	private static AbordagemFacade facade;
	
	public static AbordagemFacade getInstance(){
		if (facade == null)
			facade = new AbordagemFacade();
		return facade;
	}
	
	public AbordagemFacade() {
	
	}
	
	
	public List<Abordagem> buscarAbordagens(){
		List<Abordagem> abordagens = null;
		try {
			abordagens = AbordagemDAO.getInstance().buscarTodos(Abordagem.class);
		} catch (DAOException e) {
			log.error("Erro ao buscar abordagens", e);
		}
		return abordagens;
	}
	
}
