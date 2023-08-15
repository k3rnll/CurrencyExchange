package DAO;

public enum DAOFields {
    ID ("Id"),
    CODE ("Code"),
    FULL_NAME ("FullName"),
    SIGN ("Sign"),
    BASE_CURRENCY_ID ("BaseCurrencyId"),
    TARGET_CURRENCY_ID ("TargetCurrencyId"),
    RATE ("Rate");

    private String title;

    DAOFields(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
