package DAO;

import Model.Currency;

import java.sql.SQLException;

public interface CurrencyDAO extends DAO<Currency> {
    Currency get(String code) throws SQLException;
}
