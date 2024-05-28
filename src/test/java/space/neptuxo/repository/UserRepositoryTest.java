package space.neptuxo.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rmerezha.di.annotation.Inject;
import org.rmerezha.di.model.BeanWithMocks;
import space.neptuxo.dao.AbstractOrderDao;
import space.neptuxo.dao.AbstractProductDao;
import space.neptuxo.dao.AbstractReviewDao;
import space.neptuxo.dao.AbstractUserDao;
import space.neptuxo.dto.ProfileDto;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.dto.UpdatePasswordDto;
import space.neptuxo.dto.UserDto;
import space.neptuxo.entity.Product;
import space.neptuxo.entity.User;
import space.neptuxo.exception.UserNotFoundException;
import space.neptuxo.mapper.ReviewDtoMapper;
import space.neptuxo.mapper.ReviewMapper;
import space.neptuxo.mapper.UserDtoMapper;
import space.neptuxo.mapper.UserMapper;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util.DependencyInjector;
import space.neptuxo.util.PasswordHasher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    private AbstractUserRepository repository;
    private UserMapper userMapper;
    private UserDtoMapper userDtoMapper;
    private AbstractUserDao userDao;
    private AbstractProductDao productDao;
    private Connection connection;
    private PasswordHasher passwordHasher;


    @BeforeEach
    void setUp() {
        BeanWithMocks<? extends AbstractUserRepository> beanForTest = DependencyInjector.getBeanForTest(AbstractUserRepository.class);
        repository = beanForTest.bean();
        userDao = (AbstractUserDao) beanForTest.mocks().get(AbstractUserDao.class);
        productDao = (AbstractProductDao) beanForTest.mocks().get(AbstractProductDao.class);
        userMapper = (UserMapper) beanForTest.mocks().get(UserMapper.class);
        userDtoMapper = (UserDtoMapper) beanForTest.mocks().get(UserDtoMapper.class);
        passwordHasher = (PasswordHasher) beanForTest.mocks().get(PasswordHasher.class);
        connection = mock(Connection.class);
        ConnectionPool connectionPool = (ConnectionPool) beanForTest.mocks().get(ConnectionPool.class);
        when(connectionPool.get()).thenReturn(connection);
    }

    @Test
    void findByEmailAndPasswdUserNotFound() throws SQLException {
        String email = "test@example.com";
        String password = "password";

        when(userDao.findByEmail(email, connection)).thenReturn(Optional.empty());

        Optional<ReadUserDto> result = repository.findByEmailAndPasswd(email, password);

        assertFalse(result.isPresent());
    }

    @Test
    void findByEmailAndPasswdInvalidPassword() throws SQLException {
        String email = "test@example.com";
        String password = "password";
        User user = new User(11, "username", "email", "passwd");
        user.setPasswd("hashedPassword");

        when(userDao.findByEmail(email, connection)).thenReturn(Optional.of(user));
        when(passwordHasher.checkPassword(password, "hashedPassword")).thenReturn(false);

        Optional<ReadUserDto> result = repository.findByEmailAndPasswd(email, password);

        assertFalse(result.isPresent());
    }

    @Test
    void findByEmailAndPasswd_ValidPassword() throws SQLException {
        String email = "test@example.com";
        String password = "password";
        User user = new User(11, "username", "email", "passwd");
        user.setPasswd("hashedPassword");
        ReadUserDto readUserDto = new ReadUserDto(11, "username", "email");

        when(userDao.findByEmail(email, connection)).thenReturn(Optional.of(user));
        when(passwordHasher.checkPassword(password, "hashedPassword")).thenReturn(true);
        when(userMapper.map(user)).thenReturn(readUserDto);

        Optional<ReadUserDto> result = repository.findByEmailAndPasswd(email, password);

        assertTrue(result.isPresent());
        assertEquals(readUserDto, result.get());
    }

    @Test
    void findProfileUserNotFound() throws SQLException {
        String username = "username";

        when(userDao.findByUsername(username, connection)).thenReturn(Optional.empty());

        ProfileDto result = repository.findProfile(username);

        assertNotNull(result);
        assertFalse(result.user().isPresent());
        assertTrue(result.products().isEmpty());
    }

    @Test
    void findProfileUserFound() throws SQLException {
        String username = "username";
        User user = new User(11, "username", "email", "passwd");
        ReadUserDto readUserDto = new ReadUserDto(11, "username", "email");
        List<Product> products = List.of(Product.builder().build(), Product.builder().build());

        when(userDao.findByUsername(username, connection)).thenReturn(Optional.of(user));
        when(userMapper.map(user)).thenReturn(readUserDto);
        when(productDao.findByUserId(user.getId(), connection)).thenReturn(products);

        ProfileDto result = repository.findProfile(username);

        assertNotNull(result);
        assertTrue(result.user().isPresent());
        assertEquals(readUserDto, result.user().get());
        assertEquals(products, result.products());
    }

    @Test
    void updatePasswdUserNotFound() throws SQLException {
        long userId = 1L;
        UpdatePasswordDto dto = new UpdatePasswordDto("oldPassword", "newPassword");

        when(userDao.findById(userId, connection)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> repository.updatePasswd(userId, dto));
    }

    @Test
    void updatePasswdInvalidOldPassword() throws SQLException {
        long userId = 1L;
        UpdatePasswordDto dto = new UpdatePasswordDto("oldPassword", "newPassword");
        User user = new User(11, "username", "email", "passwd");
        user.setPasswd("hashedPassword");

        when(userDao.findById(userId, connection)).thenReturn(Optional.of(user));
        when(passwordHasher.checkPassword("oldPassword", "hashedPassword")).thenReturn(false);

        boolean result = repository.updatePasswd(userId, dto);

        assertFalse(result);
        verify(userDao, never()).update(any(), any());
    }

    @Test
    void updatePasswdValidOldPassword() throws SQLException {
        long userId = 1L;
        UpdatePasswordDto dto = new UpdatePasswordDto("oldPassword", "newPassword");
        User user = new User(11, "username", "email", "passwd");
        user.setPasswd("hashedPassword");

        when(userDao.findById(userId, connection)).thenReturn(Optional.of(user));
        when(passwordHasher.checkPassword("oldPassword", "hashedPassword")).thenReturn(true);
        when(passwordHasher.hashPassword("newPassword")).thenReturn("newHashedPassword");

        boolean result = repository.updatePasswd(userId, dto);

        assertTrue(result);
        assertEquals("newHashedPassword", user.getPasswd());
        verify(userDao).update(user, connection);
    }

    @Test
    void saveSuccess() throws SQLException {
        UserDto userDto = new UserDto(11, "username", "email", "passwd");
        User user = new User(11, "username", "email", "passwd");
        user.setPasswd("plainPassword");

        when(userDtoMapper.map(userDto)).thenReturn(user);
        when(passwordHasher.hashPassword("plainPassword")).thenReturn("hashedPassword");

        repository.save(userDto);

        assertEquals("hashedPassword", user.getPasswd());
        verify(userDao).save(user, connection);
    }
}