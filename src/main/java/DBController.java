import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DBController {
    private final String dbUrl = "jdbc:sqlite:\\CE_DB.db";
    private final DBConnectionPool connectionPool = new DBConnectionPool(dbUrl);


    public ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode){
        ExchangeRate exchangeRate = null;
        String query = String.format(
                        "SELECT * FROM ExchangeRates WHERE " +
                        "BaseCurrencyId IN (SELECT id FROM Currencies WHERE Code = '%s') " +
                        "AND " +
                        "TargetCurrencyId IN (SELECT id FROM Currencies WHERE Code = '%s')",
                        baseCurrencyCode, targetCurrencyCode);
        ResultSet result = executeStatement(query);
        if (Objects.nonNull(result)) {
            try {
                if (result.next()) {
                    exchangeRate = new ExchangeRate(
                            result.getInt("ID"),
                            getCurrency(baseCurrencyCode),
                            getCurrency(targetCurrencyCode),
                            result.getDouble("Rate"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exchangeRate;
    }

    public Set<Currency> getCurrenciesSet() {
        Set<Currency> currenciesSet = new HashSet<>();
        String query = "SELECT * FROM Currencies";
        ResultSet result = executeStatement(query);
        if (Objects.nonNull(result)) {
            try {
                while (result.next()) {
                    Currency currency = new Currency(
                            result.getInt("ID"),
                            result.getString("Code"),
                            result.getString("FullName"),
                            result.getString("Sign")
                    );
                    currenciesSet.add(currency);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return currenciesSet;
    }

    public Currency getCurrency(String currencyCode) {
        Currency currency = null;
        String query = String.format("SELECT * FROM Currencies WHERE Code = '%s'", currencyCode);
        ResultSet result = executeStatement(query);
        if (Objects.nonNull(result)) {
            try {
                currency = new Currency(
                        result.getInt("ID"),
                        result.getString("Code"),
                        result.getString("FullName"),
                        result.getString("Sign"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return currency;
    }

    private ResultSet executeStatement(String query) {
        ResultSet resultSet = null;
        Connection connection = connectionPool.getConnection();
        if (Objects.nonNull(connection)) {
            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }
        return resultSet;
    }
}
