package space.neptuxo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {
    USER_EXIST(1, "User with this username or email already exists"),
    LOGIN(2, "User is already logged in"),
    USER_NOT_FOUND(3, "User with this email address does not exist"),
    WRONG_PASSWD(4, "Wrong password"),
    FORBIDDEN(5, "Access is denied");

    private final int code;
    private final String message;

}
