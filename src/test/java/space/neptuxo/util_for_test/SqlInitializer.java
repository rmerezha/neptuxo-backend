package space.neptuxo.util_for_test;

import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.util.Objects;

public class SqlInitializer {

    private final static String INSERT_SCRIPT;

    private final static String TRUNCATE_SCRIPT;

    private final static String INSERT_SCRIPTS_NAME_FILE = "insert_script.sql";

    private final static String TRUNCATE_SCRIPTS_NAME_FILE = "truncate_script.sql";

    static {
        INSERT_SCRIPT = initSqlScripts(INSERT_SCRIPTS_NAME_FILE);
        TRUNCATE_SCRIPT = initSqlScripts(TRUNCATE_SCRIPTS_NAME_FILE);
    }

    @SneakyThrows
    public static void insert(Connection connection) {
        try (var ps = connection.createStatement()) {
            ps.executeUpdate(INSERT_SCRIPT);
        }
    }

    @SneakyThrows
    public static void clear(Connection connection) {
        try (var ps = connection.createStatement()) {
            ps.executeUpdate(TRUNCATE_SCRIPT);
        }
    }

    @SneakyThrows
    private static String initSqlScripts(String fileName) {
        try (BufferedInputStream in = new BufferedInputStream(Objects.requireNonNull(SqlInitializer.class.getClassLoader().getResourceAsStream(fileName)))) {
            byte[] bytes = in.readAllBytes();
            return new String(bytes);
        }
    }

}
