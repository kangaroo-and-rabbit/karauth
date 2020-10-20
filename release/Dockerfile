FROM archlinux:latest

# update system
RUN pacman -Syu --noconfirm && pacman-db-upgrade
#install jdk-openjdk java-openjfx
RUN pacman -S --noconfirm jdk-openjdk java-openjfx
# clean all the caches
RUN pacman -Scc --noconfirm

ENV LANG=C.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/java-14-openjdk
ENV JAVAFX_HOME=$JAVA_HOME
ENV PATH=/usr/lib/jvm/java-14-openjdk/bin/:$PATH
#ENV JAVA_VERSION=14.0.2


RUN mkdir /application/
ADD scenarium-oauth.jar /application/
WORKDIR /application/

CMD ["java", "-cp", "/application/scenarium-oauth.jar", "io.scenarium.oauth.WebLauncher"]

