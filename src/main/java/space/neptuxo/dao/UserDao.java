package space.neptuxo.dao;

import lombok.SneakyThrows;
import space.neptuxo.entity.User;
import space.neptuxo.exception.SQLConstraintException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDao implements AbstractUserDao {

    private static final String FIND_BY_ID = """
            SELECT id,
                   username,
                   email,
                   passwd
            FROM users
            WHERE id = ?;
            """;
    private static final String SAVE = """
            INSERT INTO users (username, email, passwd)
            VALUES (?, ?, ?);
            """;
    private static final String UPDATE = """
            UPDATE users
            SET username = ?,
                email = ?,
                passwd = ?
            WHERE id = ?;
            """;
    private static final String REMOVE = """
            DELETE FROM users
            WHERE id = ?;
            """;
    private final static String FIND_BY_EMAIL = """
            SELECT id, username, email, passwd
            FROM users
            WHERE email = ?;
            """;

    private final static String FIND_BY_USERNAME = """
            SELECT id, username, email
            FROM users
            WHERE username = ?;
            """;


    @Override
    @SneakyThrows
    public Optional<User> findById(Long id, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            User dbUser = null;
            if (rs.next()) {
                dbUser = buildUser(rs);
            }
            return Optional.ofNullable(dbUser);
        }
    }

    @Override
    @SneakyThrows
    public boolean remove(Long id, Connection connection) {
        try (var ps = connection.prepareStatement(REMOVE)) {
            ps.setLong(1, id);
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    public void save(User user, Connection connection) {
        try (var ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswd());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong("id"));
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new SQLConstraintException(e);
        }
    }

    @Override
    @SneakyThrows
    public boolean update(User user, Connection connection) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswd());
            ps.setLong(4, user.getId());
            return ps.executeUpdate() != 0;
        }
    }


    @Override
    @SneakyThrows
    public Optional<User> findByEmail(String email, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_BY_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            User dbUser = null;
            if (rs.next()) {
                dbUser = buildUser(rs);
            }
            return Optional.ofNullable(dbUser);
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> findByUsername(String username, Connection connection) {
        try (var ps = connection.prepareStatement(FIND_BY_USERNAME)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            User user = null;
            if (rs.next()) {
                user = User.builder()
                        .id(rs.getLong("id"))
                        .username(rs.getString("username"))
                        .build();
            }
            return Optional.ofNullable(user);

        }
    }

    @SneakyThrows
    private User buildUser(ResultSet rs) {
        return User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .email(rs.getString("email"))
                .passwd(rs.getString("passwd"))
                .build();
    }


}
