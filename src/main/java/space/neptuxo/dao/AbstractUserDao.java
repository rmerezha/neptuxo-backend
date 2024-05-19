package space.neptuxo.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import space.neptuxo.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractUserDao implements Dao<User, Long> {

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

    @Override
    @SneakyThrows
    public Optional<User> findById(Long id) {
        try (var ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            User user = null;
            if (rs.next()) {
                user = buildUser(rs);
            }
            return Optional.ofNullable(user);
        }
    }

    @Override
    @SneakyThrows
    public boolean remove(Long id) {
        try (var ps = connection.prepareStatement(REMOVE)) {
            ps.setLong(1, id);
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
    protected User buildUser(ResultSet rs) {
        return User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .email(rs.getString("email"))
                .passwd(rs.getString("passwd"))
                .build();
    }

    public abstract Optional<User> findByEmail(String email);

}
