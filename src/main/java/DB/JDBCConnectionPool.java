package DB;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCConnectionPool {

    private JDBCConnectionPool() {}

    public static Connection getConnection() throws SQLException {
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/CurrencyExchange");
            return dataSource.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }
}
