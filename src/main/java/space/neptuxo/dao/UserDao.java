package space.neptuxo.dao;

import lombok.SneakyThrows;
import space.neptuxo.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;

public class UserDao extends AbstractUserDao {

    private final Connection connection;

    private final static String FIND_BY_EMAIL = """
            SELECT id, username, email, passwd
            FROM users
            WHERE email = ?;
            
            """;
    public UserDao(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    @SneakyThrows
    @Override
    public Optional<User> findByEmail(String email) {
        try (var ps = connection.prepareStatement(FIND_BY_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            User user = null;
            if (rs.next()) {
                user = buildUser(rs);
            }
            return Optional.ofNullable(user);


        }
    }

}
