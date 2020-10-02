package io.scenarium.oauth.model;

/*
CREATE TABLE `user` (
  `id` bigint NOT NULL COMMENT 'table ID' AUTO_INCREMENT PRIMARY KEY,
  `login` varchar(128) COLLATE 'utf8_general_ci' NOT NULL COMMENT 'login of the user',
  `password` varchar(128) COLLATE 'latin1_bin' NOT NULL COMMENT 'password of the user hashed (sha512)',
  `email` varchar(512) COLLATE 'utf8_general_ci' NOT NULL COMMENT 'email of the user',
  `emailValidate` bigint COMMENT 'date of the email validation',
  `newEmail` varchar(512) COLLATE 'utf8_general_ci' COMMENT 'email of the user if he want to change',
  `authorisationLevel` enum("REMOVED", "BLOCKED", "USER", "ADMIN") NOT NULL DEFAULT 'USER' COMMENT 'user level of authorization'
) AUTO_INCREMENT=10;

 */


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class User {
	public Long id;
	public String login;
	public String password;
	public String email;
	public Long emailValidate; // time of validation
	public String newEmail;
	public State authorisationLevel;

	public User() {}

	public User(long id, String login, String password, String email, long emailValidate, String newEmail, State authorisationLevel) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.email = email;
		this.emailValidate = emailValidate;
		this.newEmail = newEmail;
		this.authorisationLevel = authorisationLevel;
	}

	public User(ResultSet rs) {
		int iii = 1;
		try {
			this.id = rs.getLong(iii++);
			this.login = rs.getString(iii++);
			this.password = rs.getString(iii++);
			this.email =  rs.getString(iii++);
			this.emailValidate = rs.getLong(iii++);
			this.newEmail = rs.getString(iii++);
			this.authorisationLevel = State.valueOf(rs.getString(iii++));
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	/*
	public void serialize(ResultSet rs) {
		int iii = 1;
		try {
			this.id = rs.getLong(iii++);
			this.login = rs.getString(iii++);
			this.password = rs.getString(iii++);
			this.email =  rs.getString(iii++);
			this.emailValidate = rs.getLong(iii++);
			this.newEmail = rs.getString(iii++);
			this.authorisationLevel = State.valueOf(rs.getString(iii++));
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	*/

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", login='" + login + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", emailValidate=" + emailValidate +
				", newEmail='" + newEmail + '\'' +
				", authorisationLevel=" + authorisationLevel +
				'}';
	}
}
