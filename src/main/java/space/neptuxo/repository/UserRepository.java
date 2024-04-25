package space.neptuxo.repository;

import lombok.RequiredArgsConstructor;
import space.neptuxo.dao.UserDao;
import space.neptuxo.dto.CreateUserDto;
import space.neptuxo.entity.User;
import space.neptuxo.mapper.CreateUserMapper;
import space.neptuxo.util.ConnectionPool;
import space.neptuxo.util.Error;
import space.neptuxo.util.ErrorHandler;

import java.sql.SQLException;

@RequiredArgsConstructor
public class UserRepository {

    private final CreateUserMapper mapper;
    private final ErrorHandler errorHandler;

    public boolean save(CreateUserDto dto) {
        try (var connection = ConnectionPool.get()) {
            UserDao dao = new UserDao(connection);
            User user = mapper.map(dto);
            dao.save(user);
            return true;
        } catch (SQLException e) {
            errorHandler.add(Error.USER_EXIST);
            return false;
        }
    }
}
