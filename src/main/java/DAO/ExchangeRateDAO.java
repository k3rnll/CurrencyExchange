package DAO;

import Model.ExchangeRate;

import java.sql.SQLException;

public interface ExchangeRateDAO extends DAO<ExchangeRate> {
    ExchangeRate get(String baseCurrencyCode, String targetCurrencyCode) throws SQLException;
}
