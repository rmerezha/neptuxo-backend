package space.neptuxo.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}