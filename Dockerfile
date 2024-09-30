# Utiliser une image de base Tomcat
FROM tomcat:10.1.30-jdk17-temurin-noble

# Supprimer les applications web par défaut de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copier votre fichier WAR dans le répertoire webapps de Tomcat
COPY target/ecommerce-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ecommerce.war

# Exposer le port 8080 (port par défaut de Tomcat)
EXPOSE 8080

# Démarrer Tomcat
CMD ["catalina.sh", "run"]
