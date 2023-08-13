import DTO.CurrencyDTO;
import DTO.ExchangeRateDTO;

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
                toDTO(exchangeRate.getBaseCurrencyId()),
                toDTO(exchangeRate.getTargetCurrencyId()),
                exchangeRate.getRate().toString());
    }
}
