package space.neptuxo.util;

public enum Status {
    SUCCESS("success"),
    FAIL("fail");

    private final String type;
    Status(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
