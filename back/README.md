Generic OAuth interface
=======================


mvn install

mvn compile

mvn package

// download all dependency in out/maven/dependency
mvn dependency:copy-dependencies

java -cp out/maven/scenarium-oauth-0.1.0.jar  org.kar.oauth.WebLauncher


// create a single package jar
mvn clean compile assembly:single



java -cp out/maven/scenarium-oauth-0.1.0-jar-with-dependencies.jar  org.kar.oauth.WebLauncher



