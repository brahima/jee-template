package fr.leroymerlin.jeeapp.core.services.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.leroymerlin.fwk.ws.authentication.AuthenticatorService;

/**
 * Service d'authentification des utilisateurs de WebServices.
 * 
 * @author eperu, jhaeyaert
 */
@Service("jeeapp-spring.authenticatorService")
public class JeeAuthenticatorServiceImpl implements AuthenticatorService {

	/**
	 * Logger par défaut de la classe.
	 */
	private static final Log LOGGER = LogFactory.getLog(JeeAuthenticatorServiceImpl.class);

	/**
	 * Constructeur par défaut.
	 */
	public JeeAuthenticatorServiceImpl() {

		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAuthenticated(String username, String password) {

		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Authentification de l'utilisateur [%s] avec le mot de passe [*****].", String.valueOf(username)));
			}

			return true;
		} catch (Exception e) {
			LOGGER.error(String.format("Authentification échouée pour l'utilisateur [%s].", username), e);
		}

		return false;
	}
}
