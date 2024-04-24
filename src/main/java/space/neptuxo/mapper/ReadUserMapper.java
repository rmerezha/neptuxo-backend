package space.neptuxo.mapper;

import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.entity.User;

public class ReadUserMapper implements Map<ReadUserDto, User> {

    @Override
    public User map(ReadUserDto obj) {
        return new User(obj.id(), obj.username(), obj.email(), null);
    }

}
