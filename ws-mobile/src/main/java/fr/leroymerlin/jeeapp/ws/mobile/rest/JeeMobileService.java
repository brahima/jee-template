package fr.leroymerlin.jeeapp.ws.mobile.rest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.springframework.stereotype.Component;

import fr.leroymerlin.fwk.core.faults.BusinessFault;
import fr.leroymerlin.fwk.core.faults.TechnicalFault;
import fr.leroymerlin.fwk.utils.log.LogUtils;



/**
 * Web service à destination des applications mobiles.<br>
 *
 * 
 */
@Component("jeeapp-spring.mobileService")
@Path("/v1.0/service")
public class JeeMobileService {

	 
	/**
	 * Logger du service rest.
	 */
	private static final Log LOGGER = LogFactory.getLog(JeeMobileService.class);
	
	/**
	 * Méthode de test.
	 *  @param client
	 *            version du client.
	 * @param server
	 *            version du serveur.
	 * @return Hello world dans un Json utf-8.
	 */
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response testMethod(@QueryParam("server") String server,
			@QueryParam("client") String client) {
		String helloWorld = "Hello World !" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		return  Response.ok(helloWorld).build();
	}
	
	
	/**
	 * Permet de logger la méthode appelée avec une version du ws et du client.
	 * 
	 * @param logger
	 *            du service.
	 * @param method
	 *            méthode appelée.
	 * @param version
	 *            version du ws.
	 * @param client
	 *            version du client.
	 */
	protected void logStat(Log logger, String method, String version, String client) {
		LogUtils.log(logger, Level.INFO, "Method [%s], ws-version[%s], client-version[%s]", method, version, client);
	}
}