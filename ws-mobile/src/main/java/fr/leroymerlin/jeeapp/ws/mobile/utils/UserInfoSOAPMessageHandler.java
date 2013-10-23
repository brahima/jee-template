package fr.leroymerlin.jeeapp.ws.mobile.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handler des messages SOAP pour positionner les informations utilisateurs.
 * 
 */
public class UserInfoSOAPMessageHandler implements SOAPHandler<SOAPMessageContext> {

	/**
	 * Namespace du ws à appeler.
	 */
	private static final String  NAMESPACE = "http://xxx.leroymerlin.fr";

	/**
	 * Identifiant de l'attribut "userInfo".
	 */
	private static final String USER_INFO_LOCAL_PART = "userInfo";

	/**
	 * Identifiant de l'attribut "id".
	 */
	private static final String ID_LOCAL_PART = "id";

	/**
	 * Identifiant de l'attribut "firstname".
	 */
	private static final String FIRSTNAME_LOCAL_PART = "firstname";

	/**
	 * Identifiant de l'attribut "lastname".
	 */
	private static final String LASTNAME_LOCAL_PART = "lastname";

	/**
	 * Logger par défaut de la classe.
	 */
	private static final Log LOGGER = LogFactory.getLog(UserInfoSOAPMessageHandler.class);

	/**
	 * L'identifiant de l'utilisateur.
	 */
	private final String id;

	/**
	 * Le prénom de l'utilisateur.
	 */
	private final String firstname;

	/**
	 * Le nom de l'utilisateur.
	 */
	private final String lastname;

	/**
	 * Constructeur avec les informations utilisateurs en paramètres.
	 * 
	 * @param id
	 *            L'identifiant technique de l'utilisateur (LDAP)
	 * @param firstname
	 *            Le prénom de l'utilisateur.
	 * @param lastname
	 *            Le nom de l'utilisateur.
	 */
	public UserInfoSOAPMessageHandler(String id, String firstname, String lastname) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	@Override
	public void close(MessageContext mc) {

	}

	@Override
	public boolean handleFault(SOAPMessageContext smc) {
		return false;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		boolean returnCode = true;
		final Boolean isOutbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (isOutbound != null && isOutbound.booleanValue()) {

			try {
				SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();

				// Si aucun header, création.
				if (header == null) {
					header = envelope.addHeader();
				}

				SOAPElement userInfo = header.addChildElement(new QName(NAMESPACE, USER_INFO_LOCAL_PART));
				userInfo.addChildElement(new QName(NAMESPACE, ID_LOCAL_PART)).addTextNode(this.id);
				userInfo.addChildElement(new QName(NAMESPACE, FIRSTNAME_LOCAL_PART)).addTextNode(this.firstname);
				userInfo.addChildElement(new QName(NAMESPACE, LASTNAME_LOCAL_PART)).addTextNode(this.lastname);
			} catch (Exception e) {
				LOGGER.error("Une exception est survenue durant la récupération des informations d'authentification.", e);
				returnCode = false;
			}
		}

		return returnCode;
	}

	@Override
	public Set<QName> getHeaders() {
		Set<QName> headers = new HashSet<QName>();

		headers.add(new QName(NAMESPACE, USER_INFO_LOCAL_PART));

		return headers;
	}

	/**
	 * Permet d'ajouter le handler au Port permettant d'effectuer les appels WebServices.<br>
	 * Ce handler permet de positionner l'id, le prénom et le nom d'utilisateur dans la trame SOAP.
	 * 
	 * @param id
	 *            L'identifiant de l'utilisateur.
	 * @param firstname
	 *            Le prénom de l'utilisateur.
	 * @param lastname
	 *            Le nom de l'utilisateur.
	 * @param port
	 *            Le port du Web Services.
	 */
	@SuppressWarnings("rawtypes")
	public static void bind(String id, String firstname, String lastname, BindingProvider port) {

		UserInfoSOAPMessageHandler handler = new UserInfoSOAPMessageHandler(id, firstname, lastname);

		// Récupération de la liste des handlers.
		Binding binding = port.getBinding();
		List<Handler> handlerList = binding.getHandlerChain();

		if (handlerList == null) {
			handlerList = new ArrayList<Handler>();
		}

		// Ajout du handler d'authentification à la liste des handlers existants.
		handlerList.add(handler);
		binding.setHandlerChain(handlerList);
	}

}
