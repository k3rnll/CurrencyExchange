package DAO;

import DB.DBTables;
import DB.JDBCConnectionPool;
import Model.Currency;
import Model.ExchangeRate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAOImpl implements ExchangeRateDAO{
    @Override
    public ExchangeRate get(Long id) throws SQLException {
        String query = String.format(
                "SELECT %s, %s, %s, %s FROM %s WHERE %s = %d",
                DAOFields.ID.getTitle(),
                DAOFields.BASE_CURRENCY_ID.getTitle(),
                DAOFields.TARGET_CURRENCY_ID.getTitle(),
                DAOFields.RATE.getTitle(),
                DBTables.EXCHANGE_RATES.getTitle(),
                DAOFields.ID.getTitle(),
                id);
        try (Connection connection = JDBCConnectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            if (result.next()) {
                CurrencyDAO currencyDAO = new CurrencyDAOImpl();
                Currency baseCurrency = currencyDAO.get(result.getLong(2));
                Currency targetCurrency = currencyDAO.get(result.getLong(3));
                return new ExchangeRate(
                        result.getLong(1),
                        baseCurrency,
                        targetCurrency,
                        result.getDouble(4));
            }
        }
        return null;
    }

    @Override
    public ExchangeRate get(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        String query = String.format(
                "SELECT %s, %s, %s, %s FROM %s ",
                        DAOFields.ID.getTitle(),
                        DAOFields.BASE_CURRENCY_ID.getTitle(),
                        DAOFields.TARGET_CURRENCY_ID.getTitle(),
                        DAOFields.RATE.getTitle(),
                        DBTables.EXCHANGE_RATES.getTitle()) +
                "WHERE " + String.format(
                "%s IN (SELECT %s FROM %s WHERE %s = '%s') ",
                        DAOFields.BASE_CURRENCY_ID.getTitle(),
                        DAOFields.ID.getTitle(),
                        DBTables.CURRENCIES.getTitle(),
                        DAOFields.CODE.getTitle(),
                        baseCurrencyCode) +
                "AND " + String.format(
                "%s IN (SELECT %s FROM %s WHERE %s = '%s')",
                        DAOFields.TARGET_CURRENCY_ID.getTitle(),
                        DAOFields.ID.getTitle(),
                        DBTables.CURRENCIES.getTitle(),
                        DAOFields.CODE.getTitle(),
                        targetCurrencyCode);
        try (Connection connection = JDBCConnectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            if (result.next()) {
                CurrencyDAO currencyDAO = new CurrencyDAOImpl();
                Currency baseCurrency = currencyDAO.get(result.getLong(2));
                Currency targetCurrency = currencyDAO.get(result.getLong(3));
                return new ExchangeRate(
                        result.getLong(1),
                        baseCurrency,
                        targetCurrency,
                        result.getDouble(4));
            }
        }
        return null;
    }

    @Override
    public List<ExchangeRate> getAll() throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String query = String.format(
                "SELECT %s, %s, %s, %s FROM %s",
                DAOFields.ID.getTitle(),
                DAOFields.BASE_CURRENCY_ID.getTitle(),
                DAOFields.TARGET_CURRENCY_ID.getTitle(),
                DAOFields.RATE.getTitle(),
                DBTables.EXCHANGE_RATES.getTitle());
        try (Connection connection = JDBCConnectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            while (result.next()) {
                CurrencyDAO currencyDAO = new CurrencyDAOImpl();
                Currency baseCurrency = currencyDAO.get(result.getLong(2));
                Currency targetCurrency = currencyDAO.get(result.getLong(3));
                exchangeRates.add(new ExchangeRate(
                                    result.getLong(1),
                                    baseCurrency,
                                    targetCurrency,
                                    result.getDouble(4)));
            }
        }
        return exchangeRates;
    }

    @Override
    public Long save(ExchangeRate exchangeRate) throws SQLException {
        return 0L;
    }

    @Override
    public Long insert(ExchangeRate exchangeRate) throws SQLException {
        return 0L;
    }

    @Override
    public Long update(ExchangeRate exchangeRate) throws SQLException {
        return 0L;
    }

    @Override
    public Long delete(ExchangeRate exchangeRate) {
        return 0L;
    }
}
