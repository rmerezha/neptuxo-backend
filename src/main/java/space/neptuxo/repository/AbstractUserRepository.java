package space.neptuxo.repository;

import space.neptuxo.dto.ProfileDto;
import space.neptuxo.dto.ReadUserDto;
import space.neptuxo.dto.UpdatePasswordDto;
import space.neptuxo.dto.UserDto;

import java.util.Optional;

public interface AbstractUserRepository {

    void save(UserDto dto);

    Optional<ReadUserDto> findByEmailAndPasswd(String email, String passwd);

    ProfileDto findProfile(String username);

    boolean updatePasswd(long id, UpdatePasswordDto dto);

}
