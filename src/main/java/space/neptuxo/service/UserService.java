package space.neptuxo.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.neptuxo.dao.UserDao;
import space.neptuxo.dto.CreateUserDto;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.mapper.CreateUserMapper;
import space.neptuxo.repository.UserRepository;
import space.neptuxo.request.LoginRequest;
import space.neptuxo.util.ErrorHandler;
import space.neptuxo.util.JsonParser;

import java.io.InputStream;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class UserService {

    private final ErrorHandler errorHandler;


    private final UserRepository userRepository;

    public boolean registration(InputStream json) {

        CreateUserDto newUser = JsonParser.parse(json, CreateUserDto.class);

        return userRepository.save(newUser);

    }

    public Optional<ReadUserDto> login(InputStream json) {

        LoginRequest userForLogin = JsonParser.parse(json, LoginRequest.class);

        return userRepository.findByEmailAndPasswd(userForLogin.email(), userForLogin.passwd());

    }

//    public ReadUserDto searchProfile(String username) {
//        userRepository.
//    }
}
