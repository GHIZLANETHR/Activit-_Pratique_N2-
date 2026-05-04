#  Activité Pratique N°2 — Application Web Spring Boot avec Sécurité

> Application web Java/Spring Boot avec authentification, autorisation, gestion des données et interface Thymeleaf.

---

##  Table des matières

- [Aperçu du projet](#aperçu-du-projet)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Structure du projet](#structure-du-projet)
- [Installation et lancement](#installation-et-lancement)
- [Configuration](#configuration)
- [Sécurité](#sécurité)
- [Base de données](#base-de-données)
- [Fonctionnalités](#fonctionnalités)
- [Auteur](#auteur)

---

##  Aperçu du projet

Ce projet est une application web développée dans le cadre de l'**Activité Pratique N°2**. Il s'agit d'une application Spring Boot complète intégrant :

- Une couche de **persistance JPA/Hibernate** avec support MySQL et H2
- Une couche de **sécurité Spring Security** avec authentification et autorisation
- Une interface utilisateur construite avec **Thymeleaf** et **Bootstrap 5**
- Un support **OAuth2** (login social et serveur de ressources)
- Une validation des formulaires avec **Bean Validation**

---

##  Technologies utilisées

| Technologie | Version | Rôle |
|---|---|---|
| Java | 21 | Langage principal |
| Spring Boot | 3.5.11 | Framework principal |
| Spring Data JPA | (inclus Boot) | ORM & accès base de données |
| Spring Security | (inclus Boot) | Authentification & autorisation |
| Spring Web MVC | (inclus Boot) | Contrôleurs REST et MVC |
| Thymeleaf | (inclus Boot) | Moteur de templates HTML |
| Thymeleaf Layout Dialect | (inclus) | Layouts réutilisables |
| thymeleaf-extras-springsecurity6 | (inclus) | Intégration Thymeleaf + Security |
| Bootstrap | 5.3.3 (WebJar) | Interface responsive |
| Lombok | (inclus Boot) | Réduction du boilerplate Java |
| H2 Database | (inclus Boot) | Base de données en mémoire (dev) |
| MySQL | (inclus Boot) | Base de données relationnelle (prod) |
| Spring OAuth2 Client | (inclus Boot) | Connexion via fournisseurs OAuth2 |
| Spring OAuth2 Resource Server | (inclus Boot) | Protection des ressources par JWT |
| Bean Validation | (inclus Boot) | Validation des données |
| Maven | 3.x | Gestion de build et dépendances |

---

##  Prérequis

Avant de lancer le projet, assurez-vous d'avoir installé :

- **Java 21+** — [Télécharger ici](https://adoptium.net/)
- **Maven 3.8+** — [Télécharger ici](https://maven.apache.org/download.cgi) *(ou utiliser le wrapper `mvnw` inclus)*
- **MySQL 8+** *(optionnel, pour le mode production)* — [Télécharger ici](https://dev.mysql.com/downloads/)
- Un IDE comme **IntelliJ IDEA** ou **Eclipse/STS**

---

##  Structure du projet

```
Activit-_Pratique_N2-/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties    # Configuration du wrapper Maven
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ma/yassine/activitepratique2/
│   │   │       ├── entities/           # Entités JPA (modèles de données)
│   │   │       ├── repositories/       # Interfaces Spring Data JPA
│   │   │       ├── services/           # Logique métier
│   │   │       ├── web/                # Contrôleurs MVC
│   │   │       ├── security/           # Configuration Spring Security
│   │   │       └── ActivitePratique2Application.java  # Point d'entrée
│   │   └── resources/
│   │       ├── templates/              # Templates Thymeleaf (.html)
│   │       ├── static/                 # Ressources statiques (CSS, JS, images)
│   │       └── application.properties  # Configuration de l'application
│   └── test/
│       └── java/                       # Tests unitaires et d'intégration
├── .gitattributes
├── .gitignore
├── mvnw                                # Maven Wrapper (Linux/Mac)
├── mvnw.cmd                            # Maven Wrapper (Windows)
└── pom.xml                             # Configuration Maven & dépendances
```

---

##  Installation et lancement

### 1. Cloner le dépôt

```bash
git clone https://github.com/GHIZLANETHR/Activit-_Pratique_N2-.git
cd Activit-_Pratique_N2-
```

### 2. Lancer avec H2 (mode développement — aucune config requise)

```bash
# Linux / Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

L'application démarre sur **http://localhost:8080**

### 3. Lancer avec MySQL (mode production)

Créer la base de données MySQL :

```sql
CREATE DATABASE activitepratique2;
```

Mettre à jour `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/activitepratique2
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Puis lancer :

```bash
./mvnw spring-boot:run
```

### 4. Construire le JAR

```bash
./mvnw clean package
java -jar target/activitepratique2-0.0.1-SNAPSHOT.jar
```

---

##  Configuration

Le fichier principal de configuration est `src/main/resources/application.properties`.

```properties
# Serveur
server.port=8080

# Base de données H2 (développement)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Thymeleaf
spring.thymeleaf.cache=false
```

>  **Astuce** : La console H2 est accessible via `/h2-console` en mode développement pour inspecter les données en mémoire.

---

##  Sécurité

L'application intègre **Spring Security** avec les mécanismes suivants :

### Authentification
- **Formulaire de connexion** classique (username / password)
- **OAuth2 Login** — Connexion via des fournisseurs externes (Google, GitHub, etc.)

### Autorisation
- Protection des routes par rôles (ex : `ROLE_USER`, `ROLE_ADMIN`)
- Intégration Thymeleaf/Security pour afficher/masquer des éléments selon le rôle de l'utilisateur connecté (`sec:authorize`)

### OAuth2 Resource Server
- Protection des endpoints API via **JWT** (JSON Web Tokens)

### Exemple de configuration Security (simplifié)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
```

---

##  Base de données

Le projet supporte deux bases de données :

### H2 (développement)
- Base en mémoire, créée automatiquement au démarrage
- Réinitialisée à chaque redémarrage
- Console d'administration disponible sur `/h2-console`

### MySQL (production)
- Base persistante
- Schéma géré par Hibernate (`ddl-auto=update`)
- Nécessite la création manuelle de la base

---

##  Fonctionnalités

-  **Authentification** par formulaire et OAuth2 (Google, GitHub…)
-  **Gestion des rôles** : accès différencié selon le profil utilisateur
-  **CRUD complet** des entités métier via interface web
-  **Validation des formulaires** côté serveur (Bean Validation)
-  **Interface responsive** avec Bootstrap 5 et Thymeleaf
-  **Hot reload** en développement grâce à Spring DevTools
-  **Tests** avec Spring Boot Test et Spring Security Test

---

##  Auteur

**GHIZLANETHR**
Étudiante — Activité Pratique N°2

- GitHub : [@GHIZLANETHR](https://github.com/GHIZLANETHR)

---

##  Licence

Ce projet est réalisé dans un cadre académique. Tous droits réservés.
