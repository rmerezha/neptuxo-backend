package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.UserDto;
import space.neptuxo.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoMapperTest {

    private final UserDtoMapper mapper = new UserDtoMapper();

    @Test
    void map() {

        UserDto dto = new UserDto(0, "username", "email", "passwd");

        User actual = mapper.map(dto);

        User expected = new User(0, dto.username(), dto.email(), dto.passwd());

        assertEquals(expected, actual);

    }
}