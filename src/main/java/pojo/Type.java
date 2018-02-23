package pojo;

public enum Type {
    ELECTRICITY("electricity"),
    GAS("gas");
    public final String value;

    Type(String val) {
        this.value = val;
    }
}
