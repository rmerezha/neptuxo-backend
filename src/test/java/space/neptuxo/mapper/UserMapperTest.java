package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    UserMapper mapper = new UserMapper();

    @Test
    void map() {
        User user = new User(25, "username", "email", "asd");

        ReadUserDto actual = mapper.map(user);

        ReadUserDto expected = new ReadUserDto(user.getId(), user.getUsername(), user.getEmail());

        assertEquals(expected, actual);
    }
}