package DAO;

public class CurrencyDAO {
    private Integer id;
    private String code;
    private String fullName;
    private String sign;

    public CurrencyDAO(String code, String fullName, String sign) {
        this.id = 0;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public CurrencyDAO(Integer id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
