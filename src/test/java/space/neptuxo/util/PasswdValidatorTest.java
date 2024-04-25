package space.neptuxo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void checkPassword() {

        boolean result = PasswordHasher.checkPassword("qwerty123", "$2a$10$9muRfMuexTtjCHmnQEMrzO415tEJgn865l/zegWGazuKlPHlxrKQG");

        assertTrue(result);
    }

    @Test
    void hashPassword() {
        String password = "qwerty123";

        String hashPassword = PasswordHasher.hashPassword(password);

        assertTrue(PasswordHasher.checkPassword(password, hashPassword));
    }
}