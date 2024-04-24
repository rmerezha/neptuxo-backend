package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class ReadUserMapperTest {

    private final ReadUserMapper mapper = new ReadUserMapper();

    @Test
    void map() {

        ReadUserDto dto = new ReadUserDto(25, "username", "email");

        User actual = mapper.map(dto);

        assertEquals(new User(dto.id(), dto.username(), dto.email(), null), actual);

    }
}