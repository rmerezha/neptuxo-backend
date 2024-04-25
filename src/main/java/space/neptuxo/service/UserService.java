package space.neptuxo.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.neptuxo.dto.CreateUserDto;
import space.neptuxo.mapper.CreateUserMapper;
import space.neptuxo.repository.UserRepository;
import space.neptuxo.util.ErrorHandler;
import space.neptuxo.util.JsonBuilder;
import space.neptuxo.util.JsonParser;
import space.neptuxo.util.Status;

import java.io.InputStream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserService {

    private ErrorHandler errorHandler = new ErrorHandler();
    private CreateUserMapper mapper = new CreateUserMapper();
    public boolean registration(InputStream in) {

        CreateUserDto newUser = JsonParser.parse(in, CreateUserDto.class);
        UserRepository userRepository = new UserRepository(mapper, errorHandler);
        return userRepository.save(newUser);

    }
}
