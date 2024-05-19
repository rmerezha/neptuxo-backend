package space.neptuxo.integration.dao;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.neptuxo.dao.AbstractUserRepository;
import space.neptuxo.entity.User;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util_for_test.SqlInitializer;

import java.sql.Connection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class AbstractUserRepositoryIT {

    private AbstractUserRepository userDao;
    private Connection connection;
    @BeforeEach
    public void init() {
        connection = ConnectionPool.get();
        SqlInitializer.insert(connection);
        userDao = new AbstractUserRepository(connection);
    }

    @SneakyThrows
    @AfterEach
    public void clear() {
        SqlInitializer.clear(connection);
        connection.close();
    }

    @Test
    void findById() {

        long id = 5;

        Optional<User> actual = userDao.findById(id);

        assertEquals(id , actual.map(User::getId).orElseGet(Assertions::fail));

    }

    @Test
    void save() {

        User user = new User(0, "test1", "test1@neptuxo.com", "qwerty");

        userDao.save(user);

        Optional<User> actual = userDao.findById(user.getId());
        assertNotEquals(0, user.getId());
        assertTrue(actual.isPresent());
        assertEquals(user, actual.get());
    }

    @Test
    void update() {

        User expected = new User(5, "updated_name", "updated_email", "updated_passwd");

        boolean result = userDao.update(expected);
        User actual = userDao.findById(expected.getId()).orElseGet(Assertions::fail);

        assertTrue(result);
        assertEquals(expected, actual);

    }

    @Test
    void remove() {

        long id = 7;

        boolean result = userDao.remove(id);

        assertTrue(result);
        assertEquals(Optional.empty(), userDao.findById(id));

    }

}