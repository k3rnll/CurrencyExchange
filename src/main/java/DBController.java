import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DBController {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private static String dbUrl = "jdbc:sqlite:\\CE_DB.db";


    public Set<Currency> getCurrenciesSet(){
        Set<Currency> currenciesSet = new HashSet<>();
        if(connectDB()){
            try {
                resultSet = statement.executeQuery("SELECT * FROM Currencies");
                while(resultSet.next()){
                    Currency currency = new Currency(
                            resultSet.getInt("ID"),
                            resultSet.getString("Code"),
                            resultSet.getString("FullName"),
                            resultSet.getString("Sign")
                    );
                    currenciesSet.add(currency);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeDB();
            }
        }
        return currenciesSet;
    }

    private boolean closeDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbUrl);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
