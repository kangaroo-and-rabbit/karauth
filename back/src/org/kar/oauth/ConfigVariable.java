package org.kar.oauth;

public class ConfigVariable {

    public static String getDBHost() {
        return System.getenv("org.kar.oauth.db.host", "localhost");
    }

    public static String getDBPort() {
        return System.getenv("org.kar.oauth.db.port", "3306");
    }

    public static String getDBLogin() {
        return System.getenv("org.kar.oauth.db.login", "root");
    }

    public static String getDBPassword() {
        return System.getenv("MYSQL_ROOT_PASSWORD", "klkhj456gkgtkhjgvkujfhjgkjhgsdfhb3467465fgdhdesfgh");
    }

    public static String getDBName() {
        return System.getenv("MYSQL_DATABASE", "oauth");
    }

    public static String getlocalAddress() {
        return System.getenv("org.kar.oauth.address", "http://0.0.0.0:17080/oauth/api/");
    }
}
