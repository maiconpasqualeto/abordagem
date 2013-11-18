/**
 * 
 */
package br.com.sixtec.abordagem.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.sixtec.abordagem.persistencia.base.AdministradorPersistencia;
import br.com.sixtec.abordagem.persistencia.base.PersistenciaException;

/**
 * @author maicon
 *
 */
public class AbordagemListener implements ServletContextListener {

	/**
	 * 
	 */
	public AbordagemListener() {
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		AdministradorPersistencia.close();

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			AdministradorPersistencia.createEntityManagerFactory("abordagem");
		} catch (PersistenciaException e) {
			Logger.getLogger(AbordagemListener.class).error(e);
		}

	}

}
