	
package fr.leroymerlin.jeeapp.core.test;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.junit.internal.runners.InitializationError;

import fr.leroymerlin.fwk.core.configuration.PropertiesConfigLoader;
import fr.leroymerlin.fwk.core.exception.technical.ConfigurationException;

/**
 * Classe permettant le chargement de la configuration Unitils par environnement.
 * 
 * @author jhaeyaert
 */
@SuppressWarnings("deprecation")
public class JeeTestClassRunner extends org.unitils.UnitilsJUnit4TestClassRunner {

	/**
	 * Redéfinition du constucteur.
	 * 
	 * @param testClass
	 *            la classe testée.
	 * @throws InitializationError
	 *             levée si une exception survient lors du chargement de la configuration Unitils.
	 */
	public JeeTestClassRunner(Class<?> testClass) throws InitializationError {

		super(testClass);

		try {
			// Récupération de la configuration.
			CombinedConfiguration unitilsProperties = PropertiesConfigLoader.loadConfigurationFromEnv("unitils.properties", "jeeapp-jee",
					false, false);

			// Pousse toutes les propriétés dans les propriétés système (seul et unique moyen pour les transmettre à Unitils).
			System.getProperties().putAll(ConfigurationConverter.getProperties(unitilsProperties));
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
		 
	
}
