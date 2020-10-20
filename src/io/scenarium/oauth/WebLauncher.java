
package io.scenarium.oauth;

import io.scenarium.oauth.db.DBConfig;
import io.scenarium.oauth.db.DBEntry;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.lang.System.Logger.Level;
import java.net.URI;
import java.sql.SQLException;


public class WebLauncher {
	private WebLauncher() {}
	public static final URI BASE_URI = getBaseURI();
	public static DBConfig dbConfig;
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/oauth/api/").port(17080).build();
	}

	public static void main(String[] args) {
		ResourceConfig rc = new ResourceConfig();
		// remove cors ==> all time called by an other system...
		rc.register(new CORSFilter());
		// add default resource:
		rc.registerClasses(UserResource.class);
		rc.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());

		dbConfig = new DBConfig(ConfigVariable.getDBHost(),
				Integer.parseInt(ConfigVariable.getDBPort()),
				ConfigVariable.getDBLogin(),
				ConfigVariable.getDBPassword(),
				ConfigVariable.getDBName());
		dbConfig = new DBConfig("localhost",
				15306,
				"root",
				"klkhj456gkgtkhjgvkujfhjgkjhgsdfhb3467465fgdhdesfgh",
				"oauth");
		/*
		DBEntry entry = new DBEntry(dbConfig);
		try {
			entry.test();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		entry.disconnect();
		entry = null;
		*/
		try {
			HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
			server.start();

			System.out.println(String.format(
					"Jersey app started at " + "%s\nHit enter to stop it...",
					BASE_URI, BASE_URI));

			System.in.read();
			server.shutdownNow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
