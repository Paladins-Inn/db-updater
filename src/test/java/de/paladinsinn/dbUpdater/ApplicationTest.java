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

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests für die CLI-Argument-Verarbeitung.
 */
class ApplicationTest {

    @Test
    void hilfeFlagGibtExitcode0() {
        int exitCode = new CommandLine(new Application()).execute("--help");
        assertEquals(0, exitCode, "--help sollte mit Exit-Code 0 beenden");
    }

    @Test
    void versionFlagGibtExitcode0() {
        int exitCode = new CommandLine(new Application()).execute("--version");
        assertEquals(0, exitCode, "--version sollte mit Exit-Code 0 beenden");
    }

    @Test
    void ohneArgumenteGibtExitcode0() {
        int exitCode = new CommandLine(new Application()).execute();
        assertEquals(0, exitCode, "Ohne Argumente sollte mit Exit-Code 0 (Hilfe) beenden");
    }
}

