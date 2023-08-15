package DAO;

import DB.DBTables;
import DB.JDBCConnectionPool;
import Model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAOImpl implements CurrencyDAO {

    @Override
    public Currency get(int id) throws SQLException {
        String query = String.format(
                "SELECT %s, %s, %s, %s FROM %s WHERE %s = %d",
                DAOFields.ID.getTitle(),
                DAOFields.CODE.getTitle(),
                DAOFields.FULL_NAME.getTitle(),
                DAOFields.SIGN.getTitle(),
                DBTables.CURRENCIES.getTitle(),
                DAOFields.ID.getTitle(),
                id);
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
        String query = String.format(
                "SELECT %s, %s, %s, %s FROM %s WHERE %s = %s",
                DAOFields.ID.getTitle(),
                DAOFields.CODE.getTitle(),
                DAOFields.FULL_NAME.getTitle(),
                DAOFields.SIGN.getTitle(),
                DBTables.CURRENCIES.getTitle(),
                DAOFields.CODE.getTitle(),
                code);
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
        String query = String.format(
                "SELECT %s, %s, %s, %s FROM %s",
                DAOFields.ID.getTitle(),
                DAOFields.CODE.getTitle(),
                DAOFields.FULL_NAME.getTitle(),
                DAOFields.SIGN.getTitle(),
                DBTables.CURRENCIES.getTitle());
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
    public int save(Currency currency) throws SQLException {
        return 0;
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
    public int update(Currency currency) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Currency currency) {
        return 0;
    }
}
