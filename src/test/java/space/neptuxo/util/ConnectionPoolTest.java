package space.neptuxo.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionPoolTest {

    private final ConnectionPool connectionPool = new ConnectionPool();
    private final int poolSize = Integer.parseInt(new PropertiesUtil().get("db.pool.size"));

    @Test
    void get() throws NoSuchFieldException, IllegalAccessException, SQLException {
        Connection connection = connectionPool.get();

        assertNotNull(connection);

        assertTrue(connection.getClass().getName().contains("$Proxy"));

        ArrayBlockingQueue<Connection> pool = getPrivateField(ConnectionPool.class, "pool");
        assertEquals(poolSize - 1, pool.size());

        connection.close();
        assertEquals(poolSize, pool.size());
    }

    private ArrayBlockingQueue<Connection> getPrivateField(Class<?> clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (ArrayBlockingQueue<Connection>) field.get(null);
    }
}