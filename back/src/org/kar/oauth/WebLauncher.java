
package org.kar.oauth;

import org.kar.oauth.db.DBConfig;
import org.kar.oauth.db.DBEntry;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.System.Logger.Level;
import java.net.URI;
import java.sql.SQLException;
import java.util.Properties;


public class WebLauncher {
	private WebLauncher() {}
	public static DBConfig dbConfig;
	private static URI getBaseURI() {
		return UriBuilder.fromUri(ConfigVariable.getlocalAddress()).build();
	}

	public static void main(String[] args) {
		ResourceConfig rc = new ResourceConfig();
		// remove cors ==> all time called by an other system...
		rc.register(new CORSFilter());
		// add default resource:
		rc.registerClasses(UserResource.class);
		// add jackson to be discovenr when we are ins standalone server
		rc.register(JacksonFeature.class);
        // enable this to show low level request
		//rc.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());
		System.out.println("Connect on the BDD:");
		System.out.println("    getDBHost: '" + ConfigVariable.getDBHost() + "'");
		System.out.println("    getDBPort: '" + ConfigVariable.getDBPort() + "'");
		System.out.println("    getDBLogin: '" + ConfigVariable.getDBLogin() + "'");
		System.out.println("    getDBPassword: '" + ConfigVariable.getDBPassword() + "'");
		System.out.println("    getDBName: '" + ConfigVariable.getDBName() + "'");
		dbConfig = new DBConfig(ConfigVariable.getDBHost(),
				Integer.parseInt(ConfigVariable.getDBPort()),
				ConfigVariable.getDBLogin(),
				ConfigVariable.getDBPassword(),
				ConfigVariable.getDBName());

		System.out.println(" ==> " + dbConfig);
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(getBaseURI(), rc);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Stopping server..");
				server.shutdownNow();
			}
		}, "shutdownHook"));

		// run
		try {
			server.start();
			System.out.println("Jersey app started at " + getBaseURI());
			System.out.println("Press CTRL^C to exit..");
			Thread.currentThread().join();
		} catch (Exception e) {
			System.out.println("There was an error while starting Grizzly HTTP server.");
			e.printStackTrace();
		}
	}
}
