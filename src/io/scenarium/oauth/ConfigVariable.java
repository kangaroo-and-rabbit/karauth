package io.scenarium.oauth;

public class ConfigVariable {

    public static String getDBHost() {
        return System.getProperty("io.scenarium.web.oauth.db.host", "localhost");
    }

    public static String getDBPort() {
        return System.getProperty("io.scenarium.web.oauth.db.port", "15306");
    }

    public static String getDBLogin() {
        return System.getProperty("io.scenarium.web.oauth.db.login", "root");
    }

    public static String getDBPassword() {
        return System.getProperty("io.scenarium.web.oauth.db.password", "klkhj456gkgtkhjgvkujfhjgkjhgsdfhb3467465fgdhdesfgh");
    }

    public static String getDBName() {
        return System.getProperty("io.scenarium.web.oauth.db.name", "oauth");
    }

    public static String getlocalAddress() {
        return System.getProperty("io.scenarium.web.oauth.address", "http://localhost:17080/oauth/api/");
    }
}
