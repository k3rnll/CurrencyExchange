import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DBController {
    private final String dbUrl = "jdbc:sqlite:\\CE_DB.db";
    private final DBConnectionPool connectionPool = new DBConnectionPool(dbUrl);


    public Set<ExchangeRate> getExchangeRatesSet() {
        Set<ExchangeRate> exchangeRatesSet = new HashSet<>();
        String query = "SELECT * FROM ExchangeRates";
        ResultSet result = executeStatement(query);
        if (Objects.nonNull(result)) {
            try {
                while (result.next()) {
                    ExchangeRate exchangeRate = new ExchangeRate(
                            result.getInt("ID"),
                            getCurrency(result.getInt("BaseCurrencyId")),
                            getCurrency(result.getInt("TargetCurrencyId")),
                            result.getDouble("Rate"));
                    exchangeRatesSet.add(exchangeRate);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  exchangeRatesSet;
    }

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

    public Currency getCurrency(int id) {
        return getCurrencyByQuery(String.format("SELECT * FROM Currencies WHERE ID = '%d'", id));
    }

    public Currency getCurrency(String currencyCode) {
        return getCurrencyByQuery(String.format("SELECT * FROM Currencies WHERE Code = '%s'", currencyCode));
    }

    private Currency getCurrencyByQuery(String query) {
        Currency currency = null;
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
