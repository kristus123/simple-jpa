package org.simplejpa;

import org.flywaydb.core.Flyway;

import java.util.Objects;

public class Migrate {

    public static void run() {
        Flyway.configure().dataSource(
                String.format("jdbc:postgresql://%s:5432/wfm", Objects.requireNonNull(System.getenv("DATABASE_URL"))),
                "postgres",
                "postgres"
        ).load().migrate();
    }
}
