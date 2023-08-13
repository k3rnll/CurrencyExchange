import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DBController {
    private final JDBCConnectionPool connectionPool = new JDBCConnectionPool();

    public Set<ExchangeRate> getExchangeRatesSet() throws SQLException{
        Set<ExchangeRate> exchangeRatesSet = new HashSet<>();
        String query = "SELECT * FROM ExchangeRates";
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            while (result.next()) {
                ExchangeRate exchangeRate = new ExchangeRate(
                        result.getInt("ID"),
                        getCurrency(result.getInt("BaseCurrencyId")),
                        getCurrency(result.getInt("TargetCurrencyId")),
                        result.getDouble("Rate"));
                exchangeRatesSet.add(exchangeRate);
            }
            return exchangeRatesSet;
        }
    }

    public ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        String query = String.format(
                        "SELECT * FROM ExchangeRates WHERE " +
                        "BaseCurrencyId IN (SELECT id FROM Currencies WHERE Code = '%s') " +
                        "AND " +
                        "TargetCurrencyId IN (SELECT id FROM Currencies WHERE Code = '%s')",
                        baseCurrencyCode, targetCurrencyCode);
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            if (result.getInt("ID") != 0) {
                return new ExchangeRate(
                        result.getInt("ID"),
                        getCurrency(baseCurrencyCode),
                        getCurrency(targetCurrencyCode),
                        result.getDouble("Rate"));
            }
            return null;
        }
    }

    public Set<Currency> getCurrenciesSet() throws SQLException {
        Set<Currency> currenciesSet = new HashSet<>();
        String query = "SELECT * FROM Currencies";
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            while (result.next()) {
                Currency currency = new Currency(
                        result.getInt("ID"),
                        result.getString("Code"),
                        result.getString("FullName"),
                        result.getString("Sign")
                );
                currenciesSet.add(currency);
            }
            return currenciesSet;
        }
    }

    public Currency getCurrency(int id) throws SQLException {
        return getCurrencyByQuery(String.format("SELECT * FROM Currencies WHERE id = '%d'", id));
    }

    public Currency getCurrency(String currencyCode) throws SQLException {
        return getCurrencyByQuery(String.format("SELECT * FROM Currencies WHERE Code = '%s'", currencyCode));
    }

    private Currency getCurrencyByQuery(String query) throws SQLException{
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            if (result.getInt("ID") != 0) {
                return new Currency(
                        result.getInt("ID"),
                        result.getString("Code"),
                        result.getString("FullName"),
                        result.getString("Sign")
                );
            }
            return null;
        }
    }
}
