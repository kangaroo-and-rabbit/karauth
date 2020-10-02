package io.scenarium.oauth.model;

/*

CREATE TABLE `application` (
  `id` bigint NOT NULL COMMENT 'Unique ID of the application' AUTO_INCREMENT PRIMARY KEY,
  `description` text COMMENT 'description of the application',
  `token` varchar(128) COLLATE 'latin1_bin' NOT NULL COMMENT 'Token (can be not unique)'
) AUTO_INCREMENT=10;

*/

import java.sql.ResultSet;
import java.sql.SQLException;

public class Application {
    public long id;
    public String description;
    public String token;

    public Application() {
    }

    public Application(long id, String description, String token) {
        this.id = id;
        this.description = description;
        this.token = token;
    }

    public Application(ResultSet rs) {
        int iii = 1;
        try {
            this.id = rs.getLong(iii++);
            this.description = rs.getString(iii++);
            this.token = rs.getString(iii++);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
