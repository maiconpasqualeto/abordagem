/**
 * 
 */
package br.com.sixtec.abordagem.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.faces.FacesException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.sixtec.abordagem.dao.base.BridgeBaseDAO;
import br.com.sixtec.abordagem.dao.base.HibernateBaseDAOImp;
import br.com.sixtec.abordagem.entidades.Abordagem;
import br.com.sixtec.abordagem.persistencia.base.AdministradorPersistencia;

/**
 * @author maicon
 *
 */
public class AbordagemDAO extends BridgeBaseDAO {
	
	private static final Logger log = Logger.getLogger(AbordagemDAO.class);
	
	private static AbordagemDAO dao;
	
	public static AbordagemDAO getInstance(){
		if (dao == null)
			dao = new AbordagemDAO();
		return dao;
	}

	/**
	 * 
	 */
	public AbordagemDAO() {
		super(new HibernateBaseDAOImp());
	}
	
	public Abordagem buscarAboradgemPorPlaca(String placa) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		Abordagem a = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select a from Abordagem a ");
			hql.append("where a.placa = :placa ");
			TypedQuery<Abordagem> q = em.createQuery(hql.toString(), Abordagem.class);
			q.setParameter("placa", placa.toUpperCase());
			
			a = q.getSingleResult();
						
		} catch (NoResultException e) {
			// não retornou nenhum objeto não faz nada
		} catch (Exception e) {
			log.error("Erro ao buscar abordagem por placa", e);
			throw new FacesException(e);
		} finally {
			em.close();
		}
		return a;
	}
	
	public List<Abordagem> buscarAbordagensPorData(Date dataInicio, Date dataFim) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		List<Abordagem> abordagens = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select a from Abordagem a ");
			hql.append("where a.dataAbordagem >= :dataInicio and a.dataAbordagem <= :dataFim ");
			hql.append("order by a.dataAbordagem ");
			TypedQuery<Abordagem> q = em.createQuery(hql.toString(), Abordagem.class);
			q.setParameter("dataInicio", dataInicio);
			q.setParameter("dataFim", dataFim);
			
			abordagens = q.getResultList();
						
		} catch (Exception e) {
			log.error("Erro ao buscar abordagem por data", e);
			throw new FacesException(e);
		} finally {
			em.close();
		}
		return abordagens;
	}
	
	public List<Abordagem> buscarAbordagensPorDataEquipe(Date dataInicio, Date dataFim, String equipe) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		List<Abordagem> abordagens = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select a from Abordagem a ");
			hql.append("where a.dataAbordagem >= :dataInicio and a.dataAbordagem <= :dataFim ");
			hql.append("and a.equipe = :equipe ");
			hql.append("order by a.dataAbordagem ");
			TypedQuery<Abordagem> q = em.createQuery(hql.toString(), Abordagem.class);
			q.setParameter("dataInicio", dataInicio);
			q.setParameter("dataFim", dataFim);
			q.setParameter("equipe", equipe);
			
			abordagens = q.getResultList();
						
		} catch (Exception e) {
			log.error("Erro ao buscar abordagem por data e equipe", e);
			throw new FacesException(e);
		} finally {
			em.close();
		}
		return abordagens;
	}
	
	public Abordagem buscarAbordagemPorPlacaEData(String placa, Date dataAbordagem) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		Abordagem a = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select a from Abordagem a ");
			hql.append("where a.placa = :placa and a.dataAbordagem = :dataAbordagem");
			TypedQuery<Abordagem> q = em.createQuery(hql.toString(), Abordagem.class);
			q.setParameter("placa", placa);
			q.setParameter("dataAbordagem", dataAbordagem);		
			
			a = q.getSingleResult();
						
		} catch (NoResultException e) {
			// não retornou nenhum objeto não faz nada
		} catch (Exception e) {
			log.error("Erro ao buscar abordagem por placa", e);
			throw new FacesException(e);
		} finally {
			em.close();
		}
		return a;
	}
	
	public Abordagem buscarAbordagemPorPlacaDataEquipe(String placa, Date dataAbordagem, String equipe) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		Abordagem a = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select a from Abordagem a ");
			hql.append("where a.placa = :placa and a.dataAbordagem = :dataAbordagem ");
			if (equipe == null)
				hql.append("and a.equipe is null");
			else
				hql.append("and a.equipe = :equipe");
			TypedQuery<Abordagem> q = em.createQuery(hql.toString(), Abordagem.class);
			q.setParameter("placa", placa);
			q.setParameter("dataAbordagem", dataAbordagem);
			if (equipe != null)
				q.setParameter("equipe", equipe);
			
			q.setMaxResults(1);
			
			a = q.getSingleResult();
						
		} catch (NoResultException e) {
			// não retornou nenhum objeto não faz nada
		} catch (Exception e) {
			log.error("Erro ao buscar abordagem por placa", e);
			throw new FacesException(e);
		} finally {
			em.close();
		}
		return a;
	}
	
	public void atualizaAbordagem(String placa, Date dataAbordagem,
			String equipe, String daems, BigDecimal valorIcms, 
			BigDecimal valorMulta, Integer numDocs) {
		
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			
			StringBuilder hql = new StringBuilder();
			hql.append("update Abordagem a ");
			hql.append("set a.daems=:daems, a.valorICMS=:valorICMS, a.valorMulta=:valorMulta, a.numDocs=:numDocs ");
			hql.append("where a.placa = :placa and a.dataAbordagem = :dataAbordagem ");
			if (equipe == null)
				hql.append("and a.equipe is null ");
			else
				hql.append("and a.equipe = :equipe ");
			
			Query q = em.createQuery(hql.toString());
			
			q.setParameter("daems", daems.isEmpty() ? null : daems);
			q.setParameter("valorICMS", valorIcms);
			q.setParameter("valorMulta", valorMulta);
			
			q.setParameter("placa", placa.toUpperCase());
			q.setParameter("dataAbordagem", dataAbordagem);	
			q.setParameter("numDocs", numDocs);	
			
			if (equipe != null)
				q.setParameter("equipe", equipe);
			
			q.executeUpdate();
						
			t.commit();
		} catch (Exception e) {
			t.rollback();
			log.error("Erro ao alterar abordagem", e);
			throw new FacesException(e);
		} finally {
			em.close();
		}		
	}

}
