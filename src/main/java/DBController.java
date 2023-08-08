import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DBController {
    private DBConnectionPool connectionPool = new DBConnectionPool(dbUrl);
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private static String dbUrl = "jdbc:sqlite:\\CE_DB.db";


    public ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode){
        ExchangeRate exchangeRate = null;
        try {
            Currency baseCurrency = getCurrency(baseCurrencyCode);
            Currency targetCurrency = getCurrency(targetCurrencyCode);
            connectDB();

            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM ExchangeRates WHERE " +
                    "BaseCurrencyId IN (SELECT id FROM Currencies WHERE Code = '%s') " +
                    "AND " +
                    "TargetCurrencyId IN (SELECT id FROM Currencies WHERE Code = '%s')",
                    baseCurrency.getCode(), targetCurrency.getCode()));
            if(resultSet.next()) {
                exchangeRate = new ExchangeRate(
                        resultSet.getInt("id"),
                        baseCurrency,
                        targetCurrency,
                        resultSet.getDouble("Rate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return exchangeRate;
    }

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

    public Currency getCurrency(String currencyCode) {
        Currency currency = null;
        Connection conn = connectionPool.getConnection();
        if (Objects.nonNull(conn)) {
            try {
                String query = String.format("SELECT * FROM Currencies WHERE Code = '%s'", currencyCode);
                Statement st = conn.createStatement();
                ResultSet res = st.executeQuery(query);
                if (res.next()) {
                    currency = new Currency(
                            res.getInt("ID"),
                            res.getString("Code"),
                            res.getString("FullName"),
                            res.getString("Sign")
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connectionPool.releaseConnection(conn);
            }

        }
        return currency;
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
