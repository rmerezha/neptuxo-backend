package space.neptuxo.repository;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import space.neptuxo.dao.AbstractUserRepository;
import space.neptuxo.dto.CreateUserDto;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.entity.User;
import space.neptuxo.mapper.CreateUserMapper;
import space.neptuxo.mapper.UserMapper;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util.Error;
import space.neptuxo.util.ErrorHandler;
import space.neptuxo.util.PasswordHasher;

import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {

    private final CreateUserMapper createMapper;
    private final UserMapper userMapper;
    private final ErrorHandler errorHandler;
    private final AbstractUserRepository dao;

    @SneakyThrows
    public Optional<ReadUserDto> findByEmailAndPasswd(String email, String passwd) {
        try (var connection = ConnectionPool.get()) {
            space.neptuxo.dao.UserRepository dao = new space.neptuxo.dao.UserRepository(connection);
            Optional<User> user = dao.findByEmail(email);
            if (user.isEmpty()) {
                errorHandler.add(Error.USER_NOT_FOUND);
                return Optional.empty();
            }
            if (!PasswordHasher.checkPassword(passwd, user.get().getPasswd())) {
                errorHandler.add(Error.WRONG_PASSWD);
                return Optional.empty();
            }

            return user.map(userMapper::map);
        }
    }

    public boolean save(CreateUserDto dto) {
        try (var connection = ConnectionPool.get()) {
            space.neptuxo.dao.UserRepository dao = new space.neptuxo.dao.UserRepository(connection);
            User user = createMapper.map(dto);
            user.setPasswd(PasswordHasher.hashPassword(user.getPasswd()));
            dao.save(user);
            return true;
        } catch (SQLException e) {
            errorHandler.add(Error.USER_EXIST);
            return false;
        }
    }
}
