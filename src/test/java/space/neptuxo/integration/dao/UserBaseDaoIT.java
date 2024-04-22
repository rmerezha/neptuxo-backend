package space.neptuxo.integration.dao;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import space.neptuxo.dao.UserBaseDao;
import space.neptuxo.entity.User;
import space.neptuxo.util.ConnectionPool;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class UserBaseDaoIT {

    private static final String CLEAR_TABLE = """
            TRUNCATE TABLE users RESTART IDENTITY CASCADE;
            """;

    @SneakyThrows
    @AfterEach
    public void clear() {
        try (var connection = ConnectionPool.get();
             var ps = connection.prepareStatement(CLEAR_TABLE)) {
            ps.executeUpdate();
        }

    }

    private final UserBaseDao userDao = new UserBaseDao(ConnectionPool.get());

    @Test
    void findById() {
        User user = buildUser();
        userDao.save(user);

        Optional<User> actual = userDao.findById(user.getId());

        assertEquals(user.getId(), actual.map(User::getId).orElseGet(Assertions::fail));
    }

    @Test
    void remove() {
        User user = buildUser();
        userDao.save(user);

        boolean result = userDao.remove(user.getId());

        assertTrue(result);
        assertEquals(Optional.empty(), userDao.findById(user.getId()));
    }

    @Test
    void save() {
        User user = buildUser();

        userDao.save(user);

        assertNotEquals(0, user.getId());
    }

    @Test
    void update() {
        User user = buildUser();
        userDao.save(user);
        String newUsername = "username";
        user.setUsername(newUsername);
        userDao.update(user);

        assertEquals(newUsername, userDao.findById(user.getId()).orElseGet(Assertions::fail).getUsername());
    }

    private static User buildUser() {
        return User.builder()
                .username("test1")
                .email("test1@test.org")
                .passwd("qwerty")
                .build();
    }
}