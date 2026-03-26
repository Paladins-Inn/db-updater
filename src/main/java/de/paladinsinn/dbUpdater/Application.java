/*
 * Copyright (c) 2025. Roland T. Lichti, Kaiserpfalz EDV-Service
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.paladinsinn.dbUpdater;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * CLI-Anwendung zum Ausführen von Liquibase-Datenbankmigrationen.
 *
 * <p>Verwendung:</p>
 * <pre>
 *   java -jar db-updater.jar -u
 *   java -jar db-updater.jar --update-database
 * </pre>
 *
 * <p>Konfiguration über Umgebungsvariablen:</p>
 * <ul>
 *   <li>{@code SPRING_DATASOURCE_URL} – JDBC-URL der Datenbank</li>
 *   <li>{@code SPRING_DATASOURCE_USERNAME} – Datenbankbenutzer</li>
 *   <li>{@code SPRING_DATASOURCE_PASSWORD} – Datenbankpasswort</li>
 *   <li>{@code LIQUIBASE_DEFAULT_SCHEMA} – Standard-Schema (optional, Standard: {@code public})</li>
 *   <li>{@code LIQUIBASE_CONTEXTS} – Liquibase-Kontexte (optional)</li>
 *   <li>{@code LIQUIBASE_LABELS} – Liquibase-Labels (optional)</li>
 * </ul>
 *
 * @author Roland T. Lichti, Kaiserpfalz EDV-Service
 * @version 1.0.0
 * @since 2025-01-01
 */
@SpringBootApplication
@Command(
        name = "db-updater",
        mixinStandardHelpOptions = true,
        version = "db-updater @project.version@",
        description = "CLI-Werkzeug zum Ausführen von Liquibase-Datenbankmigrationen.",
        footer = {
                "",
                "Konfiguration über Umgebungsvariablen:",
                "  SPRING_DATASOURCE_URL       JDBC-URL der Datenbank",
                "  SPRING_DATASOURCE_USERNAME  Datenbankbenutzer",
                "  SPRING_DATASOURCE_PASSWORD  Datenbankpasswort",
                "  LIQUIBASE_DEFAULT_SCHEMA    Standard-Schema (Standard: public)",
                "  LIQUIBASE_CONTEXTS          Liquibase-Kontexte (optional)",
                "  LIQUIBASE_LABELS            Liquibase-Labels (optional)"
        }
)
@Slf4j
public class Application implements Runnable {

    @Option(
            names = {"-u", "--update-database"},
            description = "Liquibase-Datenbankupdate ausführen und beenden."
    )
    private boolean updateDatabase;

    /**
     * Einstiegspunkt der Anwendung. Die Argument-Verarbeitung erfolgt über picocli.
     *
     * @param args Kommandozeilenargumente
     */
    static void main(final String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    /**
     * Wird von picocli nach dem Parsen der Argumente aufgerufen.
     *
     * <p>Wenn {@code -u} / {@code --update-database} angegeben wurde, wird Spring Boot
     * ohne Web-Server gestartet, Liquibase führt das Datenbankupdate durch,
     * und die Anwendung beendet sich danach automatisch.</p>
     *
     * <p>Ohne Argument wird die Hilfe ausgegeben.</p>
     */
    @Override
    public void run() {
        if (updateDatabase) {
            try (
                @SuppressWarnings("unused") var ctx = new SpringApplicationBuilder(Application.class)
                    .web(WebApplicationType.NONE)
                    .properties("spring.liquibase.enabled=true")
                    .run()) {
                log.info("Datenbankupdate erfolgreich abgeschlossen.");
            }
        } else {
            new CommandLine(this).usage(System.err);
        }
    }
}

