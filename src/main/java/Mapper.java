import DTO.CurrencyDTO;

public class Mapper {
    public CurrencyDTO toDTO(Currency currency) {
        return new CurrencyDTO(
                currency.getId().toString(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());
    }
}
