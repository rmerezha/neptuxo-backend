package space.neptuxo.util;

import lombok.Getter;

public enum SessionKey {
    USER("user");

    private final String key;
    SessionKey(String key) {
        this.key = key;
    }

    public String get() {
        return key;
    }


}
