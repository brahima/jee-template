package fr.leroymerlin.jeeapp.core.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Statistics;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import fr.leroymerlin.fwk.test.mock.MockFactory;
import fr.leroymerlin.fwk.utils.spring.ApplicationContextHolder;

/**
 * Classe abstraire de test utilisée comme classe mère de tous les tests pour chargé la configuration Spring.
 * 
 * @author eperu, jhaeyaert
 */
@SpringApplicationContext(value = { "spring/context-jeeapp-jee-test.xml" })
@Transactional(TransactionMode.DISABLED)
@Ignore
@RunWith(value = JeeTestClassRunner.class)
public abstract class GlobalTestCase extends UnitilsJUnit4 {

	/**
	 * Logger par défaut de la classe.
	 */
	private static final Log LOGGER = LogFactory.getLog(GlobalTestCase.class);

	/**
	 * La configuration par environnement.
	 */
	@SpringBean("jeeapp-spring.applicationConfig")
	protected Configuration applicationConfig;

	/**
	 * Mock template jms.
	 */
	@Mock
	protected JmsTemplate mockXaJmsTemplate;

	/**
	 * Factory permettant la création d'un entity manager.
	 */
	@SpringBean("jeeapp-spring.entityManagerFactory")
	private EntityManagerFactory emf;

	/**
	 * L'entity manager courant.
	 */
	private EntityManager em;

	/**
	 * Flag indiquant si l'entity manager a été créé pour l'exécution du test ou si un entity manager déjà existant a été récupéré.
	 */
	private boolean isNewEm;

	/**
	 * Constructeur par défaut.
	 */
	public GlobalTestCase() {

		super();
	}

	/**
	 * Positionne les mocks par défaut.<br>
	 * Les mocks suivants sont positionnés :
	 * <ul>
	 * <li>depositService.onLineTillHelper</li>
	 * </ul>
	 * 
	 * @throws Exception
	 *             levée si une exception survient lors de la mise en place des mocks.
	 */
	@Before
	public void setDefaultMocks() throws Exception {

		MockitoAnnotations.initMocks(this);
 
	}

	/**
	 * Ouvre l'entity manager au début de chaque test afin de permettre le chargement des associations lazy.
	 */
	@Before
	public void openEM() {

		// Récupération de l'entity manager si existant.
		this.em = EntityManagerFactoryUtils.getTransactionalEntityManager(this.emf);
		this.isNewEm = false;

		if (this.em == null) {
			// Pas d'entity manager existant, création.
			this.em = this.emf.createEntityManager();

			// Positionne le flag pour indiquer la création d'un nouvel entity manager.
			this.isNewEm = true;

			// Associe l'entity manager au Thread courant.
			TransactionSynchronizationManager.bindResource(this.emf, new EntityManagerHolder(this.em));
		}
	}

 

	/**
	 * Ferme l'entity manager à la fin de chaque test.
	 */
	@After
	public void closeEM() {

		// Uniquement dans le cas ou un entity manager a été créé.
		if (this.isNewEm) {
			// Dissocie l'entity manager du Thread courant.
			TransactionSynchronizationManager.unbindResource(this.emf);

			// Ferme l'entity manager.
			EntityManagerFactoryUtils.closeEntityManager(this.em);
		}
	}

	/**
	 * Permet d'afficher les statistiques ehcache alimenté tout au long de la classe de test.
	 */
	// @AfterClass
	public static void displayCacheStatistics() {

		CacheManager cacheManager = ApplicationContextHolder.getBean("jeeapp-spring.cacheManager");

		if (cacheManager != null) {
			if (LOGGER.isDebugEnabled()) {
				Ehcache[] caches = new Ehcache[] { cacheManager.getEhcache("entities"), cacheManager.getEhcache("entities.collections") };

				for (Ehcache ehcache : caches) {
					Statistics statistics = ehcache.getStatistics();
					LOGGER.debug(String.format("Cache\t: %s", ehcache.getName()));
					LOGGER.debug(String.format("stats enabled\t: %s", ehcache.isStatisticsEnabled()));
					LOGGER.debug(String.format("Count\t: %s", statistics.getObjectCount()));
					LOGGER.debug(String.format("Hits\t: %s", statistics.getCacheHits()));
					LOGGER.debug(String.format("Misses\t: %s", statistics.getCacheMisses()));
				}
			}
		}
	}
}