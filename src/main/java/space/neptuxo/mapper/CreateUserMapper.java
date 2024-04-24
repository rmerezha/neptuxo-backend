package space.neptuxo.mapper;

import space.neptuxo.dto.CreateUserDto;
import space.neptuxo.entity.User;

public class CreateUserMapper implements Map<CreateUserDto, User> {

    @Override
    public User map(CreateUserDto obj) {
        return User.builder()
                .username(obj.username())
                .email(obj.email())
                .passwd(obj.passwd())
                .build();
    }

}
