package space.neptuxo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {
    USER_EXIST(1, "User with this username or email already exists"),
    LOGIN(2, "User is already logged in");

    private final int code;
    private final String message;

}
