package space.neptuxo.mapper;

import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.entity.User;

public class UserMapper implements Map<User, ReadUserDto>{
    @Override
    public ReadUserDto map(User obj) {
        return ReadUserDto.builder()
                .id(obj.getId())
                .username(obj.getUsername())
                .email(obj.getEmail())
                .build();
    }
}
