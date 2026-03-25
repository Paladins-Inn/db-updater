# db-updater

CLI-Werkzeug zum Ausführen von Liquibase-Datenbankmigrationen auf Basis von Spring Boot 4.

## Verwendung

```bash
# Datenbankupdate ausführen
java -jar db-updater.jar -u
java -jar db-updater.jar --update-database

# Hilfe anzeigen
java -jar db-updater.jar --help

# Version anzeigen
java -jar db-updater.jar --version
```

## Konfiguration

Die Datenbankverbindung und Liquibase-Parameter werden über Umgebungsvariablen konfiguriert:

| Variable                   | Beschreibung                            | Standard                            |
|----------------------------|-----------------------------------------|-------------------------------------|
| `SPRING_DATASOURCE_URL`    | JDBC-URL der Datenbank                  | `jdbc:postgresql://localhost:5432/db` |
| `SPRING_DATASOURCE_USERNAME` | Datenbankbenutzer                     | *(leer)*                            |
| `SPRING_DATASOURCE_PASSWORD` | Datenbankpasswort                     | *(leer)*                            |
| `LIQUIBASE_DEFAULT_SCHEMA` | Standard-Schema für Liquibase           | `public`                            |
| `LIQUIBASE_CONTEXTS`       | Liquibase-Kontexte (kommagetrennt)      | *(alle)*                            |
| `LIQUIBASE_LABELS`         | Liquibase-Label-Filter                  | *(alle)*                            |

Alternativ kann eine `application.yaml` im Arbeitsverzeichnis oder unter `config/application.yaml`
abgelegt werden, um Spring-Boot-Konfiguration zu überschreiben.

## Changelogs

Die Liquibase-Changelogs werden unter `src/main/resources/db/changelog/` abgelegt.
Die Masterdatei `db.changelog-master.yaml` bindet alle Teilmigrationen ein:

```yaml
databaseChangeLog:
  - include:
      file: db/changelog/v1/001-init.yaml
  - include:
      file: db/changelog/v1/002-weitere-migration.yaml
```

## Build

```bash
mvn clean package
```

Das fertige Jar liegt unter `target/db-updater.jar`.

## Beispiel: Kubernetes Job

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: db-updater
spec:
  template:
    spec:
      containers:
        - name: db-updater
          image: registry.example.com/db-updater:latest
          args: ["--update-database"]
          env:
            - name: SPRING_DATASOURCE_URL
              value: "jdbc:postgresql://postgres:5432/mydb"
            - name: SPRING_DATASOURCE_USERNAME
              valueFrom:
                secretKeyRef:
                  name: db-secret
                  key: username
            - name: SPRING_DATASOURCE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: db-secret
                  key: password
      restartPolicy: Never
```

## Lizenz

GNU Affero General Public License v3.0 – siehe [LICENSE](LICENSE).

