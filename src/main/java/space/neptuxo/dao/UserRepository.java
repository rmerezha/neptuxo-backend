package space.neptuxo.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import space.neptuxo.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepository implements AbstractUserRepository {

    private final Connection connection;


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

    @Override
    @SneakyThrows
    public Optional<User> findById(User user) {
        try (var ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setLong(1, user.getId());
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
    public boolean remove(User user) {
        try (var ps = connection.prepareStatement(REMOVE)) {
            ps.setLong(1, user.getId());
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    @SneakyThrows
    public void save(User obj) {
        try (var ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, obj.getUsername());
            ps.setString(2, obj.getEmail());
            ps.setString(3, obj.getPasswd());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                obj.setId(generatedKeys.getLong("id"));
            }
        }
    }

    @Override
    @SneakyThrows
    public boolean update(User obj) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, obj.getUsername());
            ps.setString(2, obj.getEmail());
            ps.setString(3, obj.getPasswd());
            ps.setLong(4, obj.getId());
            return ps.executeUpdate() != 0;
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



    @SneakyThrows
    @Override
    public Optional<User> findByEmailAndPasswd(User user) {
        try (var ps = connection.prepareStatement(FIND_BY_EMAIL)) {
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();
            User dbUser = null;
            if (rs.next()) {
                dbUser = buildUser(rs);
            }
            return Optional.ofNullable(dbUser);


        }
    }

}
