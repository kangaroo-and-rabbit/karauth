package org.kar.oauth;

public class ConfigVariable {

    public static String getDBHost() {
        String out = System.getenv("org.kar.oauth.db.host");
        if (out == null) {
            return"localhost";
        }
        return out;
    }

    public static String getDBPort() {
        String out = System.getenv("org.kar.oauth.db.port");
        if (out == null) {
            return"3306";
        }
        return out;
    }

    public static String getDBLogin() {
        String out = System.getenv("org.kar.oauth.db.login");
        if (out == null) {
            return"root";
        }
        return out;
    }

    public static String getDBPassword() {
        String out = System.getenv("MYSQL_ROOT_PASSWORD");
        if (out == null) {
            return"klkhj456gkgtkhjgvkujfhjgkjhgsdfhb3467465fgdhdesfgh";
        }
        return out;
    }

    public static String getDBName() {
        String out = System.getenv("MYSQL_DATABASE");
        if (out == null) {
            return"oauth";
        }
        return out;
    }

    public static String getlocalAddress() {
        String out = System.getenv("org.kar.oauth.address");
        if (out == null) {
            return"http://0.0.0.0:17080/oauth/api/";
        }
        return out;
    }
}
