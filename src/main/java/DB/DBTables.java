package DB;

public enum DBTables {
    CURRENCIES ("Currencies"),
    EXCHANGE_RATES ("ExchangeRates");

    private String title;

    DBTables(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
