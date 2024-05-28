package space.neptuxo.repository;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.rmerezha.di.annotation.Inject;
import space.neptuxo.dao.AbstractProductDao;
import space.neptuxo.dao.AbstractUserDao;
import space.neptuxo.dto.ProfileDto;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.dto.UpdatePasswordDto;
import space.neptuxo.dto.UserDto;
import space.neptuxo.entity.Product;
import space.neptuxo.entity.User;
import space.neptuxo.exception.UserNotFoundException;
import space.neptuxo.mapper.UserDtoMapper;
import space.neptuxo.mapper.UserMapper;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util.PasswordHasher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements AbstractUserRepository {

    @Inject
    private UserMapper userMapper;
    @Inject
    private UserDtoMapper userDtoMapper;

    @Inject
    private AbstractUserDao userDao;

    @Inject
    private AbstractProductDao productDao;

    @Inject
    private ConnectionPool connectionPool;

    @Inject
    private PasswordHasher passwordHasher;

    @Override
    @SneakyThrows
    public Optional<ReadUserDto> findByEmailAndPasswd(String email, String passwd) {
        try (var connection = connectionPool.get()) {

            Optional<User> user = userDao.findByEmail(email, connection);
            if (user.isEmpty()) {
                return Optional.empty();
            }
            if (!passwordHasher.checkPassword(passwd, user.get().getPasswd())) {
                return Optional.empty();
            }
            return user.map(userMapper::map);
        }
    }

    @Override
    public ProfileDto findProfile(String username) {
        Connection connection = null;
        try {
            connection = connectionPool.get();
            connection.setAutoCommit(false);
            Optional<ReadUserDto> user = userDao.findByUsername(username, connection).map(userMapper::map);
            List<Product> products = new ArrayList<>();
            if (user.isPresent()) {
                products.addAll(productDao.findByUserId(user.get().id(), connection));
            }
            connection.commit();
            return new ProfileDto(user, products);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    @Override
    public boolean updatePasswd(long id, UpdatePasswordDto dto) {
        Connection connection = null;
        try {
            connection = connectionPool.get();
            connection.setAutoCommit(false);
            Optional<User> optionalUser = userDao.findById(id, connection);

            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException();
            }

            User user = optionalUser.get();
            boolean isCorrectPasswd = false;
            if (passwordHasher.checkPassword(dto.oldPasswd(), user.getPasswd())) {
                user.setPasswd(passwordHasher.hashPassword(dto.newPasswd()));
                userDao.update(user, connection);
                isCorrectPasswd = true;
            }
            connection.commit();
            return isCorrectPasswd;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @Override
    public void save(UserDto dto) {
        try (var connection = connectionPool.get()) {
            User user = userDtoMapper.map(dto);
            user.setPasswd(passwordHasher.hashPassword(user.getPasswd()));
            userDao.save(user, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
