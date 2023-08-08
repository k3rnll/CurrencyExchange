import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBConnectionPool {
    private final ArrayList<Connection> availableConnections;
    private final ArrayList<Connection> usedConnections;
    private final int poolSize;

    public DBConnectionPool(String dbUrl) {
        availableConnections = new ArrayList<>();
        usedConnections = new ArrayList<>();
        poolSize = 10;
        for (int i = 0; i < poolSize; i++) {
            try {
                Class.forName("org.sqlite.JDBC");
                Connection connection = DriverManager.getConnection(dbUrl);
                availableConnections.add(connection);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public Connection getConnection() {
        if (availableConnections.size() > 0) {
            Connection connection = availableConnections.get(availableConnections.size() - 1);
            availableConnections.remove(availableConnections.size() - 1);
            usedConnections.add(connection);
            return connection;
        }
        return null;
    }

    public void releaseConnection(Connection connection) {
        if ((usedConnections.size() > 0) && (usedConnections.remove(connection))) {
            availableConnections.add(connection);
        }
    }

}
