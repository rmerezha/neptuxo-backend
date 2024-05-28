package space.neptuxo.mapper;

import lombok.NoArgsConstructor;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.entity.User;

@NoArgsConstructor
public class UserMapper implements Mapper<User, ReadUserDto> {
    @Override
    public ReadUserDto map(User obj) {
        return ReadUserDto.builder()
                .id(obj.getId())
                .username(obj.getUsername())
                .email(obj.getEmail())
                .build();
    }
}
