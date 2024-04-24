package space.neptuxo.mapper;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.CreateUserDto;
import space.neptuxo.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserMapperTest {

    private final CreateUserMapper mapper = new CreateUserMapper();

    @Test
    void map() {

        CreateUserDto dto = new CreateUserDto("username", "email", "passwd");

        User actual = mapper.map(dto);

        assertEquals(new User(0, dto.username(), dto.email(), dto.passwd()), actual);

    }
}