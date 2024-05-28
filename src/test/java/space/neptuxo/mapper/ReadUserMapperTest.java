package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadUserMapperTest {

    private final ReadUserMapper mapper = new ReadUserMapper();

    @Test
    void map() {

        ReadUserDto dto = new ReadUserDto(0, "username", "email");

        User actual = mapper.map(dto);

        User expected = new User(0, dto.username(), dto.email(), null);

        assertEquals(expected, actual);


    }
}