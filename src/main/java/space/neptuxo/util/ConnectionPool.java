package space.neptuxo.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionPool {

    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWD_KEY = "db.passwd";
    private static final ArrayList<Connection> sourceConnections = new ArrayList<>();

    private static final PropertiesUtil properties = new PropertiesUtil();
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
        int poolSize = Integer.parseInt(properties.get(POOL_SIZE_KEY));
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
                    properties.get(URL_KEY),
                    properties.get(USER_KEY),
                    properties.get(PASSWD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
