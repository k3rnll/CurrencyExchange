package DAO;

import DB.JDBCConnectionPool;
import Model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAOImpl implements CurrencyDAO {

    @Override
    public Currency get(Long id) throws SQLException {
        String query = String.format("SELECT Id, Code, FullName, Sign FROM Currencies WHERE Id = '%d'", id);
        try (Connection connection = JDBCConnectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            if (result.next()) {
                return new Currency(
                        result.getLong(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4));
            }
        }
        return null;
    }

    @Override
    public Currency get(String code) throws SQLException {
        String query = String.format("SELECT Id, Code, FullName, Sign FROM Currencies WHERE Code = '%s'", code);
        try (Connection connection = JDBCConnectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            if (result.next()) {
                return new Currency(
                        result.getLong(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4));
            }
        }
        return null;
    }

    @Override
    public List<Currency> getAll() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        String query = "SELECT Id, Code, FullName, Sign FROM Currencies";
        try (Connection connection = JDBCConnectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            while (result.next()) {
                currencies.add(new Currency(
                        result.getLong(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4)));
            }
        }
        return currencies;
    }

    @Override
    public Long save(Currency currency) throws SQLException {
        return 0L;
    }

    @Override
    public Long insert(Currency currency) throws SQLException {
        String query = String.format(
                "INSERT INTO Currencies (Code, FullName, Sign) " +
                        "VALUES ('%s', '%s', '%s')",
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());
        try (Connection connection = JDBCConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.executeUpdate();
            ResultSet returnedKeys = statement.getGeneratedKeys();
            returnedKeys.next();
            return returnedKeys.getLong(1);
        }
    }

    @Override
    public Long update(Currency currency) throws SQLException {
        return 0L;
    }

    @Override
    public Long delete(Currency currency) {
        return 0L;
    }
}
