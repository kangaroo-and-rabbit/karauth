
package io.scenarium.oauth;

import io.scenarium.oauth.db.DBConfig;
import io.scenarium.oauth.db.DBEntry;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
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
		try {
			FileInputStream propFile = new FileInputStream( "/application/properties.txt");
			Properties p = new Properties(System.getProperties());
			p.load(propFile);
			for (String name : p.stringPropertyNames()) {
				String value = p.getProperty(name);
				// inject property if not define in cmdline:
				if (System.getProperty(name) == null) {
					System.setProperty(name, value);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File of environment variable not found: 'properties.txt'");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
			HttpServer server = GrizzlyHttpServerFactory.createHttpServer(getBaseURI(), rc);
			server.start();

			System.out.println(String.format(
					"Jersey app started at " + "%s\nHit enter to stop it...",
					getBaseURI(), getBaseURI()));

			System.in.read();
			server.shutdownNow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
