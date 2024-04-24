package space.neptuxo.mapper;

import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.entity.User;

public class UserMapper implements Map<User, ReadUserDto> {

    @Override
    public ReadUserDto map(User obj) {
        return new ReadUserDto(obj.getId(), obj.getUsername(), obj.getEmail());
    }

}
