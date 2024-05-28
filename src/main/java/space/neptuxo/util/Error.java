package space.neptuxo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {
    USER_EXIST(1, "User with this username or email already exists"),
    LOGIN(2, "User is already logged in"),
    USER_NOT_FOUND(3, "User with this email address does not exist"),
    WRONG_EMAIL_OR_PASSWD(4, "Wrong email or password"),
    FORBIDDEN(5, "Access is denied"),
    USER_NOT_EXIST(6, "User with this username does not exist"),
    WRONG_PASSWD(7, "Wrong password"),
    HAS_ACTIVE_ORDERS(8, "User has active orders"),
    PRODUCT_NOT_EXIST(9, "This product doen not exist"),
    ORDER_NOT_COMPLETED(10, "Order is not completed"),
    PROFILE_NOT_FOUND(11, "Profile does not found");

    private final int code;
    private final String message;

}
