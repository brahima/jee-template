package fr.leroymerlin.jeeapp.core.dao.hibernate;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.leroymerlin.fwk.dao.DAOJPAHibernate;

/**
 * Implémentation Hibernate du projet.
 * 
 * @author eperu, jhaeyaert
 * 
 * @param <Type>
 *            Entity's type this instance has to manage.
 * @param <IdType>
 *            Entity identifier's type this instance has to manage.
 */
public abstract class JeeDAOJPAHibernate<Type, IdType extends Serializable> extends DAOJPAHibernate<Type, IdType> {

	/**
	 * The {@link EntityManager} use to manage entities.
	 * <p>
	 * The {@link EntityManager} is injected by container (Spring, Application Server, ...).
	 */
	@PersistenceContext(unitName = "jeeapp-spring.persistenceUnit")
	protected EntityManager em;

	/**
	 * {@link DAOJPAHibernate#DAOJPAHibernate(Class)}.
	 * 
	 * @param clazz
	 *            une classe de la couche modele de l'application.
	 */
	public JeeDAOJPAHibernate(Class<Type> clazz) {
		super(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EntityManager getEntityManager() {
		return this.em;
	}

}
