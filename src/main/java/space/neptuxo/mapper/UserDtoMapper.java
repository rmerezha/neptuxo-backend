package space.neptuxo.mapper;

import lombok.NoArgsConstructor;
import space.neptuxo.dto.UserDto;
import space.neptuxo.entity.User;

@NoArgsConstructor
public class UserDtoMapper implements Mapper<UserDto, User> {

    @Override
    public User map(UserDto obj) {
        return User.builder()
                .username(obj.username())
                .email(obj.email())
                .passwd(obj.passwd())
                .build();
    }

}
