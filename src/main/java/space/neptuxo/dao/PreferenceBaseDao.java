package space.neptuxo.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import space.neptuxo.entity.Preference;
import space.neptuxo.entity.ProductType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

@RequiredArgsConstructor
public class PreferenceBaseDao implements Dao<Preference, Long> {

    private final Connection connection;

    private static final String FIND_BY_ID = """
            SELECT id,
                   user_id,
                   type
            FROM preference
            WHERE id = ?;
            """;

    private static final String SAVE = """
            INSERT INTO preference (user_id, type)
            VALUES (?, ?);
            """;

    private static final String UPDATE = """
            UPDATE preference
            SET user_id = ?,
                type = ?
            WHERE id = ?
            """;

    private static final String REMOVE = """
            DELETE FROM preference
            WHERE id = ?;
            """;

    @Override
    @SneakyThrows
    public Optional<Preference> findById(Long id) {
        try (var ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            Preference pr = null;
            if (rs.next()) {
                pr = buildPreference(rs);
            }
            return Optional.ofNullable(pr);
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
    public boolean update(Preference obj) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setLong(1, obj.getUserId());
            ps.setString(2, obj.getType().name());
            ps.setLong(3, obj.getId());
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    @SneakyThrows
    public void save(Preference obj) {
        try (var ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, obj.getUserId());
            ps.setString(2, obj.getType().name());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                obj.setId(generatedKeys.getLong("id"));
            }

        }
    }

    @SneakyThrows
    private Preference buildPreference(ResultSet rs) {
        return Preference.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .type(ProductType.valueOf(rs.getString("type")))
                .build();
    }
}
