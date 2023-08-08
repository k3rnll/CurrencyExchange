import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DBController {
    private final String dbUrl = "jdbc:sqlite:\\CE_DB.db";
    private final DBConnectionPool connectionPool = new DBConnectionPool(dbUrl);

    public Set<ExchangeRate> getExchangeRatesSet() throws SQLException{
        Set<ExchangeRate> exchangeRatesSet = new HashSet<>();
        String query = "SELECT * FROM ExchangeRates";
        ResultSet result = executeStatement(query);
        if (Objects.nonNull(result)) {
            while (result.next()) {
                ExchangeRate exchangeRate = new ExchangeRate(
                        result.getInt("ID"),
                        getCurrency(result.getInt("BaseCurrencyId")),
                        getCurrency(result.getInt("TargetCurrencyId")),
                        result.getDouble("Rate"));
                exchangeRatesSet.add(exchangeRate);
            }
        }
        return  exchangeRatesSet;
    }

    public ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        ExchangeRate exchangeRate = null;
        String query = String.format(
                        "SELECT * FROM ExchangeRates WHERE " +
                        "BaseCurrencyId IN (SELECT id FROM Currencies WHERE Code = '%s') " +
                        "AND " +
                        "TargetCurrencyId IN (SELECT id FROM Currencies WHERE Code = '%s')",
                        baseCurrencyCode, targetCurrencyCode);
        ResultSet result = executeStatement(query);
        if (Objects.nonNull(result)) {
            if (result.next()) {
                exchangeRate = new ExchangeRate(
                        result.getInt("ID"),
                        getCurrency(baseCurrencyCode),
                        getCurrency(targetCurrencyCode),
                        result.getDouble("Rate"));
            }
        }
        return exchangeRate;
    }

    public Set<Currency> getCurrenciesSet() throws SQLException {
        Set<Currency> currenciesSet = new HashSet<>();
        String query = "SELECT * FROM Currencies";
        ResultSet result = executeStatement(query);
        if (Objects.nonNull(result)) {
            while (result.next()) {
                Currency currency = new Currency(
                        result.getInt("ID"),
                        result.getString("Code"),
                        result.getString("FullName"),
                        result.getString("Sign")
                );
                currenciesSet.add(currency);
            }
        }
        return currenciesSet;
    }

    public Currency getCurrency(int id) throws SQLException {
        return getCurrencyByQuery(String.format("SELECT * FROM Currencies WHERE id = '%d'", id));
    }

    public Currency getCurrency(String currencyCode) throws SQLException {
        return getCurrencyByQuery(String.format("SELECT * FROM Currencies WHERE Code = '%s'", currencyCode));
    }

    private Currency getCurrencyByQuery(String query) throws SQLException{
        Currency currency = null;
        ResultSet result = executeStatement(query);
        if (Objects.nonNull(result)) {
            currency = new Currency(
                    result.getInt("ID"),
                    result.getString("Code"),
                    result.getString("FullName"),
                    result.getString("Sign"));
        }
        return currency;
    }

    private ResultSet executeStatement(String query) throws SQLException {
        ResultSet resultSet = null;
        Connection connection = connectionPool.getConnection();
        if (Objects.nonNull(connection)) {
            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }
        return resultSet;
    }
}
