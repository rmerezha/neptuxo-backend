package space.neptuxo.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

@UtilityClass
public class ConnectionPool {

    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWD_KEY = "db.passwd";
    private static final ArrayList<Connection> sourceConnections = new ArrayList<>();
    private static ArrayBlockingQueue<Connection> pool;

    static {
        loadDriver();
        initPool();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initPool() {
        int poolSize = Integer.parseInt(PropertiesUtil.get(POOL_SIZE_KEY));
        pool = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            var sourceConnection = createConnection();
            var proxyConnection = (Connection)
                    Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class},
                            (proxy, method, args) -> method.getName().equals("close")
                                    ? pool.add((Connection) proxy)
                                    : method.invoke(sourceConnection, args));
            pool.add(proxyConnection);
            sourceConnections.add(sourceConnection);
        }
    }

    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USER_KEY),
                    PropertiesUtil.get(PASSWD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
