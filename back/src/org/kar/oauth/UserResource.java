package org.kar.oauth;

import org.kar.oauth.db.DBEntry;
import org.kar.oauth.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@Path("/users")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class UserResource {

	public UserResource() {
	}
	// curl http://localhost:9993/api/users
	@GET
	public List<User> getUsers() {
		System.out.println("getUsers");
		DBEntry entry = new DBEntry(WebLauncher.dbConfig);
		List<User> out = new ArrayList<>();
		String query = "SELECT * FROM user";
		try {
			Statement st = entry.connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			System.out.println ("List of user:");
			while (rs.next()) {
				out.add(new User(rs));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		entry.disconnect();
		entry = null;
		return out;
	}

	// curl http://localhost:9993/api/users/3
	@GET
	@Path("{id}")
	public User getUser(@PathParam("id") long userId) {
		System.out.println("getUser " + userId);
		DBEntry entry = new DBEntry(WebLauncher.dbConfig);
		String query = "SELECT * FROM user WHERE id = ?";
		try {
			PreparedStatement ps = entry.connection.prepareStatement(query);
			ps.setLong(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User out = new User(rs);
				entry.disconnect();
				return out;
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		entry.disconnect();
		return null;
	}

	public UserSmall getUserSmall(long userId) {
		System.out.println("getUser " + userId);
		DBEntry entry = new DBEntry(WebLauncher.dbConfig);
		String query = "SELECT id, login, email, authorisationLevel FROM user WHERE id = ?";
		try {
			PreparedStatement ps = entry.connection.prepareStatement(query);
			ps.setLong(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				UserSmall out = new UserSmall(rs);
				entry.disconnect();
				// Admin is just for OAuth interface management
				if (out.authorisationLevel == State.ADMIN) {
					out.authorisationLevel = State.USER;
				}
				return out;
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		entry.disconnect();
		return null;
	}
	// curl -d '{"id":3,"login":"HeeroYui","password":"bouloued","email":"yui.heero@gmail.com","emailValidate":0,"newEmail":null,"authorisationLevel":"ADMIN"}' -H "Content-Type: application/json" -X POST http://localhost:9993/api/users
	@POST
	public Response createUser(User user) {
		System.out.println("getUser " + user);
		/*
		DBEntry entry = new DBEntry(WebLauncher.dbConfig);
		String query = "SELECT * FROM user WHERE id = ?";
		try {
			PreparedStatement ps = entry.connection.prepareStatement(query);
			ps.setLong(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User out = new User(rs);
				entry.disconnect();
				return out;
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		entry.disconnect();
		entry = null;
		return null;
		 */
		String result = "User saved ... : " + user;
		return Response.status(201).entity(result).build();
	}

	@GET
	@Path("/check_login")
	public Response checkLogin(@QueryParam("login") String login) {
		System.out.println("checkLogin: " + login );

		DBEntry entry = new DBEntry(WebLauncher.dbConfig);
		String query = "SELECT COUNT(*) FROM user WHERE login = ?";
		try {
			PreparedStatement ps = entry.connection.prepareStatement(query);
			ps.setString(1, login);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				entry.disconnect();
				if (count >= 1) {
					return Response.ok().build();
				}
				return Response.status(404).build();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		entry.disconnect();
		return Response.status(500).build();
	}
	@GET
	@Path("/check_email")
	public Response checkEmail(@QueryParam("email") String email) {
		System.out.println("checkEmail: " + email );

		DBEntry entry = new DBEntry(WebLauncher.dbConfig);
		String query = "SELECT COUNT(*) FROM user WHERE email = ?";
		try {
			PreparedStatement ps = entry.connection.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				entry.disconnect();
				if (count >= 1) {
					return Response.ok().build();
				}
				return Response.status(404).build();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		entry.disconnect();
		return Response.status(500).build();
	}

	// Clean all old tokens...
	public void clanToken(DBEntry entry) {
		String query = "DELETE FROM token WHERE endValidityTime <= now()";
		try {
			PreparedStatement ps = entry.connection.prepareStatement(query);
			ps.execute();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}


	@GET
	@Path("/check_token")
	public Response checkToken(@QueryParam("id") long id, @QueryParam("token") String token) {
		System.out.println("checkToken: " + id + " tokk=" + token );
		DBEntry entry = new DBEntry(WebLauncher.dbConfig);
		clanToken(entry);
		String query = "SELECT * FROM token WHERE userId = ? AND token = ?";
		try {
			System.out.println("try ..." );
			PreparedStatement ps = entry.connection.prepareStatement(query);
			ps.setLong(1, id);
			ps.setString(2, token);
			ResultSet rs = ps.executeQuery();
			System.out.println("query done ..." );
			if (rs.next()) {
				System.out.println("have create a token" );
				Token tok = new Token(rs);
				// TODO check time of token !!!! ==> can not be possible, cleaned before ...
				if (true) {
					UserSmall ret = getUserSmall(id);
					if (ret.authorisationLevel != State.USER) {
						return Response.status(401).build();
					}
					return Response.ok(ret).build();
				}
				// Token expired ...
				return Response.status(498).build();
			} else {
				System.out.println(" error check" );
				// No token available !!!
				entry.disconnect();
				return Response.status(404).build();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		entry.disconnect();
		return Response.status(500).build();
	}
	// curl -d '{"login": "qsdfqsdfqsdfqsdqs", "method": "v1", "time": "10-02-2020 12:31:25 1025", "password": "82c8493f1670fbc1906f371bf3a4f0f9522c374dfc92766cc1a02e0f81523f731b9514be42b7a039d2094f78bf6fac3ddae891b7f0ebc97d6498d9dfc44f441f"}' -H "Content-Type: application/json" -X POST http://localhost:9993/api/users/get_token

	// I do not understand why angular request option before, but this is needed..
	@OPTIONS
	@Path("/get_token")
	public Response getTokenOption(){
		return Response.ok()
				.header("Allow", "POST")
				.header("Allow", "OPTIONS")
				.build();
	}

	@POST
	@Path("/get_token")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getToken(DataGetToken data) {
		System.out.println("login: " + data.login );
		// check good version:
		if (!data.method.contentEquals("v1")) {
			String result = "Authentiocate-method-error (wrong version: '" + data.method + "')";
			System.out.println("    result: " + result);
			return Response.status(403).entity(result).build();
		}
		// verify login or email is correct:
		if (data.login.length() < 6) {
			String result = "Authentiocate-method-error (email or login too small: '" + data.login + "')";
			System.out.println("    result: " + result);
			return Response.status(403).entity(result).build();
		}
		// email or login?
		String query = "SELECT * FROM user WHERE login = ?";
		if (data.login.contains("@")) {
			System.out.println("Check with email");
			query = "SELECT * FROM user WHERE email = ?";
		} else {
			System.out.println("Check with login");
		}
		User user = null;
		// get the user password:
		DBEntry entry = new DBEntry(WebLauncher.dbConfig);
		try {
			PreparedStatement ps = entry.connection.prepareStatement(query);
			ps.setString(1, data.login);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User(rs);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			String result = "SERVER Internal error";
			System.out.println("    result: " + result);
			entry.disconnect();
			return Response.status(500).entity(result).build();
		}
		if (user == null || user.id <= 0) {
			entry.disconnect();
			String result = "Authentiocate-wrong email/login '" + data.login + "')";
			System.out.println("    result: " + result);
			entry.disconnect();
			return Response.status(404).entity(result).build();
		}
		// Check the password:
		String passwodCheck = getSHA512("login='" + data.login + "';pass='" + user.password + "';date='" + data.time + "'");
		if (!passwodCheck.contentEquals(data.password)) {
			String result = "Password error ...";
			System.out.println("    result: " + result);
			entry.disconnect();
			return Response.status(403).entity(result).build();
		}
		if (user.authorisationLevel != State.USER && user.authorisationLevel != State.ADMIN) {
			// unauthorized
			return Response.status(401).build();
		}
		// Create a token in the BDD
		System.out.println("All is good: " + user);
		// create token:
		String tok = randomString(128);
		long idToken = -1;
		query = "INSERT INTO `token` (`userId`, `token`, `createTime`, `endValidityTime`) VALUES (?, ?, now(), ADDTIME(now(),'20:12:18'));";
		try {
			PreparedStatement ps = entry.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, user.id);
			ps.setString(2, tok);
			int insertDone = ps.executeUpdate();
			if (insertDone != 0) {
				ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
					idToken = rs.getLong(1);
				}
				//Token token = new Token(rs);
				System.out.println("generated_value=" + idToken);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			String result = "SERVER Internal error can not create token";
			System.out.println("    result: " + result);
			entry.disconnect();
			return Response.status(500).entity(result).build();
		}
		if (idToken == -1) {
			String result = "SERVER cat not generate token";
			System.out.println("    result: " + result);
			entry.disconnect();
			return Response.status(500).entity(result).build();
		}

		query = "SELECT * FROM token WHERE id = ?";
		try {
			PreparedStatement ps = entry.connection.prepareStatement(query);
			ps.setLong(1, idToken);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Token out = new Token(rs);
				entry.disconnect();
				return Response.status(201).entity(out).build();
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		entry.disconnect();
		String result = "SOME error ???";

		return Response.status(403).entity(result).build();

		//return Response.status(500).build();
	}
/*
	@Path("/bookings")
	public TrainBookingResource getTrainBookingResource() {
		System.out.println("TrainResource.getTrainBookingResource()");
		
		return new TrainBookingResource();
	}

	 */

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public String getSHA512(String passwordToHash){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String randomString(int count) {
		Random rand = new Random(System.nanoTime());
		String s = new String();
		int nbChar = count;
		for (int i = 0; i < nbChar; i++) {
			char c = (char) rand.nextInt();
			while ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c < '0' || c > '9'))
				c = (char) rand.nextInt();
			s = s + c;
		}
		return s;
	}
}
































