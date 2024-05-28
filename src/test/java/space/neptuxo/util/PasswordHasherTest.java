package space.neptuxo.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordHasherTest {

    private final PasswordHasher passwordHasher = new PasswordHasher();

    private static Stream<Arguments> getData() {
        return Stream.of(
                Arguments.of("qwerty123", true),
                Arguments.of("fake_passwd", false)
        );
    }

    @ParameterizedTest
    @MethodSource("getData")
    void checkPassword(String plainPassword, boolean expected) {
        String hashedPassword = "$2a$10$9muRfMuexTtjCHmnQEMrzO415tEJgn865l/zegWGazuKlPHlxrKQG";

        boolean actual = passwordHasher.checkPassword(plainPassword, hashedPassword);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getData")
    void hashPassword(String passwd, boolean expected) {
        String password = "qwerty123";

        String hashPassword = passwordHasher.hashPassword(passwd);

        assertEquals(expected, passwordHasher.checkPassword(password, hashPassword));
    }
}