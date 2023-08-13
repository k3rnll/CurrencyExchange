public class ExchangeRate {
    private final Integer id;
    private final Currency baseCurrency;
    private final Currency targetCurrency;
    private Double rate;

    public ExchangeRate(Integer id, Currency baseCurrency, Currency targetCurrency, double rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public Currency getBaseCurrencyId() {
        return baseCurrency;
    }

    public Currency getTargetCurrencyId() {
        return targetCurrency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", baseCurrency=" + baseCurrency +
                ", targetCurrency=" + targetCurrency +
                ", rate=" + rate +
                '}';
    }
}
