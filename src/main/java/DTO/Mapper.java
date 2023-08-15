package DTO;

import Model.Currency;
import Model.ExchangeRate;

public class Mapper {
    public CurrencyDTO toDTO(Currency currency) {
        return new CurrencyDTO(
                currency.getId().toString(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());
    }

    public ExchangeRateDTO toDTO(ExchangeRate exchangeRate) {
        return new ExchangeRateDTO(
                exchangeRate.getId().toString(),
                toDTO(exchangeRate.getBaseCurrency()),
                toDTO(exchangeRate.getTargetCurrency()),
                exchangeRate.getRate().toString());
    }
}
